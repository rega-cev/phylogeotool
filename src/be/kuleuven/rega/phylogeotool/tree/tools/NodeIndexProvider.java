package be.kuleuven.rega.phylogeotool.tree.tools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import be.kuleuven.rega.phylogeotool.data.csv.CsvUtils;

public class NodeIndexProvider {
	
	private HashMap<String,Integer> labelToIndexMap = null;
	
	public NodeIndexProvider(File csv) throws IOException {
		if(csv != null) {
			labelToIndexMap = CsvUtils.getNodeLabelToIndexMapping(csv);
		}
	}
	
	public int getIndex(String label) {
		//System.out.println(label);
		if(labelToIndexMap != null) {
			return labelToIndexMap.get(label);
		} else {
			return Integer.parseInt(label);
		}
	}
	
}
