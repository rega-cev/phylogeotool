package be.kuleuven.rega.phylogeotool.tree;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	
	@Override
	public String toString() {
		return this.label + " " + this.node1.getId() + " to " + this.node2.getId() + " distance: " + this.distance;
	}
	
	@Override
	public boolean equals(Object other) {
		Edge otherEdge = (Edge)other;
		if(this.getNode1().equals(otherEdge.getNode1()) || this.getNode1().equals(otherEdge.getNode2())) {
			if(this.getNode2().equals(otherEdge.getNode1()) || this.getNode2().equals(otherEdge.getNode2())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        if(this.getNode1().getId() < this.getNode2().getId()) {
        	builder.append(this.getNode1().getId()).append(this.getNode2().getId());
        } else {
        	builder.append(this.getNode2().getId()).append(this.getNode1().getId());
        }
		return builder.toHashCode();
    }
}
