package be.kuleuven.rega.jung.test;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMemoryResource;
import eu.webtoolkit.jwt.WPainter;
import eu.webtoolkit.jwt.utils.WebGraphics2D;

public class GraphTest extends WApplication {

	private Graph<Integer,String> original;
	private WebGraphics2DMine graphics = new WebGraphics2DMine(new WPainter());
	
	public GraphTest(WEnvironment env) {
		super(env);
		setTitle("GraphTest");
		original = new DirectedSparseGraph<Integer,String>();
		original.addVertex((Integer)1);
		original.addVertex((Integer)2);
		original.addVertex((Integer)3);
		original.addEdge("Edge1", 1, 2);
		original.addEdge("Edge2", 2, 3);
		
		Layout<Integer, String> layout = new CircleLayout<Integer, String>(original);
		layout.setSize(new Dimension(300,300)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		VisualizationImageServer<Integer,String> vv = new VisualizationImageServer<Integer, String>(layout, new Dimension(350,350));
		
		//WPaintedWidget wPaintedWidget = new Painter(getRoot());
		//getRoot().addWidget(wPaintedWidget);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		BufferedImage image = toBufferedImage(vv.getImage(new Point(0,0), new Dimension(350,350)));
		try {
			ImageIO.write(image, "png", outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		WImage wImage = new WImage(getRoot());
		WMemoryResource wMemoryResource = new WMemoryResource();
		wMemoryResource.setData(outputStream.toByteArray());
		wImage.setImageLink(new WLink(wMemoryResource));
		getRoot().addWidget(wImage);
	}
	
	public Graph<Integer,String> getGraph() {
		return this.original;
	}
	
	private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }
}
