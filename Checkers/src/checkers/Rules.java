/* Rules Class
 * 
 * Dan Thurston, Mike Scoboria
 * 
 * This class is used to facilitate our checkers game.
 * An instance of this class will be created from the GUI.
 * 
 * HERE ARE FUNCTIONS FOR USE FROM GUI:
 * 		Rules();      			
 * 		// CONSTRUCTOR, does not return anything.
 * 		Rules(int userColor);	
 * 		// CONSTRUCTOR, If userColor is 1 user is black, if 2 user is red
 * 		validUserPiece(int squareNum);  
 * 		// Returns true if user does have a piece on squareNum, else false
 * 		validUserMove(int squareNum);	
 * 		// Returns true if the last validUserPiece 
 * 		//selected can move to squareNum
 * 		returnBoard();		 			
 * 		// Returns int[][] containing the matrix for the board
 */

package checkers;

import static java.lang.System.*;

public class Rules {

	private static int[][] board; // Black pieces are 1's, Reds are 2's
	private int user;  	// If 1 user is black, if 2 user is red
	private int computer;
	private int currentPiece; // Holder for user selected piece
	private int userPiecesCaptured;
	private int compPiecesCaptured;

	// Constructor - initializes pieces on board
	public Rules(){
		board = new int [8][8];
		currentPiece = 0;
		
		userPiecesCaptured = 0;
		compPiecesCaptured = 0;

		for (int a = 0; a < 8; a+= 2){
			board[0][a+1] = 1;
			board[1][a] = 1;
			board[2][a+1] = 1;
			board[7][a] = 2;
			board[6][a+1] = 2;
			board[5][a] = 2;

		}

		user = 2;	// Default if not specified
		computer = 1;
	}

	// Constructor - initializes pieces on board
	// If userColor is 1 user is black, if 2 user is red
	public Rules(int userColor){
		board = new int [8][8];
		currentPiece = 0;

		for (int a = 0; a < 8; a+= 2){
			board[0][a+1] = 1;
			board[1][a] = 1;
			board[2][a+1] = 1;
			board[7][a] = 2;
			board[6][a+1] = 2;
			board[5][a] = 2;
		}

		if (userColor == 1){
			user = 1;
			computer = 2;
		}
		else{
			user = 2;
			computer = 1;
		}
	}

	
	// Returns two numbers: first is row, second 
	//is column. 99 is invalid squareNum
	private int[] SquareNumberToMatrixValue2(int squareNum){

		//int[] matrixLocation = new int[2];
		int[] matrixLocation = {-1, -1};
		
		for(int i = 1; i < 33; i += 4){
			if(squareNum < i ){ matrixLocation[0] = 99; break; }
			if(squareNum >= i){ matrixLocation[0]++; } 
		}
		
		/*
		// Find row 
		if (squareNum < 5)
			matrixLocation[0] = 0;
		else if (squareNum < 9)
			matrixLocation[0] = 1;
		else if (squareNum < 13)
			matrixLocation[0] = 2;
		else if (squareNum < 17)
			matrixLocation[0] = 3;
		else if (squareNum < 21)
			matrixLocation[0] = 4;
		else if (squareNum < 25)
			matrixLocation[0] = 5;
		else if (squareNum < 29)
			matrixLocation[0] = 6;
		else if (squareNum < 33)
			matrixLocation[0] = 7;
		else  // INVALID!
			matrixLocation[0] = 99;
	*/
		
		/*/Find Column
		for(int i = 1; i <= 8; i++){	
			if(squareNum - i % 8 == 0){
				matrixLocation[1] = i;
			}
			if(squareNum - (i+4) % 8 == 0){
				matrixLocation[1] = (i-1);
			}
		}
		*/

		// Find column
		if (squareNum == 5 || squareNum == 13 			// squareNum - 5 % 8 = 0
				|| squareNum == 21 || squareNum == 29) // or matrixLocation[1] = squareNum - 5 % 8
			matrixLocation[1] = 0;
		
		else if (squareNum == 1 || squareNum == 9 		// squareNum - 1 % 8 = 0
				|| squareNum == 17 || squareNum == 25)  // or if square matrixLocation[1] = squareNum % 8
			matrixLocation[1] = 1;
		
		else if (squareNum == 6 || squareNum == 14 		// squareNum - 6 % 8 = 0
				|| squareNum == 22 || squareNum == 30)
			matrixLocation[1] = 2;
		
		else if (squareNum == 2 || squareNum == 10 		// squareNum - 2 % 8 = 0
				|| squareNum == 18 || squareNum == 26)
			matrixLocation[1] = 3;
		
		else if (squareNum == 7 || squareNum == 15		// squareNum - 7 % 8 = 0
				|| squareNum == 23 || squareNum == 31)
			matrixLocation[1] = 4;
		
		else if (squareNum == 3 || squareNum == 11 		// squareNum - 3 % 8 = 0
				|| squareNum == 19 || squareNum == 27)
			matrixLocation[1] = 5;
		
		else if (squareNum == 8 || squareNum == 16 		// squareNum - 8 % 8 = 0
				|| squareNum == 24 || squareNum == 32)
			matrixLocation[1] = 6;
		
		else if (squareNum == 4 || squareNum == 12 		// squareNum - 4 % 8 = 0
				|| squareNum == 20 || squareNum == 28)
			matrixLocation[1] = 7;
		else  // INVALID!
			matrixLocation[1] = 99;

		return matrixLocation;
	}
	
