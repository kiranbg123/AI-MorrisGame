import java.util.*;
import java.io.*;

/**
 * @author kxb140230
 *
 */
public class MorrisGame
{
	
	/** MidEnd Game static estimation suggested by professor 
	 *
	 * @param board representing the position
	 * @return static estimation for the given board position
	 */
	public static int StaticEstimationForMidEndGame(MorrisPList board)
	{
		int numBlackPieces = board.getNumberOfPieces(pType.B);
		int numWhitePieces = board.getNumberOfPieces(pType.W);
		List<MorrisPList> list = generateMovesMidgameEndgame(board);
		int numBlackMoves = list.size();
		if (numBlackPieces <= 2)
		{
			return 10000;
		}
		else if (numWhitePieces <= 2)
		{
			return -10000;
		}
		else if (numBlackPieces == 0)
		{
			return 10000;
		}
		else
		{
			return 1000*(numWhitePieces - numBlackPieces) - numBlackMoves;
		}
	}
	
	/** Opening game static estimation suggested by professor 
	 *
	 * @param board representing the position
	 * @return static estimation for the given board position
	 */
	public static int StaticEstimaionForOpenGame(MorrisPList board)
	{
		return (board.getNumberOfPieces(pType.W) - board.getNumberOfPieces(pType.B));
	}
	
	/** Opening game static estimation improved by considering how many potential mills White has
	 *
	 * @param board representing the position
	 * @return static estimation for the given board position
	 */
	public static int StaticEstimaionImprovedForOpenGame(MorrisPList board)
	{
		return (board.getNumberOfPieces(pType.W) + getNoOfPotentialMills(pType.W, board) - board.getNumberOfPieces(pType.B));
	}
	
	/** MidEndgame static estimation improved by considering how many potential mills White has
	 *
	 * @param board representing the position
	 * @return static estimation for the given board position
	 */
	
	/**
	 *
	 * @param board representing the position and position type
	 * @return the number of positions that pos could take which would result in a mill
	 */
	public static int getNoOfPotentialMills(pType pos, MorrisPList board)
	{
		int counter = 0;
		for (int i = 0; i< board.size(); i++)
		{
			pType boardPosition = board.get(i);
			if (boardPosition == pType.X)
			{
				if (checkAllMills(i, boardPosition, board))
				{
					counter++;
				}
			}
		}
		return counter;
	}
	public static int StaticEstimationImprovedForMidEndGame(MorrisPList board)
	{
		//Get the number of black, white and potential mills for white
		int noOfPotentialMills = getNoOfPotentialMills(pType.W, board);
		int noOfBlackPiece = board.getNumberOfPieces(pType.B);
		int noOfWhitePiece = board.getNumberOfPieces(pType.W);
		
		List<MorrisPList> list = generateMovesMidgameEndgame(board);
		int numBMoves = list.size();
		if (noOfBlackPiece <= 2)
		{
			return 10000;
		}
		else if (noOfWhitePiece <= 2)
		{
			return -10000;
		}
		else if (noOfBlackPiece == 0)
		{
			return 10000;
		}
		else
		{
			return 1000*(noOfWhitePiece - noOfBlackPiece + noOfPotentialMills) - numBMoves;
		}
	}	

	/**
	 *
	 * @param board representing the position and position type
	 * @return the list containing all opening stage moves for white from given board
	 */
	public static List<MorrisPList> generateMovesOpening(MorrisPList board)
	{
		
		return generateAdd(board);
	}

	/**
	 *
	 * @param board representing the position and position type
	 * @return the list containing all opening stage moves for black from given board
	 */
	public static List<MorrisPList> generateMovesOpeningBlack(MorrisPList board)
	{
		MorrisPList tempb = board.getFlipBoard();
		List<MorrisPList> moves = generateMovesOpening(tempb);
		/*for (int i = 0; i <= moves.size(); i++)
		{
			MorrisPositionList b = moves.get(i);
			moves.set(i, b.getFlipBoard());
		}*/
		for (int i = 0; i < moves.size(); i++)
		{
			MorrisPList b = moves.get(i);
			moves.set(i, b.getFlipBoard());
		}
		return moves;
	}

	/**
	 *
	 * @param board representing the position and position type
	 * @return the list containing all MidEnd stage moves for white from given board
	 */
	public static List<MorrisPList> generateMovesMidgameEndgame(MorrisPList board)
	{
		if (board.getNumberOfPieces(pType.W) == 3)
		{
			return generateHopping(board);
		}
		else
		{
			return generateMove(board);
		}
	}

	
	/**
	 *
	 * @param board representing the position and position type
	 * @return the list containing all MidEnd stage moves for black from given board
	 */
	public static List<MorrisPList> generateMovesMidgameEndgameBlack(MorrisPList board)
	{
		MorrisPList tempb = board.getFlipBoard();
		List<MorrisPList> moves = generateMovesMidgameEndgame(tempb);
		ArrayList<MorrisPList> out = new ArrayList<MorrisPList>();
		for (int i = 0; i < moves.size(); i++)
		{
			MorrisPList b = moves.get(i);
			out.add(b.getFlipBoard());
		}
		return out;
	}

