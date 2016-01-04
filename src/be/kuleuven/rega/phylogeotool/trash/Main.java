package be.kuleuven.rega.phylogeotool.trash;

//package be.kuleuven.rega.treedraw.test;
//
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;

public class Main extends WtServlet {

	@Override
	public WApplication createApplication(WEnvironment env) {
		return new Application(env);
	}
	
}

//public class Main {
//	
//	public static void main(String args[]) throws FileNotFoundException {
//		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/TDRDetector/fullPortugal/trees/fullTree.Midpoint.tree"));
////		jebl.evolution.trees.Tree jeblTree = ReadNewickTree.readNewickTree(new FileReader("/Users/ewout/Documents/phylogeo/TestCases/Portugal/besttree.997.midpoint.solved.newick"));
////		jebl.evolution.trees.Tree jeblTree = ReadTree.readTree(new FileReader("/Users/ewout/Documents/phylogeo/TestCases/testTree.phylo"));
//		Tree tree = ReadTree.jeblToTreeDraw((SimpleRootedTree)jeblTree, new ArrayList<String>());
//	}
//	
//}