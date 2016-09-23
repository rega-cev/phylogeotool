package be.kuleuven.rega.phylogeotool.pplacer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StreamGobbler extends Thread {
	private InputStream is;
	private String type;
	private ArrayList<String> lines = new ArrayList<String>();
	
	public StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				lines.add(line);
				System.out.println(type + ">" + line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public String getLastLine() {
		return lines.get(lines.size() - 1);
	}
	
	public static StreamGobbler runProcess(String args[]) {
		Runtime rt = Runtime.getRuntime();
		try {
			System.out.println("Process: " + args[0]);
			Process process = rt.exec(args);
			// any error message?
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");            
            
            // any output?
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT");
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
                                    
            // any error???
            int exitVal = process.waitFor();
            System.out.println("Exit value: " + exitVal);

            return outputGobbler;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}