	/**
	 *
	 * @param board representing the position and position type
	 * @return the list of all possible additions for white from given board
	 */
	public static List<MorrisPList> generateAdd(MorrisPList board)
	{
		ArrayList<MorrisPList> l = new ArrayList<MorrisPList>();
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == pType.X)
			{
				MorrisPList b = board.getCopy();
				b.set(i, pType.W);
				if (closeMill(i, b))
				{
			//		System.out.println(generateMove(board));
					l = generateRemove(b, l);
				}
				else
				{
					l.add(b);
				}
			}
		}
		return l;
	}

	/**
	 *
	 * @param board representing the position and position type
	 * @return the list of all possible moves for white from the given board when white can hop(numWhite = 2)
	 */
	public static List<MorrisPList> generateHopping(MorrisPList board)
	{
		ArrayList<MorrisPList> l = new ArrayList<MorrisPList>();
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == pType.W)
			{
				for (int j = 0; j < board.size(); j++)
				{
					if (board.get(j) == pType.X)
					{
						MorrisPList b = board.getCopy();
						b.set(i, pType.X);
						b.set(j, pType.W);
						if (closeMill(j, b))
						{
							generateRemove(b, l);
						}
						else
						{
							l.add(b);
						}
					}
				}
			}
		}
		return l;
	}


	/**
	 *
	 * @param board representing the position and position type
	 * @return the list of all possible moves for white from the given board(both additions and removals)
	 */
	public static List<MorrisPList> generateMove(MorrisPList board)
	{
		ArrayList<MorrisPList> l = new ArrayList<MorrisPList>();
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == pType.W)
			{
				List<Integer> n = neighbors(i);
				for (int j : n)
				{
					if (board.get(j) == pType.X)
					{
						MorrisPList b = board.getCopy();
						b.set(i, pType.X);
						b.set(j, pType.W);
						if (closeMill(j, b))
						{
							l = generateRemove(b, l);
						}
						else
						{
							l.add(b);
						}
					}
				}
			}
		}
		return l;
	}

	
	/**
	 *
	 * @param board representing the position and position type
	 * @return the list of all possible removals that white could perform from the given board(occurs when white gets a mill)
	 */
	public static ArrayList<MorrisPList> generateRemove(MorrisPList board, ArrayList<MorrisPList> l)
	{
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == pType.B)
			{
				if (!closeMill(i, board))
				{
					MorrisPList b = board.getCopy();
					b.set(i, pType.X);
					l.add(b);
				}
			}
		}
		return l;
	}

	/** The following function stores the neighbors for the given vertex
	 *
	 * @param vertex j
	 * 
	 */
	public static List<Integer> neighbors(int j)
	{
		//The following cases are hard-coded based on the diagram provided by the professor for Morris Game
		switch(j)
		{
			case 0:
				return Arrays.asList(1, 3, 8);
			case 1:
				return Arrays.asList(0, 2, 4);
			case 2:
				return Arrays.asList(1, 5, 13);
			case 3:
				return Arrays.asList(0, 4, 6, 9);
			case 4:
				return Arrays.asList(1, 3, 5);
			case 5:
				return Arrays.asList(2, 4, 7, 12);
			case 6:
				return Arrays.asList(3, 7, 10);
			case 7:
				return Arrays.asList(5, 6, 11);
			case 8:
				return Arrays.asList(0, 9, 20);
			case 9:
				return Arrays.asList(3, 8, 10, 17);
			case 10:
				return Arrays.asList(6, 9, 14);
			case 11:
				return Arrays.asList(7, 12, 16);
			case 12:
				return Arrays.asList(5, 11, 13, 19);
			case 13:
				return Arrays.asList(2, 12, 22);
			case 14:
				return Arrays.asList(10, 15, 17);
			case 15:
				return Arrays.asList(14, 16, 18);
			case 16:
				return Arrays.asList(11, 15, 19);
			case 17:
				return Arrays.asList(9, 14, 18, 20);
			case 18:
				return Arrays.asList(15, 17, 19, 21);
			case 19:
				return Arrays.asList(12, 16, 18, 22);
			case 20:
				return Arrays.asList(8, 17, 21);
			case 21:
				return Arrays.asList(18, 20, 22);
			case 22:
				return Arrays.asList(13, 19, 21);
			default:
				return (new ArrayList<Integer>());
		}
	}

		
	/** The following function determines if i makes a mill(ie gets three connected nodes in a row)
	 *
	 * @param position i, board b
	 * 
	 */
	public static boolean closeMill(int i, MorrisPList b)
	{
		pType C = b.get(i);
		if (C == pType.X)
		{
			return false;
		}
		else
		{
			return checkAllMills(i, C, b);
		}
	}
	

	private static boolean checkMill(MorrisPList b, pType C, int v1, int v2)
	{
		return (b.get(v1) == C && b.get(v2) == C);
	}

		
   /** The following function checks all possible mills to see if position type C fills a mill at i
	*
	* @param position i, board b
	* 
	*/
	public static boolean checkAllMills(int i, pType C, MorrisPList b)
	{	
		/*
		 * The following mills are coded based on the diagram provided by the professor for Morris Game in the hand out.
		 */
		switch(i)
		{
			case 0:
				return (checkMill(b, C, 1, 2) || checkMill(b, C, 8, 20) || checkMill(b, C, 3, 6));
			case 1:
				return (checkMill(b, C, 0, 2));
			case 2:
				return (checkMill(b, C, 0, 1) || checkMill(b, C, 5, 7) || checkMill(b, C, 13, 22));
			case 3:
				return (checkMill(b, C, 0, 6) || checkMill(b, C, 4, 5) || checkMill(b, C, 9, 17));
			case 4:
				return (checkMill(b, C, 3, 5));
			case 5:
				return (checkMill(b, C, 3, 4) || checkMill(b, C, 2, 7) || checkMill(b, C, 12, 19));
			case 6:
				return (checkMill(b, C, 0, 3) || checkMill(b, C, 10, 14));
			case 7:
				return (checkMill(b, C, 2, 5) || checkMill(b, C, 11, 16));
			case 8:
				return (checkMill(b, C, 0, 20) || checkMill(b, C, 9, 10));
			case 9:
				return (checkMill(b, C, 8, 10) || checkMill(b, C, 3, 17));
			case 10:
				return (checkMill(b, C, 8, 9) || checkMill(b, C, 6, 14));
			case 11:
				return (checkMill(b, C, 7, 16) || checkMill(b, C, 12, 13));
			case 12:
				return (checkMill(b, C, 11, 13) || checkMill(b, C, 5, 19));
			case 13:
				return (checkMill(b, C, 11, 12) || checkMill(b, C, 2, 22));
			case 14:
				return (checkMill(b, C, 17, 20) || checkMill(b, C, 15, 16) || checkMill(b, C, 6, 14));
			case 15:
				return (checkMill(b, C, 14, 16) || checkMill(b, C, 18, 21));
			case 16:
				return (checkMill(b, C, 14, 15) || checkMill(b, C, 19, 22) || checkMill(b, C, 7, 11));
			case 17:
				return (checkMill(b, C, 3, 9) || checkMill(b, C, 14, 20) || checkMill(b, C, 18, 19));
			case 18:
				return (checkMill(b, C, 17, 19) || checkMill(b, C, 15, 21));
			case 19:
				return (checkMill(b, C, 17, 18) || checkMill(b, C, 16, 22) || checkMill(b, C, 5, 12));
			case 20:
				return (checkMill(b, C, 0, 8) || checkMill(b, C, 14, 17) || checkMill(b, C, 21, 22));
			case 21:
				return (checkMill(b, C, 20, 22) || checkMill(b, C, 15, 18));
			case 22:
				return (checkMill(b, C, 2, 13) || checkMill(b, C, 16, 19) || checkMill(b, C, 20, 21));
			default:
				return false;
		}
	}	
	
	/*
		Writes the optimal move to the specified file 
	*/
	public static void writeOutput(OutputObject out, String fName)
	{
		try {
			FileWriter fileWriter = new FileWriter(fName);

			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(out.toString());

			bufferedWriter.close();
		}
		catch(IOException ex) {
			System.out.println("Error writing to the file '" + fName + "'");
		}
	}
	/** The following function reads the board configuration
	 *
	 * @param file name 
	 * @return list of characters(board configuration) ex: xxxxxxxxxxxxxxxxWxxxxxx 
	 * 
	 */
	public static List<Character> getBoardConfig(String fName)
	{
		String line = null;
		
		try
		{
			FileReader fileReader = new FileReader(fName);
			BufferedReader bufferReader = new BufferedReader(fileReader);
			line = bufferReader.readLine();
			ArrayList<Character> out = new ArrayList<Character>();
			for (char a : line.toCharArray())
			{
				out.add(a);
			}
			bufferReader.close();
			return out;
		}
		catch(FileNotFoundException ex)
		{
			System.out.println( "Not able to open the file '" + fName + "' .Please check file path & permision");
		}
		catch(IOException ex)
		{
			System.out.println("Error reading the file '" + fName + "'");
		}
		return null;
	}
}