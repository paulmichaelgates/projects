package core;

import core.Player.Type;

/**
 * Checkers Game Logic
 * @author Paul Gates
 *
 */
public class CheckersLogic {
	private char[][] board; 
	Player winner=null; 
	private int playerXCount = GameUtil.playerPeices; 
	private int playerOCount = 12; 
	private Player currentPlayer; 

	/**
	 * 
	 * initializes the game board array
	 */
	public CheckersLogic() {
		board = new char[8][8]; 
		
		createStartBoard(); 
	}

	

	/**
	 * updates the board with validate move 
	 * @param X	column value of updated X or O piece
	 * @param Y row value of updated X or O piece
	 * @param player player that is playing the move
	 */
	public void updateBoard(int piece_X, int piece_Y, int move_X, int move_Y, Player player) {
		System.out.println("piece you gave me="+"X="+piece_X+" Y="+piece_Y);
		remove(piece_X, piece_Y); 
		canTakeOpponent(piece_X,piece_Y,move_X, move_Y, true);
	    board[move_Y][move_X] = player.getSymbol(); 
	    
	    //TODO remove this 
	    displayCheckersBoard();
		
	
	}

	/**
	 * remove the peice and update player peice count
	 * @param X column to remove
	 * @param Y row to remove
	 */
	private void remove(int X, int Y) {
		
		board[Y][X] = GameUtil.blankSymbol; 
	       
	}


	/** 
	 * 
	 * validates the move based on starting and ending coordinates
	 * @param peice_X starting column 
	 * @param peice_Y starti ng row 
	 * @param move_X destination column 
	 * @param move_Y destination row
	 * @param player player that is playing the move
	 * @return boolean that determines the validity of the move
	 */
	public boolean isValidMove(int peice_X, int peice_Y, int move_X, int move_Y, Player player) {
		currentPlayer = player;
		
		try {
			if(peice_X>=0&&peice_X<8 && peice_Y>=0&&peice_Y<8 && move_X>=0&&move_X<8 && move_Y>=0&&move_Y<8) {
	
				boolean validate; 
				
				validate = peiceIsValidFor(peice_X, peice_Y); 
				if(!validate)return false; 
				
				validate = rowIsValidFor(move_Y, peice_Y); 	
				if(!validate)return false; 
				
				validate = spaceIsBlank(move_X, move_Y); 
				if(!validate) return false; 
				
				validate = isDiagonalMove(peice_X, move_X, move_Y, peice_Y); 
				if(!validate) return false; 
				
			
			return true;  
		}else {
			throw new InvalidMoveException(1);
		}
		
		}catch(InvalidMoveException e) {
			// TODO: only show when player is making a move
			// leave for now for testing purposes 
			e.initiateExceptionWarningDependingOn(currentPlayer);
		}
		
		return false;
	} 


/**
 * 
 * private method - 
 * helper method determines if the move was in fact diagonal and then if there is an 
 * opponent to overtake 
 * @param peice_X
 * @param move_X
 * @param move_Y
 * @param peice_Y
 * @param player
 * @return boolean indicating validity of move
 */
	private boolean isDiagonalMove(int peice_X, int move_X, int move_Y, int peice_Y) {
		try {
			
			if(peice_X==move_X&&move_Y==peice_Y) {
				throw new InvalidMoveException(9);
			}
			
			// X 
			if(currentPlayer.getType()==Type.X) {
				
					if( ((move_X==peice_X+1)||(move_X ==peice_X-1)) && (move_Y == peice_Y-1) ){
						
						return true;
					}else if(( (move_X == peice_X+2)||(move_X == peice_X-2))&&(move_Y == peice_Y-2) ) {
						
							boolean isTakingOpponent = canTakeOpponent(peice_X, peice_Y, move_X, move_Y, false); 
							return isTakingOpponent; 
					}else {
						throw new InvalidMoveException(5);
					}
			}else {
				
				if( ((move_X==peice_X+1)||(move_X==peice_X-1)) && (move_Y == peice_Y+1) ){
					
					return true;
				}else if(( (move_X == peice_X+2)||(move_X == peice_X-2))
						&&(move_Y == peice_Y+2) ) {
					
						boolean isTakingOpponent = canTakeOpponent(peice_X, peice_Y, move_X, move_Y, false); 
						return isTakingOpponent; 
				}else {
					throw new InvalidMoveException(5);
				}
				
			}
		
	
		}catch(InvalidMoveException e) {
			e.initiateExceptionWarningDependingOn(currentPlayer);
		}
			
		return false; 
	}

