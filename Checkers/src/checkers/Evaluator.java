package checkers;

import java.util.ArrayList;
//import static java.lang.System.*;
//import java.util.Random;

/** Evaluator Class
 * 
 * Dan Thurston, Mike Scoboria
 * 
 * Defines all methods related board state evaluation
 */
public class Evaluator{

	/**
	 * PLY is used to indicate how many 'black turn/red turn' cycles 
	 * there are (I just found out that I'm using the term ply 
	 * incorrectly though, so I might change this later.  Basically, 
	 * since we have to do a 6 ply search, making PLY here equal 3.0 
	 * is equivalent.) 
	 * 
	 */
	private static final double PLY = 3.0;

	// 1 if computer is black, 2 if computer is red
	private static int computer; 
	private static int computerKing;
	private static int user;
	private static int userKing;

	//for use in isJumpAvailable and isMveAvailable
	private int thisPlayer, thisPlayerKing, opponent, opponentKing;

	public Evaluator(int ComputerColor){

		computer = ComputerColor;
		computerKing = computer + 2;

		if (computer == 1)
			user = 2;
		else
			user = 1;

		userKing = user + 2;
	}

	/**
	 * jturn tells the method whose turn it is; the method finds
	 * red or black jumps accordingly 
	 * 
	 * jumpsBoard is the current state of the board to be evaluated for 
	 * possible jumps
	 * 
	 */
	private int[] isJumpAvailable(int[][] jumpsBoard, double jturn){

		// jumps will have 4 entries per available jump 
		//(piece row, piece col, dest row, dest col)
		int[] jumps = new int[82];  
		int jumpsCount = 0;

		if(jturn % 1.0 == 0){

			thisPlayer = computer;
			thisPlayerKing = computerKing;
			opponent = user;
			opponentKing = userKing;
		}
		else if(jturn % 1.0 != 0){

			thisPlayer = user;
			thisPlayerKing = userKing;
			opponent = computer;
			opponentKing = computerKing;	
		}

		// Loop through all spaces checking pieces on 
		//computers team for available jumps
		for(int a = 0; a < 8; a++){
			for (int b = 0; b < 8; b++){
				// If thisPlayer is not king, check only forward diagonals
				if (jumpsBoard[a][b] == thisPlayer){ 
					// If thisPlayer is 1, check downward
					if (thisPlayer == 1){  
						// Make sure jump has distance
						if (a != 6 && a != 7 && b != 6 && b != 7){ 
							if(jumpsBoard[a+1][b+1] == opponent 
									 // if diagonal is opponent piece
								|| jumpsBoard[a+1][b+1] == opponentKing){
								// If jump destination is empty,
								//jump is available
								if (jumpsBoard[a+2][b+2] == 0){ 

									// CHECK FOR MORE JUMPS
									jumps[jumpsCount] = a;
									jumps[jumpsCount+1] = b;
									jumps[jumpsCount+2] = a+2;
									jumps[jumpsCount+3] = b+2;
									jumpsCount += 4;
								}
							}
						}
						// Make sure jump has distance
						if (a != 6 && a != 7 && b != 0 && b != 1){ 
							if(jumpsBoard[a+1][b-1] == opponent 
									// if diagonal is opponent piece
								|| jumpsBoard[a+1][b-1] == opponentKing){ 
								// If jump destination is empty, 
								//jump is available
								if (jumpsBoard[a+2][b-2] == 0){ 
									// CHECK FOR MORE JUMPS
									jumps[jumpsCount] = a;
									jumps[jumpsCount+1] = b;
									jumps[jumpsCount+2] = a+2;
									jumps[jumpsCount+3] = b-2;
									jumpsCount += 4;
								}
							}
						}
					}
					else{ // If thisPlayer is 2, check upward
						// Make sure jump has distance
						if (a != 0 && a != 1 && b != 0 && b != 1){ 
							// if diagonal is opponent piece
							if(jumpsBoard[a-1][b-1] == opponent 
								|| jumpsBoard[a-1][b-1] == opponentKing){ 
							// If jump destination is empty, 
								//jump is available
								if (jumpsBoard[a-2][b-2] == 0){ 
									jumps[jumpsCount] = a;
									jumps[jumpsCount+1] = b;
									jumps[jumpsCount+2] = a-2;
									jumps[jumpsCount+3] = b-2;
									jumpsCount += 4;
								}
							}
						}
						// Make sure jump has distance
						if (a != 0 && a != 1 && b != 6 && b != 7){ 
							if(jumpsBoard[a-1][b+1] == opponent 
									// if diagonal is opponent piece
								|| jumpsBoard[a-1][b+1] == opponentKing){ 
							// If jump destination is empty, 
								//jump is available
								if (jumpsBoard[a-2][b+2] == 0){ 
									jumps[jumpsCount] = a;
									jumps[jumpsCount+1] = b;
									jumps[jumpsCount+2] = a-2;
									jumps[jumpsCount+3] = b+2;
									jumpsCount += 4;
								}
							}
						}
					}

				}
				// If thisPlayer is king, check both diagonals
				else if(jumpsBoard[a][b] == thisPlayerKing){ 
					// Make sure jump has distance
					if (a != 0 && a != 1 && b != 0 && b != 1){ 
						if (jumpsBoard[a-1][b-1] == opponent 
								// if diagonal is opponent piece
							|| jumpsBoard[a-1][b-1] == opponentKing){ 
							// If jump destination is empty, 
							//jump is available
							if (jumpsBoard[a-2][b-2] == 0){ 
								jumps[jumpsCount] = a;
								jumps[jumpsCount+1] = b;
								jumps[jumpsCount+2] = a-2;
								jumps[jumpsCount+3] = b-2;
								jumpsCount += 4;
							}
						}
					}
					// Make sure jump has distance
					if (a != 7 && a != 6 && b != 7 && b != 6){ 
						if (jumpsBoard[a+1][b+1] == opponent 
								// if diagonal is opponent piece
							|| jumpsBoard[a+1][b+1] == opponentKing){ 
							// If jump destination is empty, 
							//jump is available
							if (jumpsBoard[a+2][b+2] == 0){ 
								jumps[jumpsCount] = a;
								jumps[jumpsCount+1] = b;
								jumps[jumpsCount+2] = a+2;
								jumps[jumpsCount+3] = b+2;
								jumpsCount += 4;
							}
						}
					}			// Make sure jump has distance
					if (a != 7 && a != 6 && b != 0 && b != 1){ 
						if (jumpsBoard[a+1][b-1] == opponent 
								// if diagonal is opponent piece
							|| jumpsBoard[a+1][b-1] == opponentKing){ 
							// If jump destination is empty, 
							//jump is available
							if (jumpsBoard[a+2][b-2] == 0){ 
								jumps[jumpsCount] = a;
								jumps[jumpsCount+1] = b;
								jumps[jumpsCount+2] = a+2;
								jumps[jumpsCount+3] = b-2;
								jumpsCount += 4;
							}
						}
					}
					 // Make sure jump has distance
					if (a != 0 && a != 1 && b != 7 && b != 6){
						if (jumpsBoard[a-1][b+1] == opponent 
								// if diagonal is opponent piece
							|| jumpsBoard[a-1][b+1] == opponentKing){ 
							// If jump destination is empty, 
							//jump is available
							if (jumpsBoard[a-2][b+2] == 0){ 
								jumps[jumpsCount] = a;
								jumps[jumpsCount+1] = b;
								jumps[jumpsCount+2] = a-2;
								jumps[jumpsCount+3] = b+2;
								jumpsCount += 4;
							}
						}
					}
				}
			}
		}

		jumps[81] = jumpsCount;

		return jumps;
	}

