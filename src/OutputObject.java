
/**
 * @author kxb140230
 *
 */
public class OutputObject
{
	public int estimate, numberOfNodes;
	public MorrisPList board;
	public String toString()
	{
		return 	"BoardPosition:\t\t\t" + board + "\n" +
				"Positions Evaluated:\t" + numberOfNodes + "\n" + 
				"MINIMAX estimate:\t\t" + estimate;
	}
}