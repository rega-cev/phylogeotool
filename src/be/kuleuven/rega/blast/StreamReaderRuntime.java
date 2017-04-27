package be.kuleuven.rega.blast;

import java.io.File;
import java.io.IOException;

public class StreamReaderRuntime {

	public static Process exec(String command, String[] envp, File dir) throws IOException{
		Runtime rt = Runtime.getRuntime();
		
		Process ps = rt.exec(command, envp, dir);

		StreamReaderThread stdout = new StreamReaderThread(ps.getInputStream(), System.out, "stdout: ");
		stdout.start();
		
		StreamReaderThread stderr = new StreamReaderThread(ps.getErrorStream(), System.err, "stderr: ");
		stderr.start();

		return ps;
	}
}
