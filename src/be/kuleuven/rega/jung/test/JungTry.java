package be.kuleuven.rega.jung.test;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class JungTry {
	
	public static void main(String[] args) {
		Graph<Integer,String> original = new DirectedSparseGraph<Integer,String>();
		original.addVertex((Integer)1);
		original.addVertex((Integer)2);
		original.addVertex((Integer)3);
		original.addEdge("Edge1", 1, 2);
		original.addEdge("Edge2", 2, 3);
		
		Layout<Integer, String> layout = new CircleLayout<Integer, String>(original);
		layout.setSize(new Dimension(500,500)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServerMine<Integer,String> vv = new BasicVisualizationServerMine<Integer, String>(layout, new Dimension(350,350));
		vv.setRenderer(new BasicRendererMine<Integer, String>());
		//vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		//frame.pack();
		frame.setVisible(true);
		frame.setBounds(new Rectangle(500, 500));
		}
	
}
