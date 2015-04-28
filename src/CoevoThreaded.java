//Author: Alex Calamaro

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

public class CoevoThreaded {

	public final int NUM_THREADS = 4; // Change the number of physical + logical
										// cores used. Match to your machine.
	public final int POPULATION_SIZE = 16; // Adjust the number of individuals
											// evolved. Powers of 2 are ideal.
	public final int GENERATIONS = 5;
	public final int TOURNAMENT_SIZE = 4; // Do not make this be more than
											// population size/numThreads.
	public final boolean CROSSOVER = false;

	public ArrayBlockingQueue<AI> winners = new ArrayBlockingQueue<AI>(
			POPULATION_SIZE / 2);
	public ArrayBlockingQueue<AI> losers = new ArrayBlockingQueue<AI>(
			POPULATION_SIZE / 2);

	public CoevoThreaded() {
		DataQueue.setQueueSize(POPULATION_SIZE);
	}

	// Assigns the subsets of AI's to different threads. Starts the threads.
	public void binaryTournament(AI[][] groups) {
		for (int i = 0; i < groups.length; i++) {
			Thread t = new Thread(new AIThread((groups[i]), TOURNAMENT_SIZE,
					CROSSOVER));
			t.start();
		}
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		// Initialize the population and storage queue
		CoevoThreaded evolver = new CoevoThreaded();
		Population population = new Population(evolver.POPULATION_SIZE);
		DataQueue.setQueueSize(evolver.POPULATION_SIZE);

		// Perform parallel tournament on segmented population

		for (int i = 0; i < evolver.GENERATIONS; i++) {
			population.shufflePop();
			System.out.println("Starting Generation: " + i);
			population.split(evolver.NUM_THREADS);
			evolver.binaryTournament(population.getContainer());

			// Update population
			population.updatePop(DataQueue.getResults());
		}
		population.printBestIndividual();
	}

	// Static queue to allow communication from threads
	public static class DataQueue {
		public static ArrayBlockingQueue<AI> AIQueue;

		public static void setQueueSize(int num) {
			AIQueue = new ArrayBlockingQueue<AI>(num);
		}

		public static void addToQueue(AI[] ind) {
			for (AI i : ind) {
				AIQueue.add(i);
			}
		}

		public static AI[] getResults() throws InterruptedException {
			while (AIQueue.remainingCapacity() != 0) {
				Thread.sleep(30);
			}

			int size = (AIQueue.size());
			AI[] results = new AI[size];

			for (int i = 0; i < size; i++) {
				results[i] = AIQueue.take();
			}
			return results;
		}
	}

	// Thread class where games are run
	public class AIThread implements Runnable {
		AI[] players;
		AI[] winners;
		int tSize;
		boolean crossover;

		public AIThread(AI[] input, int tournSize, boolean cross) {
			this.players = input;
			this.tSize = tournSize;
			this.crossover = cross;
			this.winners = new AI[input.length];
		}

		// code that is actually executed in a separate thread.
		// Binary tournament to assign fitness. Then selection and mutation
		public void run() {

			QuoridorBoard board;
			for (int t = 0; t < (Math.log(players.length) / Math.log(2)); t++) {
				for (int i = 0; i < players.length - 1; i++) {
					if (players[i].fitness == t) {
						for (int j = i + 1; j < players.length; j++) {
							if (players[j].fitness == t) {
								board = new QuoridorBoard(players[i],
										players[j]);
								board.play();
								if (board.getLoser() != null) {
									AI winner = board.getWinner();
									AI loser = board.getLoser();
									winner.fitness++;
									players[i] = winner;
									players[j] = loser;
								}
								break;
							}
						}
					}
				}
			}

			// Tournament selection

			for (int x = 0; x < this.players.length; x++) {
				AI[] tournAi = new AI[tSize];
				for (int y = 0; y < tSize; y++) {
					int pick = (int) (Math.random() * this.players.length);
					tournAi[y] = players[pick].clone();
				}
				int max = 0;
				AI winner = new AI();
				for (AI ai : tournAi) {
					if (ai.fitness > max) {
						max = ai.fitness;
						winner = ai.clone();
					}
				}
				winners[x] = winner.clone();
			}
			if (crossover) {
				for (int z = 0; z < this.winners.length - 1; z += 2) {
					winners[z].crossover(winners[z + 1]);
				}
			}
			for(AI blerp: winners){
				blerp.mutate();
			}
			DataQueue.addToQueue(this.winners);
			return;
		}
	}
}
