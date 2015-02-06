package be.kuleuven.rega.jung.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedState;
import eu.webtoolkit.jwt.KeyboardModifier;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WMouseEvent.Button;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class GraphTestWidget extends WPaintedWidget {

	private Graph<Integer, String> original;
	private VisualizationViewer<Integer, String> vv;
	private WebGraphics2DMine graphics = null;

	public GraphTestWidget() {
		original = new DirectedSparseGraph<Integer, String>();
		original.addVertex((Integer) 1);
		original.addVertex((Integer) 2);
		original.addVertex((Integer) 3);
		original.addEdge("Edge1", 1, 2);
		original.addEdge("Edge2", 2, 3);
		original.addEdge("Edge3", 3, 1);

		final Layout<Integer, String> layout = new CircleLayout<Integer, String>(original);
		layout.setSize(new Dimension(350, 350));
		layout.setGraph(original);
		layout.initialize();
		
		Transformer<Integer,Paint> vertexColor = new Transformer<Integer,Paint>() {
			public Paint transform(Integer i) {
				if(i == 1) return Color.GREEN;
				else if(i==2) return Color.BLUE;
				return Color.RED;
			}
	    };
		
	    vv = new VisualizationViewer<Integer, String>(layout);
		vv.setRenderer(new BasicRendererMine<Integer, String>());
		vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
		
		DefaultModalGraphMouse<Integer, String> modalMouse = new DefaultModalGraphMouse<Integer, String>();
		modalMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(modalMouse);
		
		clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				int dx = event.getDragDelta().x;
				int dy = event.getDragDelta().y;
				if (dx*dx + dy*dy < 4) {
					modalMouse.mouseClicked(convertMouseEvent(event, MouseEvent.MOUSE_CLICKED));
				}
			}
		});
		
		mouseWentDown().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				modalMouse.mousePressed(convertMouseEvent(event, MouseEvent.MOUSE_PRESSED));
			}
		});

		mouseWentUp().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				modalMouse.mouseReleased(convertMouseEvent(event, MouseEvent.MOUSE_RELEASED));
			}
		});

		mouseDragged().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				modalMouse.mouseDragged(convertMouseEvent(event, MouseEvent.MOUSE_DRAGGED));
			}
		});
		
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
	}

	MouseEvent convertMouseEvent(WMouseEvent event, int type) {
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
		
		return new MouseEvent(vv, type, System.currentTimeMillis(),
					modifiers, event.getWidget().x, event.getWidget().y, 1, event.getButton() == Button.RightButton, button);
	}
	
	public JComponent getGraph() {
		return this.vv;
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

	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		graphics = new WebGraphics2DMine(painter);
		vv.paint(graphics);
	}
}
