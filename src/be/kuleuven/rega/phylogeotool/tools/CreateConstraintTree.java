package be.kuleuven.rega.phylogeotool.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAReader;

public class CreateConstraintTree {

	public static void main(String[] args) {
		try {
//			FASTAReader fastaReader = FASTAReader.getInstance(new File("/Users/ewout/Documents/phylogeo/EUResist_POL/alignment.ninja.noSDRM.fasta"));
//			BufferedReader bufferedReader = new BufferedReader(new FileReader(
//					"/Users/ewout/Documents/phylogeo/EUResist_POL/constraint_tree/constraint_tree.csv"));
//			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
//					"/Users/ewout/Documents/phylogeo/EUResist_POL/constraint_tree/constraint_tree.fasta"));
			FASTAReader fastaReader = FASTAReader.getInstance(new File("/Users/ewout/Documents/phylogeo/EUResist_POL/alignment.ninja.noSDRM.fasta"));
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
			"/Users/ewout/Documents/phylogeo/EUResist_POL/subtyping_attempt2/result.cut.sorted.csv"));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					"/Users/ewout/Documents/phylogeo/EUResist_POL/constraint_tree/full_constraint_tree.txt"));

			
			String line = "";

//			List<String> temp = Arrays.asList("18726", "18748", "18771", "18848", "18909", "18957", "146", "157", "12226", "12228", "4654", "5745", "6363",
//					"6436", "8673", "8750", "9070", "10492", "13283", "15245", "4807", "4837", "7239", "9109", "11315", "12981", "13632", "14775", "20587",
//					"20930", "14", "15", "16", "17", "18", "19", "20", "20938", "32049", "6", "85", "86", "291", "1063", "3770", "4056", "6950", "10044",
//					"18520", "32015", "34098", "34187", "34974", "35373", "37101", "37739", "301", "4297", "4652", "20929", "18663", "20656", "33620", "33678",
//					"37914", "40063", "41194", "41354", "12", "1091", "1125", "41132", "17894", "17919", "17950", "17973", "24668", "24691", "25696", "31677");
			List<String> temp = new ArrayList<String>();
			
			Map<String, Set<String>> subtypes = new HashMap<String, Set<String>>();

			FASTAEntry fastaEntry = null;
			while ((fastaEntry = fastaReader.readNext()) != null) {
				temp.add(fastaEntry.getHeaderLine().replaceAll("\\s+", "").substring(1));
			}
			
			String subtype = "";
			String id = "";
			while ((line = bufferedReader.readLine()) != null) {
				id = line.split(",")[0];
				subtype = line.split(",")[1];
				if (!subtypes.containsKey(subtype)) {
					subtypes.put(subtype, new HashSet<String>());
				}
				if(temp.contains(id)) {
					subtypes.get(subtype).add(id);
				}
			}

//			Queue<String> partTree = new PriorityQueue<String>();
			StringBuilder stringBuilder = new StringBuilder("((");
//			System.out.print("(");
			for (Entry<String, Set<String>> entry : subtypes.entrySet()) {
				Set<String> queue = entry.getValue();
				if(queue.size() > 1) {
					Iterator<String> iterator = queue.iterator();
					while (iterator.hasNext()) {
						String id2 = iterator.next();
						stringBuilder.append(id2 + ",");
					}
					stringBuilder.setLength(stringBuilder.length() - 1);
					stringBuilder.append("),(");
				} else {
					if(!entry.getValue().isEmpty()) {
						stringBuilder.setLength(stringBuilder.length() - 1);
						stringBuilder.append(entry.getValue().iterator().next() + ",(");
					}
				}
			}
			stringBuilder.setLength(stringBuilder.length() - 2);
			stringBuilder.append(");");
			bufferedWriter.write(stringBuilder.toString());
			bufferedWriter.flush();

//			while (!partTree.isEmpty() && (partTree.size() > 1)) {
//				String element1 = partTree.poll();
//				String element2 = partTree.poll();
//				partTree.add("(" + element1 + "," + element2 + ")");
//			}
//
//			System.out.println(partTree.poll());
			
//			FASTAEntry fastaEntry = null;
//			while ((fastaEntry = fastaReader.readNext()) != null) {
//				if (temp.contains(fastaEntry.getHeaderLine().replaceAll("\\s+", "").substring(1))) {
//					bufferedWriter.write(fastaEntry.getHeaderLine() + "\n");
//					bufferedWriter.write(fastaEntry.getSequence() + "\n");
//					bufferedWriter.flush();
//				}
//			}
			bufferedReader.close();
			bufferedWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
