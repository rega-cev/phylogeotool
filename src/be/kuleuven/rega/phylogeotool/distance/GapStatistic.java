package be.kuleuven.rega.phylogeotool.distance;

import java.util.Iterator;

import be.kuleuven.rega.phylogeotool.tree.Node;
import be.kuleuven.rega.phylogeotool.tree.Tree;

public class GapStatistic {

	public GapStatistic() {
		
	}
	
	public double Wk(Tree fullTree, Tree tree, int k) {
		double sum = 0.0;
		double Dr = 0.0;

		if(k == 1) {
			Dr = Dr(fullTree, tree.getRootNode());
			sum += (1d/(2*(fullTree.getRootNode().getLeaves().size()))) * Dr;
		} else {
			for(Node node:tree.getLeaves()) {
				Dr = Dr(fullTree, node);
				sum += (1d/(2*(node.getSize()))) * Dr;
			}
		}
		
		return sum;
	}
	
	public double Dr(Tree fullTree, Node startNode) {
		/* Sum of the pairwise distances */
		double fullDistance = 0.0;
		/* If array looks like this: [1,2,3,4,5]
		 * We do the following: Calculate distances (1,2), (1,3), (1,4), (1,5)
		 * Than calculate distances (2,3), (2,4), (2,5)
		 * Than calculate distances (3,4), (3,5)
		 * Finally (4,5)
		 */
		for(Node node:fullTree.getNodeById(startNode.getId()).getLeaves()) {
			Iterator<Node> nodesIterator = fullTree.getNodeById(startNode.getId()).getLeaves().iterator();
			boolean foundNode = false;
			while(nodesIterator.hasNext()) {
				Node currentNode = nodesIterator.next();
				if(currentNode.equals(node)) {
					// Do distance calculations from here to the end.
					foundNode = true;
				}
				
				if(foundNode && !currentNode.equals(node)) {
					fullDistance += fullTree.getDistance(node, currentNode);
				}
			}
		}
		return fullDistance;
	}
	
}
