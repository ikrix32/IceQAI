package com.iceq.gui;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import javax.swing.JTextArea;

public class TextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	
	ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
	
	public void append(int b){
		tempBuffer.write(b);
		
		if(b == '\n' || b == ' '){
			String text = new String(tempBuffer.toByteArray(), StandardCharsets.UTF_8);
			tempBuffer.reset();
			// redirects data to the text area
	        append(text);
	        // scrolls the text area to the end of data
	        setCaretPosition(getDocument().getLength());
	        // keeps the textArea up to date
	       // textArea.update(textArea.getGraphics());
		}
	}
}