	/**
	 * determine if opponent is being taken if so update the board and game board data
	 * @param peice_X origin of peice column 
	 * @param peice_Y origin of peice row
	 * @param move_X destination move column 
	 * @param move_Y destination move row
	 * @param player player object that is making the move 
	 * @return validity of take
	 */

	private boolean canTakeOpponent(int peice_X, int peice_Y, int move_X, int move_Y, Boolean removeFlag) {
		
		try {
			
			int directionScalar = 1; 
			if(currentPlayer.getType()==Type.O) {directionScalar = -1;}
			
			int opponent_X=0, opponent_Y=0; 
			if(peice_X == move_X-2){
			
				opponent_X = move_X-1; 
				opponent_Y = move_Y+(1*directionScalar); 
				if(removeFlag) {
					remove(opponent_X, opponent_Y); 
				}

			}else if(peice_X == move_X+2) {
				
				opponent_X = move_X+1; 
				opponent_Y = move_Y+(1*directionScalar); 
				if(removeFlag) {
					remove(opponent_X, opponent_Y); 
				}
				
			}else {
				throw new InvalidMoveException(7);
				
			}
			
			if(board[opponent_Y][opponent_X] != currentPlayer.getSymbol()&& board[opponent_Y][opponent_X] !=GameUtil.blankSymbol) {
				if(currentPlayer.getType()==Type.X) {
					--playerOCount;
				}else {
					--playerXCount; 
				}
				return true; 
			}else {
				throw new InvalidMoveException(8);
			}
			
		}catch(InvalidMoveException e) {
			e.initiateExceptionWarningDependingOn(currentPlayer);
		}
		return false;
		
		
		

}
		




	private boolean spaceIsBlank(int X, int Y) {
		try {
			
			if(board[Y][X]==GameUtil.blankSymbol) {
				return true; 
			}else {
				throw new InvalidMoveException(4);
			}

		}catch(InvalidMoveException e) {
			e.initiateExceptionWarningDependingOn(currentPlayer);
		}
		return false; 
	}


	//TODO: helper method java doc
    private boolean peiceIsValidFor(int X, int Y) {
    
    	try {
    		if(board[Y][X]==currentPlayer.getSymbol()) {
    			return true; 
    		}
			else{
				throw new InvalidMoveException(0);
			}
    	}catch(InvalidMoveException e){
    		e.initiateExceptionWarningDependingOn(currentPlayer);
    	}
		return false;
		
	}
    
	private boolean rowIsValidFor(int move_Y, int peice_Y) {
		
	try {
		if(currentPlayer.getType()==Type.X) {
			if(move_Y==peice_Y-1 || move_Y==peice_Y-2){
				return true;
			}else {
				throw new InvalidMoveException(2); 
			}
		}else {
			if(move_Y ==peice_Y+1 || move_Y==peice_Y+2){
				return true;
			}else {	
				throw new InvalidMoveException(2); 
			}
		}
	
	}catch(InvalidMoveException e) {
		e.initiateExceptionWarningDependingOn(currentPlayer);
	}
	return false;
	
	}


