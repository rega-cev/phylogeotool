package be.kuleuven.rega.webapp.widgets;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.io.FileUtils;

import eu.webtoolkit.jwt.AlignmentFlag;
import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WDate;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WFileResource;
import eu.webtoolkit.jwt.WFileUpload;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WRegExpValidator;
import eu.webtoolkit.jwt.WTable;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WTextArea;
import eu.webtoolkit.jwt.WValidator;
import eu.webtoolkit.jwt.WWidget;

public class PPlacerForm {
	
	private WTable table = null;
	
//	private WContainerWidget container = new WContainerWidget();
//	private WVBoxLayout wvBoxLayout = new WVBoxLayout();
	private WDialog dialog;
	private WTextArea textArea;
	private File uploadedFile = null;
	private WLineEdit email = new WLineEdit();
	private WText out = new WText();
	
	public PPlacerForm(WDialog dialog) {
		dialog.setWidth(new WLength(630));
		
		table = new WTable(dialog.getContents());
		table.addStyleClass("tablePPlacer", true);
		table.setHeaderCount(1);
	    table.setWidth(new WLength("100%"));
	    table.setStyleClass("tableDialog");
	    
	    WLabel nucleotideSeqLabel = new WLabel("Nucleotide sequence");
//	    nucleotideSeqLabel.setStyleClass("bold");
//	    nucleotideSeqLabel.setBuddy(textArea);
	    table.getElementAt(1, 1).addWidget(nucleotideSeqLabel);
	    this.textArea(table);
	    
	    WLabel uploadFastaFileLabel = new WLabel("Upload fasta file");
//	    uploadFastaFileLabel.setStyleClass("bold");
	    table.getElementAt(2, 1).addWidget(uploadFastaFileLabel);
	    this.fileUpload(table);
	    this.emailTextArea(table);
	}
	
	public void addStartButton(WPushButton button) {
		button.setWidth(new WLength(60));
		table.getElementAt(4, 2).addWidget(button);
	}
	
	public void addCancelButton(WPushButton cancel) {
		cancel.setWidth(new WLength(60));
		table.getElementAt(4, 2).addWidget(cancel);
		table.getElementAt(4, 2).setContentAlignment(AlignmentFlag.AlignRight);
	}

	public void fileUpload(WTable wTable) {
		final WFileUpload fileUpload = new WFileUpload();
		fileUpload.setToolTip("Please select a fasta file containing sequences in fasta format");
//		WHBoxLayout whBoxLayout = new WHBoxLayout();
		fileUpload.setMargin(new WLength(10), EnumSet.of(Side.Right));
//		whBoxLayout.addWidget(fileUpload);
//		final WPushButton uploadButton = new WPushButton("Upload");
//		uploadButton.setMargin(new WLength(10), EnumSet.of(Side.Left, Side.Right));
//		whBoxLayout.addWidget(uploadButton);
		final WText out = new WText();
//		whBoxLayout.addWidget(out);
//		wvBoxLayout.addLayout(whBoxLayout);
//		wvBoxLayout.addWidget(out);
		
		table.getElementAt(2, 2).addWidget(fileUpload);
		
//		uploadButton.clicked().addListener(dialog, new Signal.Listener() {
//			public void trigger() {
//				if(!fileUpload.getSpoolFileName().equals("")) {
//					fileUpload.upload();
//					uploadButton.disable();
//				} else {
//					out.setText("Please select a file to upload");
//				}
//			}
//		});
		fileUpload.changed().addListener(dialog, new Signal.Listener() {
			public void trigger() {
				out.setText("File upload has changed.");
				fileUpload.upload();
			}
		});
		fileUpload.uploaded().addListener(dialog, new Signal.Listener() {
			public void trigger() {
				out.setText("File upload is finished.");
				WFileResource wFileResource = new WFileResource("text/plain", fileUpload.getSpoolFileName());
				try {
					List<String> lines = Files.readAllLines(Paths.get(wFileResource.getFileName()),StandardCharsets.UTF_8);
					StringBuffer stringBuffer = new StringBuffer();
					for (String line : lines) {
						stringBuffer.append(line + "\n");
					}
					textArea.setText(stringBuffer.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
//				uploadButton.enable();
				
				Path path = null;
				try {
					path = Files.createTempDirectory("pplacer");
					if (wFileResource != null) {
						File newFileName = new File(path + File.separator + "sequences.fasta");
						File oldFileName = new File(wFileResource.getFileName());
						if(oldFileName.exists())
							FileUtils.moveFile(oldFileName, newFileName);
							uploadedFile = newFileName;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		fileUpload.fileTooLarge().addListener(dialog, new Signal.Listener() {
			public void trigger() {
				out.setText("File is too large.");
			}
		});
	}

	public void textArea(WTable table) {
		textArea = new WTextArea();
		textArea.setColumns(70);
		textArea.setRows(5);
		textArea.setPlaceholderText("Upload or enter a nucleotide sequence in fasta format that you'd want to PPlace in the tree.");
//		textArea.disable();
		final WText out = new WText("<p></p>");
		out.addStyleClass("help-block");
		textArea.changed().addListener(dialog, new Signal.Listener() {
			public void trigger() {
				out.setText("<p>Text area changed at " + WDate.getCurrentDate().toString() + ".</p>");
			}
		});
		table.getElementAt(1, 2).addWidget(textArea);
	}
	
	public void emailTextArea(WTable table) {
		WLabel wLabel = new WLabel("E-mail");
//		wLabel.setStyleClass("bold");
		table.getElementAt(3, 1).addWidget(wLabel);
		email.setWidth(new WLength(300));
		email.setPlaceholderText("Enter your email address.");
		table.getElementAt(3, 2).addWidget(email);
		WRegExpValidator emailValidator = new WRegExpValidator("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
		email.setValidator(emailValidator);
	}

	public File getUploadedFile() {
		return this.uploadedFile;
	}
	
	public String getEmail() {
		if(email.validate() == WValidator.State.Valid) {
			return email.getText();
		} else {
			return null;
		}
	}

	public WWidget getWidget() {
		return this.table;
	}

	public boolean isFormValid() {
		if (!email.getText().equals("") && email.validate() == WValidator.State.Valid) {
			if(textArea.getText() != null && !textArea.getText().equals("")) {
				return true;
			} else {
				out.setText("Please upload a file.");
				return false;
			}
		} else {
			out.setText("Email address is not valid. Please correct.");
			return false;
		}
	}
}