	/**
	 * mturn tells the method whose turn it is; the method finds
	 * red or black moves accordingly 
	 * 
	 * movesBoard is the current state of the board to be evaluated for 
	 * possible moves
	 * 
	 * 
	 */
	private int[] isMoveAvailable(int[][] movesBoard, double mturn){

		// moves will have 4 entries per available move 
		//(piece row, piece col, dest row, dest col)
		int[] moves = new int[82];  
		int movesCount = 0;

		if(mturn % 1.0 == 0){

			thisPlayer = computer;
			thisPlayerKing = computerKing;
			opponent = user;
			opponentKing = userKing;
		}
		else if(mturn % 1.0 != 0){

			thisPlayer = user;
			thisPlayerKing = userKing;
			opponent = computer;
			opponentKing = computerKing;

		}

		// Loop through all spaces checking pieces on 
		//computers team for available moves
		for(int a = 0; a < 8; a++){
			for (int b = 0; b < 8; b++){
				// If computer is not king, check only forward diagonals
				if (movesBoard[a][b] == thisPlayer){ 
					// If computer is 1, check downward
					if (thisPlayer == 1){  
						// Make sure move has distance
						if (a != 7 && b != 7){ 
							// if diagonal is empty
							if(movesBoard[a+1][b+1] == 0){ 
								moves[movesCount] = a;
								moves[movesCount+1] = b;
								moves[movesCount+2] = a+1;
								moves[movesCount+3] = b+1;
								movesCount += 4;
							}
						}
						// Make sure move has distance
						if (a != 7 && b != 0){ 
							// if diagonal is empty
							if(movesBoard[a+1][b-1] == 0){ 
								moves[movesCount] = a;
								moves[movesCount+1] = b;
								moves[movesCount+2] = a+1;
								moves[movesCount+3] = b-1;
								movesCount += 4;
							}
						}
					}
					else{ // If computer is 2, check upward
						// Make sure move has distance
						if (a != 0 && b != 0){ 
							// if diagonal is empty
							if(movesBoard[a-1][b-1] == 0){ 
								moves[movesCount] = a;
								moves[movesCount+1] = b;
								moves[movesCount+2] = a-1;
								moves[movesCount+3] = b-1;
								movesCount += 4;
							}
						}
						// Make sure move has distance
						if (a != 0 && b != 7){ 
							// if diagonal is empty
							if(movesBoard[a-1][b+1] == 0){ 
								moves[movesCount] = a;
								moves[movesCount+1] = b;
								moves[movesCount+2] = a-1;
								moves[movesCount+3] = b+1;
								movesCount += 4;
							}
						}
					}

				}
				// If computer is king, check both diagonals
				else if(movesBoard[a][b] == thisPlayerKing){ 
					// Make sure move has distance
					if (a != 0 && b != 0){ 
						// if diagonal is empty
						if (movesBoard[a-1][b-1] == 0){ 
							moves[movesCount] = a;
							moves[movesCount+1] = b;
							moves[movesCount+2] = a-1;
							moves[movesCount+3] = b-1;
							movesCount += 4;
						}
					}
					// Make sure move has distance
					if (a != 7 && b != 7){ 
						 // if diagonal is empty
						if (movesBoard[a+1][b+1] == 0){
							moves[movesCount] = a;
							moves[movesCount+1] = b;
							moves[movesCount+2] = a+1;
							moves[movesCount+3] = b+1;
							movesCount += 4;
						}
					}
					// Make sure move has distance
					if (a != 7 && b != 0){ 
						// if diagonal is empty
						if (movesBoard[a+1][b-1] == 0){ 
							moves[movesCount] = a;
							moves[movesCount+1] = b;
							moves[movesCount+2] = a+1;
							moves[movesCount+3] = b-1;
							movesCount += 4;
						}
					}
					// Make sure move has distance
					if (a != 0 && b != 7){ 
						// if diagonal is empty
						if (movesBoard[a-1][b+1] == 0){ 
							moves[movesCount] = a;
							moves[movesCount+1] = b;
							moves[movesCount+2] = a-1;
							moves[movesCount+3] = b+1;
							movesCount += 4;
						}
					}
				}
			}
		}

		moves[81] = movesCount;

		return moves;
	}

