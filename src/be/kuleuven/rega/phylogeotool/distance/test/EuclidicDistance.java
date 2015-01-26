package be.kuleuven.rega.phylogeotool.distance.test;

import java.awt.Point;

public class EuclidicDistance {
	
	public static double calculateEuclidicDistance(Point point1, Point point2) {
		return point1.distance(point2);
	}

	public static void main(String args[]) {
		Point[] points = new Point[8];
		Point point1 = new Point(1,1);
		Point point2 = new Point(1,2);
		Point point3 = new Point(2,2);
		Point point4 = new Point(4,1);
		Point point5 = new Point(5,1);
		Point point6 = new Point(4,5);
		Point point7 = new Point(5,5);
		Point point8 = new Point(5,4);
		points[0] = point1;
		points[1] = point2;
		points[2] = point3;
		points[3] = point4;
		points[4] = point5;
		points[5] = point6;
		points[6] = point7;
		points[7] = point8;
		System.out.println("1,2,3,4,5,6,7,8");
		for(int i = 0; i < points.length;i++) {
			for (int j = 0; j < points.length; j++) {
				System.out.print(points[i].distance(points[j]) + ",");
			}
			System.out.print("\n");
		}
	}
}
