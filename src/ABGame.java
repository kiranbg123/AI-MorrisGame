import java.util.*;


/**
 * @author kxb140230
 *
 */
public class ABGame
{
	public static void main(String[] args)
	{
		String inputFileName = args[0];
		String outputFileName = args[1];
		int depth = Integer.parseInt(args[2]);

		MorrisPList initBoard = new MorrisPList(MorrisGame.getBoardConfig(inputFileName));

		OutputObject algOut = ABMiniMax(depth, true, initBoard, Integer.MIN_VALUE, Integer.MAX_VALUE);


		MorrisGame.writeOutput(algOut, outputFileName);
	}
	
	
	public static OutputObject ABMiniMax(int depth, boolean isWhite, MorrisPList board, int alpha, int beta)
	{
		OutputObject out = new OutputObject();

		/* Get the open board estimation if we are at terminal node*/
		if (depth == 0)
		{
			out.estimate = MorrisGame.StaticEstimationForMidEndGame(board);
			out.numberOfNodes++;
			return out;
		}

		List<MorrisPList> list;
		OutputObject in = new OutputObject();
		//List<MorrisPositionList> list = new ArrayList<MorrisPositionList>(); 
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
			if (isWhite)
			{
				in = ABMiniMax(depth - 1, false, b, alpha, beta);
				out.numberOfNodes += in.numberOfNodes;
				out.numberOfNodes++;
				if (in.estimate > alpha)
				{
					alpha = in.estimate;
					out.board = b;
				}
			}
			else
			{
				in = ABMiniMax(depth - 1, true, b, alpha, beta);
				out.numberOfNodes += in.numberOfNodes;
				if (in.estimate < beta)
				{
					beta = in.estimate;
					out.board = b;
				}
			}
			if (alpha >= beta)
			{
				break;
			}
		}
		
		out.estimate = (isWhite) ? alpha : beta;
		return out;
	}
}