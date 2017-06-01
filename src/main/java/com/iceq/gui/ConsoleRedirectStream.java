package com.iceq.gui;

import java.io.IOException;
import java.io.OutputStream;

public class ConsoleRedirectStream extends OutputStream {
    private TextArea textArea;
    
    public ConsoleRedirectStream(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
    	textArea.append(b);
    }
}