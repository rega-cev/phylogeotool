package be.kuleuven.rega.webapp.widgets;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;
import org.yeastrc.fasta.FASTAEntry;
import org.yeastrc.fasta.FASTAReader;

import be.kuleuven.rega.webapp.Main;
import eu.webtoolkit.jwt.AlignmentFlag;
import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.TextFormat;
import eu.webtoolkit.jwt.WDate;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WFileResource;
import eu.webtoolkit.jwt.WFileUpload;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMouseEvent;
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
	    table.setWidth(new WLength(630));
	    table.setStyleClass("tableDialog");
	    
	    WLabel nucleotideSeqLabel = new WLabel("<b>Nucleotide sequence</b><br />(FASTA format)");
	    nucleotideSeqLabel.setTextFormat(TextFormat.XHTMLText);
//	    nucleotideSeqLabel.setStyleClass("bold");
//	    nucleotideSeqLabel.setBuddy(textArea);
	    table.getElementAt(1, 1).addWidget(nucleotideSeqLabel);
	    this.textArea(table);
	    
	    WLabel uploadFastaFileLabel = new WLabel("<b>Upload fasta file</b>");
	    uploadFastaFileLabel.setTextFormat(TextFormat.XHTMLText);
	    table.getElementAt(2, 1).addWidget(uploadFastaFileLabel);
	    this.fileUpload(table);
	    this.emailTextArea(table);
	    table.getElementAt(4, 2).addWidget(out);
	    table.getElementAt(4, 2).setColumnSpan(2);
	}
	
	public void addStartButton(WPushButton button) {
		button.setWidth(new WLength(60));
		table.getElementAt(5, 3).addWidget(button);
	}
	
	public void addCancelButton(WPushButton cancel) {
		cancel.setWidth(new WLength(60));
		table.getElementAt(5, 3).addWidget(cancel);
		table.getElementAt(5, 3).setContentAlignment(AlignmentFlag.AlignRight);
	}

	public void fileUpload(WTable wTable) {
		final WFileUpload fileUpload = new WFileUpload();
		fileUpload.setToolTip("Please select a fasta file containing your sequence in fasta format");
//		WHBoxLayout whBoxLayout = new WHBoxLayout();
		fileUpload.setMargin(new WLength(10), EnumSet.of(Side.Right));
//		whBoxLayout.addWidget(fileUpload);
//		final WPushButton uploadButton = new WPushButton("Upload");
//		uploadButton.setMargin(new WLength(10), EnumSet.of(Side.Left, Side.Right));
//		whBoxLayout.addWidget(uploadButton);
//		whBoxLayout.addWidget(out);
//		wvBoxLayout.addLayout(whBoxLayout);
//		wvBoxLayout.addWidget(out);
		
		table.getElementAt(2, 2).addWidget(fileUpload);
		table.getElementAt(2, 3).addWidget(out);
		
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
					FASTAReader fastaReader = FASTAReader.getInstance(wFileResource.getFileName());
					checkWTextArea(fastaReader);
				} catch (Exception e) {
					e.printStackTrace();
				}

//				uploadButton.enable();
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
		textArea.setWidth(new WLength(440));
		textArea.setRows(5);
		textArea.setPlaceholderText("Upload or enter a nucleotide sequence in fasta format that you'd want to PPlace in the tree.");
//		textArea.disable();
		final WText out = new WText("<p></p>");
		out.addStyleClass("help-block");
		textArea.changed().addListener(dialog, new Signal.Listener() {
			public void trigger() {
				out.setText("<p>Text area changed at " + WDate.getCurrentDate().toString() + ".</p>");
				InputStream stream = new ByteArrayInputStream(textArea.getText().getBytes(StandardCharsets.UTF_8));
				try {
					FASTAReader fastaReader = FASTAReader.getInstance(stream);
					checkWTextArea(fastaReader);
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		table.getElementAt(1, 2).setColumnSpan(2);
		table.getElementAt(1, 2).addWidget(textArea);
	}
	
	public void emailTextArea(WTable table) {
		WLabel wLabel = new WLabel("<b>E-mail</b>");
		wLabel.setTextFormat(TextFormat.XHTMLText);
		table.getElementAt(3, 1).addWidget(wLabel);
		email.setWidth(new WLength(300));
		email.setPlaceholderText("Enter your email address.");
		table.getElementAt(3, 2).setColumnSpan(2);
		table.getElementAt(3, 2).addWidget(email);
		WRegExpValidator emailValidator = new WRegExpValidator("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
		email.setValidator(emailValidator);
	}

	public String getFastaSequence() {
		return textArea.getText();
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
				if(StringUtils.countMatches(textArea.getText(), ">") == 1) {
					return true;
				} else {
					out.setText("<font color=\"red\">Please upload a fasta file or paste sequence in fasta format in text area.</font>");
					return false;
				}
			} else {
				out.setText("<font color=\"red\">Please upload a fasta file or paste sequence in fasta format in text area.</font>");
				return false;
			}
		} else {
			out.setText("<font color=\"red\">Email address is not valid. Please correct.</font>");
			return false;
		}
	}
	
	private void checkWTextArea(FASTAReader fastaReader) {
		try {
			FASTAEntry fastaEntry;
			int counter = 0;
			int maxSequences = 1;
			textArea.setText("");
			while((fastaEntry = fastaReader.readNext()) != null) {
				counter++;
				if(counter <= maxSequences) {
					String textAreaText = textArea.getText();
					if(counter > 1) {
						textAreaText = textAreaText + "\n";
					}
					textArea.setText(textAreaText + fastaEntry.getHeaderLine().replaceAll(" ", "_") + "\n" + fastaEntry.getSequence());
				} else {
					final WDialog wDialog = new WDialog("Warning");
					
					WTable wTable = new WTable(wDialog.getContents());
					wTable.addStyleClass("tablePPlacer", true);
					wTable.setHeaderCount(1);
					wTable.setWidth(new WLength("100%"));
					wTable.setStyleClass("tableDialog");
					
					WImage wImage = new WImage(new WLink(Main.getApp().getServletContext().getContextPath().concat("/images/warning.png")));
					wImage.setWidth(new WLength(50));
					wImage.setHeight(new WLength(50));
					
					wTable.getElementAt(1, 1).addWidget(wImage);
					wTable.getElementAt(1, 1).setRowSpan(2);
					
					wTable.getElementAt(1, 2).addWidget(new WText("Your fasta file contained more than one sequence."));
					wTable.getElementAt(2, 2).addWidget(new WText("Only the first sequence will be used."));
					WPushButton wPushButton = new WPushButton("OK");
					wPushButton.setWidth(new WLength(75));
					wPushButton.clicked().addListener(dialog,
				            new Signal1.Listener<WMouseEvent>() {
		                public void trigger(WMouseEvent e1) {
		                	wDialog.reject();
		                }
		            });
					wTable.getElementAt(3, 2).addWidget(wPushButton);
					wTable.getElementAt(3, 2).setContentAlignment(AlignmentFlag.AlignCenter);
					wDialog.setPopup(true);
					wDialog.rejectWhenEscapePressed();
					wDialog.show();
					break;
				}
			}
		} catch (Exception e) {
			out.setText("File format not recognised as fasta file");
		}
	}
}
