package stegoga;

import java.util.Arrays;
import java.util.Comparator;

/*
 * Population is used to maintain our genetic population of chromozomes.
 * It provides various methods to facilitate chromozome fetch , modify
 * and save it back to the population. 
 */
public class Population{
	
	//these are what our population is made up of - Chromozomes !!
	private Chromozome[] chromozomes;
	
	
	/* Creates our population for 
	 * applying genetic algorithm.
	 */
	public Population(int populationSize){
		chromozomes = new Chromozome[populationSize];
		for(int i=0;i<populationSize;i++){
			chromozomes[i] = new Chromozome();
		}
	}
	
	
	/*
	 * Initialize our population with the intial set of
	 * chromozomes we created by digitizing the image.
	 * */
	public void initializePopulation(Chromozome[] chromozomes){
		this.chromozomes = new Chromozome[chromozomes.length];
		for(int i=0;i<chromozomes.length;i++){
			this.chromozomes[i] = chromozomes[i];
		}
	}
	
	
	/*
	 * Returns the size of the population. 
	 */
	public int size(){
		return this.chromozomes.length;
	}

	
	/*
	 * Save the selected chromozome to our population.
	 * */
	public void updateChromozome(Chromozome c){
		for(int i=0;i<this.chromozomes.length;i++){
			if(this.chromozomes[i].id == c.id){
				this.chromozomes[i] = c;
			}
		}		
	}
	
	
	/*
	 * Get all chromozomes.
	 */
	public Chromozome[] getAllChromozome(){
		return this.chromozomes;
	}	

	
	/*
	 * Returns the fittest chromozome in the population. 
	 * Time complexity is O(n)
	 */
	public Chromozome getFittest(){
		Chromozome c = this.chromozomes[0];
		for(int i=0;i<this.chromozomes.length;i++){
			if(this.chromozomes[i].getFitness() < c.getFitness()){
				c = this.chromozomes[i];
			} 
		}
		return c;
	}
	
	
	/*
	 * Returns the no. of best chromozomes with fitness in ascending order
	 * by sorting the fitness of each individual.
	 * no. of best chromozomes = Utils.FIT_CHROMO_COUNT
	 * We maintain elitism here so that we don't re-select the chosen chromozome
	 * for the next solution.
	 */
	public Chromozome[] getFittestChromoCount(){		
		//Sort our population based on fitness
		Arrays.sort(this.chromozomes,new Comparator<Chromozome>() {

			public int compare(Chromozome o1, Chromozome o2) {
				return o1.compareTo(o2);
			}
			
		});
		
		//Return the chosen ones
		Chromozome[] fittestChroms  = new Chromozome[Utils.FIT_CHROMO_COUNT];
		int index = 0;
		for(int i=0;i<this.chromozomes.length;i++){
			if(this.chromozomes[i].getSolutionId()==-1){
				fittestChroms[index++]=this.chromozomes[i];
			}
			if (index==Utils.FIT_CHROMO_COUNT){
				break;
			}
		}
		return fittestChroms;
	}
	
}