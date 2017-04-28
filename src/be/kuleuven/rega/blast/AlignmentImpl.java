package be.kuleuven.rega.blast;

import java.io.File;

public class AlignmentImpl extends Alignment {

	private AlignmentSequenceType sequenceType;
	private File alignmentFile;
	
	public AlignmentImpl(AlignmentSequenceType sequenceType, File alignmentFile) {
		this.sequenceType = sequenceType;
		this.alignmentFile = alignmentFile;
	}
	
	@Override
	public AlignmentSequenceType getSequenceType() {
		return this.sequenceType;
	}

	@Override
	public File getFileHandle() {
		return this.alignmentFile;
	}

}
