//package be.kuleuven.rega.webapp.widgets;
//
//import java.awt.Color;
//import java.io.File;
//import java.io.IOException;
//
//import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
//import org.apache.batik.bridge.BridgeContext;
//import org.apache.batik.bridge.DocumentLoader;
//import org.apache.batik.bridge.GVTBuilder;
//import org.apache.batik.bridge.UserAgent;
//import org.apache.batik.bridge.UserAgentAdapter;
//import org.apache.batik.gvt.GraphicsNode;
//import org.apache.batik.util.XMLResourceDescriptor;
//import org.w3c.dom.svg.SVGDocument;
//
//import be.kuleuven.rega.webapp.WebGraphics2DMine;
//import eu.webtoolkit.jwt.WLength;
//import eu.webtoolkit.jwt.WPaintDevice;
//import eu.webtoolkit.jwt.WPaintedWidget;
//import eu.webtoolkit.jwt.WPainter;
//
//public class SVGWidget extends WPaintedWidget {
//
//	private String fileName;
//	
//	public SVGWidget(String treeRenderLocation, String clusterId) {
//		this.fileName = treeRenderLocation + File.separator + clusterId + ".svg";
//		System.out.println("Reading: " + fileName);
//	}
//	
//	@Override
//	protected void paintEvent(WPaintDevice paintDevice) {
//		WPainter painter = new WPainter(paintDevice);
//		WebGraphics2DMine graphics = new WebGraphics2DMine(painter);
//		graphics.setColor(Color.BLACK);
//		System.out.println("Here");
//		this.setWidth(new WLength(500));
//		this.setHeight(new WLength(500));
//		
//		String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
//		SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
//		SVGDocument doc = null;
//		try {
//			doc = df.createSVGDocument(fileName);
//		} catch (IOException e) {
//		}
//		UserAgent userAgent = new UserAgentAdapter();
//		DocumentLoader loader = new DocumentLoader(userAgent);
//		BridgeContext ctx = new BridgeContext(userAgent, loader);
//		ctx.setDynamicState(BridgeContext.DYNAMIC);
//		GVTBuilder builder = new GVTBuilder();
//		GraphicsNode rootGN = builder.build(ctx, doc);
//		rootGN.paint(graphics);
//	}
//	
//}
