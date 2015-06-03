//package be.kuleuven.rega.phylogeotool.tree.tools.test;
//
//import static org.junit.Assert.assertEquals;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.junit.Test;
//
//import be.kuleuven.rega.phylogeotool.tree.Node;
//import be.kuleuven.rega.phylogeotool.tree.tools.DistanceProvider;
//import be.kuleuven.rega.phylogeotool.tree.tools.NodeIndexProvider;
//
//public class DistanceProviderTest {
//	
//	@Test
//	public void testDistanceMatrix() {
//		try {
//			DistanceProvider distanceProvider = new DistanceProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/distances.csv"));
//			
//			assertEquals(0,distanceProvider.getDistance(0, 0),0.0);
//			assertEquals(0.115,distanceProvider.getDistance(0, 1),0.0);
//			assertEquals(0.106,distanceProvider.getDistance(0, 2),0.0);
//			assertEquals(0.026,distanceProvider.getDistance(1, 2),0.0);
//			assertEquals(0.118,distanceProvider.getDistance(0, 3),0.0);
//			assertEquals(0.03,distanceProvider.getDistance(1, 3),0.0);
//			assertEquals(0.031,distanceProvider.getDistance(2, 3),0.0);
//
//			// Last lines of the file
//			assertEquals(0.117,distanceProvider.getDistance(3164, 0),0.0);
//			assertEquals(0.039,distanceProvider.getDistance(3164, 1),0.0);
//			assertEquals(0.037,distanceProvider.getDistance(3164, 2),0.0);
//			
//			assertEquals(0.066,distanceProvider.getDistance(3164, 3161),0.0);
//			assertEquals(0.072,distanceProvider.getDistance(3164, 3162),0.0);
//			assertEquals(0.046,distanceProvider.getDistance(3164, 3163),0.0);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//	}
//	
//	@Test
//	public void testGetDistanceEasy() {
//		try {
//			NodeIndexProvider nodeIndexProvider = new NodeIndexProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/outputSubGRemovedHighMutRegion.csv")); 
//			DistanceProvider distanceProvider = new DistanceProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/distances.csv"));
//
//			Node node1 = new Node("10030_66215_2002", nodeIndexProvider);
//			Node node2 = new Node("10071_92658_2003",nodeIndexProvider);
//			
//			assertEquals(0.026, distanceProvider.getDistance(node1, node2),0.0);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetDistance() {
//		try {
//			NodeIndexProvider nodeIndexProvider = new NodeIndexProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/outputSubGRemovedHighMutRegion.csv")); 
//			DistanceProvider distanceProvider = new DistanceProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/distances.csv"));
//			
//			Node node1 = new Node("48533_153627_2007", nodeIndexProvider);
//			Node node2 = new Node("759671_BM78428_2013",nodeIndexProvider);
//			
//			assertEquals(0.022, distanceProvider.getDistance(node1, node2),0.0);
////			System.out.println(distanceProvider.getDistance(2304, 3135));
////			System.out.println(distanceProvider.getDistance(3135, 2304));
//			
////			DistanceProvider distanceProvider = new DistanceProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/testCsv.csv"));
////			assertEquals(0,distanceProvider.getDistance(0, 0),0.0);
////			assertEquals(0.115,distanceProvider.getDistance(0, 1),0.0);
////			assertEquals(0.115,distanceProvider.getDistance(1, 0),0.0);
////			assertEquals(0.106,distanceProvider.getDistance(0, 2),0.0);
////			assertEquals(0.106,distanceProvider.getDistance(2, 0),0.0);
////			assertEquals(0.026,distanceProvider.getDistance(1, 2),0.0);
////			assertEquals(0.026,distanceProvider.getDistance(2, 1),0.0);
////			assertEquals(0.118,distanceProvider.getDistance(0, 3),0.0);
////			assertEquals(0.118,distanceProvider.getDistance(3, 0),0.0);
////			assertEquals(0.03,distanceProvider.getDistance(1, 3),0.0);
////			assertEquals(0.03,distanceProvider.getDistance(3, 1),0.0);
////			assertEquals(0.031,distanceProvider.getDistance(2, 3),0.0);
////			assertEquals(0.031,distanceProvider.getDistance(3, 2),0.0);
////			assertEquals(0.028,distanceProvider.getDistance(5, 3),0.0);
////			assertEquals(0.028,distanceProvider.getDistance(3, 5),0.0);
//			
//			node1 = new Node("10030_66215_2002", nodeIndexProvider);
//			node2 = new Node("10071_92658_2003",nodeIndexProvider);
//			assertEquals(0.026,distanceProvider.getDistance(node1, node2),0.0);
//			
//			node1 = new Node("Ref.B.FR.83.HXB2_LAI_IIIB_BRU.K03455", nodeIndexProvider);
//			node2 = new Node("10872_57303_2001",nodeIndexProvider);
//			assertEquals(0.108,distanceProvider.getDistance(node1, node2),0.0);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGet2DDistance() {
//		try {
//			NodeIndexProvider nodeIndexProvider = new NodeIndexProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/testNodeIndexProvider.csv"));
//			DistanceProvider distanceProvider = new DistanceProvider(new File("/home/ewout_kul/projects/phylogeo/phylogeotool/lib/testDistances.csv"));
//		
//			Node node1 = new Node("Node1", nodeIndexProvider);
//			Node node2 = new Node("Node4",nodeIndexProvider);
//			
//			assertEquals(2.0, distanceProvider.getDistance(node1, node2),0.0);
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		
//	}
//
//}
