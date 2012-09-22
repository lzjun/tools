package com.lzjun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lzjun
 * @email lzjun567@gmail.com
 * 
 */
public class ImageScratchUtils {

	private URI uri;

	public ImageScratchUtils(String uri) {
		try {
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取 资源标签集合
	 * @param pattern  
	 * @return
	 */
	public Set<String> getTags(String pattern){
		Set<String> set = new HashSet<String>();
		try {
			URLConnection conn = uri.toURL().openConnection();
			InputStream in = conn.getInputStream();
			byte[] tmp = new byte[1024];
			StringBuilder sbuilder = new StringBuilder(2048);
			int len;
			while ((len = in.read(tmp)) != -1) {
				sbuilder.append(new String(tmp, 0, len, "utf-8"));
			}
			String pageContent = sbuilder.toString();

			String[] imgTags = pageContent.split(pattern);

			for (int i = 1; i < imgTags.length; i++) {
				String temp = imgTags[i];
				set.add(pattern + temp.substring(0, temp.indexOf(">") + 1));
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return set;
	}

	/**
	 * 解析 标签 ,返回资源的url
	 * @param tag
	 * @return
	 */
	private Set<URL> parseTag(String pattern){
		Set<String> tags = getTags(pattern);
		
		Set<URL> set = new HashSet<URL>();
		
		for (String tag : tags) {
			System.out.println(tag);
			int index = tag.indexOf("http://");
			if(index!=-1){
				int endIndex  = tag.indexOf("\"", index);
				String urlStr = tag.substring(index, endIndex);

				URL url = null;
				try {
					if (urlStr.startsWith("/")) {
						urlStr = uri.getScheme() + "://" + uri.getHost() + urlStr;
					}
					url = new URL(urlStr);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				set.add(url);
			}
			
			}
			
		return set;
	}

	/**
	 * save resource
	 * @param url
	 */
	private void downloadReosurce(Set<URL> urls) {

		String dir = System.getProperty("user.home") + "/downloads";
		File file = new File(dir);

		if (!file.exists()) {
			file.mkdirs();
		}
		
		for (URL url : urls) {
			URLConnection conn;
			try {
				conn = url.openConnection();
				InputStream in = conn.getInputStream();
			
				String fileName = url.getFile().substring(
						url.getFile().lastIndexOf("/") + 1);

				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath()
						+ "/" + fileName);

				byte[] tmp = new byte[1024];

				int len;

				while ((len = in.read(tmp)) != -1) {
					fos.write(tmp, 0, len);
				}
				System.out.println(url.getFile() + "is saved");
				fos.flush();
				fos.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		

	}

	public static void main(String[] args) throws Exception {

		ImageScratchUtils obj = new ImageScratchUtils("http://www.himdc.com/ppt/1061.html");

		obj.downloadReosurce(obj.parseTag("<a href="));
	}
}
