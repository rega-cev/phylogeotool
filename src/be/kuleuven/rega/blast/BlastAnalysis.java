/*
 * Copyright (C) 2008 Rega Institute for Medical Research, KULeuven
 * 
 * See the LICENSE file for terms of use.
 */
package be.kuleuven.rega.blast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implements similarity based analyses using NCBI blast.
 */
public class BlastAnalysis {
	public static String blastPath = "";
    public static String formatDbCommand = "formatdb";
    public static String blastCommand = "blastall";
    
    abstract class BlastSequence {
    	abstract int getLength();
    	abstract void writeFastaOutput(FileOutputStream fos);
    }
    
	enum AlignmentSequenceType {
		AA, NT
	}
    abstract class Alignment {
    	abstract AlignmentSequenceType getSequenceType();
    	abstract void writeFastaOutput(FileOutputStream fos);
    }

    /**
     * Defines a region in a reference sequence.
     */
    public static class Region {
    	private String name;
    	private int begin, end;
    	
    	public Region(String name, int begin, int end) {
    		this.name = name;
    		this.begin = begin;
    		this.end = end;
    	}

		public int getBegin() {
			return begin;
		}

		public int getEnd() {
			return end;
		}

		public String getName() {
			return name;
		}

		/**
		 * @param queryBegin
		 * @param queryEnd
		 * @param minimumOverlap
		 * @return whether the region overlaps with a region defined by queryBegin
		 *   and queryEnd, with a minimum overlap.
		 */
		public boolean overlaps(int queryBegin, int queryEnd, int minimumOverlap) {
			int overlapBegin = Math.max(queryBegin, begin);
			int overlapEnd = Math.min(queryEnd, end);
			
			return (overlapEnd - overlapBegin) > minimumOverlap;
		}
    }

    public static class ReferenceTaxus {
    	private String taxus;
    	private List<Region> regions;
		private String reportOther;
		private int reportOtherOffset;
		private int priority; // lower value means higher priority

    	public ReferenceTaxus(String taxus, int priority) {
    		this.taxus = taxus;
    		this.priority = priority;
    		this.regions = new ArrayList<Region>();
    	}
    	
    	void setReportAsOther(String taxus, int offset) {
    		this.reportOther = taxus;
    		this.reportOtherOffset = offset;
    	}

    	void addRegion(Region r) {
    		if (this.regions == null)
    			this.regions = new ArrayList<Region>();
    		
    		regions.add(r);
    	}
    	
    	public List<Region> getRegions() {
    		return regions;
    	}

		public String getTaxus() {
			return taxus;
		}
		
		public int getPriority() {
			return priority;
		}

		public String reportAsOther() {
			return reportOther;
		}
		
		public int reportAsOtherOffset() {
			return reportOtherOffset;
		}
    }
    
    private Double cutoff;
	private Double maxPValue;
    private boolean relativeCutoff;
    private String blastOptions;
	private String formatDbOptions;
	private Map<String, ReferenceTaxus> referenceTaxa;
	private String detailsOptions;

	/**
	 * A result from a blast analysis.
	 * 
	 * It contains information on the location of the sequence with respect to a
	 * reference genome (which may be the best match or a predefined referenceTaxus).
	 * 
	 * It also tells you whether the query sequence is reference complimented with
	 * respect to the reference sequences.
	 */
    public class Result {
    	private BlastSequence sequence;
        private float score;
        private int start;
        private int end;
        private int matchDiffs;
        private int matchLength;
        private ReferenceTaxus refseq;
		private boolean reverseCompliment;
		private String detailsFile;

        public Result(BlastSequence sequence, float score,
        		int length, int diffs, int start, int end, ReferenceTaxus refseq, boolean reverseCompliment) {
        	this.sequence = sequence;
            this.score = score;
            this.matchLength = length;
            this.matchDiffs = diffs;
            this.start = start;
            this.end = end;
            this.refseq = refseq;
            this.reverseCompliment = reverseCompliment;
        }

        public boolean haveSupport() {
            if (cutoff == null)
                return false;
            else
                return score >= cutoff;
        }

		private float calcSimilarity() {
			return (float)(matchLength - matchDiffs)/matchLength * 100;
		}

        /**
         * @return Returns the score.
         */
        public float getScore() {
            return score;
        }
        
        /**
        * @return Returns the start.
        */
       public int getStart() {
           return start;
       }

       /**
        * @return Returns the end.
        */
       public int getEnd() {
           return end;
       }

		public float getConcludedSupport() {
			return 0;
		}

		public boolean isReverseCompliment() {
			return reverseCompliment;
		}
		
		public ReferenceTaxus getReference() {
			return refseq;
		}

		public void setDetailsFile(String detailsFile) {
			this.detailsFile = detailsFile;
		}
    }

    boolean supportsMultiple() {
    	return detailsOptions != null;
    }


