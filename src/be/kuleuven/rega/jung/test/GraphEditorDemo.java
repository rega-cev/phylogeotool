package be.kuleuven.rega.jung.test;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class GraphEditorDemo {

	public static void main(String[] args) throws IOException {
		// Creating a graph to demonstrate.
		Graph<Integer, String> basis = new SparseMultigraph<Integer, String>();
		basis.addVertex(Integer.valueOf(0));
		basis.addVertex(Integer.valueOf(1));
		basis.addVertex(Integer.valueOf(2));
		basis.addEdge("Edge 1", Integer.valueOf(0), Integer.valueOf(1));
		basis.addEdge("Edge 2", Integer.valueOf(0), Integer.valueOf(2));
		basis.addEdge("Edge 3", Integer.valueOf(1), Integer.valueOf(2));
		final Layout<Integer, String> layout = new CircleLayout<Integer, String>(basis);
		layout.setSize(new Dimension(300, 300));
		VisualizationViewer<Integer, String> vv = new VisualizationViewer<Integer, String>(layout);
		vv.setPreferredSize(new Dimension(350, 350));
		// Attach the listener for listening to when vertices are selected (or
		// deselected).
		// You can also listen for changes to the selection of edges, by using
		// vv.getPickedEdgeState() in
		// place of vv.getPickedVertexState().
		final PickedState<Integer> pickedState = vv.getPickedVertexState();
		pickedState.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object subject = e.getItem();
				if (subject instanceof Integer) {
					Integer vertex = (Integer) subject;
					if (pickedState.isPicked(vertex)) {
						System.out.println("Vertex " + vertex + " is now selected");
					} else {
						System.out.println("Vertex " + vertex + " no longer selected");
					}
				}
			}
		});
		// Set the mouse to "picking" mode so that vertices and edges can be
		// selected.
		DefaultModalGraphMouse<Integer, String> modalMouse = new DefaultModalGraphMouse<Integer, String>();
		modalMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(modalMouse);
		// Set up rendering for the vertices.
		//RenderContext<Integer, String> renderContext = vv.getRenderContext();
		//renderContext.setVertexLabelTransformer(new ToStringLabeller<Integer>());
		//renderContext.setEdgeLabelTransformer(new ToStringLabeller<String>());
		//vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		// Draw the graph on screen.
		JFrame frame = new JFrame("Simple Graph View 2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

}
