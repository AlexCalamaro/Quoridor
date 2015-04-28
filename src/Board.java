import java.util.ArrayList;


public interface Board {

	public ArrayList<Move> getValidMoves(AI caller);
	public int getMyWalls(AI caller);
	public int getOpponentsWalls(AI caller);
	public int distanceFromWall(AI caller, Direction direction);
	public boolean oddDistance(AI caller, Direction direction);
	public int getMyEndDistance(AI caller);
	public int getOpponentsEndDistance(AI caller);
	public void makeMove(AI caller, Move move);
	public void undoMove();
	public Direction getDirectionOfMove(AI caller, Move move);
	public void play();
	public AI getWinner();
	public AI getLoser();
}
