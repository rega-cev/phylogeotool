package be.kuleuven.rega.webapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jebl.evolution.graphs.Node;
import jebl.evolution.trees.RootedTree;

import org.apache.commons.collections15.Transformer;

import be.kuleuven.rega.clustering.KMedoidsToPhylo;
import be.kuleuven.rega.phylogeotool.tools.NexusExporter;
import be.kuleuven.rega.phylogeotool.tools.TreeTools;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import eu.webtoolkit.jwt.KeyboardModifier;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WMouseEvent.Button;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class GraphWidget<V, E> extends WPaintedWidget {

	private KMedoidsToPhylo<V, E> kMedoidsToPhylo;
	// private Graph<V, E> original;
	private RootedTree jeblTree;
	private Forest<V, E> original;
	private VisualizationViewer<V, E> vv;
	private WebGraphics2DMine graphics = null;
	private Layout<V, E> layout;
	private GraphProperties<V, E> graphProperties;
	private double size;
	private HashMap<Node, Color> mappedColorNode;
	private int nrClusters = 12;

	public GraphWidget(File locationNodeIndexProvider, File locationDistances) throws IOException {
		this.loadGraph(locationNodeIndexProvider, locationDistances, null);
		init();
	}

	public GraphWidget(File locationNodeIndexProvider, File locationDistances, File treeLocation) throws IOException {
		this.loadGraph(locationNodeIndexProvider, locationDistances, treeLocation);
		init();
	}

	public void init() {
		this.graphProperties = new GraphProperties<V, E>();
		//layout = new TreeLayout<V, E>(original);
		int distx = 50;
		int disty = 35;
		
		layout = new TreeLayout<V,E>(original, (int)(disty * (16d/nrClusters)), distx);
		// ((SpringLayout)layout).setForceMultiplier(0.0);
		layout.setGraph(original);
		mappedColorNode = new HashMap<Node, Color>();

		vv = new VisualizationViewer<V, E>(layout);
		vv.setRenderer(new BasicRenderer<V, E>());
		this.graphProperties.setNodeColor(this, original, getJeblTree());
		this.setVertexColor(graphProperties.getVertexColor(this, getJeblTree()));
		this.setVertexSize(graphProperties.getVertexSizeAccordingNrOfElements(this, getJeblTree()));
		this.setVertexLabel(graphProperties.getDefaultVertexLabel(this, getJeblTree()));
		this.setEdgeShape();
		Dimension d = layout.getSize();
		Point2D center = new Point2D.Double(d.width / 2, d.height / 2);
		vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).rotate(-Math.PI / 2, center);

		final DefaultModalGraphMouse<Integer, String> modalMouse = new DefaultModalGraphMouse<Integer, String>();
		modalMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(modalMouse);

		this.setMouseListeners(modalMouse);

		final PickedState<V> pickedState = vv.getPickedVertexState();
		vv.setVertexToolTipTransformer(graphProperties.getVertexInformation());
		graphProperties.setDefaultPickedStateListener(pickedState, this);
	}

	private void setMouseListeners(final DefaultModalGraphMouse<Integer, String> modalMouse) {
		doubleClicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				modalMouse.mousePressed(convertMouseEvent(event, MouseEvent.MOUSE_PRESSED));
			}
		});