	public BlastAnalysis(Alignment alignment, Double cutoff, Double maxPValue,
                         boolean relativeCutoff, String blastOptions,
                         String detailsOptions, File workingDir) {
        this.cutoff = cutoff;
        this.maxPValue = maxPValue;
        this.relativeCutoff = relativeCutoff;
        this.blastOptions = blastOptions != null ? blastOptions : "";
        this.detailsOptions = detailsOptions;
        if (alignment.getSequenceType() == AlignmentSequenceType.AA) {
        	this.blastOptions = "-p blastx " + this.blastOptions;
        	if (detailsOptions != null)
        		this.detailsOptions = "-p blastx " + this.detailsOptions;
        	this.formatDbOptions = "";
        } else if (alignment.getSequenceType() == AlignmentSequenceType.NT) {
        	this.blastOptions = "-p blastn " + this.blastOptions;
        	if (detailsOptions != null)
        		this.detailsOptions = "-p blastn " + this.detailsOptions;
        	this.formatDbOptions = "-p F";
        } 
        this.referenceTaxa = new HashMap<String, ReferenceTaxus>();
    }
	
	private File getTempFile(String f) throws IOException {
		return File.createTempFile(f, "temp");
	}

    private Result compute(Alignment alignment, BlastSequence sequence, File workingDir)
            throws Exception {
        Process formatdb = null;
        Process blast = null;
        try {
            if (sequence.getLength() != 0) {
                File db = getTempFile("db.fasta");
                FileOutputStream dbFile = new FileOutputStream(db);
                //FileDescriptor fd = dbFile.getFD();
                alignment.writeFastaOutput(dbFile);
                //dbFile.flush();
                //fd.sync();
                dbFile.close();

                File query = getTempFile("query.fasta");
                FileOutputStream queryFile = new FileOutputStream(query);
                //FileDescriptor fd2 = dbFile.getFD();
                sequence.writeFastaOutput(queryFile);
                //queryFile.flush();
                //fd2.sync();
                queryFile.close();
                        
                String cmd = blastPath + formatDbCommand + " " + formatDbOptions + " -o T -i " + db.getAbsolutePath();
                System.err.println(cmd);
                
                formatdb = StreamReaderRuntime.exec(cmd, null, workingDir);
                int exitResult = formatdb.waitFor();

                if (exitResult != 0) {
                    throw new Exception("formatdb exited with error: " + exitResult);
                }
                
                cmd = blastPath + blastCommand + " " + blastOptions
                	+ " -i " + query.getAbsolutePath()
                    + " -m 8 -d " + db.getAbsolutePath();
                System.err.println(cmd);

                blast = Runtime.getRuntime().exec(cmd, null, workingDir);
                InputStream inputStream = blast.getInputStream();

                final LineNumberReader reader
                    = new LineNumberReader(new InputStreamReader(inputStream));

                BlastResults br = new BlastResults() {
					public String[] next() throws Exception {
						String s = null;
						try {
							s = reader.readLine();
						} catch (IOException ioe) {
							throw new Exception("Error: I/O Error while invoking blast: " + ioe.getMessage());
						}
		    			if (s == null)
		    				return null;
		    			System.err.println(s);
		
		    			String[] values = s.split("\t");
		    			if (values.length != 12)
		    				throw new Exception("blast result format error");
		    			return values;
					}
                };
                
                boolean aa = alignment.getSequenceType() == AlignmentSequenceType.AA;
                Result result = parseBlastResults(br, this, aa, sequence);
                
                exitResult = blast.waitFor();

                blast.getErrorStream().close();
                blast.getInputStream().close();
                blast.getOutputStream().close();

                if (exitResult != 0) {
                    throw new Exception("blast exited with error: " + exitResult);
                }      

                db.delete();                
                query.delete();

                if (alignment.getSequenceType() == AlignmentSequenceType.NT) {
                    getTempFile("db.fasta.nhr").delete();
                    getTempFile("db.fasta.nin").delete();
                    getTempFile("db.fasta.nsd").delete();
                    getTempFile("db.fasta.nsi").delete();
                    getTempFile("db.fasta.nsq").delete();
                } else if (alignment.getSequenceType() == AlignmentSequenceType.AA) {
                    getTempFile("db.fasta.phr").delete();
                    getTempFile("db.fasta.pin").delete();
                    getTempFile("db.fasta.psd").delete();
                    getTempFile("db.fasta.psi").delete();
                    getTempFile("db.fasta.psq").delete();
                }

				if (result != null) {
					return result;
				} else {
					return createResult(sequence, null, 0, 0, 0, 0, 0, false);
				}
            } else
                return createResult(sequence, null, 0, 0, 0, 0, 0, false);
        } catch (IOException e) {
            if (formatdb != null)
                formatdb.destroy();
            if (blast != null)
                blast.destroy();
            throw new Exception("Error: I/O Error while invoking blast: "
                + e.getMessage());
        } catch (InterruptedException e) {
            if (formatdb != null)
                formatdb.destroy();
            if (blast != null)
                blast.destroy();
            throw new Exception("Error: I/O Error while invoking blast: "
                + e.getMessage());
        }
    }
    
