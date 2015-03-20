package be.kuleuven.rega.webapp;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Random;

import jebl.evolution.graphs.Node;
import jebl.evolution.trees.RootedTree;

import org.apache.commons.collections15.Transformer;

import be.kuleuven.rega.clustering.GraphVertex;
import be.kuleuven.rega.phylogeotool.interfaces.ICluster;
import be.kuleuven.rega.phylogeotool.tree.tools.JeblTools;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class GraphProperties<V, E> {

	private static final String[] indexcolors = new String[]{
        "#FFFFFF", "#FFFF00", "#1CE6FF", "#FF34FF", "#FF4A46", "#008941", "#006FA6", "#A30059",
        "#FFDBE5", "#7A4900", "#0000A6", "#63FFAC", "#B79762", "#004D43", "#8FB0FF", "#997D87",
        "#5A0007", "#809693", "#FEFFE6", "#1B4400", "#4FC601", "#3B5DFF", "#4A3B53", "#FF2F80",
        "#61615A", "#BA0900", "#6B7900", "#00C2A0", "#FFAA92", "#FF90C9", "#B903AA", "#D16100",
        "#DDEFFF", "#000035", "#7B4F4B", "#A1C299", "#300018", "#0AA6D8", "#013349", "#00846F",
        "#372101", "#FFB500", "#C2FFED", "#A079BF", "#CC0744", "#C0B9B2", "#C2FF99", "#001E09",
        "#00489C", "#6F0062", "#0CBD66", "#EEC3FF", "#456D75", "#B77B68", "#7A87A1", "#788D66",
        "#885578", "#FAD09F", "#FF8A9A", "#D157A0", "#BEC459", "#456648", "#0086ED", "#886F4C",

        "#34362D", "#B4A8BD", "#00A6AA", "#452C2C", "#636375", "#A3C8C9", "#FF913F", "#938A81",
        "#575329", "#00FECF", "#B05B6F", "#8CD0FF", "#3B9700", "#04F757", "#C8A1A1", "#1E6E00",
        "#7900D7", "#A77500", "#6367A9", "#A05837", "#6B002C", "#772600", "#D790FF", "#9B9700",
        "#549E79", "#FFF69F", "#201625", "#72418F", "#BC23FF", "#99ADC0", "#3A2465", "#922329",
        "#5B4534", "#FDE8DC", "#404E55", "#0089A3", "#CB7E98", "#A4E804", "#324E72", "#6A3A4C",
        "#83AB58", "#001C1E", "#D1F7CE", "#004B28", "#C8D0F6", "#A3A489", "#806C66", "#222800",
        "#BF5650", "#E83000", "#66796D", "#DA007C", "#FF1A59", "#8ADBB4", "#1E0200", "#5B4E51",
        "#C895C5", "#320033", "#FF6832", "#66E1D3", "#CFCDAC", "#D0AC94", "#7ED379", "#012C58"
	};
	
	public Transformer<V, Paint> getDefaultVertexColor() {
		Transformer<V, Paint> vertexColor = new Transformer<V, Paint>() {
			public Paint transform(V i) {
				return new Color(0);
			}
		};
		return vertexColor;
	}

	public Transformer<V, Paint> getVertexColor(final GraphWidget<V,E> graphWidget, final RootedTree tree) {
		Transformer<V, Paint> vertexColor = new Transformer<V, Paint>() {
			public Paint transform(V i) {
				if(i instanceof Node) {
					if(graphWidget.getForest().getChildCount(i) == 0) {
						List<Node> leafs = JeblTools.getLeaves(tree, (Node)i);
						for(Node tempNode:leafs) {
							return graphWidget.getMappedColorNode().get(tempNode);
						}
					}
				}
				return new Color(000000);
			}
		};
		return vertexColor;
	}
	
	public void setNodeColor(GraphWidget<V,E> graphWidget, Forest<V, E> original, RootedTree tree) {
		int i = 0;

		for(V v:original.getVertices()) {
			if(graphWidget.getForest().getChildCount(v) == 0) {
				List<Node> leafs = JeblTools.getLeaves(tree, (Node)v);
				for(Node tempNode:leafs) {
					graphWidget.addColor(tempNode, Color.decode(indexcolors[i]));
				}
				i++;
			}
		}
	}

	public Transformer<V, Shape> getVertexSizeAccordingNrOfElements(final GraphWidget<V,E> graphWidget, final RootedTree tree) {
		Transformer<V, Shape> vertexSize = new Transformer<V, Shape>() {
			public Shape transform(V i) {
				Shape shape = new Ellipse2D.Double(0, 0, graphWidget.getSize(), graphWidget.getSize());
				if(i instanceof Node) {
					shape = new Ellipse2D.Double(0-5/2, 0-5/2, 5, 5);
					//shape = AffineTransform.getTranslateInstance(-10,-10).createTransformedShape(shape);
					Node node = (Node)i;
					if(graphWidget.getForest().getChildCount(i) == 0) {
						if(JeblTools.getLeaves(tree, node).size() < 5) {
							return shape;
						}
//						} else if(JeblTools.getLeaves(tree, node).size() < 10) {
//							return AffineTransform.getScaleInstance(1.5,1.5).createTransformedShape(shape);
//						} else if(JeblTools.getLeaves(tree, node).size() < 25) {
//							return AffineTransform.getScaleInstance(2,2).createTransformedShape(shape);
//						} else {
//							return AffineTransform.getScaleInstance(2.5,2.5).createTransformedShape(shape);
//						}
						
//						System.out.println(JeblTools.getLeaves(tree, node).size());
//						System.out.println(20*JeblTools.getLeaves(tree, node).size());
						
						Double radius = Math.sqrt((5*JeblTools.getLeaves(tree, node).size())/Math.PI);
//						System.out.println(radius);
						shape = new Ellipse2D.Double(0 - radius/2,0 - radius/2, radius, radius);
					} else {
						return new Ellipse2D.Double(0-5/2, 0-5/2, 5, 5);
					}

				}

				return shape;
			}
		};
		return vertexSize;
	}

	public Transformer<V, String> getDefaultVertexLabel(final GraphWidget<V,E> graphWidget, final RootedTree tree) {
		Transformer<V, String> vertexLabel = new Transformer<V, String>() {
			public String transform(V i) {
				if (i instanceof GraphVertex) {
					GraphVertex graphVertex = (GraphVertex) i;
					return Integer.toString(graphVertex.getSize());
				} else if(i instanceof Node) {
					Node node = (Node)i;
					if(graphWidget.getForest().getChildCount(i) == 0) {
						return Integer.toString(JeblTools.getLeaves(tree, node).size());
					}
				}
				return "";
			}
		};
		return vertexLabel;
	}

	public void setDefaultPickedStateListener(final PickedState<V> pickedState, final GraphWidget<V, E> graphWidget) {
		pickedState.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object subject = e.getItem();
				V vertex = (V) subject;
				if (pickedState.isPicked(vertex)) {
					if (vertex instanceof Node) {
						Node node = (Node) vertex;
						graphWidget.resetMappedColorNode();
						Forest<V,E> forest = graphWidget.getKMedoidsToPhylo().getJungGraph(graphWidget.getJeblTree(), node, graphWidget.getKMedoidsToPhylo().getNrOfClusters());
						//forest.gete
						graphWidget.setForest(forest);
						Layout<V, E> layout = new TreeLayout<V, E>(forest);
						graphWidget.getVisualisationViewer().setGraphLayout(layout);
						setNodeColor(graphWidget, forest, graphWidget.getJeblTree());
						graphWidget.writeTree();
						graphWidget.update();
					}
				} else {
					System.out.println("Vertex " + vertex + " no longer selected");
				}
			}
		});
	}
	
	public Transformer<V, String> getVertexInformation() {
		Transformer<V, String> vertexLabel = new Transformer<V, String>() {
			public String transform(V vertex) {
				if(vertex instanceof Node) {
					System.out.println("Tooltip");
				}
				return "";
			}
		};
		return vertexLabel;
	}
}