	// Returns two numbers: first is row, second 
	//is column. 99 is invalid squareNum
	private int[] SquareNumberToMatrixValue(int squareNum){

		int[] matrixLocation = new int[2];
		
		// Find row 
		if (squareNum > 0 && squareNum < 5)
			matrixLocation[0] = 0;
		else if (squareNum > 4 && squareNum < 9)
			matrixLocation[0] = 1;
		else if (squareNum > 8 && squareNum < 13)
			matrixLocation[0] = 2;
		else if (squareNum > 12 && squareNum < 17)
			matrixLocation[0] = 3;
		else if (squareNum > 16 && squareNum < 21)
			matrixLocation[0] = 4;
		else if (squareNum > 20 && squareNum < 25)
			matrixLocation[0] = 5;
		else if (squareNum > 24 && squareNum < 29)
			matrixLocation[0] = 6;
		else if (squareNum > 28 && squareNum < 33)
			matrixLocation[0] = 7;
		else  // INVALID!
			matrixLocation[0] = 99;

		// Find column
		if (squareNum == 5 || squareNum == 13 
				|| squareNum == 21 || squareNum == 29)
			matrixLocation[1] = 0;
		else if (squareNum == 1 || squareNum == 9 
				|| squareNum == 17 || squareNum == 25)
			matrixLocation[1] = 1;
		else if (squareNum == 6 || squareNum == 14 
				|| squareNum == 22 || squareNum == 30)
			matrixLocation[1] = 2;
		else if (squareNum == 2 || squareNum == 10 
				|| squareNum == 18 || squareNum == 26)
			matrixLocation[1] = 3;
		else if (squareNum == 7 || squareNum == 15
				|| squareNum == 23 || squareNum == 31)
			matrixLocation[1] = 4;
		else if (squareNum == 3 || squareNum == 11 
				|| squareNum == 19 || squareNum == 27)
			matrixLocation[1] = 5;
		else if (squareNum == 8 || squareNum == 16 
				|| squareNum == 24 || squareNum == 32)
			matrixLocation[1] = 6;
		else if (squareNum == 4 || squareNum == 12 
				|| squareNum == 20 || squareNum == 28)
			matrixLocation[1] = 7;
		else  // INVALID!
			matrixLocation[1] = 99;

		return matrixLocation;
	}

	// Returns the value of the piece at the specified square
	int returnPieceAtSquare(int squareNum){
		return board[SquareNumberToMatrixValue
		 (squareNum)[0]][SquareNumberToMatrixValue(squareNum)[1]];
	}

	// Returns true if the square contains a user piece
	private boolean userPiece(int squareNum){
		if (returnPieceAtSquare(squareNum) == user 
				|| returnPieceAtSquare(squareNum) == user + 2)
			return true;
		else
			return false;
	}

