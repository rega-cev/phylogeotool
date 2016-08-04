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

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WDate;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WFileResource;
import eu.webtoolkit.jwt.WFileUpload;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WRegExpValidator;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WTextArea;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WValidator;
import eu.webtoolkit.jwt.WWidget;

public class PPlacerForm {
	private WContainerWidget container = new WContainerWidget();
	private WVBoxLayout wvBoxLayout = new WVBoxLayout();
	private WDialog dialog;
	private WTextArea textArea;
	private File uploadedFile = null;
	private WLineEdit email = new WLineEdit();
	private WText out = new WText();
	
	public PPlacerForm(WDialog dialog) {
		this.dialog = dialog;
		dialog.setWidth(new WLength(500));
		container.setLayout(wvBoxLayout);
		this.fileUpload(wvBoxLayout);
		this.textArea(wvBoxLayout);
		this.emailTextArea(wvBoxLayout);
		wvBoxLayout.addWidget(out);
	}

	public void fileUpload(WVBoxLayout wvBoxLayout) {
		final WFileUpload fileUpload = new WFileUpload();
		WHBoxLayout whBoxLayout = new WHBoxLayout();
		fileUpload.setMargin(new WLength(10), EnumSet.of(Side.Right));
		whBoxLayout.addWidget(fileUpload);
//		final WPushButton uploadButton = new WPushButton("Upload");
//		uploadButton.setMargin(new WLength(10), EnumSet.of(Side.Left, Side.Right));
//		whBoxLayout.addWidget(uploadButton);
		final WText out = new WText();
		wvBoxLayout.addLayout(whBoxLayout);
		wvBoxLayout.addWidget(out);
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

	public void textArea(WVBoxLayout wvBoxLayout) {
		textArea = new WTextArea();
		textArea.setColumns(80);
		textArea.setRows(5);
		textArea.setPlaceholderText("Upload a nucleotide sequence that you'd want to PPlace in the tree.");
//		textArea.disable();
		final WText out = new WText("<p></p>");
		out.addStyleClass("help-block");
		textArea.changed().addListener(dialog, new Signal.Listener() {
			public void trigger() {
				out.setText("<p>Text area changed at " + WDate.getCurrentDate().toString() + ".</p>");
			}
		});
		wvBoxLayout.addWidget(textArea);
	}
	
	public void emailTextArea(WVBoxLayout wvBoxLayout) {
		WHBoxLayout whBoxLayout = new WHBoxLayout();
		WLabel wLabel = new WLabel("E-mail");
		whBoxLayout.addWidget(wLabel);
		email.setPlaceholderText("Enter your email address.");
		whBoxLayout.addWidget(email);
		wvBoxLayout.addLayout(whBoxLayout);
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
		return this.container;
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
