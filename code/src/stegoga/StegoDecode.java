package stegoga;

import java.awt.TextArea;
import java.awt.TextField;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JTextArea;

public class StegoDecode {

	ImageHandler imgHandler;
	PixelData [][] decodePixels;
	int width;
	int height;
	String pitFile;
	String stegoImage;
	String pass;
	Pit[] pitTable;
	byte[] cipherBytes;
	int[] icipherSigInts;
	int[] icipherUSgInts;
	StegoCrypt steoCrypt;
	TextArea logger;
	String decoderFileExt;
		
	public StegoDecode(String pitFile, String stegoImage, String pass,TextArea logger){
		imgHandler = new ImageHandler();
		this.pitFile = pitFile;
		this.stegoImage = stegoImage;
		this.pass = pass;
		this.steoCrypt = new StegoCrypt(logger);
		this.logger = logger;
		this.decoderFileExt = "";
		this.logger.append("Initialized the decoder engine..");
	}
	
	public void Decode() throws FileNotFoundException{
		
		this.decodePixels = imgHandler.GetImageData(stegoImage);
		width = imgHandler.getWidth();
		height = imgHandler.getHeight();
		this.logger.append("\nFinished reading the stego image..");
		this.logger.append("\nReading and initializng pit from file..");
		ReadFile rf = new ReadFile();
		String[] pitText = null;
		try {
			pitText = rf.readLines(this.pitFile);
		} catch (IOException e) {
			this.logger.append(e.getMessage());
		}
		this.pitTable = new Pit[pitText.length-1];
		this.decoderFileExt = pitText[0];
		
		//we already read the 0th position, which is the file extension.
	    for(int i=1;i<pitText.length;i++){
	    	String[] tokens = pitText[i].split(",");
	    	this.pitTable[i-1] = new Pit();
    		int x;
    		int y;
    		int[] dxs = new int[3];
    		x = Integer.parseInt(tokens[0]);
    		y = Integer.parseInt(tokens[1]);	
    		dxs[0] = Integer.parseInt(tokens[2]);
    		dxs[1] = Integer.parseInt(tokens[3]);
    		dxs[2] = Integer.parseInt(tokens[4]);
    		this.pitTable[i-1] = new Pit(x,y,dxs);	
	    }
	    this.logger.append("\nFinished reading pit from file..");
	    	    		
		this.logger.append("\ndecoding bytes...");
		int[] dcipherBytes = new int[this.pitTable.length];
		int dcipherIndex = 0;
		for(int i=0;i<this.pitTable.length;i++){
			for(int x=0;x<width;x++){
				for(int y=0;y<height;y++){
					if(this.pitTable[i].getX()==x && this.pitTable[i].getY()==y){
						int decodedIndex = this.pitTable[i].getDecodedValueIndex();
						int decodedValue = 0;
						int pitValue = 0;
						switch(decodedIndex){
						case 0:
							pitValue =  this.pitTable[i].getDecodeValue();
							if (pitValue >= 0){
								decodedValue = (this.decodePixels[x][y].Red() + this.pitTable[i].getDecodeValue());
							}
							else{
								decodedValue = (this.pitTable[i].getDecodeValue() + this.decodePixels[x][y].Red());
							}								
							break;
						case 1:
							pitValue = this.pitTable[i].getDecodeValue();
							if(pitValue >= 0) {
								decodedValue = (this.decodePixels[x][y].Green() + this.pitTable[i].getDecodeValue());
							}
							else{
								decodedValue = (this.pitTable[i].getDecodeValue() + this.decodePixels[x][y].Green());
							}
							break;
						case 2:
							pitValue =  this.pitTable[i].getDecodeValue();
							if(pitValue >= 0){
								decodedValue = (this.decodePixels[x][y].Blue() + this.pitTable[i].getDecodeValue());
							}
							else{
								decodedValue = (this.pitTable[i].getDecodeValue() + this.decodePixels[x][y].Blue());
							}
							break;
						}
						dcipherBytes[dcipherIndex++] = decodedValue;
					}
				}
			}
		}
		
		this.logger.append("\n");
		this.logger.append("\nSigned Byte Decode is:    ");
		this.logger.append("\n");
		
		byte[]cipherDecodedBytes = this.steoCrypt.getSignedBytesFromInt(dcipherBytes);
		int[] isigned = this.steoCrypt.getSignedIntegerEncodedBytes(cipherDecodedBytes);
		int[] iunsigned = this.steoCrypt.getUnsignedIntegerBytes(isigned);
		this.steoCrypt.setCipherBytes(cipherDecodedBytes);
		this.steoCrypt.setPassword(this.pass);
	    this.steoCrypt.Decrypt();
	    

	    File flOriginal = new File("original"+this.decoderFileExt);
		FileOutputStream fos = new FileOutputStream(flOriginal);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		try {
			bos.write(this.steoCrypt.getMessageBytes());
			bos.close();
		} catch (IOException e) {
			this.logger.append(e.getStackTrace().toString());
		}			
		this.logger.append("\n");
		this.logger.append("Finished decoding, Wrote original message to : "+ flOriginal.getName());
	}
}