	/**
	 * Call to recursive method, return result to GUI
	 */
	public int[][] evaluate(int[][] currentBoard){

		currentBoard = evaluate(currentBoard, PLY);
		return currentBoard;
	}

	/**
	 * Implements minimax search.  Accepts a current state
	 * board, generates possible moves and recursively 
	 * passes each new board to evaluate() until playerTurn
	 * is zero.  Then returns board with its utility value.
	 * 
	 * Values then move up the recursive calls, choosing the 
	 * max values for the computer turns and min for the player
	 * until the final value is determined.  The board associated 
	 * with this value is returned to the calling method (which passes
	 * it back to the gui)
	 */
	public int[][] evaluate(int[][] currentBoard, double playerTurn){

		int[][] tempBoard;
		int[][] bestBoard = copyArray(currentBoard);
		int[][] bestValue = copyArray(currentBoard);
		ArrayList<int[][]> multiMoves = new ArrayList<int[][]>();

		boolean jump = true;
		boolean makeKing = false;
		// Piece location (x1, x2) and destination (y1, y2)
		int x1, x2, y1, y2;  
		int[] pieceLocation = new int[2];


		/**
		 * the following is the recursive step of the min/max search
		 * for each move returned by the calls to isJumpAvailable() or
		 * isMoveAvailable() (whichever applies) a recursive call is made
		 * to evaluate(3)(currentBoard) with tempBoard where (iteratively) 
		 * tempBoard is set to  currentBoard then has a move made from 
		 * one of the options.  The recursive step is performed as long 
		 * as the ply > 0.
		 * 
		 */
		if(playerTurn > 0.0){	
			//check for possible jumps first
			int[] possibleMoves = isJumpAvailable(currentBoard, playerTurn);

			//if no jumps are possible, check for all possible moves
			if (possibleMoves[81] == 0){
				possibleMoves = isMoveAvailable(currentBoard, playerTurn);
				jump = false;
			}

			//Loop through possible moves here
			for (int i = 0; i < possibleMoves[81]/4; i++){

				tempBoard = copyArray(currentBoard);

				x1 = possibleMoves[i*4];
				x2 = possibleMoves[i*4+1];
				y1 = possibleMoves[i*4+2];
				y2 = possibleMoves[i*4+3];

				pieceLocation[0] = y1;
				pieceLocation[1] = y2;

				//set end position value in matrix to the value
				//currently in the start position
				tempBoard[y1][y2] = tempBoard[x1][x2];
				//set the start position value to 0 (empty)
				tempBoard[x1][x2] = 0;

				//if the chosen move was a jump, set the value of the 
				//position jumped over to 0
				if(jump == true){ tempBoard[(x1 + y1)/2][(x2+y2)/2] = 0; }

				//if any moved piece is moved into a king-me position

				makeKing = isKing(tempBoard);
				setKing(tempBoard);

				// Check for additional jumps if 
				//current jump didn't become a king
				if (jump == true && makeKing == false){

					multiMoves.add(copyArray(tempBoard));
					tempBoard = moreJumps(pieceLocation, tempBoard);

					tempBoard = setKing(tempBoard);
				}

				//tempBoard passed to evaluate3 for further 
				//recursive processing in minmax.  Returned value 
				//is the same as original tempBoard
				//with a utility value added to index [0][0].
				tempBoard = evaluate(tempBoard, playerTurn - .5);

				//if first iteration of the for loop set
				//currentBoard to tempBoard.  (the next returned 
				//tempBoard will be compared to this based on 
				//ply level (whether min or max) and value
				if(i == 0){
					bestValue[0][0] = tempBoard[0][0];
					if(playerTurn == PLY)
					{ bestBoard = copyArray(tempBoard); }
				}

				//if ply is an even value, then at a max call 
				if(playerTurn % 1.0 == 0.0 && i > 0){

					//if the returned tempBoard has a greater
					//utility value then the current bestValue
					//set bestValue[0][0] to tempBoard[0][0]
					if(tempBoard[0][0] > bestValue[0][0]){

						bestValue[0][0] = tempBoard[0][0];

						//copy the board state of tempBoard if 
						//to bestBoard if this is the highest 
						//level of the tree testing moves
						if(playerTurn == PLY)
						{ bestBoard = copyArray(tempBoard); }
					}
				}
				//if ply is a fractional value, then at a min call
				else if(playerTurn % 1.0 != 0.0 && i > 0){
					//if the returned tempBoard has a lesser 
					//utility value then currentBoard
					//set currentBoard to tempBoard
					if(tempBoard[0][0] < bestValue[0][0]){
						bestValue[0][0] = tempBoard[0][0];
					}
				}
			}
		}

		/**
		 * Base step occurs below here.  Only occurs when ply = 0.
		 * Evaluates the state of the board with an external 
		 * function that determines the utility (somehow).  Then 
		 * places the int value returned by this function in an 
		 * unused square of the board and returns the board to the 
		 * calling recursive step.
		 */
		else if(playerTurn == 0.0){

			//currentBoard[0][0] = getStateValue(currentBoard);
			currentBoard[0][0] = boardScore(currentBoard);

			return currentBoard;	
		}

		//set the currentBoard to the board with the correct value 
		if(playerTurn < PLY && playerTurn > 0){

			currentBoard = copyArray(bestValue);
		}
		//set currentBoard to the board with the move made
		else if(playerTurn == PLY){ 

			currentBoard = copyArray(bestBoard);
			currentBoard[0][0] = 0;

			multiMoves.add(copyArray(currentBoard));
			//System.out.println("Number of matrices added" +
				//	" to multimoves " + multiMoves.size());

			for (int j = 0; j < multiMoves.size(); j++) {

				//printBoard(multiMoves.get(j));
			}
		}	

		return currentBoard;
	}

