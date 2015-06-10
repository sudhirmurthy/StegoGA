package stegoga;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Color;
import java.awt.TextArea;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class StegoEncode {
	StegoCrypt stegoCrypt; 
	byte[] cipherBytes;
	int[] icipherSigInts;
	int[] icipherUSgInts;
	String imgFilePath; 
	Chromozome[] chromozomes;
	Pit[] pitTable;
	int width;
	int height;
	ImageHandler imgHandler;
	PixelData[][] pixelData;
	JLabel originalImg;
	JLabel stegoImg;
	TextArea logger;
	String fileExt;
	
	public StegoEncode(String imgFilePath,String password, String msgFileName,JLabel original, JLabel stego,TextArea logger){
		this.stegoCrypt = new StegoCrypt();
		this.setPassword(password);
		File f = new File(msgFileName);
		FileInputStream fis;
		this.logger = logger;
		byte[] data;
		this.fileExt = Utils.getFileExtension(f);
		try {
			fis = new FileInputStream(f);
			data = new byte[(int) f.length()];
			fis.read(data);
			fis.close();			
			if(this.fileExt.equals(".bmp")){
				this.setMessage(data);
			}
			else{
				String str = new String(data, "UTF-8");			
				this.setMessage(str);	
			}
			this.imgFilePath = imgFilePath;
			this.imgHandler = new ImageHandler();
			this.originalImg = original;
			this.stegoImg = stego;
			this.logger.append("Initialized Encoder..");
		}catch (Exception e) {
			this.logger.append(e.getStackTrace().toString());
		}		
	}
	
	private void setPassword(String password){
		if(this.stegoCrypt!=null){
			stegoCrypt.setPassword(password);
		}
	}
	
	private void setMessage(String msg){
		if(this.stegoCrypt!=null){
			stegoCrypt.setPlainText(msg);
		}
	}
	
	
	private void setMessage(byte[] msg){
		this.stegoCrypt.setImageBytes(msg);					
	}

	public void Encrypt(){
		if(this.stegoCrypt!=null){
			stegoCrypt.Encrypt();
			this.cipherBytes = stegoCrypt.getCipherBytes();
			//-127 to 128
			icipherSigInts = stegoCrypt.getSignedIntegerEncodedBytes(cipherBytes);
			//0-255
			this.logger.append("\nSigned Byte Decode is:   ");
			this.logger.append("\n");
			for(int sl = 0;sl<icipherSigInts.length;sl++){
				this.logger.append(" ");
				this.logger.append(icipherSigInts[sl]+"");
			}
			
			icipherUSgInts = stegoCrypt.getUnsignedIntegerBytes(icipherSigInts);
			this.logger.append("");
			this.logger.append("\nUnSigned Byte Decode is:  ");
			this.logger.append("\n");
			for(int sl = 0;sl<icipherUSgInts.length;sl++){
				this.logger.append(" ");
				this.logger.append(icipherUSgInts[sl]+"");
			}
						
			this.pixelData =  this.imgHandler.GetImageData(this.imgFilePath);
			this.width = this.imgHandler.getWidth();
			this.height = this.imgHandler.getHeight();
			
			this.logger.append("");
			this.logger.append("\nCreating Chromozomes of the read Pixel Data..");
			
			this.chromozomes = new Chromozome[width * height];
			int index = 0;
			for(int x = 0;x<width;x++){
				for(int y = 0;y<height;y++){
					PixelData pix = pixelData[x][y];
					this.chromozomes[index] = new Chromozome(index, x, y, pix.Red(), pix.Green(),pix.Blue());
					index++;
				}
			}		       
			this.originalImg.setIcon(new ImageIcon(this.imgHandler.getBuffImage())); 
			this.originalImg.repaint();
			this.logger.append("\nFinished..Ready for GA..");
		}		
	}	
	
	public void DoGA() throws IOException{
		this.logger.append("\nDoing GA..");
		this.logger.repaint();
		this.logger.append("\nSolutions found so far..");
		
		GA geneticAlgoritm = new GA(this.icipherUSgInts,this.chromozomes.length);
		for(int i=0;i<this.icipherUSgInts.length;i++){
			geneticAlgoritm.initializePopulation(this.chromozomes);
			Population pop = geneticAlgoritm.evolvePopulation();	
			Chromozome[] chroms = pop.getAllChromozome();
			for(int j = 0;j<chroms.length;j++){
				this.chromozomes[j] = chroms[j];
			}			
			
			for(int j=0;j<this.chromozomes.length;j++){
				if(this.chromozomes[j].getSolutionId() == i){
					this.logger.append("\n"+this.chromozomes[j].toString());
					this.logger.repaint();
				}					
			}						
		}
		
		this.logger.append("\n---------------");
		this.logger.append("\nFinished GA..");
		this.logger.repaint();
		
		//BurnImage
		this.logger.append("\nStarted burning image..");
		for(int x=0;x<width;x++){
			for(int y = 0; y<width;y++){
				for(int k = 0;k< chromozomes.length;k++){
					if(chromozomes[k].getX()==x && chromozomes[k].getY()==y){
						pixelData[x][y].SetPixel(x, y, 255,chromozomes[k].getGene(0) ,chromozomes[k].getGene(1),chromozomes[k].getGene(2));
					}
				}
			}
		}		
		imgHandler.setImageData(pixelData);
		imgHandler.WriteImageToDisk("stego.bmp");
		this.logger.append("\nFinished burning image..");
		this.stegoImg.setIcon(new ImageIcon(this.imgHandler.getBuffImage())); 
		this.stegoImg.repaint();
		this.logger.repaint();
		// PIT
		this.logger.append("\nGenerating pit..");
		this.pitTable = new Pit[icipherUSgInts.length];
		for(int i=0;i<pitTable.length;i++){
			this.pitTable[i] = new Pit();
		}
		
		int pitTableIndex = 0;
		for(int i=0;i<this.icipherUSgInts.length;i++){
			for(int j = 0;j<this.chromozomes.length;j++){
				if(this.chromozomes[j].getSolutionId()==i){
					int[] minDxs = Utils.getMinDx(this.chromozomes[j].getAllGenes(),this.icipherUSgInts[i]);
					pitTable[pitTableIndex] = new Pit(this.chromozomes[j].getX(),this.chromozomes[j].getY(),minDxs);
					pitTableIndex++;
				}
			}
		}
		
		File file = new File("pit.csv");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(this.fileExt);
		bw.write("\n");
		
		for(int i=0;i<this.pitTable.length;i++){
			bw.write(this.pitTable[i].toString());
			bw.write("\n");
		}		
		bw.close();
		
		this.logger.append("\nFinished generated pit : pit.csv");
		this.logger.repaint();
		this.stegoImg.setIcon(new ImageIcon(this.imgHandler.getBuffImage())); 
		this.stegoImg.repaint();
	}
		
}
