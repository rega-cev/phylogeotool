package be.kuleuven.rega.webapp.widgets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import be.kuleuven.rega.enums.SequenceExportFormat;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WFileResource;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.servlet.WebRequest;
import eu.webtoolkit.jwt.servlet.WebResponse;

public class WDownloadSequencesResource extends WFileResource {

	private SequenceExportFormat sequenceExportFormat;
	private ByteArrayOutputStream byteArrayOutputStream;
	
	public WDownloadSequencesResource(WObject parent, String fileName, SequenceExportFormat sequenceExportFormat, ByteArrayOutputStream byteArrayOutputStream) {
		super("application/fasta", fileName, parent);
		this.sequenceExportFormat = sequenceExportFormat;
		this.byteArrayOutputStream = byteArrayOutputStream;
	}
	
	@Override
	public void handleRequest(WebRequest request, WebResponse response) {
		if (sequenceExportFormat.equals(SequenceExportFormat.FASTA)) {
			// FASTA
			response.setContentType("application/fasta");
			this.suggestFileName(getFileName() + ".fasta");
		} else {
			// ERROR
		}
		try {
			response.getOutputStream().write(byteArrayOutputStream.toByteArray());	
			response.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Signal dataChanged() {
		return super.dataChanged();
	}
}