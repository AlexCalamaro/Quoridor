//Author: Andrew Elenbogen

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class AI implements Mover {
	
	//holds all the nodes
	private HashMap<String, Double> weights;
	
	//random generator
	private Random random;
	
	//holds the fitness of this individual
	public int fitness;

	//creates a custom AI
	public AI(double oddDistanceWeight, double wallsLeftWeight,
			double distanceFromWallWeight, double distanceFromEndWeight, double randomMove) {
		this();
		defaultWeights( oddDistanceWeight,  wallsLeftWeight,
				 distanceFromWallWeight,  distanceFromEndWeight, randomMove);
	}
	
	//creates a random AI
	public AI()
	{
		this.random=new Random();
		fitness=0;
		defaultWeights( random.nextFloat()*20.0 - 20 , random.nextFloat()*20.0 - 20 , random.nextFloat()*20.0 - 20 , random.nextFloat()*20.0 - 20, 0);
		
	}
	
	//sets the default weights
	private void defaultWeights(double oddDistanceWeight, double wallsLeftWeight,
			double distanceFromWallWeight, double distanceFromEndWeight, double randomMove)
	{
		weights = new HashMap<String, Double>();
		weights.put("oddDistance", oddDistanceWeight);
		weights.put("wallsLeft", wallsLeftWeight);
		weights.put("distanceFromWall", distanceFromWallWeight);
		weights.put("distanceFromEnd", distanceFromEndWeight);
		weights.put("randomMove", randomMove);
	}
	
	//mutates based on a Gaussian of +-1
	public void mutate()
	{
		Set<String> keyset=weights.keySet();
		for(String currentKey:keyset)
		{
			this.setWeight(currentKey, weights.get(currentKey)
					-(random.nextGaussian()*20.0)-20);
		}
	}
	
	//crosses over two individuals by uniform crossover on the weighs
	public void crossover(AI other)
	{
		Set<String> keyset=weights.keySet();
		for(String currentKey:keyset)
		{
			if(random.nextBoolean())
			{
				double temp=this.getWeight(currentKey);
				this.setWeight(currentKey, other.getWeight(currentKey));
				other.setWeight(currentKey, temp);
			}
		}
	}
	public double getWeight(String key)
	{
		return weights.get(key);
	}
	public void setWeight(String key, double value)
	{
		weights.put(key, value);
	}
	
	//clones an AI
	public AI clone()
	{
		AI toReturn=new AI();
		Set<String> keyset=weights.keySet();
		for(String currentKey:keyset)
		{
			toReturn.setWeight(currentKey, weights.get(currentKey));
		}
		return toReturn;
	}
	
	//returns the best move based on this individual's weights
	public Move getMove(Board board)
	{
		//gets all possible moves
		ArrayList<Move> candidateMoves=board.getValidMoves(this);
		if(candidateMoves.size()==0){
			return null;
		}
		//shuffles them
		Collections.shuffle(candidateMoves, random);
		Move currentBestMove=candidateMoves.get(0);
		double bestMoveEval=evaluate(currentBestMove, board);
		//finds the best move
		for(Move currentMove: candidateMoves)
		{
			double currentEval=evaluate(currentMove, board);
			if(currentEval>bestMoveEval)
			{
				bestMoveEval=currentEval;
				currentBestMove=currentMove;
			}
		}
		return currentBestMove;
	}
	//North is y-1
	//East is x+1
	//evaluates a move to see how good it is
	private double evaluate(Move move, Board board)
	{
		Direction dir=board.getDirectionOfMove(this, move);
		board.makeMove(this, move);
		double sum=0;
		double temp;
		if(board.oddDistance(this, dir ))
		{
			temp=1;
		}
		else
		{
			temp=0;
		}
		//adds up all the weights accordingly using our neural net
		sum+=this.getWeight("oddDistance")*temp;
		sum+=this.getWeight("wallsLeft")*(board.getMyWalls(this)-board.getOpponentsWalls(this));
		sum+=this.getWeight("distanceFromWall")*board.distanceFromWall(this, dir);
		sum+=this.getWeight("distanceFromEnd")*(board.getMyEndDistance(this)-board.getOpponentsEndDistance(this));
		sum+=this.getWeight("randomMove")*(random.nextInt(10));
		board.undoMove();
		return sum;
	}
	public String toString(){
		return weights.toString();
	}
	public String outputString(){
		String s = "";
		 s += this.getWeight("oddDistance") + " ";
		 s += this.getWeight("wallsLeft") + " ";
		 s += this.getWeight("distanceFromWall") + " ";
		 s += this.getWeight("distanceFromEnd") + " ";
		 s += this.getWeight("randomMove") + " ";
		 return s;
	}
}
	