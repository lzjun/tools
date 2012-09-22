import java.util.Iterator;

/**
 * java 实现 fibonacci 迭代器
 * 使用函数fib加数组array是很不合理的事情
 * 一旦num比较大时，程序就会发生堆异常
 * @author lzjun
 */
public class Fibonacci implements Iterator<Integer> {

	 //缓存数组
	private int[] array;

	 //当前位置
	private int current;

	public Fibonacci(int num) {
		array = new int[num];
		for (int i = 0; i < num; i++) {
			array[i] = fib(i);
		}
	}
	@Override
	public boolean hasNext() {
		return current != array.length;
	}

	@Override
	public Integer next() {
		return array[current++];
	}

	@Override
	public void remove() {
		array[current++] = 0;
	}

	//fibonacci 函数，返回第a个元素的值
	private int fib(int a) {
		if (a == 0 || a == 1) {
			return a;
		} else {
			return fib(a - 1) + fib(a - 2);
		}
	}
	
	
	//测试
	public static void main(String[] args) {
		Fibonacci f = new Fibonacci(40);

		while (f.hasNext()) {
			System.out.println(f.next());
		}

	}

}