    public interface BlastResults {
    	String [] next() throws Exception;
    }
	public static Result parseBlastResults(BlastResults results, BlastAnalysis ba, boolean aa, BlastSequence sequence) throws Exception {
		int seqLength = sequence.getLength();
		int queryFactor = aa ? 3 : 1;

		String[] best = null, secondBest = null;
		int start = Integer.MAX_VALUE;
		int end = -1;

		boolean reverseCompliment = false;

		ReferenceTaxus refseq = null;
	
		final int SCORE_IDX = 11;
		final int REFID_IDX = 1;

		for (;;) {
			String [] values = results.next();
			if (values == null)
				break;

			if (best == null)
				best = values;

			ReferenceTaxus referenceTaxus = ba.referenceTaxa
					.get(values[REFID_IDX]);

			/*
			 * First condition: there are no explicit reference taxa configured
			 * -- use best match
			 * 
			 * Second condition: - the referenceTaxus is the first - or has a
			 * higher priority than the current refseq and belongs to the same
			 * cluster (note priority is smaller number means higher priority)
			 */
			if ((ba.referenceTaxa.isEmpty() && values == best)
					|| (referenceTaxus != null
							 && (refseq == null || referenceTaxus
							.getPriority() < refseq.getPriority()))) {
				refseq = referenceTaxus;
				boolean queryReverseCompliment = Integer.parseInt(values[7])
						- Integer.parseInt(values[6]) < 0;
				boolean refReverseCompliment = Integer.parseInt(values[9])
						- Integer.parseInt(values[8]) < 0;
				int offsetBegin = Integer.parseInt(values[6]);
				int offsetEnd = seqLength
						- Integer.parseInt(values[7]);
				if (queryReverseCompliment) {
					offsetBegin = seqLength - offsetBegin;
					offsetEnd = seqLength - offsetEnd;
					reverseCompliment = true;
				}
				if (refReverseCompliment) {
					String tmp = values[8];
					values[8] = values[9];
					values[9] = tmp;
					reverseCompliment = true;
				}
				start = Integer.parseInt(values[8]) * queryFactor - offsetBegin;
				end = Integer.parseInt(values[9]) * queryFactor + offsetEnd;

				if (refseq != null && refseq.reportAsOther() != null) {
					refseq = ba.referenceTaxa.get(refseq.reportAsOther());
					start += refseq.reportAsOtherOffset();
					end += refseq.reportAsOtherOffset();
				}
			}
		}

		if (best != null) {
			int length = Integer.valueOf(best[3]);
			int diffs = Integer.valueOf(best[4]) + Integer.valueOf(best[5]); // #diffs + #gaps
			float score = Float.valueOf(best[11]);
			float pValue = Float.valueOf(best[10]);
			if (ba.maxPValue != null && pValue > ba.maxPValue)
				score = -1;

			if (ba.relativeCutoff) {
				if (secondBest != null)
					score = score / Float.valueOf(secondBest[11]);
			}

			if (start == Integer.MAX_VALUE)
				start = -1;

			Result result = ba.createResult(sequence, refseq,
					score, length, diffs, start, end, reverseCompliment);

			return result;
		} else {
			return null;
		}
	}

    private void runBlastQuery(File query, File db, File workingDir) throws IOException, InterruptedException, Exception {
        String cmd = blastPath + blastCommand + " " + detailsOptions
            	+ " -i " + query.getAbsolutePath()
                + " -T -d " + db.getAbsolutePath();
        System.err.println(cmd);
        Process blast = Runtime.getRuntime().exec(cmd, null, workingDir);

        int result = blast.waitFor();

        blast.getErrorStream().close();
        blast.getInputStream().close();
        blast.getOutputStream().close();

		if (result != 0) { 
			throw new Exception("Blast exited with error: " + result);
		}
	}

	private void copyToFile(File outputFile, InputStream stdout)
			throws IOException {
		InputStreamReader isr = new InputStreamReader(stdout);
        BufferedReader br = new BufferedReader(isr);
        FileWriter osw = new FileWriter(outputFile);
        
        String line = null;
        while ( (line = br.readLine()) != null)
        	osw.write(line + '\n');
        br.close();
        osw.close();
	}
    
    private Result createResult(BlastSequence sequence, ReferenceTaxus refseq,
                                float score, int length, int diffs, int start, int end, boolean reverseCompliment) {
    	return new Result(sequence, score, length, diffs, start, end, refseq, reverseCompliment);
    }

    Result run(Alignment alignment, BlastSequence sequence, File workingDir) throws Exception {
        try {
            return compute(alignment, sequence, workingDir);
        } catch (Exception e) {
            throw e;
        }
    }

	public Double getCutoff() {
		return cutoff;
	}

	public void addReferenceTaxus(ReferenceTaxus t) {
		referenceTaxa.put(t.getTaxus(), t);
	}

	public Set<String> getRegions() {
		Set<String> result = new HashSet<String>();
		for (ReferenceTaxus t : referenceTaxa.values())
			for (Region r : t.getRegions())
				result.add(r.getName());
		
		return result;
	}
}
