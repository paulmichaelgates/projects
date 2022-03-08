package core;

import core.Player.Type;


/**
 * contains various constants used throughout the program
 * @author Paul Gates
 *
 */
public class GameUtil {
	/**
	 * Game constants
	 */
	final static public char playerXSymbol = 'X'; 
	final static public char playerOSymbol = 'O';
	final static public char emptySymbol = '_';
	public final static int boardLength = 8; 
	public final static int playerPeices = 12; 
	public final static String letterRow = "    a   b   c   d   e   f   g   h";
	public static char blankSymbol = ' ';
	public static String programName = "Checkers"; 
	static public void promptPlayer(Player player) {
		
		
		String msg = "it is time for Player "; 
		
		if(player.getType() == Type.X) {

			msg =msg + playerXSymbol; 
			
		}else {
			msg =msg + playerOSymbol; 
			
		}
		
		msg = msg + "'s  turn...";
		
		System.out.println(msg);  
		System.out.println("---"); 
		System.out.println(); 
	}
	
	
	
/**
 * Displays the winning message
 * @param player that has won the game
 */
	public static void winnerMsg(Player player) {
		System.out.println("GAME OVER: Congrats to Player "+player.getSymbol()+"!");
		
	}
	
}
