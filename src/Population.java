//Author Alex Calamaro
import java.io.*;

public class Population {
	public final int POPULATION_SIZE;
	AI[] individuals;
	private AI[][] container;
	private final String FILENAME = "populationlog.txt";

	public Population(int i) {
		this.POPULATION_SIZE = i;
		initPop(this.POPULATION_SIZE);
	}

	// Initialize the population to a given size.
	public void initPop(int num) {
		if (num % 2 == 1) {
			num -= 1;
		}
		this.individuals = new AI[num];
		for (int i = 0; i < num; i++)
			this.individuals[i] = new AI();
	}

	// Splits the population to perform tournaments in parallel.
	public void split(int cores) {
		int counter = 0;
		this.container = new AI[cores][POPULATION_SIZE / cores];
		for (AI ai : this.individuals) {
			ai.fitness = 0;
		}
		for (int x = 0; x < cores; x++) {
			for (int y = 0; y < POPULATION_SIZE / cores; y++) {
				this.container[x][y] = this.individuals[counter];
				counter++;
			}
		}
	}

	public AI[][] getContainer() {
		return this.container;
	}

	// Shuffles the population of individuals
	public void shufflePop() {
		for (int i = 0; i < this.individuals.length; i += 2) {
			double random = Math.random();
			int rand = (int) (this.individuals.length * random);

			AI temp = individuals[0];
			this.individuals[0] = individuals[rand];
			this.individuals[rand] = temp;
		}
	}

	// Passes a new array of individuals back to the population class.
	public void updatePop(AI[] arr) {
		this.individuals = arr;

	}

	public AI getWinner() {
		AI winner = new AI();
		int max = 0;
		for (int i = 0; i < this.individuals.length; i++) {
			if (this.individuals[i].fitness > max) {
				max = this.individuals[i].fitness;
				winner = this.individuals[i].clone();
			}
		}
		return winner;
	}

	// Prints a given individual to designated file.
	public void printToFile(AI input) {
		try {
			// Create file
			FileWriter fstream = new FileWriter(FILENAME);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(input.toString() + "\n");
			// Close the output stream
			out.close();
		} catch (Exception e) { // Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	// Prints the population of every generation to a file titled by designated
	// filename. Might be computationally expensive.
	public void printPopulationToFile(int i) throws IOException {
		FileWriter fstream;
		BufferedWriter out;

		File f = new File(FILENAME);
		if (!f.isFile()) {
			try { // Create file
				fstream = new FileWriter(FILENAME);
				out = new BufferedWriter(fstream);
				out.write("Generation: " + i + "\r\n");

				for (AI ai : this.individuals) {
					out.write(ai.toString() + "\r\n");
				}
				// Close the output stream
				out.close();
			} catch (Exception e) { // Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		} else {
			try { // append to file if file already exists
				fstream = new FileWriter(FILENAME, true);
				out = new BufferedWriter(fstream);
				out.write("Generation: " + i + "\r\n");
				for (AI ai : this.individuals) {
					out.write(ai.toString() + "\r\n");
				}
				out.close();
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	public void printBestIndividual() {
		// Since many of the individuals in population have same fitness, we
		// will perform one more tournament.
		System.out.println("Finalizing");
		QuoridorBoard board;
		for (int t = 0; t < (Math.log(individuals.length) / Math.log(2)); t++) {
			for (int i = 0; i < individuals.length - 1; i++) {
				if (individuals[i].fitness == t) {
					for (int j = i + 1; j < individuals.length; j++) {
						if (individuals[j].fitness == t) {
							board = new QuoridorBoard(individuals[i],
									individuals[j]);
							board.play();
							if (board.getWinner() != null) {
								AI winner = board.getWinner();
								AI loser = board.getLoser();
								winner.fitness++;
								individuals[i] = winner;
								individuals[j] = loser;
							}
							break;
						}
					}
				}
			}
		}

		try {
			FileWriter fstream = new FileWriter("evoOutput.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			int max1 = 0;
			int max2 = 0;
			AI best1 = new AI();
			AI best2 = new AI();
			for (AI ai : individuals) {
				if (ai.fitness > max1) {
					max1 = ai.fitness;
					best1 = ai.clone();
				} else if (ai.fitness > max2) {
					max2 = ai.fitness;
					best2 = ai.clone();
				}
			}
			out.write(best1.outputString() + "\r\n" + best2.outputString()
					+ "\r\n");
			out.close();
			System.out.println("Done");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
