package stegoga; 
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.image.PixelGrabber;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.*;

public class TestMain {

	public static void main(String[] args){
		try{
			StegoCrypt s = new StegoCrypt();
			s.getPassword("Pass888899999999");
			s.getPlainText("The World Cup begins tomorrow..");
			s.Encrypt();
			s.Decrypt();
			MyImageHandler img = new MyImageHandler();
			img.GetImage("redbull.jpg");
			int depth = img.getBitDepth();
			System.out.println("Image depth found is :" + depth);
		}catch(Exception e){
			System.out.println("Exception in main thread..."+e.getMessage());
		}	
	}
	
}




