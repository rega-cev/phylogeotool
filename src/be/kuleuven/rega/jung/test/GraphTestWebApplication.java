package be.kuleuven.rega.jung.test;

import eu.webtoolkit.jwt.AlignmentFlag;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WVBoxLayout;


public class GraphTestWebApplication extends WApplication {

	private GraphTestWidget graphTestWidget;
	
	public GraphTestWebApplication(WEnvironment env) {
		super(env);
		setTitle("PhyloGeoTool");
		
		WVBoxLayout layout = new WVBoxLayout(getRoot());
		WHBoxLayout controlsLayout = new WHBoxLayout();
		layout.addLayout(controlsLayout, 0, AlignmentFlag.AlignJustify, AlignmentFlag.AlignTop);
//		WPushButton pb = new WPushButton("Click Me!");
//		
//		pb.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
//
//			@Override
//			public void trigger(WMouseEvent arg) {
//				layout.addWidget(graphTestWidget = new GraphTestWidget(), 1);
//				layout.removeWidget(pb);
//			}
//			
//		});
		
		layout.addWidget(graphTestWidget = new GraphTestWidget(), 1);
		//graphTestWidget.getGraph().repaint();
	}
	
}