	public int coordinatesToSquare(int row, int col){

		int count = 0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if((i+j) % 2 != 0){
					count++;
					if(i == row && j == col){ return count;} 
				}
			}
		}
		return -1;
	}

	// Returns true if the square contains a computer piece
	private boolean computerPiece(int row, int column){
		
		if (board[row][column] == computer || board[row][column] == computer + 2)
			return true;
		else { return false; }
	}

	// Returns true if at least one jump is possible from the 
	// current board position
	public boolean isNextJumpAvailable(int a, int b){

		boolean jumpPossible = false;
		// If user is 1 or king, check downward
		if (user == 1 || board[a][b] == user + 2){
			if(b > 1 && a < 6){ // Make sure jump has distance
				// if diagonal is opponent piece
				if((board[a+1][b-1] == computer 
						|| board[a+1][b-1] == computer+2) 
						&& board[a+2][b-2] == 0){ 
					//System.out.println("Jump down-left possible\n");
					jumpPossible = true;
				}
				else{ //System.out.println("Jump down-left not possible\n"); 
					
				}
			}
			if(a < 6 && b < 6){ // Make sure jump has distance
				// if diagonal is opponent piece
				if((board[a+1][b+1] == computer 
						|| board[a+1][b+1] == computer+2)
						&& board[a+2][b+2] == 0){ 
					//System.out.println("Jump down-right possible\n");
					jumpPossible = true;
				}
				else{ 
					//System.out.println("Jump down-right not possible\n"); 
				}
			}
		}
		// If user is 2 or king, check upward
		if(user == 2 || board[a][b] == user + 2){ 
			if (a > 1 && b > 1){ // Make sure jump has distance
				// if diagonal is opponent piece
				if((board[a-1][b-1] == computer 
						|| board[a-1][b-1] == computer+2) 
						&& board[a-2][b-2] == 0){ 
					//System.out.println("Jump up-left possible\n");
					jumpPossible = true;
				}
				else{ //System.out.println
					//("Jump up-left not possible\n"); 
				}
			}
			if(a > 1 && b < 6){ // Make sure jump has distance
				// if diagonal is opponent piece
				if((board[a-1][b+1] == computer 
						|| board[a-1][b+1] == computer+2) 
						&& board[a-2][b+2] == 0){ 
					//System.out.println("Jump up-right possible\n");
					jumpPossible = true;
				}
				else{ 
					//System.out.println("Jump up-right not possible\n"); 
				}
			}
		}
		
		return jumpPossible;
	}
         	
	// Check if jump is possible
	private boolean isJumpAvailable(){

		// Loop through all spaces checking pieces 
		//on users team for available jumps
		for(int a = 0; a < 8; a++){
			for (int b = 0; b < 8; b++){
				// If user is not king, check only forward diagonals
				if (board[a][b] == user){ 
					 // If user is 1, check downward
					if (user == 1){ 
						// Make sure jump has distance
						if (a != 6 && a != 7 && b != 6 && b != 7){ 
							if(board[a+1][b+1] == computer 
									// if diagonal is opponent piece
									|| board[a+1][b+1] == computer+2){ 
								// If jump destination is empty, jump is available
								if (board[a+2][b+2] == 0){ 
									return true;
								}
							}
						}
						// Make sure jump has distance
						else if (a != 6 && a != 7 && b != 0 && b != 1){ 
							if(board[a+1][b-1] == computer 
									// if diagonal is opponent piece
									|| board[a+1][b-1] == computer+2){ 
								// If jump destination is empty, jump is available
								if (board[a+2][b-2] == 0){ 
									return true;
								}
							}
						}
					}
					else{ // If user is 2, check upward
						// Make sure jump has distance
						if (a != 0 && a != 1 && b != 0 && b != 1){ 
							// if diagonal is opponent piece
							if(board[a-1][b-1] == computer 
									|| board[a-1][b-1] == computer+2){ 
								// If jump destination is empty, jump is available
								if (board[a-2][b-2] == 0){ 
									return true;
								}
							}
						}
						// Make sure jump has distance
						else if (a != 0 && a != 1 && b != 6 && b != 7){ 
							// if diagonal is opponent piece
							if(board[a-1][b+1] == computer 
									|| board[a-1][b+1] == computer+2){ 
								// If jump destination is empty, jump is available
								if (board[a-2][b+2] == 0){ 
									return true;
								}
							}
						}
					}
				}
				// If user is king, check both diagonals
				else if(board[a][b] == user + 2){ 
					// Make sure jump has distance
					if (a != 0 && a != 1 && b != 0 && b != 1){ 
						// if diagonal is opponent piece
						if (board[a-1][b-1] == computer 
								|| board[a-1][b-1] == computer+2){ 
							// If jump destination is empty, jump is available
							if (board[a-2][b-2] == 0){ 
								return true;
							}
						}
					}
					// Make sure jump has distance
					else if (a != 7 && a != 6 && b != 7 && b != 6){ 
						// if diagonal is opponent piece
						if (board[a+1][b+1] == computer 
								|| board[a+1][b+1] == computer+2){ 
							// If jump destination is empty, jump is available
							if (board[a+2][b+2] == 0){ 
								return true;
							}
						}
					}
					// Make sure jump has distance
					else if (a != 7 && a != 6 && b != 0 && b != 1){ 
						if (board[a+1][b-1] == computer 
								// if diagonal is opponent piece
								|| board[a+1][b-1] == computer+2){ 
							// If jump destination is empty, jump is available
							if (board[a+2][b-2] == 0){ 
								return true;
							}
						}
					}
					// Make sure jump has distance
					else if (a != 0 && a != 1 && b != 7 && b != 6){ 
						if (board[a-1][b+1] == computer 
								// if diagonal is opponent piece
								|| board[a-1][b+1] == computer+2){ 
							// If jump destination is empty, jump is available
							if (board[a-2][b+2] == 0){ 
								return true;
							}
						}
					}
				}
			}
		}
		// If no jumps found return false
		return false;
	}

	//returns true if the selected piece can
	//legally be moved according to Checkers rules
	public boolean validUserPiece(int squareNum){
		if (userPiece(squareNum)){
			currentPiece = squareNum;
			return true;
		}
		else
			return false;	
	}

	//returns true if a move taken is a legal move
	public boolean validUserMove(int squareNum){
		int[] pieceLocation;
		int[] moveLocation;

		if(squareNum == -1) return false;

		moveLocation = SquareNumberToMatrixValue(squareNum);

		// Check that a piece is selected
		if (currentPiece != 0)
			pieceLocation = SquareNumberToMatrixValue(currentPiece);
		else
			return false;

		// Check that the square to move to is empty
		if (returnPieceAtSquare(squareNum) != 0)
			return false;

		// If legal diagonal square selected then make 
		//move (IF NO JUMP AVAILABLE)
		if (!isJumpAvailable() && moveLocation[0] 
				== pieceLocation[0] - 1 
				&& board[pieceLocation[0]][pieceLocation[1]] != 1
				&& (moveLocation[1] == pieceLocation[1] + 1 
				|| moveLocation[1] == pieceLocation[1] - 1)){ 
			// If going up on board and piece is a 2, 3 or 4
			board[moveLocation[0]][moveLocation[1]] 
					= board[pieceLocation[0]][pieceLocation[1]];
			board[pieceLocation[0]][pieceLocation[1]] = 0;

			// If necessary, make king piece
			if (moveLocation[0] == 0 && 
					// If a 2 gets to top row, make king (4)
					board[moveLocation[0]][moveLocation[1]] == 2) 
				board[moveLocation[0]][moveLocation[1]] = 4;

			return true;
		}
		// If going down on board and piece is a 1, 3 or 4
		else if (!isJumpAvailable() && moveLocation[0] 
				== pieceLocation[0] + 1 
				&& board[pieceLocation[0]][pieceLocation[1]] != 2
				&& (moveLocation[1] == pieceLocation[1] + 1 
				|| moveLocation[1] == pieceLocation[1] - 1)){ 
			board[moveLocation[0]][moveLocation[1]] 
					= board[pieceLocation[0]][pieceLocation[1]];
			board[pieceLocation[0]][pieceLocation[1]] = 0;

			// If necessary, make king piece
			if (moveLocation[0] == 7 
					// If a 1 gets to bottom row, make king (2)
					&& board[moveLocation[0]][moveLocation[1]] == 1) 
				board[moveLocation[0]][moveLocation[1]] = 3;

			return true;
		}
		// Check for possible jump
		else if (moveLocation[0] 
				== pieceLocation[0] - 2 
				&& board[pieceLocation[0]][pieceLocation[1]] != 1
				&& (moveLocation[1] == pieceLocation[1] + 2 
						// If up jump and piece is 2, 3 ,4
				|| moveLocation[1] == pieceLocation[1] - 2)){  
			int avgRow = (moveLocation[0] + pieceLocation[0]) / 2;
			int avgCol = (moveLocation[1] + pieceLocation[1]) / 2;
			if (computerPiece(avgRow, avgCol)){
				board[avgRow][avgCol] = 0;
				board[moveLocation[0]][moveLocation[1]] 
						= board[pieceLocation[0]][pieceLocation[1]];
				board[pieceLocation[0]][pieceLocation[1]] = 0;

				// If necessary, make king piece
				if (moveLocation[0] == 0 
						 // If a 2 gets to top row, make king (4)
						&& board[moveLocation[0]][moveLocation[1]] == 2){
					board[moveLocation[0]][moveLocation[1]] = 4;
				}
				
				return true;
			}
		}
		else if (moveLocation[0] == pieceLocation[0] + 2 
				&& board[pieceLocation[0]][pieceLocation[1]] != 2
				&& (moveLocation[1] == pieceLocation[1] + 2 
						// If down jump and piece is 1, 3 ,4
				|| moveLocation[1] == pieceLocation[1] - 2)){  
			int avgRow = (moveLocation[0] + pieceLocation[0]) / 2;
			int avgCol = (moveLocation[1] + pieceLocation[1]) / 2;
			if (computerPiece(avgRow, avgCol)){
				board[avgRow][avgCol] = 0;
				board[moveLocation[0]][moveLocation[1]] 
						= board[pieceLocation[0]][pieceLocation[1]];
				board[pieceLocation[0]][pieceLocation[1]] = 0;

				// If necessary, make king piece
				if (moveLocation[0] == 7 
						// If a 1 gets to bottom row, make king (2)
						&& board[moveLocation[0]][moveLocation[1]] == 1){ 
					board[moveLocation[0]][moveLocation[1]] = 3;
				}
				
				return true;
			}
		}
		return false;
	}

	//tabulates captured pieces
	public void countCapturedPieces(){
		
		userPiecesCaptured = 0;
		compPiecesCaptured = 0;
		
		for(int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++) {
				
				if(board[i][j] == user 
						|| board[i][j] == user + 2)
					userPiecesCaptured++;
				else if(board[i][j] == computer 
						|| board[i][j] == computer + 2)
					compPiecesCaptured++;
			}
		}
	}
	
	//return current board state
	public int[][] returnBoard(){
		return board;
	}


	
	//returns true if a piece has been kinged in the 
	//previous move
	public boolean justKinged(int prevColor, int endSquare){
        
        if(prevColor != returnPieceAtSquare(endSquare))
            return true;
            
        return false;
   	}

	
	
	
	
	
	
	
	
	
	//mutators below
	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		Rules.board = board;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public int getComputer() {
		return computer;
	}

	public void setComputer(int computer) {
		this.computer = computer;
	}

	public int getUserPiecesCaptured() {
		return userPiecesCaptured;
	}

	public int getCompPiecesCaptured() {
		return compPiecesCaptured;
	}

	
	
	
	// Returns a 2D array representing the board. 
	// 0's are empty, 1's are black, 2's are red.
	public void printBoard(){
		for (int a = 0; a < 8; a++){
			for (int b = 0; b < 8; b++){
				out.print(board[a][b] + "  ");
			}
			out.println();
		}		
		out.println();
	}

}