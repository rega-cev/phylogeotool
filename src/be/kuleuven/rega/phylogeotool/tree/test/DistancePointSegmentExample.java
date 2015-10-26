package be.kuleuven.rega.phylogeotool.tree.test;

import java.awt.geom.Point2D;

/*
 * http://paulbourke.net/geometry/pointlineplane/DistancePoint.java
 */

public class DistancePointSegmentExample {

	/**
	 * Wrapper function to accept the same arguments as the other examples
	 * 
	 * @param x3
	 * @param y3
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double distanceToSegment(double x3, double y3, double x1, double y1, double x2, double y2) {
		final Point2D p3 = new Point2D.Double(x3, y3);
		final Point2D p1 = new Point2D.Double(x1, y1);
		final Point2D p2 = new Point2D.Double(x2, y2);
		return distanceToSegment(p1, p2, p3);
	}

	/**
	 * Returns the distance of p3 to the segment defined by p1,p2;
	 * 
	 * @param p1
	 *            First point of the segment
	 * @param p2
	 *            Second point of the segment
	 * @param p3
	 *            Point to which we want to know the distance of the segment
	 *            defined by p1,p2
	 * @return The distance of p3 to the segment defined by p1,p2
	 */
	public static double distanceToSegment(Point2D p1, Point2D p2, Point2D p3) {

		final double xDelta = p2.getX() - p1.getX();
		final double yDelta = p2.getY() - p1.getY();

		if ((xDelta == 0) && (yDelta == 0)) {
			throw new IllegalArgumentException("p1 and p2 cannot be the same point");
		}

		final double u = ((p3.getX() - p1.getX()) * xDelta + (p3.getY() - p1.getY()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point2D closestPoint;
		if (u < 0) {
			closestPoint = p1;
		} else if (u > 1) {
			closestPoint = p2;
		} else {
			closestPoint = new Point2D.Double(p1.getX() + u * xDelta, p1.getY() + u * yDelta);
		}

		return closestPoint.distance(p3);
	}
}
