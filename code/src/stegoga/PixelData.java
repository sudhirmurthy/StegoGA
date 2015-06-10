package stegoga;

public class PixelData{
	
	private int x;
	private int y;
	private int pixelValue;
	private int alpha;
	private int red;
	private int green;
	private int blue;
	
	public PixelData(){
		this.x = -1;
		this.y = -1;
		this.pixelValue = -1;
		this.alpha = -1;
		this.red = -1;
		this.green=-1;
		this.blue = -1;
	}
	
	public PixelData(int x,int y,int pixelData){
		this.x = x;
		this.y = y;
		this.pixelValue = pixelData;
		
		//use the bit mask as shown below to fetch r,g,b values
        this.alpha = (this.pixelValue >> 24) & 0xff;
        this.red   = (this.pixelValue >> 16) & 0xff;
        this.green = (this.pixelValue >>  8) & 0xff;
        this.blue  = (this.pixelValue      ) & 0xff;
	}
	
	public void SetPixel(int x, int y,int alpha,int red,int green,int blue){
		this.x = x;
		this.y = y;
		this.alpha = alpha;
		this.red = red;
		this.green= green;
		this.blue = blue;
		//create a 32-bit color with a,r,g,b values.
		this.pixelValue =(alpha << 24) + (red << 16) + (green << 8) + blue;		
	}
		
	public int Pixel(){
		return this.pixelValue;
	}
	
	public int Alpha(){
		return this.alpha;	
	}
	
	public int Red(){
		return this.red;
	}
	
	public int Green(){
		return this.green;
	}
	
	public int Blue(){
		return this.blue;
	}	
	
}