	// Recursive function to make more jumps
	private int[][] moreJumps(int[] pieceLocation, int[][] b){

		int[] newLocation = new int[2];

		// Check for jump up to the left (if piece is not a 1)
		if ((pieceLocation[0] > 1 && pieceLocation[1] > 1 
				&& b[pieceLocation[0]][pieceLocation[1]] != 1)
				&& ((b[pieceLocation[0]-1][pieceLocation[1]-1] == user 
				|| b[pieceLocation[0]-1][pieceLocation[1]-1]  == user+2)
						&& b[pieceLocation[0]-2][pieceLocation[1]-2] == 0)){
			b[pieceLocation[0]-2][pieceLocation[1]-2] =
					b[pieceLocation[0]][pieceLocation[1]];
			b[pieceLocation[0]-1][pieceLocation[1]-1] = 0;
			b[pieceLocation[0]][pieceLocation[1]] = 0;
			if (user == 2 && pieceLocation[0]-2 == 0){
				b[pieceLocation[0]-2][pieceLocation[1]-2]= 4;
			}
			else{
				newLocation[0] = pieceLocation[0]-2;
				newLocation[1] = pieceLocation[1]-2;
				b = moreJumps(newLocation, b);
			}
		}
		// Check for jump up to the right (if piece is not a 1)
		else if ((pieceLocation[0] > 1 && pieceLocation[1] < 6 
				&& b[pieceLocation[0]][pieceLocation[1]] != 1)
				&& ((b[pieceLocation[0]-1][pieceLocation[1]+1] == user 
				|| b[pieceLocation[0]-1][pieceLocation[1]+1]  == user+2)
						&& b[pieceLocation[0]-2][pieceLocation[1]+2] == 0)){
			b[pieceLocation[0]-2][pieceLocation[1]+2] 
					= b[pieceLocation[0]][pieceLocation[1]];
			b[pieceLocation[0]-1][pieceLocation[1]+1] = 0;
			b[pieceLocation[0]][pieceLocation[1]] = 0;
			if (user == 2 && pieceLocation[0]-2 == 0){
				b[pieceLocation[0]-2][pieceLocation[1]+2]= 4;
			}
			else{
				newLocation[0] = pieceLocation[0]-2;
				newLocation[1] = pieceLocation[1]+2;
				b = moreJumps(newLocation, b);
			}
		}
		// Check for jump down to the left (if piece is not a 2)
		else if ((pieceLocation[0] < 6 && pieceLocation[1] > 1 
				&& b[pieceLocation[0]][pieceLocation[1]] != 2)
				&& ((b[pieceLocation[0]+1][pieceLocation[1]-1] == user 
				|| b[pieceLocation[0]+1][pieceLocation[1]-1]  == user+2)
						&& b[pieceLocation[0]+2][pieceLocation[1]-2] == 0)){
			b[pieceLocation[0]+2][pieceLocation[1]-2] 
					= b[pieceLocation[0]][pieceLocation[1]];
			b[pieceLocation[0]+1][pieceLocation[1]-1] = 0;
			b[pieceLocation[0]][pieceLocation[1]] = 0;
			if (user == 1 && pieceLocation[0]+2 == 7){
				b[pieceLocation[0]+2][pieceLocation[1]-2]= 3;
			}
			else{
				newLocation[0] = pieceLocation[0]+2;
				newLocation[1] = pieceLocation[1]-2;
				b = moreJumps(newLocation, b);
			}
		}
		// Check for jump down to the right (if piece is not a 2)
		else if ((pieceLocation[0] < 6 && pieceLocation[1] < 6 
				&& b[pieceLocation[0]][pieceLocation[1]] != 2)
				&& ((b[pieceLocation[0]+1][pieceLocation[1]+1] == user 
				|| b[pieceLocation[0]+1][pieceLocation[1]+1]  == user+2)
						&& b[pieceLocation[0]+2][pieceLocation[1]+2] == 0)){
			b[pieceLocation[0]+2][pieceLocation[1]+2] 
					= b[pieceLocation[0]][pieceLocation[1]];
			b[pieceLocation[0]+1][pieceLocation[1]+1] = 0;
			b[pieceLocation[0]][pieceLocation[1]] = 0;
			if (user == 1 && pieceLocation[0]+2 == 7){
				b[pieceLocation[0]+2][pieceLocation[1]+2]= 3;
			}
			else{
				newLocation[0] = pieceLocation[0]+2;
				newLocation[1] = pieceLocation[1]+2;
				b = moreJumps(newLocation, b);
			}
		}

		return b;
	}

