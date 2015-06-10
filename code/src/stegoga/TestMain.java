package stegoga; 
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.awt.image.PixelGrabber;

import javax.imageio.ImageIO;

import java.awt.image.*;
import java.awt.*;

public class TestMain {

	public static void main(String[] args){
		try{      
			StegoCrypt s = new StegoCrypt();
			//AES key-length 16 bytes minimum
			s.setPassword("1234567890123456");
			File f = new File("lena32.bmp");
			String fileExt = "";
			FileInputStream fis = new FileInputStream(f);
			byte[] data = new byte[(int) f.length()];
			fis.read(data);
			fis.close();
			fileExt = Utils.getFileExtension(f);
			if(fileExt.equals(".bmp")){
				s.setImageBytes(data);
			}
			else{
				String str = new String(data, "UTF-8");			
				s.setPlainText(str);	
			}			
			s.Encrypt();
			
			byte[] cipherBytes = s.getCipherBytes();
			//-127 to 128
			
			int[] icipherSigInts = s.getSignedIntegerEncodedBytes(cipherBytes);
			//extrapolated to 0-255
			
			System.out.println("Signed Byte Decode is:   ");
			System.out.println();
			for(int sl = 0;sl<icipherSigInts.length;sl++){
				System.out.print(" ");
				System.out.print(icipherSigInts[sl]);
			}
			
			int[] icipherUSgInts = s.getUnsignedIntegerBytes(icipherSigInts);
			System.out.println("");
			System.out.println("UnSigned Byte Decode is:  ");
			System.out.println();
			for(int sl = 0;sl<icipherUSgInts.length;sl++){
				System.out.print(" ");
				System.out.print(icipherUSgInts[sl]);
			}
			
			ImageHandler imgHandler = new ImageHandler();
			PixelData[][] pixelData =  imgHandler.GetImageData("lena128.bmp");
			
			int width = imgHandler.getWidth();
			int height = imgHandler.getHeight();
			
			System.out.println("");
			System.out.println("Init Population..");
			
			Chromozome[] chromozomes = new Chromozome[width * height];
			int index = 0;
			for(int x = 0;x<width;x++){
				for(int y = 0;y<height;y++){
					PixelData pix = pixelData[x][y];
					chromozomes[index] = new Chromozome(index, x, y, pix.Red(), pix.Green(),pix.Blue());
					index++;
				}
			}
			
			System.out.println("Doing GA..");
			
			System.out.println("Solutions found so far..");
			
			GA geneticAlgoritm = new GA(icipherUSgInts,chromozomes.length);
			for(int i=0;i<icipherUSgInts.length;i++){
				geneticAlgoritm.initializePopulation(chromozomes);
				Population pop = geneticAlgoritm.evolvePopulation();	
				Chromozome[] chroms = pop.getAllChromozome();
				for(int j = 0;j<chroms.length;j++){
					chromozomes[j] = chroms[j];
				}
				
				for(int j=0;j<chromozomes.length;j++){
					if(chromozomes[j].getSolutionId() == i){
						System.out.println(chromozomes[j].toString());
						break;
					}					
				}									
			}			
			
			System.out.println("\n---------------");
			System.out.println("Finished GA..");
			
			System.out.println("Final Solutions found..");
			for(int k=0;k<icipherUSgInts.length;k++){
				for(int j=0;j<chromozomes.length;j++){
					if(chromozomes[j].getSolutionId() == k){
						System.out.println(chromozomes[j].toString());
					}					
				}		
			}				
			
			PixelData[][] stegoPixelData = new PixelData[width][height];
			//BurnImage
			System.out.println("Started burning image..");
			for(int x=0;x<width;x++){
				for(int y = 0; y<width;y++){
					for(int k = 0;k< chromozomes.length;k++){
						if(chromozomes[k].getX()==x && chromozomes[k].getY()==y){
							stegoPixelData[x][y] = new PixelData();
							stegoPixelData[x][y].SetPixel(x, y, 255,chromozomes[k].getGene(0) ,chromozomes[k].getGene(1),chromozomes[k].getGene(2));
						}
					}
				}
			}
			
			imgHandler.setImageData(stegoPixelData);
			imgHandler.WriteImageToDisk("stego.bmp");
			System.out.println("Finished burining image..");
			
			
			// PIT
			System.out.println("Generating pit..");
			Pit[] pitTable = new Pit[icipherUSgInts.length];
			for(int i=0;i<pitTable.length;i++){
				pitTable[i] = new Pit();
			}
			
			int pitTableIndex = 0;
			for(int i=0;i<icipherUSgInts.length;i++){
				for(int j = 0;j<chromozomes.length;j++){
					if(chromozomes[j].getSolutionId()==i){
						int[] minDxs = Utils.getMinDx(chromozomes[j].getAllGenes(),icipherUSgInts[i]);
						pitTable[pitTableIndex] = new Pit(chromozomes[j].getX(),chromozomes[j].getY(),minDxs);
						pitTableIndex++;
					}
				}
			}
			File file = new File("pit.csv");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			fileExt = Utils.getFileExtension(f);
			bw.write(fileExt);
			bw.write("\n");
			
			for(int i=0;i<pitTable.length;i++){
				bw.write(pitTable[i].toString());
				bw.write("\n");
			}
			
			bw.close();
			System.out.println("Finished generated pit..");
			
			
			//Decode
			System.out.println("Reading the stego image...");
			imgHandler = new ImageHandler();
			PixelData [][] decodePixels = imgHandler.GetImageData("stego.bmp");
			width = imgHandler.getWidth();
			height = imgHandler.getHeight();
			System.out.println("Finished reading the stego image..");
						
			System.out.println("decoding bytes...");
			int[] dcipherBytes = new int[pitTable.length];
			int dcipherIndex = 0;
			for(int i=0;i<pitTable.length;i++){
				for(int x=0;x<width;x++){
					for(int y=0;y<height;y++){
						if(pitTable[i].getX()==x && pitTable[i].getY()==y){
							int decodedIndex = pitTable[i].getDecodedValueIndex();
							int decodedValue = 0;
							int pitValue = 0;
							switch(decodedIndex){
							case 0:
								pitValue =  pitTable[i].getDecodeValue();
								if (pitValue >= 0){
									decodedValue = (decodePixels[x][y].Red() + pitTable[i].getDecodeValue());
								}
								else{
									decodedValue = (pitTable[i].getDecodeValue() + decodePixels[x][y].Red());
								}								
								break;
							case 1:
								pitValue = pitTable[i].getDecodeValue();
								if(pitValue >= 0) {
									decodedValue = (decodePixels[x][y].Green() + pitTable[i].getDecodeValue());
								}
								else{
									decodedValue = (pitTable[i].getDecodeValue() + decodePixels[x][y].Green());
								}
								break;
							case 2:
								pitValue =  pitTable[i].getDecodeValue();
								if(pitValue >= 0){
									decodedValue = (decodePixels[x][y].Blue() + pitTable[i].getDecodeValue());
								}
								else{
									decodedValue = (pitTable[i].getDecodeValue() + decodePixels[x][y].Blue());
								}
								break;
							}
							dcipherBytes[dcipherIndex++] = decodedValue;
						}
					}
				}
			}
					
			
			//System.out.println("Finished decoding bytes...");
			
			//System.out.println("Original UnSigned Byte Decode is:   ");
			//System.out.println();
			//for(int sl = 0;sl<icipherSigInts.length;sl++){
				//System.out.print(" ");
				//System.out.print(icipherSigInts[sl]);
			//}
			
			
			//System.out.println("");
			//System.out.println("Signed Byte Decode is:    ");
			//System.out.println();
			
			byte[]cipherDecodedBytes = s.getSignedBytesFromInt(dcipherBytes);
			int[] isigned = s.getSignedIntegerEncodedBytes(cipherDecodedBytes);
			/*System.out.println("Signed decode..");
			System.out.println("");
			for(int sl = 0;sl<isigned.length;sl++){
				System.out.print(" ");
				System.out.print(isigned[sl]);
			}*/
			
			System.out.println("");
			//System.out.println("Unsigned decode..");
			//System.out.println("");
			int[] iunsigned = s.getUnsignedIntegerBytes(isigned);
			//for(int sl = 0;sl<iunsigned.length;sl++){
			//	System.out.print(" ");
			//	System.out.print(iunsigned[sl]);
			//}
			//System.out.println();
			s.setCipherBytes(cipherDecodedBytes);
			s.Decrypt();
			
		    File flOriginal = new File("original"+fileExt);
			FileOutputStream fos = new FileOutputStream(flOriginal);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			try {
				bos.write(s.getMessageBytes());
			} catch (IOException e) {
				System.out.println(e.getStackTrace().toString());
			}	
			bos.close();
			System.out.println("Finished decoding, Wrote original message to : "+ flOriginal.getName());
			
		}catch(Exception e){
			System.out.println("Exception in main thread..."+e.getStackTrace().toString());
		}
		
	}
	
}




