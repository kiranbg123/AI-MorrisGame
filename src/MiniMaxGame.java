import java.util.*;

/**
 * @author kxb140230
 *
 */
public class MiniMaxGame
{
	public static void main(String[] args)
	{
		String inputFileName = args[0];
		String outputFileName = args[1];
		int depth = Integer.parseInt(args[2]);

		MorrisPList initBoard = new MorrisPList(MorrisGame.getBoardConfig(inputFileName));
		
		OutputObject algOut = MiniMax(depth, true, initBoard);

		MorrisGame.writeOutput(algOut, outputFileName);
	}

	
	public static OutputObject MiniMax(int depth, boolean isWhite, MorrisPList board)
	{
		OutputObject out = new OutputObject();

		/* Get the open board estimation if we are at terminal node*/
		if (depth == 0)
		{
			out.estimate = MorrisGame.StaticEstimationForMidEndGame(board);
			out.numberOfNodes++;
			return out;
		}

		OutputObject in = new OutputObject();
		List<MorrisPList> list;
		if(isWhite)
		{
			 list = MorrisGame.generateMovesMidgameEndgame(board);
			out.estimate = Integer.MIN_VALUE;
		}
		else
		{
			list = MorrisGame.generateMovesMidgameEndgameBlack(board);
			out.estimate = Integer.MAX_VALUE;
		}
		
		//Find the estimate recursively for each list
		for (MorrisPList b : list)
		{
			//System.out.println("The Morris Position List :" + b);
			if (isWhite)
			{
				in = MiniMax(depth - 1, false, b);
				out.numberOfNodes += in.numberOfNodes;
				if (in.estimate > out.estimate)
				{
					out.estimate = in.estimate;
					out.board = b;
				}
			}
			else
			{
				in = MiniMax(depth - 1, true, b);
				out.numberOfNodes += in.numberOfNodes;
				out.numberOfNodes++;
				if (in.estimate < out.estimate)
				{
					out.estimate = in.estimate;
					out.board = b;
				}
			}
		}
		return out;
	}
}