package stegoga;


/*	GA is used to implement the genetic algorithm which provides operations for 
 *  	1] Defining and initializing our population
 *  	2] Initializing our solution  
 *  	3] How does the population evolves to the solution
 *  		- rank selection based on fitness function
 *  		- cross over ( with configurable crossover rate [0-1] )
 *  		- mutation   ( with configurable mutation rate [0-1] )
 */
public class GA{
	
	// Due to our solution space, We consider crossover rate of 0.9
	private static final double crossoverRate = 0.5;
	
	// Mutation rate is set to 5%.
	private static final double mutationRate  = 0.05;

	// We define a universal uniformRate constant(not used now)
	private static final double uniformRate   = 0.5;
	
	// We perform a steady state selection using tournaments.
	//private static final int tournamentSize = 2;
	
	// Maintain elite individuals
	private static final boolean elitism = true;
	
	//generation count
	private static final int MAX_GENERATIONS = 3;
	
	// Our Fitness member variable to help get fitness for each chromozome.
	private Fitness m_fitness; 
	
	// Our population member variable to help manage population.
	private  Population m_population;
	
	// Our current solution index in the array.
	private int Solution_Index = 0;
	
	
	/*
	 *  Default constructor for for GA. 
	 */
	public GA(int[] arr_solution, int populationSize){
		m_fitness = new Fitness(arr_solution);
		m_population = new Population(populationSize);
		//System.out.println("\nCrossOver Rate : " + crossoverRate);
		//System.out.println("\nMutation  Rate : " + mutationRate);
	}
	
	
	/*
	 * Initialize our population and calculate the fitness for each
	 * population.
	 */
	public void initializePopulation(Chromozome[] population){
		Chromozome[] fitPopulation = m_fitness.calculateFitness(population);
		m_population.initializePopulation(fitPopulation);
	}
		
	
	/* Evolve our population such that we select the 
	 * best chromozomes in every generation and mate,
	 * Cross-over makes sure that the population converges into the given solution.
	 * Mutation makes sure that we diverge away a little bit so that we have the 
	 * capacity to get out of our local maximum.
	 */
	public Population evolvePopulation(){
		
		Chromozome child = null ;
		
		for(int i=0;i<MAX_GENERATIONS;i++){
			//System.out.println("\n------------------------------------------");
			//System.out.println("\nGeneration : "+(Solution_Index));
			
			//Selection
			Chromozome parent1 = rankSelection(m_population);
			Chromozome parent2 = rankSelection(m_population);
			
			//CrossOver
			child = crossover(parent1, parent2);
			m_population.updateChromozome(child);  
			
			//See if we found the solution already
			if(child.getSolutionId() == -1 && i==(MAX_GENERATIONS -1 )){
				//Found solution closer to the desired solution.
				child.setSolutionId(this.Solution_Index);
				m_population.updateChromozome(child);
				//System.out.println("\nSet the gene as "+child.getGene(0) + ","+child.getGene(1)+","+child.getGene(2));
				break;
			}
			else if(child.getSolutionId() != -1) {
				//Found exact solution.
				break;
			}			
			
			//Mutation
			Chromozome[] fittestChromozomes =  m_population.getFittestChromoCount();
			for(int j = 0;j< fittestChromozomes.length;j++){
				//System.out.println("\nmutating the ranked population..");
				m_population.updateChromozome(mutate(fittestChromozomes[j]));
			}
		}
		
		//System.out.println("\nFound GA Solution..");
		//Preserve Elite Chromozomes.
		//System.out.println("\nSolution is  : " + m_fitness.getSolution());
		//System.out.println(", x : " + child.getX()+" , y :" + child.getY());
		//System.out.println(", GA Soln. is  :");
		for(int i=0;i<3;i++){
			//System.out.println("\nRGB : " +child.getGene(i) + " ");
		}
		//System.out.println("\n------------------------------------------");
		
		m_fitness.incrementSolutionIndex();
		this.Solution_Index++;
		
		return m_population;
	}

	
	/* GA - Crossover is defined as selecting the best genes
	 * from both the parents into the child. Since our encoding
	 * is in integers, We must have a high cross over rate to 
	 * converge to the solution quickly. At the same time a low
	 * mutation rate guarantees that we don't fall into the local
	 * minima.
	 */
	public Chromozome crossover(Chromozome parent1,
									   Chromozome parent2){
		Chromozome child = new Chromozome();
		if(parent1.getFitness() < parent2.getFitness()){
				child = parent1;
		}
		else{
				child = parent2;
		}
		//genes get closer to solution by CrossOverRate;
		int solution = m_fitness.getSolution();
		int r =  child.getGene(0);
		int g =  child.getGene(1);
		int b =  child.getGene(2);
		int dr = Math.abs(solution - r);
		int dg = Math.abs(solution - g);
		int db = Math.abs(solution - b);
		int min = dr;
		int chosen = 1;	
		if (dg < min){
			min = dg;
			chosen = 2;
		}
		if(db < min){
			min = db;
			chosen = 3;
		}		
		switch(chosen){
		
			case 1: //red
				if(r > solution){
					r-=(int)(dr*crossoverRate);
				}
				else if (r < solution) {
					r+=(int)(dr*crossoverRate);
				}
				else if(r==solution){ 
					//Answer found, value has converged into the solution.
					//don't change, elite individual..
					child.setSolutionId(this.Solution_Index);
					break;
				}
				child.setGene(0, r);
				break;
			case 2: //green
				if(g > solution){
					g-=(int)(dg*crossoverRate);
				}
				else if (g < solution) {
					g+=(int)(dg*crossoverRate);
				}
				else if (g==solution){
					//Answer found, value has converged into the solution.
					//don't change, elite individual..
					child.setSolutionId(this.Solution_Index);
					break;
				}
				child.setGene(1, g);
				break;
			case 3: //blue
				if(b > solution){
					b-=(int)(db*crossoverRate);
				}
				else if (b < solution) {
					b+=(int)(db*crossoverRate);
				}
				else if (b == solution){
					//Answer found, value has converged into the solution.
					//don't change, elite individual..
					child.setSolutionId(this.Solution_Index);
					break;
				}
				child.setGene(2,b);
				break;
		}
		
		return child;
	}
		
	
	/*
	 * Mutate the population. 
	 * Mutation is defined as randomly modifying the 
	 * genes of the given Chromozome so that
	 * we give a chance for the population to evolve and 
	 * not get stuck with the same chromozomes.(local minima) 
	 * Our mutation operation is defined as mutation(x) =>x+-(mutation_rate * (x)) ; 
	 * eg. x =  25, mutation(x) = (25 + (25*0.03)), i.e  x = 25.75 ~= (int) 26.0
	 * if operation is SUBTRACT,
	 * then,
	 * eg. x =  25, mutation(x) = (25 - (25*0.03)), i.e  x = 24.25 ~= (int) 24.0
	 * We maintain elitism by not mutating the Chromozome which is part of our solution.
	 * We can also choose to not maintain elitism as well. However, because, the problem
	 * domain is integers and we are allowing our population to evolve for 6 generations, 
	 * We make sure that we don't modify the genes of the Fittest Chromozome. 
	 */
	public Chromozome mutate(Chromozome c){
		//we apply mutation to all of the genes.(r,g,b)
		int gene_count=3;
		final int SUBRACT = 1;
		final int ADD = 2;
		int operation = Utils.randInt(1, 2);		
				
		for(int index=0;index<gene_count;index++){
			int value=c.getGene(index);
			//apply mutation formula.
			if(operation==SUBRACT){
				value-=(int)(value*mutationRate);
				//fix for value to be within our world.
				if (value < Utils.MIN_WORLD_VALUE){
					value+=(int)(value*mutationRate);
				}				
			}
			else if(operation==ADD){
				value+=(int)(value*mutationRate);
				//fix for value to be within our world.
				if (value > Utils.MAX_WORLD_VALUE){
					value-=(int)(value*mutationRate);
				}
			}
			//apply the mutated value to the gene
			c.setGene(index, value);
		}		
		return c;
	}
			
	
	/*
	 * Select Chromozomes for crossover using rank selection method
	 * various other selection methods exists - viz. tournament selection,
	 * roulette wheel etc.. Rank selection guarantees us to get to our
	 * solution quickly as we select the best chromozomes from the population.        
	 */
	private Chromozome rankSelection(Population pop){
		int randomFromRank  = Utils.randInt(0, Utils.FIT_CHROMO_COUNT - 1); 		
		Chromozome[] fittestChromozomes =  pop.getFittestChromoCount();
		return fittestChromozomes[randomFromRank];
	}
    
	
}