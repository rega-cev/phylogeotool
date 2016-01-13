package be.kuleuven.rega.webapp.widgets;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import jebl.evolution.graphs.Node;
import jebl.evolution.taxa.Taxon;
import jebl.evolution.trees.RootedTree;
import jebl.evolution.trees.Tree;
import be.kuleuven.rega.jebl.Utils;
import be.kuleuven.rega.phylogeotool.core.Cluster;
import be.kuleuven.rega.webapp.GraphProperties;
import eu.webtoolkit.jwt.WWidget;
import figtree.treeviewer.decorators.AttributableDecorator;
import figtree.treeviewer.treelayouts.RadialTreeLayout;

public class WFigTreeMine {

	private FigTreeWidget figTreeWidget;

	public WFigTreeMine(Tree tree, Cluster cluster) {
		int width = 500;
		int height = 500;
		boolean showTips = false;

		this.figTreeWidget = new FigTreeWidget();
		this.figTreeWidget.resize(width, height);
		figTreeWidget.getTreePane().setTreeLayout(new RadialTreeLayout());

//		jebl.evolution.trees.RootedTree copyTree = jebl.evolution.trees.Utils.copyTree((RootedTree) tree);
		jebl.evolution.trees.RootedTree copyTree = Utils.copyTree((RootedTree) tree);
//		jebl.evolution.trees.RootedTree copyTree = (RootedTree)tree;
		Map<String, Color> taxonNameToColor = new HashMap<String, Color>();
		Map<be.kuleuven.rega.phylogeotool.core.Node, Color> clusterToColor = GraphProperties.getClusterColor(cluster, 2);

		for (be.kuleuven.rega.phylogeotool.core.Node node : cluster.getBoundaries()) {
			for (be.kuleuven.rega.phylogeotool.core.Node leaf : cluster.getTree().getLeaves(node)) {
				taxonNameToColor.put(leaf.getLabel(), clusterToColor.get(node));
			}
		}

		for (Taxon taxon : copyTree.getTaxa()) {
			taxon.setAttribute("!color", taxonNameToColor.get(taxon.getName()));
			copyTree.getNode(taxon).setAttribute("!color", taxonNameToColor.get(taxon.getName()));
			Node parent = copyTree.getParent(copyTree.getNode(taxon));
			while (!copyTree.isRoot(parent) || parent.getAttribute("!color") != null) {
				parent.setAttribute("!color", taxonNameToColor.get(taxon.getName()));
				parent = copyTree.getParent(parent);
			}
		}

		final AttributableDecorator branchDecorator = new AttributableDecorator();
		branchDecorator.setPaintAttributeName("!color");
		figTreeWidget.getTreePane().setBranchDecorator(branchDecorator, false);

		if (showTips) {
			for (Taxon taxon : copyTree.getTaxa()) {
				if (taxon != null) {
					taxon.setAttribute("!color", taxonNameToColor.get(taxon.getName()));
				}
			}
			SimpleLabelPainter simpleLabelPainterTip = new SimpleLabelPainter(SimpleLabelPainter.PainterIntent.TIP);
			simpleLabelPainterTip.setNumberFormat(new DecimalFormat("#.####"));
			simpleLabelPainterTip.setFont(new Font("sansserif", Font.PLAIN, 8));
			simpleLabelPainterTip.setTextDecorator(branchDecorator);
			figTreeWidget.getTreePane().setTipLabelPainter(simpleLabelPainterTip);
		}

		this.setTree(copyTree);
	}

	private void setTree(RootedTree tree) {
		figTreeWidget.getTreePane().setTree(tree);
	}

	public WWidget getWidget() {
		return this.figTreeWidget;
	}
}