	/**
	 * Applying a value to the board state occurs here
	 * 
	 * @param currentBoard
	 * @return
	 */
	private int boardScore(int[][] endBoard){

		// Result controls the starting score of the board
		int result = 300, totalPieces = 0; 

		//get total number of pieces on the board
		for (int i = 0; i < endBoard.length; i++) {
			for (int j = 0; j < endBoard.length; j++) {
				if (endBoard[i][j] != 0)
					totalPieces++;
			}
		}

		if (totalPieces > 16){
			for (int i = 0; i < endBoard.length; i++) {
				for (int j = 0; j < endBoard.length; j++) {          
					// If black add 3
					if(endBoard[i][j] == computer)
					{ result = result + 15; }  
					// If white subtract 3
					else if(endBoard[i][j] == user)
					{ result = result - 15; }  
					 // If black king add five
					else if(endBoard[i][j] == computerKing)
					{ result = result + 23; } 
					// If white king subtract five
					else if(endBoard[i][j] == userKing)
					{ result = result - 23; } 

					// Add advantage for position (if not king)
					if(endBoard[i][j] == computer && computer == 1){
						if (i == 0){
							result = result + 8;
						}
						else if (i == 2){
							result = result + 1;
						}
						else if (i == 3){
							result = result + 2;
						}
						else if (i == 4){
							result = result + 3;
						}
						else if (i == 5){
							result = result + 4;
						}
						else if (i == 6){
							result = result + 5;
						}
					}
					else if(endBoard[i][j] == computer && computer == 2){
						if (i == 7){
							result = result + 8;
						}
						else if (i == 5){
							result = result + 1;
						}
						else if (i == 4){
							result = result + 2;
						}
						else if (i == 3){
							result = result + 3;
						}
						else if (i == 2){
							result = result + 4;
						}
						else if (i == 1){
							result = result + 5;
						}
					}
					else if(endBoard[i][j] == user && user == 1){
						if (i == 0){
							result = result - 8;
						}
						else if (i == 2){
							result = result - 1;
						}
						else if (i == 3){
							result = result - 2;
						}
						else if (i == 4){
							result = result - 3;
						}
						else if (i == 5){
							result = result - 4;
						}
						else if (i == 6){
							result = result - 5;
						}
					}
					else if(endBoard[i][j] == user && user == 2){
						if (i == 7){
							result = result - 8;
						}
						else if (i == 5){
							result = result - 1;
						}
						else if (i == 4){
							result = result - 2;
						}
						else if (i == 3){
							result = result - 3;
						}
						else if (i == 2){
							result = result - 4;
						}
						else if (i == 1){
							result = result - 5;
						}
					}
				}
			}
		}

		else if (totalPieces > 8){ // If ten or less total pieces, 
				//increase advantage for kings or pieces further down board.
			for (int i = 0; i < endBoard.length; i++) {
				for (int j = 0; j < endBoard.length; j++) {          
					// If black add 3
					if(endBoard[i][j] == computer)
					{ result = result + 15; }  
					// If white subtract 3	
					else if(endBoard[i][j] == user)
					{ result = result - 15; }  
					 // If black king add five
					else if(endBoard[i][j] == computerKing)
					{ result = result + 26; } 
					// If white king subtract five
					else if(endBoard[i][j] == userKing)
					{ result = result - 26; } 
					

					// Add advantage for position (if not king)
					if(endBoard[i][j] == computer && computer == 1){
						if (i == 0){
							result = result + 8;
						}
						else if (i == 2){
							result = result + 1;
						}
						else if (i == 3){
							result = result + 2;
						}
						else if (i == 4){
							result = result + 3;
						}
						else if (i == 5){
							result = result + 4;
						}
						else if (i == 6){
							result = result + 5;
						}
					}
					else if(endBoard[i][j] == computer && computer == 2){
						if (i == 7){
							result = result + 8;
						}
						else if (i == 5){
							result = result + 1;
						}
						else if (i == 4){
							result = result + 2;
						}
						else if (i == 3){
							result = result + 3;
						}
						else if (i == 2){
							result = result + 4;
						}
						else if (i == 1){
							result = result + 5;
						}
					}
					else if(endBoard[i][j] == user && user == 1){
						if (i == 0){
							result = result - 8;
						}
						else if (i == 2){
							result = result - 1;
						}
						else if (i == 3){
							result = result - 2;
						}
						else if (i == 4){
							result = result - 3;
						}
						else if (i == 5){
							result = result - 4;
						}
						else if (i == 6){
							result = result - 5;
						}
					}
					else if(endBoard[i][j] == user && user == 2){
						if (i == 7){
							result = result - 8;
						}
						else if (i == 5){
							result = result - 1;
						}
						else if (i == 4){
							result = result - 2;
						}
						else if (i == 3){
							result = result - 3;
						}
						else if (i == 2){
							result = result - 4;
						}
						else if (i == 1){
							result = result - 5;
						}
					}
				}
			}
		}

		else{ // If ten or less total pieces, increase advantage
				//for kings or pieces further down board.
			for (int i = 0; i < endBoard.length; i++) {
				for (int j = 0; j < endBoard.length; j++) {          
					// If black add 3
					if(endBoard[i][j] == computer)
					{ result = result + 15; } 
					// If white subtract 3
					else if(endBoard[i][j] == user)
					{ result = result - 15; }  
					// If black king add five
					else if(endBoard[i][j] == computerKing)
					{ result = result + 30; }  
					// If white king subtract five
					else if(endBoard[i][j] == userKing)
					{ result = result - 30; } 

					// Add advantage for position (if not king)
					if(endBoard[i][j] == computer && computer == 1){
						if (i == 0){
							result = result + 8;
						}
						else if (i == 2){
							result = result + 1;
						}
						else if (i == 3){
							result = result + 2;
						}
						else if (i == 4){
							result = result + 3;
						}
						else if (i == 5){
							result = result + 4;
						}
						else if (i == 6){
							result = result + 5;
						}
					}
					else if(endBoard[i][j] == computer && computer == 2){
						if (i == 7){
							result = result + 8;
						}
						else if (i == 5){
							result = result + 1;
						}
						else if (i == 4){
							result = result + 2;
						}
						else if (i == 3){
							result = result + 3;
						}
						else if (i == 2){
							result = result + 4;
						}
						else if (i == 1){
							result = result + 5;
						}
					}
					else if(endBoard[i][j] == user && user == 1){
						if (i == 0){
							result = result - 8;
						}
						else if (i == 2){
							result = result - 1;
						}
						else if (i == 3){
							result = result - 2;
						}
						else if (i == 4){
							result = result - 3;
						}
						else if (i == 5){
							result = result - 4;
						}
						else if (i == 6){
							result = result - 5;
						}
					}
					else if(endBoard[i][j] == user && user == 2){
						if (i == 7){
							result = result - 8;
						}
						else if (i == 5){
							result = result - 1;
						}
						else if (i == 4){
							result = result - 2;
						}
						else if (i == 3){
							result = result - 3;
						}
						else if (i == 2){
							result = result - 4;
						}
						else if (i == 1){
							result = result - 5;
						}
					}
				}
			}
		}

		return result + 5;
	}

