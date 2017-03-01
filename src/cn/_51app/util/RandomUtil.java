package cn._51app.util;

import java.text.DecimalFormat;

public class RandomUtil {

	/**
	 * tengh 2016年11月22日 下午4:24:12
	 * @param min
	 * @param max
	 * @return
	 * TODO 范围内随机一个double
	 */
	public static double randByRange(int min,int max){
		return  Double.valueOf(new DecimalFormat("######0.0").format(Math.random()*(max-min)+min));
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.err.println(randByRange(4, 6));
		}
	}
	
}
