package core;

/**
 * Custom Exception class for invalid checkers moves
 * @author Paul Gates
 *
 */
public class InvalidMoveException extends Exception{

/**
 * Message indicating exeption
 */
	String message; 
	
	/*
	 * takes integer code for exception that has occured
	 * and assigns the message
	 */
	public InvalidMoveException(int code){
		message = assignMessage(code);
	}
	
	/**
	 * Prints the message out if user has made mistake but not if CPU has
	 * @param player that made the invalid move
	 */
	public void initiateExceptionWarningDependingOn(Player player) {
		if(player.getClass()!=CheckersComputerPlayer.class) 
		 System.out.println(message);
		
	}
	
	/**
	 * assigns message depending on error code
	 * @param code indicating which type of InvalidMoveException has occured
	 * @return String containing the message
	 */
	private String assignMessage(int code) {
		 String str = "Exception Occured.\nDescription "; 
		
		switch (code) {
		case 0: 
			str = str + "First coordinate not current player's peice."; 
				break;
		case 1: str = str + "Invalid coordinates."; 
				break;
		case 2: str = str + "Move not allowed form this player."; 
				break;
		case 3:  
			str = "Move not correctly formatted";
				break;
		case 4: 
			str = str + "Destination must be blank.";
				break;
		case 5: 
			str = str + "Only diagonal moves allowed";
				break; 
		case 6: 
			str = str + "Diagonal moves cannot exceed more 2 spaces"; 
				break; 
		case 7: 
			str = str + "Cannot make this move. No oponent to take";  
				break; 
		case 8: 
			str = str + "Move only valid if taking opponent peice."; 
				break;
		case 9: 
			str = str + "Move cannot have same coordinates as moving piece.";
		default: 
			str ="Unkown Exception Occured."; 
			break; 
		}
		return str;
	}
	
}
