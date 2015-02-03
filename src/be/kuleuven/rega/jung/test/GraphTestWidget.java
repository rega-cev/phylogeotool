package be.kuleuven.rega.jung.test;

import java.awt.Graphics;

import javax.swing.JComponent;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;
import eu.webtoolkit.jwt.utils.WebGraphics2D;

public class GraphTestWidget extends WPaintedWidget {

	private Graph<Integer, String> original;
	private JComponent visualizationImageServer;
	private WebGraphics2D graphics = new WebGraphics2D(new WPainter());

	public GraphTestWidget() {
		original = new DirectedSparseGraph<Integer, String>();
		/*original.addVertex((Integer) 1);
		original.addVertex((Integer) 2);
		original.addVertex((Integer) 3);
		original.addEdge("Edge1", 1, 2);
		original.addEdge("Edge2", 2, 3);*/

		Layout<Integer, String> layout = new CircleLayout<Integer, String>(
				original);
		//layout.setSize(new Dimension(300, 300)); // sets the initial size of the
													// space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		// VisualizationImageServer is a JComponent
		visualizationImageServer = new VisualizationViewer<Integer, String>(layout) {
			@Override
			public void repaint() {
				// Signal that we need a repaint
				GraphTestWidget.this.update();
			}
			
			/*@Override
			public Graphics getGraphics() {
				return graphics;
			}*/
		};

	}

	public Graph<Integer, String> getGraph() {
		return this.original;
	}

	@Override
	public void resize(WLength width, WLength height) {
		super.resize(width, height);
		// Also resizes the tree pane
		visualizationImageServer.setSize((int) width.toPixels(),
				(int) height.toPixels());
	}

	@Override
	protected void layoutSizeChanged(int width, int height) {
		super.layoutSizeChanged(width, height);
		// Also resizes the tree pane
		visualizationImageServer.setSize(width, height);
	}

	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		// Let the javax.swing.JComponent paint itself on the JWt paint-device
		WPainter painter = new WPainter(paintDevice);
		Graphics graphics = new WebGraphics2D(painter);
		visualizationImageServer.paint(graphics);
	}

}
