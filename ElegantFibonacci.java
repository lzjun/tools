import java.util.Iterator;

/**
 * java 实现 fibonacci 迭代器 
 * 其实fibonacci可以理解为是两个值不断被替换的过程，
 * 替换原则就是 first用second替换,second用当前second值加上替换前的fris值
 * @author lzjun
 */

public class ElegantFibonacci implements Iterator<Long> {

	private long first = 0;
	private long second = 1;

	// fibonacci数列的长度
	private int num;
	// 调用next函数时的当前位置
	private int cursor;

	public ElegantFibonacci(int num) {
		this.num = num;
	}

	@Override
	public boolean hasNext() {
		return cursor != num;
	}

	@Override
	public Long next() {
		long vaule = this.first;
		this.first = this.second;
		this.second = vaule + this.second;
		cursor++;
		return vaule;
	}

	@Override
	public void remove() {
		//not implement
	}

	// 测试
	public static void main(String[] args) {
		ElegantFibonacci f = new ElegantFibonacci(50);

		while (f.hasNext()) {
			System.out.println(f.next());

		}

	}

}
