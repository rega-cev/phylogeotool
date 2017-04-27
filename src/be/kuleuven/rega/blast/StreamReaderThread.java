package be.kuleuven.rega.blast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class StreamReaderThread extends Thread {
	private InputStream in;
	private PrintStream out;
	
	private String prefix = null;
	
	public StreamReaderThread(InputStream in){
		this(in, null);
	}
	
	public StreamReaderThread(InputStream in, PrintStream out){
		this(in, out, null);
	}
	
	public StreamReaderThread(InputStream in, PrintStream out, String prefix){
		this.in = in;
		this.out = out;
		this.prefix = prefix;
	}
	
	public void run(){
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in));
			
			String line;
			while((line = br.readLine()) != null){
				if(out != null){
					if(prefix != null)
						out.print(prefix);
					out.println(line);
				}
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
