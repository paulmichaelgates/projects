package core;

import java.util.Random;

/**
 * CPU opponent Player 
 * @author Paul Gates
 *
 */
public class CheckersComputerPlayer extends Player{
	
	/**
	 * CheckersComputerPlayer is an extension of player with the added capability to generate random moves
	 * @param type the type of player (O versus X) 
	 */
	public CheckersComputerPlayer(Type type) {
		super(type);
		
	}
	
	/**
	 * generates a random move based on the type of player 
	 * @param logic CheckersLogic Object
	 * @return move a string representation of a move
	 */
	public String generateMove(CheckersLogic logic) {
		
		int[][] pieces = new int[GameUtil.playerPeices][2];
		
		char[][] board = logic.getBoard();
		
		int row = 0;
		
		// STORES AS X | Y
		for(int i =0; i < board.length; i++) {
			for(int j =0; j < board[0].length; j++) {
				if(board[i][j]==this.getSymbol()) {
					pieces[row][0] = i; //  Row Value
					pieces[row][1] = j; //  Column Value
							row++;
				}
			}
		} 
	
		
		// ROW | COL
		int[][] possibleMoves = {{1,-1},{2,-2},{1,1},{2,2}};
		
		boolean valid = false; 
		
		int peice_X = 0;
		int piece_Y = 0;
		
		int move_X = 0;
		int move_Y = 0;
		
		while(!valid) {
			Random rand = new Random();
			int randRowValue = rand.nextInt(0, pieces.length-1);
			
			// TODO fix this
			peice_X = pieces[randRowValue][1];
			piece_Y = pieces[randRowValue][0];
			
			
			int possibleMoves_Row = rand.nextInt(0, possibleMoves.length-1);
			
			move_X = peice_X + possibleMoves[possibleMoves_Row][1];
			move_Y = piece_Y + possibleMoves[possibleMoves_Row][0];
			
			valid = logic.isValidMove(peice_X, piece_Y, move_X, move_Y, this);
		}
		
		char piece_X_char = (char) (peice_X+97);
		char move_X_char = (char) (move_X+97);
		move_Y = move_Y*(-1)+GameUtil.boardLength;
		piece_Y = piece_Y*(-1)+GameUtil.boardLength;
		
		StringBuilder stringBuilder= new StringBuilder();
		
		
		stringBuilder.append(piece_Y);
		stringBuilder.append(piece_X_char);
		stringBuilder.append("-");
		stringBuilder.append(move_Y);
		stringBuilder.append(move_X_char);
		
		String move = stringBuilder.toString();
	
		return move;
		
		// TODO implement cpu as X move generation
		// not required for this project 
	}
	
}
