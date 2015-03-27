package stegoga;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.io.FileInputStream;

/*
 * StegoImageHandler provides functions to 
 * handle the stego image. i.e generate the stego image
 * and write it to the disk.
 */
public class StegoImageHandler{
	private BufferedImage m_image;
	private int m_width;
	private int m_height;
	private int[][] m_pixelRGB;
	
	public StegoImageHandler(){
		
	}
	
	public StegoImageHandler(BufferedImage buffImage,int width, int height){
		this.m_image = buffImage;
		this.m_width = width;
		this.m_height= height;
		this.m_pixelRGB = new int [this.m_width][this.m_height];
	}
	
	public boolean setPixel(int x, int y, int value){
		this.m_pixelRGB[x][y] = value;
		return true;
	}
	
	public boolean setPixel(int x, int y, int width, int height, byte value){
		return true;
	}
	
	public boolean writeImage(String filename){
		
		//Set image pixel data from the m_pixelRGB 
		for(int x=0;x<this.m_width;x++){
			for (int y=0;y<this.m_height;y++){
				this.m_image.setRGB(x, y, this.m_pixelRGB[x][y]);
			}
		}
		
		//Generate a bmp image by writing the buffered image to disk.
		File outputfile = new File(filename);
        try {
			ImageIO.write(this.m_image, "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}