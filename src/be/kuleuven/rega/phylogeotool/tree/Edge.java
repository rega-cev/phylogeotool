package be.kuleuven.rega.treedraw;

public class Edge {

	private String label;
	private Node node1;
	private Node node2;
	private double distance;

	public Edge(String label, Node node1, Node node2, double distance) {
		setLabel(label);
		setNode1(node1);
		setNode2(node2);
		setDistance(distance);
	}
	
	public Edge(String label, Node node1, Node node2) {
		setLabel(label);
		setNode1(node1);
		setNode2(node2);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Node getNode1() {
		return node1;
	}

	public void setNode1(Node node1) {
		this.node1 = node1;
	}

	public Node getNode2() {
		return node2;
	}

	public void setNode2(Node node2) {
		this.node2 = node2;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
