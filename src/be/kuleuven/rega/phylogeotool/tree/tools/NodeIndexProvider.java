package be.kuleuven.rega.phylogeotool.tree.tools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;

public class NodeIndexProvider {
	
	private HashMap<String,Integer> labelToIndexMap;
	
	public NodeIndexProvider(File csv) throws IOException {
		labelToIndexMap = CsvUtils.getNodeLabelToIndexMapping(csv);
	}
	
	public int getIndex(String label) {
		return labelToIndexMap.get(label);
	}
	
}
