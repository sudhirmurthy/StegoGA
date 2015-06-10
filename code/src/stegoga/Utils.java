package stegoga;


import java.io.File;
import java.util.*;

/* Random utility functions for the StegoGA
 * project.
 */
public class Utils{
	
	public static final int COLOR_LEN = 3;
	public static final int MAX_WORLD_VALUE = 254;
	public static final int MIN_WORLD_VALUE = 0;
	public static final int FIT_CHROMO_COUNT = 3;
	
	public Utils(){
		
	}
	
	public long getUnsignedInt(byte b){
		return 0;
	}
	
	public int getSignedInt(byte b){
		return -1;
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	/*
	 * Returns the minimum of all all genes, compared to the solution.
	 */
	public static int[] getMinDx(int[] allGenes, int solution) {
		int[] genes = allGenes;
				
		int minDx = Math.abs(solution-genes[0]);
		int chosen = 0;
		
		for(int i=1;i<genes.length;i++){
			if(Math.abs(solution- genes[i]) < minDx){
				minDx = Math.abs(solution - genes[i]);
				chosen = i;
			}
		}	
		
		for(int i=0;i<genes.length;i++){
			if(chosen == i){				 
				genes[i] = (solution - genes[i]); //capture the delta.				
			}
			else{
				genes[i] = -1000;
			}
		}
		return genes;
	}
	
	
	/*
	 * Given a file, it returns it's extension
	 */
	public static String getFileExtension(File file) {
	    String name = file.getName();
	    try {
	        return name.substring(name.lastIndexOf("."));

	    } catch (Exception e) {
	        return null;
	    }

	}
	
}