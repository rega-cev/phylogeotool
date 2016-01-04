package be.kuleuven.rega.treedraw;

public class DrawData {
	private double x;
	private double y;
	private double theta;

	public DrawData(double x, double y, double theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	public double getTheta() {
		return theta;
	}
}