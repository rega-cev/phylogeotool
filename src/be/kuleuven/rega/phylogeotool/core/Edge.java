package be.kuleuven.rega.phylogeotool.core;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Edge {

	private final String label;
	private final Node node1;
	private final Node node2;
	private final double distance;

	public Edge(String label, Node node1, Node node2, double distance) {
		this.label = label;
		this.node1 = node1;
		this.node2 = node2;
		this.distance = distance;
	}
	
	public String getLabel() {
		return label;
	}

	public Node getNode1() {
		return node1;
	}

	public Node getNode2() {
		return node2;
	}

	public double getDistance() {
		return distance;
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
