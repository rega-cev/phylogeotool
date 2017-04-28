package be.kuleuven.rega.blast;

import java.io.File;

public abstract class Alignment {
	abstract AlignmentSequenceType getSequenceType();
	abstract File getFileHandle();
}