package core;

public class Player {
	private char  symbol; 
	private Type type; 
	
	/**
	 * Player constructor
	 * @param type of player that will be created
	 */
	public Player(Type type){
		
		this.type = type; 
		
		if(type==Type.X) {
			
			symbol = GameUtil.playerXSymbol; 
			
		}else {
			symbol = GameUtil.playerOSymbol; 
		}
		
	}
	
	
	/**
	 * 
	 * @return player symbol
	 */
	public char getSymbol() {
		return symbol; 
	}
	
	/**
	 * 
	 * @return type of player
	 */
	public Type getType() {

		return type; 
	}

	/**
	 * Types of players
	 * @author Paul Gates
	 *
	 */
	public enum Type{
		X, O; 
	}
	
}




