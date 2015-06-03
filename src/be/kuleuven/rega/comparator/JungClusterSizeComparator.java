package be.kuleuven.rega.comparator;

import java.util.Comparator;

import jebl.evolution.graphs.Node;
import jebl.evolution.trees.RootedTree;
import be.kuleuven.rega.phylogeotool.tree.tools.JeblTools;

public class JungClusterSizeComparator<V> implements Comparator<V> {

	private RootedTree tree;
	
	public JungClusterSizeComparator(RootedTree tree) {
		this.tree = tree;
	}
	
	@Override
	public int compare(V vertex1, V vertex2) {
		
		int nrNodesTree1 = JeblTools.getLeaves(tree, (Node)vertex1).size();
		int nrNodesTree2 = JeblTools.getLeaves(tree, (Node)vertex2).size();
		
		if(nrNodesTree1 < nrNodesTree2) {
			return 1;
		} else if(nrNodesTree1 > nrNodesTree2) {
			return -1;
		} else {
			return 0;
		}
	}

}
