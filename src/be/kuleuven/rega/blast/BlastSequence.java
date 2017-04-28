package be.kuleuven.rega.blast;

import java.io.File;

public abstract class BlastSequence {
	abstract int getLength();
	abstract File getFileHandle();
}