//		mouseMoved().addListener(this, new Signal1.Listener<WMouseEvent>() {
//			@Override
//			public void trigger(WMouseEvent event) {
//				modalMouse.mouseMoved(convertMouseEvent(event, MouseEvent.MOUSE_MOVED));
//			}
//		});
//
//		mouseWentDown().addListener(this, new Signal1.Listener<WMouseEvent>() {
//			@Override
//			public void trigger(WMouseEvent event) {
//				modalMouse.mousePressed(convertMouseEvent(event, MouseEvent.MOUSE_PRESSED));
//			}
//		});
//
//		mouseWentUp().addListener(this, new Signal1.Listener<WMouseEvent>() {
//			@Override
//			public void trigger(WMouseEvent event) {
//				modalMouse.mouseReleased(convertMouseEvent(event, MouseEvent.MOUSE_RELEASED));
//			}
//		});
//
//		mouseDragged().addListener(this, new Signal1.Listener<WMouseEvent>() {
//			@Override
//			public void trigger(WMouseEvent event) {
//				modalMouse.mouseDragged(convertMouseEvent(event, MouseEvent.MOUSE_DRAGGED));
//			}
//		});
	}

	private void setVertexLabel(Transformer<V, String> vertexLabel) {
		vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
	}

	private void setVertexSize(Transformer<V, Shape> vertexSize) {
		vv.getRenderContext().setVertexShapeTransformer(vertexSize);
	}

	private void setVertexColor(Transformer<V, Paint> vertexColor) {
		vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
	}

	private void setEdgeShape() {
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
	}

	private MouseEvent convertMouseEvent(WMouseEvent event, int type) {
		int modifiers = 0;

		if (event.getModifiers().contains(KeyboardModifier.AltModifier))
			modifiers |= MouseEvent.ALT_DOWN_MASK;
		if (event.getModifiers().contains(KeyboardModifier.ShiftModifier))
			modifiers |= MouseEvent.SHIFT_DOWN_MASK;
		if (event.getModifiers().contains(KeyboardModifier.ControlModifier))
			modifiers |= MouseEvent.CTRL_DOWN_MASK;
		if (event.getModifiers().contains(KeyboardModifier.MetaModifier))
			modifiers |= MouseEvent.META_DOWN_MASK;

		int button = 0;

		if (event.getButton() == Button.LeftButton) {
			modifiers |= MouseEvent.BUTTON1_DOWN_MASK;
			button = MouseEvent.BUTTON1;
		} else if (event.getButton() == Button.MiddleButton) {
			modifiers |= MouseEvent.BUTTON2_DOWN_MASK;
			button = MouseEvent.BUTTON2;
		} else if (event.getButton() == Button.RightButton) {
			modifiers |= MouseEvent.BUTTON3_DOWN_MASK;
			button = MouseEvent.BUTTON3;
		}

		return new MouseEvent(vv, type, System.currentTimeMillis(), modifiers, event.getWidget().x, event.getWidget().y, 1,
				event.getButton() == Button.RightButton, button);
	}

	public void loadGraph(File nodeIndexProvider, File locationDistances, File treeLocation) throws IOException {
		kMedoidsToPhylo = new KMedoidsToPhylo<V, E>(nodeIndexProvider, locationDistances);
		if (treeLocation != null) {
			jeblTree = (RootedTree) TreeTools.readNewickTree(new FileReader(treeLocation));
			this.setGraph(kMedoidsToPhylo.getJungGraph(getJeblTree(), null, nrClusters));
		} else {
			this.setGraph(kMedoidsToPhylo.getJungGraph());
		}
	}

	public Forest<V, E> getGraph() {
		return this.original;
	}

	public void setGraph(Forest<V, E> graph) {
		this.original = graph;
	}

	public KMedoidsToPhylo<V, E> getKMedoidsToPhylo() {
		return kMedoidsToPhylo;
	}

	public Layout<V, E> getLayout() {
		return layout;
	}

	public void setLayout(Layout<V, E> layout) {
		this.layout = layout;
	}

	public VisualizationViewer<V, E> getVisualisationViewer() {
		return vv;
	}

	public void setVisualisationViewer(VisualizationViewer<V, E> vv) {
		this.vv = vv;
	}

	@Override
	public void resize(WLength width, WLength height) {
		super.resize(width, height);
		// Also resizes the tree pane
		vv.setSize((int) width.toPixels(), (int) height.toPixels());
	}

	@Override
	protected void layoutSizeChanged(int width, int height) {
		super.layoutSizeChanged(width, height);
		// Also resizes the tree pane
		vv.setSize(width, height);
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public void addColor(Node node, Color color) {
		this.mappedColorNode.put(node, color);
	}

	public void resetMappedColorNode() {
		this.mappedColorNode = new HashMap<Node, Color>();
	}

	public Map<Node, Color> getMappedColorNode() {
		return this.mappedColorNode;
	}

	public RootedTree getJeblTree() {
		return this.jeblTree;
	}

	public Forest<V, E> getForest() {
		return original;
	}

	public void setForest(Forest<V, E> original) {
		this.original = original;
	}

	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		graphics = new WebGraphics2DMine(painter);
		if ((int) paintDevice.getWidth().getValue() >= (int) paintDevice.getHeight().getValue()) {
			this.setSize(paintDevice.getHeight().getValue() / 10);
		} else {
			this.setSize(paintDevice.getWidth().getValue() / 10);
		}
		vv.paint(graphics);
	}
	
	public void writeTree() {
		NexusExporter.export(kMedoidsToPhylo.getJeblTree(), mappedColorNode);
	}
}
