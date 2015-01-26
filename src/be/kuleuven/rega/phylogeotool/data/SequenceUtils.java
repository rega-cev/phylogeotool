package be.kuleuven.rega.phylogeotool.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SequenceUtils {
	
	public static Set<Character> getDegenerateNucleotideBases(char nucleotide) {
		switch(Character.toLowerCase(nucleotide)) {
		case '-': return new HashSet<Character>(Arrays.asList('-'));
			case 'a': return new HashSet<Character>(Arrays.asList('a'));
			case 'c': return new HashSet<Character>(Arrays.asList('c'));
			case 'g': return new HashSet<Character>(Arrays.asList('g'));
			case 't': return new HashSet<Character>(Arrays.asList('t'));
			case 'w': return new HashSet<Character>(Arrays.asList('a','t'));
			case 's': return new HashSet<Character>(Arrays.asList('c','g'));
			case 'm': return new HashSet<Character>(Arrays.asList('a','c'));
			case 'k': return new HashSet<Character>(Arrays.asList('g','t'));
			case 'r': return new HashSet<Character>(Arrays.asList('a','g'));
			case 'y': return new HashSet<Character>(Arrays.asList('c','t'));
			case 'b': return new HashSet<Character>(Arrays.asList('c','g','t'));
			case 'd': return new HashSet<Character>(Arrays.asList('a','g','t'));
			case 'h': return new HashSet<Character>(Arrays.asList('a','c','t'));
			case 'v': return new HashSet<Character>(Arrays.asList('a','c','g'));
			case 'n': return new HashSet<Character>(Arrays.asList('a','c','g','t'));
		}
		return null;
	}
	
	/**
	 * @param nucleotide1
	 * @param nucleotide2
	 * Nucleotide1 is a generalization of nucleotide2
	 * 	@return 1
	 * Nucleotide1 is equal to nucleotide2
	 * 	@return 0
	 * Nucleotide1 is not a generalization of nucleotide2
	 * 	@return -1
	 */
	public static int isGeneralization(char nucleotide1, char nucleotide2) {
		if(nucleotide1 == nucleotide2) {
			return 0;
		} else if(getDegenerateNucleotideBases(Character.toLowerCase(nucleotide1)).containsAll(getDegenerateNucleotideBases(Character.toLowerCase(nucleotide2)))) {
			return 1;
		} else {
			return -1;
		}
	}

}
