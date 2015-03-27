package stegoga;

import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public final class PSNR {
	//psnr value
	private double m_psnr;
	//mean squared error value
	private float m_mse;
	//souce file path
	private String sFile;
	//modified file path
	private String mFile;
	
		public PSNR(){
			this.m_psnr = 0.0f;
        	this.m_mse = 0.0f;
        	this.sFile = "";
        	this.mFile = "";
		}
	
        public PSNR(String sourceFile, String modifiedFile) {
        	this.m_psnr = 0.0f;
        	this.m_mse = 0.0f;
        	this.sFile = sourceFile;
        	this.mFile = modifiedFile;
        }
        
        private BufferedImage getImage(File file) {
            try {
                    return ImageIO.read(file);
            } catch (IOException ioe) {
                    ioe.printStackTrace();
            }
            return null;
        }

        private double getColorDistance(Color source, Color target) {
            if (source.equals(target)) {
                    return 0.0d;
            }
            final double red = source.getRed() - target.getRed();
            final double green = source.getGreen() - target.getGreen();
            final double blue = source.getBlue() - target.getBlue();
            return Math.sqrt(red * red + blue * blue + green * green);
        }

        /*sFile - source file
         *mFile - modified file*/
        public void calculatePsnr() {
                if (sFile==null|| mFile==null) {
                        System.err.println("Two files are not provided to compare..");
                        return;
                }
                File file1 = new File(this.sFile);
                if (!file1.exists() || !file1.canRead()) {
                        System.err.println("Cannot open file " + file1.getAbsolutePath());
                        return;
                }
                File file2 = new File(this.mFile);
                if (!file2.exists() || !file2.canRead()) {
                        System.err.println("Cannot open file " + file2.getAbsolutePath());
                        return;
                }
                BufferedImage image1 = this.getImage(file1);
                BufferedImage image2 = this.getImage(file2);
                if (image1.getWidth() != image2.getWidth()) {
                        System.err.println("Those two file do not have the same width");
                        return;
                }
                if (image1.getHeight() != image2.getHeight()) {
                        System.err.println("Those two file do not have the same height");
                        return;
                }
                
                final int size = image1.getHeight() * image1.getWidth();
                int totalRed = 0;
                int totalGreen = 0;
                int totalBlue = 0;
                int maxRed = -1;
                int maxGreen = -1;
                int maxBlue = -1;
                int worstRedX = -1;
                int worstRedY = -1;
                int worstGreenX = -1;
                int worstGreenY = -1;
                int worstBlueX = -1;
                int worstBlueY = -1;
                double maxDistance = -1;
                int maxX = -1;
                int maxY = -1;
                double totalDistance = 0;

                for (int i = 0; i < image1.getWidth(); i++) {
                        for (int j = 0; j < image1.getHeight(); j++) {
                                final Color color1 = new Color(image1.getRGB(i, j));
                                final Color color2 = new Color(image2.getRGB(i, j));
                                final double distance = this.getColorDistance(color1, color2);
                                totalDistance += distance;
                                if (distance > maxDistance) {
                                        maxDistance = distance;
                                        maxX = i;
                                        maxY = j;
                                }
                                final int redDiff = color1.getRed() - color2.getRed();
                                if (redDiff > maxRed) {
                                        maxRed = redDiff;
                                        worstRedX = i;
                                        worstRedY = j;
                                }
                                final int greenDiff = color1.getGreen() - color2.getGreen();
                                if (greenDiff > maxGreen) {
                                        maxGreen = greenDiff;
                                        worstGreenX = i;
                                        worstGreenY = j;
                                }
                                final int blueDiff = color1.getBlue() - color2.getBlue();
                                if (blueDiff > maxBlue) {
                                        maxBlue = blueDiff;
                                        worstBlueX = i;
                                        worstBlueY = j;
                                }
                                totalRed += redDiff * redDiff;
                                totalGreen += greenDiff * greenDiff;
                                totalBlue += blueDiff * blueDiff;
                        }
                }
                System.out.print("maxDistance: " + maxDistance);
                System.out.println(" at: " + maxX + " " + maxY);
                System.out.println("averageDistance: " + (totalDistance / size));
                System.out.println("total red: " + totalRed);
                System.out.println("total green: " + totalGreen);
                System.out.println("total blue: " + totalBlue);
                System.out.println("Worst red at " + worstRedX + " " + worstRedY + " is " + maxRed);
                System.out.println("Worst green at " + worstGreenX + " " + worstGreenY + " is " + maxGreen);
                System.out.println("Worst blue at " + worstBlueX + " " + worstBlueY + " is " + maxBlue);
                float meanSquaredError = (totalRed + totalGreen + totalBlue) / (image1.getWidth() * image1.getHeight() * 3);   
                this.m_mse = meanSquaredError;
                System.out.println("mean squarederror is " + meanSquaredError);
                if (meanSquaredError == 0) {
                        System.out.println("peak signal to noise ratio is 0");
                        return;
                }
                double peakSignalToNoiseRatio = 10 * StrictMath.log10((255 * 255) / meanSquaredError);
                this.m_psnr = peakSignalToNoiseRatio;
                System.out.println("peak signal to noise ratio is " + peakSignalToNoiseRatio);

        }
}

