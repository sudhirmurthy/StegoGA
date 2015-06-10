package stegoga;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileInputStream;


public class ImageHandler{
	
	private Object m_pixels;
    
	// get the palette of the image, if possible
    private ColorModel m_colorModel;
    
    //our PixelData of the entire image
    private PixelData[][] m_pixelData;
    
    //height of the image
    private int height;
    
    //width of the image
    private int width;
    
    //Buffered Image 
    private BufferedImage m_Img;

    

    /*
     * Default Constructor.
     */
    public ImageHandler(){
    	this.m_colorModel = null;
    	this.m_pixels = new Object();
    	this.m_pixelData = new PixelData[0][0];
    	this.m_Img = null;
    }
    
    
    public BufferedImage getBuffImage(){
    	return this.m_Img;
    }
    
    
    /*
     * Gets the color depth of pixels, 16, 24, 32 etc..
     */
    public int getBitDepth(){
    	if (null==this.m_colorModel){
    		return -1;
    	}else
    	{
    		return this.m_colorModel.getPixelSize();
    	}
    }   
   
    
	/*
	 * Gets the red component of the pixel 
	 */
    public int getRed(int pixel){
	    if ((this.m_colorModel instanceof IndexColorModel))	  
	    	return ((IndexColorModel)this.m_colorModel).getRed(pixel);
	    else
	    	return ((DirectColorModel)this.m_colorModel).getRed(pixel);
	}
	
    
	/*
	 * Gets the green component value of a pixel
	 */
    public int getGreen(int pixel){
	    if ((this.m_colorModel instanceof IndexColorModel))	  
	    	return ((IndexColorModel)this.m_colorModel).getGreen(pixel);
	    else
	    	return ((DirectColorModel)this.m_colorModel).getGreen(pixel);
	}

    
   /* 
	* Gets the blue component value of a pixel
	*/
	public int getBlue(int pixel){
	    if ((this.m_colorModel instanceof IndexColorModel))	  
	    	return ((IndexColorModel)this.m_colorModel).getBlue(pixel);
	    else
	    	return ((DirectColorModel)this.m_colorModel).getBlue(pixel);
	}
	
	
	public int getWidth(){
		return this.width;
	}

	
	public int getHeight(){
		return this.height;
	}
	  
	  
	public PixelData[][] GetImageData(String filename){
	try{
		  
			//read image data to bufferedImage object
		    File file = new File(filename);
		    FileInputStream fis =  new FileInputStream(file);
			this.m_Img = ImageIO.read(fis);
			
			//get width and height properties.
			int width = this.m_Img.getWidth();
			int height = this.m_Img.getHeight();
			
			this.width = width;
			this.height = height;
	
			//grab pixels and pixel depth information
			PixelGrabber p = new PixelGrabber(this.m_Img,0, 0, width, height, false);
			p.grabPixels();
			this.m_pixels = p.getPixels();
		    this.m_colorModel = p.getColorModel();
		    
		    System.out.println("Pixels: " + (p.getPixels() instanceof byte[] ? "bytes" : "ints"));
		    System.out.println("Model:  " + p.getColorModel());
		    
			//get bitdepth; - 8,16,24 or 32
			int bitdepth = this.getBitDepth();
			//System.out.println("Image depth found is :" + bitdepth);
			this.m_pixelData = new PixelData[width][height];		
			
			//get pixel value and decompose it into it's rgb components.
		    for(int x = 0;x<width;x++){
		    	for (int y=0;y<height;y++){
		    		//byte is represented as a signed integer in java.
				    int color = this.m_Img.getRGB(x,y);
				    this.m_pixelData[x][y] = new PixelData(x,y,color);        
				 }
		    }
		}catch(Exception e){
	  		  System.out.println("Exception in GetImage :" + e.getMessage());
		}		 
	  	return(this.m_pixelData);
	  }
	
	
	/*
	 * Sets the image data for each pixel 
	 * specified by the x,y co-ordinate.
	 * the passedin param @pixelData is used to set
	 * the BufferredImage m_Img's pixel data. 
	 */
	  public void setImageData(PixelData[][] pixelData){
		  for(int x=0;x<this.width;x++){
			  for(int y=0;y<this.height;y++){
				  this.m_pixelData[x][y] = pixelData[x][y];
				  this.m_Img.setRGB(x, y, pixelData[x][y].Pixel());
			  }
		  }
	  }
	  
	  
	  /*
	   * Writes the BufferedImage in memory to disk..
	   */
	  public boolean WriteImageToDisk(String fileName){
		  try{
			  File outputFile = new File(fileName);
			  ImageIO.write(this.m_Img, "bmp", outputFile);
		  }catch(Exception e){
			  System.out.println("Exception generating image..");
			  return false;
		  }
		  System.out.println("Successfully wrote image to disk..");
		  return true;
	  }
	  
}