	//evaluator helper method
	private boolean isKing(int[][] tempBoard){

		for (int j = 0; j < tempBoard.length; j++) {
			for (int k = 0; k < tempBoard[j].length; k++) {

				if(j == 7 && tempBoard[j][k] 
						== computer && computer == 1){
					return true;
				}
				else if(j == 0 && tempBoard[j][k] 
						== computer && computer == 2){
					return true;
				}
				else if(j == 7 && tempBoard[j][k] 
						== user && user == 1){
					return true;
				}
				else if(j == 0 && tempBoard[j][k] 
						== user && user == 2){
					return true;
				}	
			}
		}
		return false;
	}

	//evaluator helper method
	private int[][] setKing(int[][] tempBoard){

		for (int j = 0; j < tempBoard.length; j++) {
			for (int k = 0; k < tempBoard[j].length; k++) {

				if(j == 7 && tempBoard[j][k] 
						== computer && computer == 1){
					tempBoard[j][k] = computerKing;
				}
				else if(j == 0 && tempBoard[j][k] 
						== computer && computer == 2){
					tempBoard[j][k] = computerKing;
				}
				else if(j == 7 && tempBoard[j][k] 
						== user && user == 1){
					tempBoard[j][k] = userKing;
				}
				else if(j == 0 && tempBoard[j][k]
						== user && user == 2){
					tempBoard[j][k] = userKing;
				}	
			}
		}
		return tempBoard;
	}

	//Print the board to the console	
	public void printBoard(int[][] inputBoard){


		for (int a = 0; a < 8; a++){
			for (int b = 0; b < 8; b++){

				System.out.print(inputBoard[a][b] + "  ");
			}
			System.out.println();
		}		
		System.out.println();
	}

	//method needed for copying the matrix to
	//avoid data corruption
	public int[][] copyArray(int[][] oldArray){

		int[][] newArray = new int[oldArray.length][oldArray[0].length];

		for(int i = 0; i < oldArray.length; i++){
			for (int j = 0; j < oldArray[i].length; j++) {
				newArray[i][j] = oldArray[i][j];
			}
		}

		return newArray;

	}
}