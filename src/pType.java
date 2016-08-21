

/**
 * @author kxb140230
 *
 */
/* The following class is used to represent 3 different marks each position can take */
public enum pType
{
	X ('X'),
	B ('B'),
	W ('W');

	public final char val;
	pType(char val)
	{
		this.val = val;
	}
}