package stegoga;

import java.awt.TextArea;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
 
/*
 * This class extends from OutputStream to redirect output to a JTextArrea
 */
public class CustomOutputStream extends OutputStream {
  
	//our JTextArea for logging
	private TextArea textArea;
     
	
    /*
     * Our CustomOutputStream constructor
     */
	public CustomOutputStream(TextArea textArea) {
        this.textArea = textArea;
    }
     
    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        //textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
