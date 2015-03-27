package stegoga;

/*
 * The ImageTesting module is used to perform two
 * tests - psnr and mse on the original image and the
 * stego image and use ResultsandReporting to capture
 * the results to a file.
 **/

public class ImageTesting{
	private ResultsandReporting reporting;
	private PSNR psnrmseObj; 
	
	public ImageTesting(String sourceImage,String modImage){
		this.reporting = new ResultsandReporting();
		this.psnrmseObj = new PSNR(sourceImage,modImage);
	}
	
	public void performPSNRTest(){
		this.psnrmseObj.calculatePsnr();
	}
	
	public void performMSETest(String srcFile, String stegoFile){
		
	}
	
}