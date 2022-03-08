package ui;



import javafx.scene.shape.*;
import core.CheckersLogic;
import core.GameUtil;
import core.Player;
import core.Player.Type;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage; 

/**
 * Checkers GUI implementation
 * @author Paul Gates
 *
 */
public class CheckersGUI extends Application{

	
	private PlayerButtonContainer playerWhiteButtons; 
	private PlayerButtonContainer playerBlackButtons; 
	private Player playerX;
	private Player playerO;
	private CheckersLogic logic; 
	private CheckerBoard checkersBoard; 
	private VBox XTakenBox; 
	private VBox OTakenBox;
	


	/**
	 * Constructor takes no arguments 
	 * but sets up the logic component as well as initializes the player members
	 */
	public CheckersGUI() {
		
		logic = new CheckersLogic();
		playerX = new Player(Type.X); 
		playerO = new Player(Type.O);
	}
	
	/**
	 * Starting method - launches the GUI
	 * @param args 
	 */
	public static void main(String[] args) {

		launch(args); 
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
			
			BorderPane  bpane = new BorderPane(); 
		
			checkersBoard = new CheckerBoard(logic);
	        checkersBoard.setAlignment(Pos.CENTER);
	        
	       
	        
	        // TODO create taken piece containers 
//	        XTakenBox = new VBox(400);
//	        XTakenBox.getChildren().add(new Button("TEST"));
//	        OTakenBox = new VBox(400);
//	        OTakenBox.getChildren().add(new Button("TEST"));

	        bpane.setCenter(checkersBoard);
	        bpane.setLeft(XTakenBox);
	        bpane.setRight(OTakenBox);
	        
	        Scene scene = new Scene(bpane, 600,600);
	        primaryStage.setTitle(GameUtil.programName);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        

	        // game starts with white pieces
	        logic.setCurrentPlayer(playerX);
	        gameLoop();

	}
	
	
	/**
	 * sets up the game for the next player. first check that the game is able 
	 * to continue by determining if the the opposite player is able to make any moves 
	 * on the board. if not, the game is over. Otherwise, if the opponent player is 
	 * able to make a move, then switch the current player to the opponent and continue 
	 * to the next move.
	 */
	public void runNextMove() {
	
		Player currentPlayer = logic.getCurrentPlayer();
		
		Player opponentPlayer; 
		if(currentPlayer.getType()==Type.X) {
			opponentPlayer = playerO;
		}else {
			opponentPlayer = playerX;
		}
		
		if(logic.checkGame(opponentPlayer)) {
			if(currentPlayer.getType()==Type.X) {
	
				logic.setCurrentPlayer(playerO);
				
			}else {
		
				logic.setCurrentPlayer(playerX);
			
			}
			checkersBoard.removeAllMoveButtonTiles();
			gameLoop();
		}else {
			System.out.println("THIS DOESNT RUN");
			checkersBoard.getChildren().clear();
		}
	}


	/** 
	 * game driver, makes sure that the current players pieces are available 
	 */
	private void gameLoop() {
		if(logic.getCurrentPlayer().getType()==Type.X) {
			playerWhiteButtons.enableAllButtons();
		}else {
			playerBlackButtons.enableAllButtons();
		}

	}


/**
 * Custom button class for a checkers piece
 * @author Paul gates
 *
 */
	public class PlayerButton extends Button{
		int X, Y; 
		Player player; 
		boolean selected = false; 
		
		/**
		 * Constructor
		 * @param X coordinate of this button
		 * @param Y coordinate of this button
		 * @param player that this button belongs to
		 */
		public PlayerButton(int X, int Y, Player player) {
			this.X = X; 
			this.Y = Y;
			this.player = player;  
			String colorString ="";
			if(player.getType()==Type.X) {
				colorString="#942387";
			}else {
				colorString="#238794";
			}
			 setStyle(
		                "-fx-background-radius: 5em; " +
		                "-fx-min-width: 30px; " +
		                "-fx-min-height: 30px; " +
		                "-fx-max-width: 30px; " +
		                "-fx-max-height: 30px;" +
		                "-fx-background-color: " +colorString
		        );
			  
			
			//TODO replace this with actual handler
			this.setOnAction(new PlayerButtonSelectedHandler()); 
			
		}

