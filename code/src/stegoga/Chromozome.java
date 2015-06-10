package stegoga;



/* Class Chromozome represents a single chromozome
 * with genes. The genes are the red,green and blue values
 * which are from 0-254;
 * Each chromozome has a fitness based on the solution 
 * that we are tending to. 
 * */
public class Chromozome implements Comparable<Chromozome> {

	//the genes of our chromozome is made up of r g b values of the pixel
	private int[] genes;
	//the fitness measure of our chromozome
	private int fitness;
	//the x pixel which it denotes in the source image
	private int xPixel;
	//the y pixel which it denotes in the source image
	private int yPixel;
	//if a chromozome is an elite candidate, then
	//the solution_Id is set to the solution index
	private int solution_Id;
	//each chromozome's id is it's index in our population.
	public int id;
	
	
	/* Default constructor for Chromozome.
	 * It initializes all values to -1
	 */
	public Chromozome(){
		//one for red, one for blue, one for green.
		this.genes = new int[3];
		for(int i=0;i<this.genes.length;i++){
			this.genes[i]=-1;
		}
		this.fitness = -1;
		this.xPixel = -1;
		this.yPixel = -1;
		this.solution_Id = -1;
	}
	
	
	/* Chromozome constructor to create genes from
	 * r,g,b values and track its x,y co-ordinates.
	 */
	public Chromozome(int id,int x,int y,int red,int green,int blue){
		this.id = id;
		//Create the genomes
		this.genes = new int[3];
		this.genes[0] = red;
		this.genes[1] = green;
		this.genes[2] = blue;
		//assign fitness and track it's x,y value
		this.fitness = -1;
		this.xPixel = x;
		this.yPixel = y;
		this.solution_Id = -1;
	}
	
	
	/*
	 * Returns the xPixel value 
	 */
	public int getX(){
		return this.xPixel;
	}
	
	
	/*
	 * Returns the yPixel value. 
	 */
	public int getY(){
		return this.yPixel;
	}
	
	
	/* Returns the gene at the given index.
	 */
	public int getGene(int index){
		return this.genes[index];
	}
	
	
	/* Sets the gene at the given index with 
	 * the given value.
	 */
	public void setGene(int index,int value){
		this.genes[index] = value;
	}
	
	
	/*
	 * Returns all the genes of the chromozome. 
	 */
	public int[] getAllGenes(){
		return this.genes;
	}
	
	
	/* Returns the size of the gene.
	 */
	public int size(){
		return this.genes.length;
	}
	
	
	/* Returns fitness of this chromozome 
	 */
	public int getFitness(){
		return this.fitness;
	}
	
	
	/* Sets the fitness of this chromozone
	 */
	public void setFitness(int fitness){
		this.fitness = fitness;
	}
	

	/*
	 * Set's each chromozome's individual id. 
	 */
	public void setId(int id){
		this.id = id;
	}	
	
	
	/*
	 * This value is set if the Choromozome is
	 * an elite candidate. 
	 */
	public void setSolutionId(int solutionId){
		this.solution_Id = solutionId;
	}
	
	
	/*
	 * Returns the solutionId of this Chromozome 
	 */
	public int getSolutionId(){
		return this.solution_Id;
	}
	
	
	/*
	 * Override to compare fitness of this Chromozome
	 * with the other Chromozome o 
	 */
	
	@Override
	public int compareTo(Chromozome o) {
		return Integer.compare(this.fitness, o.fitness);
	}
	
	@Override
	public String toString(){
		return "X :" + this.xPixel + ", Y:" + this.yPixel + " SolnId: " + this.solution_Id +" red: "+ this.genes[0] + ", green :" + this.genes[1] + ", blue :"+ this.genes[2];
	}
	
}