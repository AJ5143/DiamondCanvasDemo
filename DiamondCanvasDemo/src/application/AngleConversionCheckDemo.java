package application;

import java.util.Scanner;

public class AngleConversionCheckDemo {

	public static void main(String[] args) {
		double degree, radion, degreeConverted, CH, left;
		Scanner sc = new Scanner(System.in);
		System.out.println("Angle in Degree: ");
		degree = sc.nextDouble();
		radion = Math.toRadians(degree);
		System.out.println(radion);
		degreeConverted = Math.toDegrees(radion);
		System.out.println("Converted Degree: " + degreeConverted);
		CH = 10;
		left = (CH) * Math.cos(radion) / Math.sin(radion);
		System.out.println(left);
	}

}