		/**
		 * 
		 * Custom nested handler class that handles a button click on the Player Button
		 * @author Paul Gates
		 *
		 */
		class PlayerButtonSelectedHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			if(selected) {
				checkersBoard.removeAllMoveButtonTiles();
				selected =false; 
				gameLoop();
				
			}else {
				selected = true;
				System.out.println("Coordinate for the selected piece");
				System.out.print("X="+X+" ");
				System.out.print("Y="+Y);
				System.out.println();
				Integer[][] possibleMoves =  logic.getMovesFor(player, X, Y);
				//TODO test add some move button

			
				checkersBoard.showMeTheMoves(possibleMoves, X, Y);
			}

		
		}
	}
}
	/**
	 * Custom move button that is displayed to show the user all possible moves that they can 
	 * make 
	 * @author Paul Gates
	 *
	 */
	public class MoveButton extends Button{
		
		private int piece_X; 
		private int piece_Y;
		private int move_X; 
		private int move_Y; 
		private Player player; 
		
		/**
		 * Constructor 
		 * @param p_X origin coordinate X
		 * @param p_Y origin coordinate Y
		 * @param m_X destination coordinate X 
		 * @param m_Y destination coordinate Y
		 * @param player that this move button belongs to 
		 */
		public MoveButton(int p_X, int p_Y, int m_X, int m_Y, Player player){
			this.setOnAction(new MoveButtonPressedHandler()); 
			
			style(); 
			this.piece_X = p_X; 
			this.piece_Y = p_Y; 
			this.move_X = m_X; 
			this.move_Y = m_Y; 
			this.player = player; 
		}
		
		
		/**
		 * Stylizes the button 
		 */
		private void style() {
			// TODO style a player button here 
			this.setPrefHeight(50); 
			this.setPrefWidth(50);
			
			this.setStyle("-fx-background-color: #6dc9c9");
		}
		
		class MoveButtonPressedHandler implements EventHandler<ActionEvent>{

			/**
			 * update the logic object with validated move
			 * udpate the gui board
			 */
			@Override
			public void handle(ActionEvent arg0) {
				logic.updateBoard(piece_X, piece_Y, move_X, move_Y, player);
				checkersBoard.showBoard(); 
				
				System.out.println("Run the next move");
				runNextMove();
			
			}
		}
		
		
	}
	
	
 /**
  * Custom GridPane class that acts as the checker board in GUI 
  * @author Paul Gates
  * 
  */
	public class CheckerBoard extends GridPane{
	
		StackPane[] moveButtonTiles; 
		int moveButtonCount = 0; 
		/**
		 * adds possible player move buttons to the board 
		 * @param possibleMoves 2d integer array consisting of all possible moves that the player can 
		 *        make given the selected piece
		 */
		public void showMeTheMoves(Integer[][] possibleMoves, int p_x, int p_y) {
			if(possibleMoves==null)
				return;
			
			boolean moveExists = false; 
			if(logic.getCurrentPlayer().getType()==Type.X) {
				playerWhiteButtons.disableAllButtons();
				playerWhiteButtons.enableButtonAt(p_x, p_y);
			}else {
				playerBlackButtons.disableAllButtons();
				playerBlackButtons.enableButtonAt(p_x, p_y);
			}
			
		
			for(int i = 0; i<possibleMoves.length; i++) {
				Integer int_m_x =  possibleMoves[i][0];
				Integer int_m_y = possibleMoves[i][1];
				
				if(int_m_x!=null||int_m_y!=null) {
					moveExists=true;
					int m_x = int_m_x.intValue();
					int m_y = int_m_y.intValue();
					MoveButton moveButton = new MoveButton(p_x, p_y, m_x, m_y, logic.getCurrentPlayer()); 
					
					StackPane tile = getTile(Color.GREEN);
					tile.getChildren().add(moveButton);
					checkersBoard.addMoveButton(tile);
					add(tile, m_x, m_y);
				
				}
				
				
				
				
			}
			
			if(!moveExists) {
				// TODO make this update a label
				System.out.println("Moves do not exist for this playing again");
				gameLoop();
			}
			
		}
		

		private void addMoveButton(StackPane tile) {
			moveButtonTiles[moveButtonCount++] = tile;
			
		}


		/**
		 * remove all of the move button tiles 
		 */
		public void removeAllMoveButtonTiles() {
			for(int i=0; i<moveButtonTiles.length;i++) {
				if(moveButtonTiles[i]!=null) {
					getChildren().remove(moveButtonTiles[i]); 
				}
			}
			moveButtonCount=0;
		}

		public CheckerBoard(CheckersLogic logic) {
			
			// at most there can be at most 4 moves on the 
			// TODO make this a constant
			moveButtonTiles = new StackPane[4];
			
			//TODO make this constant
			setRowSpan(new Pane(), 50);
			setColumnSpan(new Pane(), 50);
			
			StackPane pane = new StackPane();
			StackPane pane2 = new StackPane();

			// TODO make this constant 
			Rectangle rect = new Rectangle(50,50);
			Rectangle rect2 = new Rectangle(50,50);
		
	
			pane.getChildren().add(rect);
			pane2.getChildren().add(rect2);
			
			showBoard(); 
	
		}
		
		/**
		 * sets up the board based on updated CheckersLogic object board
		 */
		public void showBoard() {
			this.getChildren().clear();
			

			char[][]logicBoard = logic.getBoard();
			 // print out the board 
			Color color;
			playerBlackButtons = new PlayerButtonContainer();
			playerWhiteButtons = new PlayerButtonContainer();
			
			for(int i=0; i<logicBoard.length; i++){
		    	 
		         for(int j=0; j<logicBoard[0].length; j++){
		        	 if((i+j)%2==0) {
			    		  color = Color.WHITE; 
			    	  }else {
			    		  color = Color.BLACK;
			    	  }
		        	StackPane tile = getTile(color);
		        	 if(logicBoard[i][j]==GameUtil.playerXSymbol) {
							PlayerButton playerButton = new PlayerButton(j,i, new Player(Type.X));
							playerButton.setDisable(true);
							playerWhiteButtons.add(playerButton); 
							
							// add this button to the board 
							tile.getChildren().add(playerButton);
							
						}else if(logicBoard[i][j]==GameUtil.playerOSymbol){
							PlayerButton playerButton = new PlayerButton(j,i, new Player(Type.O));
							playerButton.setDisable(true);
							playerBlackButtons.add(playerButton);
						
							// add this button the board 
							tile.getChildren().add(playerButton);
						}
						
		        	 add(tile, j, i);
		          
		         }
		
		      } 
			
		}
		
		/**
		 *  Custom method for getting a colored tile 
		 * @param color of the tile
		 * @return StackPane (tile) 
		 */
		private StackPane getTile(Color color) {
				
			StackPane tile = new StackPane();

			Rectangle colorRect = new Rectangle(50,50);
			tile.getChildren().add(colorRect);
			
			colorRect.setFill(color);

			return tile; 
		}	
		
	}
	
	/**
	 * Custom list object for player buttons
	 * @author Paul Gates
	 *
	 */
	class PlayerButtonContainer{
		private PlayerButton[] playerButtons;
		private int count; 
		
		/**
		 * constructor takes no arguments
		 * sets up the player piece array 
		 * disable all of the buttons as default
		 */
		public PlayerButtonContainer() {
			count = 0; 
			playerButtons = new PlayerButton[GameUtil.playerPeices];
			disableAllButtons(); 
		} 
	
		/**
		 * set all buttons to enabled
		 */
		public void enableAllButtons() {
			for(int i=0; i<count; i++) {
				playerButtons[i].setDisable(false);
			}
		}
		/**
		 * set all buttons to disable
		 */
		public void disableAllButtons() {
			
			for(int i=0; i<count; i++) {
	
				playerButtons[i].setDisable(true);
			}
			
		}
		/**
		 * enable a specific button 
		 * @param X coordinate of the button 
		 * @param Y coordinate of the button
		 */
		public void enableButtonAt(int X, int Y) {
			for(int i=0; i<count; i++) {
				if(playerButtons[i].X==X&&playerButtons[i].Y==Y) {
			
					playerButtons[i].setDisable(false);
				}
				
			}
		}


		/**
		 * 
		 * @return the count of the buttons in the list 
		 */
		public int getCount() {
			return count;
		}

		/**
		 * add a new player button 
		 * @param playerButton being added
		 */

		public void add(PlayerButton playerButton) {
			if(count<12)
			playerButtons[count++]=playerButton;
			//TODO write exception 
			
		}
	}
	
}
