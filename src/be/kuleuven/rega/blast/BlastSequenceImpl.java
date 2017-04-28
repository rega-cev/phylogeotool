package be.kuleuven.rega.blast;

import java.io.File;

public class BlastSequenceImpl extends BlastSequence {

	private int sequenceLength;
	private File sequenceFile;
	
	public BlastSequenceImpl(int sequenceLength, File sequenceFile) {
		this.sequenceFile = sequenceFile;
		this.sequenceLength = sequenceLength;
	}
	
	@Override
	int getLength() {
		return sequenceLength;
	}

	@Override
	File getFileHandle() {
		return sequenceFile;
	}

}
