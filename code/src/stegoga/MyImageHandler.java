package stegoga;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import javax.imageio.ImageIO;

public class MyImageHandler{
	
	private Object m_pixels;
    // get the palette of the image, if possible
    private ColorModel m_colorModel;
    
    public MyImageHandler(){
    	this.m_colorModel = null;
    	this.m_pixels = new Object();
    }
    
    public int getBitDepth(){
    	if (null==this.m_colorModel){
    		return -1;
    	}else
    	{
    		return this.m_colorModel.getPixelSize();
    	}
    }   
   
	 public int getRed(int pixel)
	  {
	    if ((this.m_colorModel instanceof IndexColorModel))	  
		return ((IndexColorModel)this.m_colorModel).getRed(pixel);
	    else
		return ((DirectColorModel)this.m_colorModel).getRed(pixel);
	  }
	
	  // returns the green component value of a pixel
	  public int getGreen(int pixel)
	  {
	    if ((this.m_colorModel instanceof IndexColorModel))	  
		return ((IndexColorModel)this.m_colorModel).getGreen(pixel);
	    else
		return ((DirectColorModel)this.m_colorModel).getGreen(pixel);
	  }

	  // returns the blue component value of a pixel
	  public int getBlue(int pixel)
	  {
	    if ((this.m_colorModel instanceof IndexColorModel))	  
		return ((IndexColorModel)this.m_colorModel).getBlue(pixel);
	    else
		return ((DirectColorModel)this.m_colorModel).getBlue(pixel);
	  }

	  public void GetImage(String filename){
	  try{
			BufferedImage oImg = ImageIO.read(new File(filename));
			int width = oImg.getWidth();
			int height = oImg.getHeight();
			int totalpixels = (width*height);
			System.out.println("image width is  :" + oImg.getWidth());
			System.out.println("image height is :" + oImg.getHeight());
			PixelGrabber p = new PixelGrabber(oImg,0, 0, width, height, false);
			p.grabPixels();
			this.m_pixels = p.getPixels();
		    this.m_colorModel = p.getColorModel();
		    System.out.println("Pixels: " + (p.getPixels() instanceof byte[] ? "bytes" : "ints"));
		    System.out.println("Model:  " + p.getColorModel());
		 
		    for(int i = 0;i<width;i++){
		    	for (int j=0;j<height;j++){
				    int c = oImg.getRGB(i,j);
				    int  red = (c & 0x00ff0000) >> 16;
				    int  green = (c & 0x0000ff00) >> 8;
				    int  blue = c & 0x000000ff;
				    
					System.out.println("RGB of pixel : " + i +"," + j + "is :" + red+ ","+green+","+blue);
		    	}
		    }
		  }catch(Exception e){
			  System.out.println("Exception in GetImage :" + e.getMessage());
		  }
	  }
	  
}
