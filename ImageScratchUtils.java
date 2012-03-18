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
	
	public Set<String> getImgTages() {
		Set<String> set = new HashSet<String>();
		try {
			URLConnection conn = uri.toURL().openConnection();

			InputStream in = conn.getInputStream();
			byte[] tmp = new byte[1024];
			StringBuilder sbuilder = new StringBuilder(2048);
			int len;
			while ((len = in.read(tmp)) != -1) {
				sbuilder.append(new String(tmp, 0, len,"utf-8"));
			}
			String pageContent = sbuilder.toString();

			String regex = "<img";
			String[] imgTags = pageContent.split(regex);


			for (int i = 1; i < imgTags.length; i++) {
				String temp = imgTags[i];
				set.add(regex + temp.substring(0, temp.indexOf(">")+1));
				
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

	
	private URL parseImgTag(String tag) {
		
		int index = tag.indexOf("src=")+4;
		char firstQuotes = tag.charAt(index);
		
		int begainIndex = tag.indexOf(firstQuotes,index)+1;
		int endIndex = tag.indexOf(firstQuotes,begainIndex);
		String urlStr = tag.substring(begainIndex,endIndex);
		
		URL url = null;
		try {
			System.out.println(urlStr);
			if(urlStr.startsWith("/")){
				urlStr = uri.getScheme()+"://"+uri.getHost()+urlStr;
			}
			System.out.println(urlStr);
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
		
	}
	
	private void saveImg(URL url){
		
		URLConnection conn;
		try {
			conn = url.openConnection();
			InputStream in = conn.getInputStream();
			
			String dir = url.getPath().substring(0, url.getPath().lastIndexOf("/"));
			
			
			dir = System.getProperty("user.home")+dir;
			
			
			
			File file = new File(dir);
			
			
			if(!file.exists()){
				file.mkdirs();
			}
			
			 String fileName = url.getFile().substring(url.getFile().lastIndexOf("/")+1);
			
			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath()+"/"+fileName);
			
			byte[] tmp = new byte[1024];
			
			int len;
			
			while((len=in.read(tmp))!=-1){
				fos.write(tmp, 0, len);
			}
			System.out.println(url.getFile()+ "is saved");
			fos.flush();
			fos.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		ImageScratchUtils obj =new ImageScratchUtils("http://www.douban.com/");
		Set<String> set = obj.getImgTages();
		
		System.out.println(set);
		for (String tag : set) {
			URL url = obj.parseImgTag(tag);
			if(url!=null)
				obj.saveImg(url);
		}
		
	}
}
