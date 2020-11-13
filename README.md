# StegoGA
Genetic Algorithm Implementation of 24-bit bmp image Steganography.

Outline of the Basic Genetic Algorithm
-----------------------------------------------------
1.[Start] Generate random population of n chromosomes (suitable solutions for the problem)
2.[Fitness] Evaluate the fitness f(x) of each chromosome x in the population
3.[New population] Create a new population by repeating following steps until the new population is complete
	-[Selection] Select two parent chromosomes from a population according to their fitness (the better fitness, the bigger chance to be selected)
	-[Crossover] With a crossover probability cross over the parents to form a new offspring (children). If no crossover was performed, offspring is an exact copy of parents.
	-[Mutation] With a mutation probability mutate new offspring at each locus (position in chromosome).
	-[Accepting] Place new offspring in a new population
4.[Replace] Use new generated population for a further run of algorithm
5.[Test] If the end condition is satisfied, stop, and return the best solution in current population
6.[Loop] Go to step 2



State change of a population
-----------------------------------------------------
1] simple state - entire population replaced each generation.
2] steady state - only few individuals are replaced each generation


Chromozome selection from population
----------------------------------------------------
1] tournament selection
2] rank based
3] roulette whell
4] probability based


Evolving GA
----------------------------------------------------
1] Generations v/s Solution 


Fitness Function
----------------------------------------------------
Each Chromozome has 3 genes [red,green,blue]

Desired Solution = {13,-127,50,25,115}

x = part of Desired Solution (13)

f(x) = Min[abs(x-red),abs(x-green),abs(x-blue)]


Simple Explanation
----------------------------------------------------
http://www.theprojectspot.com/tutorial-post/creating-a-genetic-algorithm-for-beginners/3
http://www.obitko.com/tutorials/genetic-algorithms/ga-basic-description.php

Issues
----------------------------------------------------
signed integer dipslay of bytes
validation
Total message hiding capacity = 1 byte / pixel
i.e 64*64 size image has a capacity of approx 4 kb.
