package ui;

import java.util.Scanner;

import core.CheckersComputerPlayer;
import core.CheckersLogic;
import core.GameUtil;
import core.Player;
import core.Player.Type;

/**
 * Console Based UI component of game
 * displays game board and allows input of moves
 * @author Paul Gates
 *
 */
public class CheckersTextConsole{

	private CheckersLogic logic; 
	private Player playerX; 
	private Player playerO; 
	private String[] testData = {"3c-4d", "6b-5c", "3a-4b", "5c-3a"}; 
	
	/**
	 * get the player X
	 * @return player X Type
	 */
	
	public Player getPlayerX() {
		return playerX; 
	}
	
	/**
	 * get the player O
	 * @return player O Type
	 */
	public Player getPlayerO() {
		return playerO;
	}
	
	/**
	 * set player object O
	 * @param player object that will be current O player in Game 
	 */
	public void setPlayerO(Player player) {
		playerO = player;
	}
	
	/**
	 * set player object X
	 * @param player object that will be current X player Game
	 */
	public void setPlayerX(Player player) {
		playerX = player;
	}
	/**
	 * initialize logic and player objects
	 */
	public CheckersTextConsole() {
		logic = new CheckersLogic();
		playerX = new Player(Player.Type.X); 
		playerO = new Player(Player.Type.O); 
		
	}
	
	/**
	 * driver code 
	 * @param args not used
	 */
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String userInput="";
		boolean valid;
		while (true) {
			System.out.println("Play agains computer? (Y/N)");
			userInput = scanner.nextLine().trim().toLowerCase();
			  if (userInput.equals("y")) {
			    valid = true;
			    break;
			  } else if (userInput.equals("n")) {
			    valid = false;
			    break;
			  } else {
			     System.out.println("Input was not understood.");
			  }
			}
		
		CheckersTextConsole console = new CheckersTextConsole(); 
		
		if("Y".equalsIgnoreCase(userInput)) {
			console.setPlayerO(new CheckersComputerPlayer(Type.O));
		}
		
		console.displayCheckersBoard(); 
			
		// start game play with player x 
		console.gameLoop(console.getPlayerX()); 
		
	}
	
	
	/**
	 * loops until a winner of the game is determined 
	 * drives the game togling between both players
	 * @param player current player of the game
	 */
	private void gameLoop(Player player) {
		
		Scanner scanner = new Scanner(System.in); 
		
		GameUtil.promptPlayer(player); 

	    String move="";
	    if(player.getClass()==CheckersComputerPlayer.class) {
	    	CheckersComputerPlayer cpu = (CheckersComputerPlayer) player; 
	    	move = cpu.generateMove(logic);
	    	
	    }else {
	    	move = scanner.nextLine(); 
	    }
	    

		int[] totalMove = parseMove(move); 
		if(totalMove!=null) {
			int piece_Y = totalMove[0]; 
			int piece_X = totalMove[1];
			int move_Y = totalMove[2]; 
			int move_X = totalMove[3]; 
			
			
			if(logic.isValidMove(piece_X, piece_Y, move_X, move_Y, player)){
				logic.updateBoard(piece_X, piece_Y, move_X, move_Y, player); 
				displayCheckersBoard(); 
				boolean game = logic.checkGame(player); 
				
				if(game) {
					
					if(player.getType()==Type.X) {
						gameLoop(playerO); 
					}else {
						gameLoop(playerX); 
					}
				}else {
					GameUtil.winnerMsg(player); 
				}
				
			}else {
				gameLoop(player); 
			}
		}else {
			// throw exception 
		}
		
	

	    

	}
	
	/*
	 * converts move into string
	 */
	private int[] parseMove(String move) {
		if(move.matches("\\d[a-zA-Z]-\\d[a-zA-Z]")) {
			int peice_Y = Character.getNumericValue(move.charAt(0))*(-1)+GameUtil.boardLength; 
			int peice_X = move.charAt(1)-97; // ASCII 
			
			int move_Y = Character.getNumericValue(move.charAt(3))*(-1)+GameUtil.boardLength;
			int move_X = move.charAt(4)-97; // ASCII 
			

			int[] totalMove={peice_Y, peice_X, move_Y, move_X}; 
			return totalMove; 
		}else {
			return null; 
		}
	}

	/**
	 * prints out a refined UI version of the game board
	 */
	public void displayCheckersBoard() {
		
		char[][]board = logic.getBoard();
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
