package stegoga;


/* Fitness used to define fitness for all chromozomes.
 * Fitness function is f(x) => Min[abs(x-red),abs(x-green),abs(x-blue)] 
 * If any chromozone exhibits the fitness function f(x) as = x, then 
 * that chromozone has the highest / ideal fitness in the population. 
 * The next set of fitness are exhibited by chromozomes that satisfy
 * f(x) such that x is closer to f(x).
 */
public class Fitness{
	
	//this is the desired solution
	private int[] m_solution;
	
	//this is the current solution index
	//of the desired solution.
	private int i_currSolutionIndex;

	/*
	 * Default constructor. 
	 */
	public Fitness(int[] solution){
		this.m_solution = solution;
		this.i_currSolutionIndex = 0;
	}
	
	public void incrementSolutionIndex(){
		this.i_currSolutionIndex+=1;
	}
	
	public int getSolution(){
		return this.m_solution[i_currSolutionIndex];
	}
	
	/*
	 *Calculates a fitness function for each chromozome
	 *f(x) = Min[abs(x-r),abs(x-g),abs(x-b)]
	 */
	public Chromozome[] calculateFitness(Chromozome[] all){
		int desiredSolution = m_solution[i_currSolutionIndex]; 
		
		int[] absDelta = new int[Utils.COLOR_LEN];
		for(int i=0;i<absDelta.length;i++){
			absDelta[i] = 0;
		}
		
		for(int i=0;i<all.length;i++){			
			for(int colorIndex=0;colorIndex<Utils.COLOR_LEN;colorIndex++){
				absDelta[colorIndex] =  Math.abs(desiredSolution - all[i].getGene(colorIndex));				
			}
			all[i].setFitness(this.getMinimumArray(absDelta));
		}		
		return all;
	}
	
	/* 
	 * Returns the minimum element in the given array.
	 * Tx = O(n)
	 */
	private int getMinimumArray(int[] arr){
		int min = arr[0];
		for(int i=0;i<arr.length;i++){
			if(arr[i] < min){
				min = arr[i];
			}
		}
		return min;
	}	
	
}