	private void createStartBoard_testEndGame() {
		char[][] temp = {{' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O'},
		                {' ', ' ', ' ', ' ', ' ', ' ', ' ',' '}, 
		                {' ',  ' ', ' ', ' ', ' ', 'X', ' ', 'X'},
		                {' ',  ' ', ' ', ' ', ' ', ' ', ' ', ' '},
		                {' ',  ' ', ' ', ' ', ' ', ' ',' ', ' '},
		                {' ', ' ', ' ',  ' ', ' ', ' ',' ', ' '},
		                {' ', ' ', ' ', ' ', ' ',' ',' ',   ' '},
		                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}};
		   	 
  
			for(int i=0; i< temp.length; i++){
			   for(int j=0; j<temp[0].length; j++){
			      board[i][j] = temp[i][j]; 
			   }
			} 

          
       for(int i=0; i< temp.length; i++){
          for(int j=0; j<temp[0].length; j++){
          
             board[i][j] = temp[i][j]; 
          }
       } 
        
	}
	
	private void createStartBoard(){
    	 char[][] temp = {{' ', 'O', ' ', 'O', ' ', 'O', ' ', 'O'},
                 {'O', ' ', 'O', ' ', 'O', ' ', 'O',' '}, 
                 {' ',  'O', ' ', 'O', ' ', 'O', ' ', 'O'},
                 {' ',  ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                 {' ',  ' ', ' ', ' ', ' ', ' ',' ', ' '},
                 {'X', ' ', 'X',  ' ', 'X', ' ','X', ' '},
                 {' ', 'X', ' ', 'X', ' ','X',' ',   'X'},
                 {'X', ' ', 'X', ' ', 'X', ' ', 'X', ' '}};
    	 
   
			for(int i=0; i< temp.length; i++){
			   for(int j=0; j<temp[0].length; j++){
			      board[i][j] = temp[i][j]; 
			   }
			} 
 
           
        for(int i=0; i< temp.length; i++){
           for(int j=0; j<temp[0].length; j++){
           
              board[i][j] = temp[i][j]; 
           }
        } 
         
           
      }
	
	/**
	 * gets the playing board
	 * @return raw array containing board data
	 */
	public char[][] getBoard(){
		return board; 
		
	}
	
	/**
	 * 
	 * @return number of peices for player X
	 */
	public int getPlayerXCount() {
		return playerXCount; 
	}
	
	/**
	 * 
	 * @return number of peices for player O
	 */
	public int getPlayerOCount() {
		return playerOCount; 
	}


/**
 * determines if the game is over. The player object given is checked for remaining opponents
 * and if they have any valid moves that they can still make on the board. If it turns out that 
 * this player cannot make any moves or if they are out of pieces, then the method will return 
 * false, otherwise, if the game is meant to continue, then the method will return true. 
 * @param player being checked to still be in the game
 * @return boolean indicating status of game
 */
	public boolean checkGame(Player player) {
		boolean game=false; 
		for(int i=0;i<board.length;i++) {
			for(int j=0; j<board[0].length;j++) {
				if(board[i][j]!=GameUtil.blankSymbol) {
					if((getMovesFor(player, i, j))!=null) {
						System.out.println("THERE IS AT LEAST ONE MOVE FOR ");
						return true;
					}
					
				}
			}
		}
		
		System.out.println("THIS PLAYER IS ABOUT TO LOSE");
		return game; 
		
	}


	private boolean coordinatesInBounds(int move_right_X, int move_right_Y, int move_left_X, int move_left_Y) {
		if(move_right_X<8 && move_right_Y<8 && move_right_X>=0 && move_right_Y>=0 && move_left_X<8 && move_left_Y<8 
				&& move_left_X>=0 && move_left_Y>=0) {
			return true;
		}else {
			return false;
		}
	}


/**
 * gets all posible valid moves that a player can make given a selected piece 
 * @param player that is making the move
 * @param X coordinate of origin piece 
 * @param Y coordinate of origin piece 
 * @return an Integer type array containing the moves that the player can make
 */
	public Integer[][] getMovesFor(Player player, int X, int Y) {
		
		Integer[] immediateLeftDiagonalMove = new Integer[2]; 
		Integer[] immediateRightDiagonalMove = new Integer[2]; 
		Integer[] farLeftDiagonalMove = new Integer[2]; 
		Integer[] farRightDiagonalMove = new Integer[2]; 
		
		if(player.getType()==Type.X) {
			immediateLeftDiagonalMove[1] = (Y-1);// Y 
			immediateLeftDiagonalMove[0] = X-1;// X
			
			immediateRightDiagonalMove[1] = Y-1;// Y 
			immediateRightDiagonalMove[0] = X+1;// X
			
		    farLeftDiagonalMove[1] = Y-2;// Y 
		    farLeftDiagonalMove[0] = X-2;// X
			
    		farRightDiagonalMove[1] = Y-2;// Y 
    	    farRightDiagonalMove[0] = X+2;// X
    	    
    	   
		}else {
			immediateLeftDiagonalMove[1] = (Y+1);// Y 
			immediateLeftDiagonalMove[0] = X-1;// X
			
			immediateRightDiagonalMove[1] = Y+1;// Y 
			immediateRightDiagonalMove[0] = X+1;// X
			
		    farLeftDiagonalMove[1] = Y+2;// Y 
		    farLeftDiagonalMove[0] = X-2;// X
			
    		farRightDiagonalMove[1] = Y+2;// Y 
    	    farRightDiagonalMove[0] = X+2;// X
		}
		
		
		
		Integer[][] possibleMoves = {immediateLeftDiagonalMove, immediateRightDiagonalMove, 
				farLeftDiagonalMove, farRightDiagonalMove};
		
		Integer[][] actualMoves = new Integer[4][2];
		for(int i =0; i<possibleMoves.length; i++) {
			if(isValidMove(X, Y, possibleMoves[i][0], possibleMoves[i][1], player)) {
				System.out.println("Move has been validated X="+possibleMoves[i][0]+" Y="+possibleMoves[i][1]);
				actualMoves[i][0] = possibleMoves[i][0];
				actualMoves[i][1] = possibleMoves[i][1];
			}
		}
		
		// determine if there are actually moves for this player 
		// first test print data
		boolean movesExist = false; 
		System.out.println();
		for(int i =0; i<actualMoves.length; i++) {
			for(int j =0 ; j < actualMoves[0].length; j++) {
				if(actualMoves[i][j]!=null) {
					movesExist = true;
				}
			}
		}
		System.out.println();
		
		if(movesExist) {
			return actualMoves; 
		}
		
		return null; 
		
	}


	/**
	 * getCurrentPlayer()
	 * @return the current player who is making the move 
	 */
	public Player getCurrentPlayer() {
		// TODO Auto-generated method stub
		return currentPlayer; 
	}


/**
 * sets the current player 
 * @param player that is currently playing
 */
	public void setCurrentPlayer(Player player) {
		// TODO Auto-generated method stub
		currentPlayer = player;
	}
	
	
	/**
	 * determines if the player is making a move where they can remove the opponent
	 * @param piece_X origin X coordinate
	 * @param piece_Y origin Y coordinate
	 * @param move_X destination X coordinate
	 * @param move_Y destination Y Coordinate 
	 * @param player that is making the move
	 */
	public void possibleOpponentRemoval(int piece_X, int piece_Y, int move_X, int move_Y, Player player) {
		if(Math.abs(move_X-piece_X)==2) {
			System.out.println("removing the opponent");
			int opp_Y = move_Y-2;
			int opp_X=0;
			if(move_X<piece_X) {
				opp_X = move_X+1;
			}else {
				opp_X = move_X-1;
			}
			remove(opp_X, opp_Y); 
		}
	}
	
	/**
	 * displays the checker board onto the console.  this is only used for testing purposes, 
	 * that is, referencing the logic boards representation of what the game looks like 
	 * and used to contrast with the GUI 
	 */
	public void displayCheckersBoard() {
		
	 // print out the board 
      for(int i=0; i<board.length; i++){
    
    	 System.out.println();
    	 System.out.print((i*(-1)+GameUtil.boardLength)+ " ");
         for(int j=0; j<board[0].length; j++){
         
        	 System.out.print("| "); 
            char c = board[i][j];  
            if(c==' ') {
            	System.out.print(GameUtil.emptySymbol);
            }else {
	        	System.out.print(board[i][j]);
            }
            System.out.print(" "); 
            
            
          
         }
   	  	System.out.print("|");
         System.out.println();
      } 
      
      System.out.println(GameUtil.letterRow); 
	      
		
	}
	
}
