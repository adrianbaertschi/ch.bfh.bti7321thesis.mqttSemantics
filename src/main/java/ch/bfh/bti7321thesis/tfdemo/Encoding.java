package ch.bfh.bti7321thesis.tfdemo;

public class Encoding {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double d = 22.3;
		System.out.println(Double.doubleToLongBits(d));
		System.out.println(Double.doubleToRawLongBits(d));
		System.out.println(Double.toHexString(d));
		System.out.println(Double.toString(d));
		
		int i = 22;
		System.out.println(Integer.toBinaryString(i));	
		System.out.println(Integer.toBinaryString(-22));
		
	}

}
