package application;

import java.util.Scanner;

public class PointFinder {

	public static void main(String[] args) {
		
		double x,y,mid;
		Scanner sc = new Scanner(System.in);
		System.out.println("X :");
		x = sc.nextDouble();
		System.out.println("Y :");
		y = sc.nextDouble();
		mid = (x+y)/2;
		System.out.println(mid);
		
	}

}
