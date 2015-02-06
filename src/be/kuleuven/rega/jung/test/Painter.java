package be.kuleuven.rega.jung.test;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import eu.webtoolkit.jwt.WBrush;
import eu.webtoolkit.jwt.WColor;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;
import eu.webtoolkit.jwt.utils.WebGraphics2D;

public class Painter extends WPaintedWidget {

	private static Logger logger = LoggerFactory.getLogger(PaintBrush.class);

	public Painter(WContainerWidget parent) {
		super(parent);
		this.resize(new WLength(400), new WLength(400));
	}
	
	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		painter.setBrush(new WBrush(WColor.blue).clone());
		WebGraphics2DMine graphics = new WebGraphics2DMine(painter);
		//graphics.setColor(Color.blue);
		//graphics.drawRect(0, 0, 100, 50);
		
		Graph<Integer,String> original = new DirectedSparseGraph<Integer,String>();
		original.addVertex((Integer)1);
		original.addVertex((Integer)2);
		original.addVertex((Integer)3);
		original.addEdge("Edge1", 1, 2);
		original.addEdge("Edge2", 2, 3);
		
		Layout<Integer, String> layout = new CircleLayout<Integer, String>(original);
		layout.setSize(new Dimension(300,300)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		VisualizationImageServer<Integer,String> vv = new VisualizationImageServer<Integer, String>(layout, new Dimension(350,350));
		//vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
		
//		WPainter.Image image = new WPainter.Image();
		graphics.drawImage(vv.createImage(350,350), 350, 350, null);
//		WPainter painter = new WPainter(paintDevice);
//		painter.setRenderHint(WPainter.RenderHint.Antialiasing);
//		WebGraphics2D graphics = new WebGraphics2D(painter);
//		graphics.setBackground(Color.red);
//		graphics.setColor(Color.red);
//        Rectangle r = graphics.getClipBounds();
//        if (r != null) {
//            graphics.fillRect(r.x,  r.y, r.width, r.height);
//            System.out.println("Done.");
//        } else {
//        	System.out.println("Rectangle is null.");
//        }
	}

}
