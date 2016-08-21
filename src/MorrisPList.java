import java.util.*;

/**
 * @author kxb140230
 *
 */
public class MorrisPList
{

	public List<pType> pList;

	/* Initialize the board*/
	public MorrisPList()
	{
		pList = new ArrayList<pType>();
		int i =0;
		while(i <=22)
		{
		pList.add(pType.X);
		i++;
		}
	}

	/* Allows user to specify what the board locations should be */
	public MorrisPList(List<Character> inputBoard)
	{
		pList = new ArrayList<pType>();
		for (char c : inputBoard)
		{
			pType pos = (c == 'W') ? pType.W : ((c == 'B') ? pType.B : pType.X);
			pList.add(pos);
		}
	}

	public List<Character> getCharList()
	{
		ArrayList<Character> out = new ArrayList<Character>();
		for (pType pos : pList)
		{
			out.add(pos.val);
		}
		return out;
	}

	public char[] getCharArr()
	{
		char[] out = new char[pList.size()];
		for (int i = 0; i < pList.size(); i++)
		{
			out[i] = pList.get(i).val;
		}
		return out;
	}

	public MorrisPList getCopy()
	{
		List<Character> boardState = getCharList();
		return (new MorrisPList(boardState));
	}

	/* Return the inverse of the current board(useful for generating black moves)*/
	public MorrisPList getFlipBoard()
	{
		MorrisPList out = new MorrisPList();
		for (int i = 0; i < pList.size(); i++)
		{
			pType val = pList.get(i);
			if (val == pType.B)
			{
				out.set(i, pType.W);
			}
			else if (val == pType. W)
			{
				out.set(i, pType.B);
			}
			else
			{
				out.set(i, pType.X);
			}
		}
		return out;
	}

	public int getNumberOfPieces(pType piecePos)
	{
		int counter = 0;
		for (pType pos : pList)
		{
			if (pos == piecePos)
			{
				counter++;
			}
		}
		return counter;
	}

	/*
		Some List functionality wrappers for convenience
	*/
	public pType get(int i)
	{
		return pList.get(i);
	}

	public int size()
	{
		return pList.size();
	}

	public void add(pType val)
	{
		pList.add(val);
	}

	public void set(int i, pType val)
	{
		pList.set(i, val);
	}

	public String toString()
	{
		return (new String(getCharArr()));
	}
}