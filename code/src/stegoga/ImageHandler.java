package stegoga;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.util.Random;

public class ImageHandler{
	
	private Object m_pixels;
    // get the palette of the image, if possible
    private ColorModel m_colorModel;
    
    public ImageHandler(){
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
		  
		  	//read image data to bufferedImage object
		    File file = new File(filename);
		    FileInputStream fis =  new FileInputStream(file);
			BufferedImage oImg = ImageIO.read(fis);
			
			//get width and height properties.
			int width = oImg.getWidth();
			int height = oImg.getHeight();
			//int totalpixels = (width*height);
			System.out.println("image width is  :" + oImg.getWidth());
			System.out.println("image height is :" + oImg.getHeight());
			StegoImageHandler sImg = new StegoImageHandler(oImg,width,height);
			
			//grab pixels and pixel depth information
			PixelGrabber p = new PixelGrabber(oImg,0, 0, width, height, false);
			p.grabPixels();
			this.m_pixels = p.getPixels();
		    this.m_colorModel = p.getColorModel();
		    System.out.println("Pixels: " + (p.getPixels() instanceof byte[] ? "bytes" : "ints"));
		    System.out.println("Model:  " + p.getColorModel());
		    
			//get bitdepth; - 8,16,24 or 32
			int bitdepth = this.getBitDepth();
			System.out.println("Image depth found is :" + bitdepth);
		 
			//get pixel value and decompose it it's rgb components.
		    for(int i = 0;i<width;i++){
		    	for (int j=0;j<height;j++){
		    		//byte is represented as a signed integer in java.
				    int c = oImg.getRGB(i,j);
				    System.out.println("Value of rgb is "+c);
				    
				    //use the bit mask as shown below to fetch r,g,b values
			        int alpha = (c >> 24) & 0xff;
			        int red   = (c >> 16) & 0xff;
			        int green = (c >>  8) & 0xff;
			        int blue  = (c      ) & 0xff;
			        System.out.println("old RGB of pixel : " + i +"," + j + "is :" + red+ ","+green+","+blue);
			        
			        //tinker with rgb to stitch a new image..
			        /*Random rand= new Random();
			        red = rand.nextInt(31) % 200;
			        green =  rand.nextInt(31) % 200;
			        blue = rand.nextInt(31) % 200;*/
			        
			        red = red - 10;
			        green = green - 10;
			        blue =  blue - 10;
			        if (red < 0 || red > 255 ){
			        	red = red+100;
			        }
			        if (green < 0 || green > 255){
			        	green = green+100;
			        }
			        if (blue < 0 || blue > 255){
			        	blue = blue +100;
			        }
			       System.out.println("new RGB of pixel : " + i +"," + j + "is :" + red+ ","+green+","+blue);
			       Color clrPixel = new Color(red, green,blue);
			       int rgb = clrPixel.getRGB();
			       sImg.setPixel(i, j, rgb);
			        
				    /*Another method to obtain r,g,b		    
				    int  red = (c & 0x00ff0000) >> 16;
				    int  green = (c & 0x0000ff00) >> 8;
				    int  blue = c & 0x000000ff;*/		        
		    	}
		    }
		    sImg.writeImage("lena64_modified.bmp");
		    System.out.println("written image to disk..");
		    
		  }catch(Exception e){
			  System.out.println("Exception in GetImage :" + e.getMessage());
		  }
	  }
	  
	  
	  
}
