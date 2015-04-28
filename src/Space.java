//Author: Aidan Caroll

public class Space {
	//true if there is a wall in that direction
	boolean north;
	boolean south;
	boolean east;
	boolean west;
	boolean player;
	boolean visited;
	
	public Space(){
		north = false;
		south = false;
		east = false;
		west = false;
		player = false;
		visited = false;
	}
	
	//adds a wall in a given direction
	public void removeWall(Direction dir){
		switch(dir){
			case NORTH:
				north = false;
				break;
			case SOUTH:
				south = false;
				break;
			case EAST:
				east = false;
				break;
			case WEST:
				west = false;
				break;
			default:
				break;
			
		}
	}
	
	//adds a wall in a given direction
	public void addWall(Direction dir){
		switch(dir){
			case NORTH:
				north = true;
				break;
			case SOUTH:
				south = true;
				break;
			case EAST:
				east = true;
				break;
			case WEST:
				west = true;
				break;
			default:
				break;
			
		}
	}
	
	//tells the space there is a player here
	public void setPlayer(boolean b){
		player = b;
	}
	
	public boolean hasPlayer(){
		return player;
	}
	
	public void visit(){
		visited = true;
	}
	
	public void unvisit(){
		visited = false;
	}
	
	public boolean wasVisited(){
		return visited;
	}
	
	//true if there is a wall in the given direction
	public boolean hasWall(Direction dir){

		switch(dir){
			case NORTH:
				return north;
			case SOUTH:
				return south;
			case EAST:
				return east;
			case WEST:
				return west;
			default:
				return false;
		
		}
	}
	
	
}
