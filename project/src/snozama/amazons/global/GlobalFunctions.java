package snozama.amazons.global;

public final class GlobalFunctions {
	/**
	 * Function that flips input from 1 to 0 or 0 to 1.
	 * 
	 * @param input		The integer whose value one wants to flip.
	 * @return	{@value 1} if input is {@value 0}, {@value 1} otherwise.
	 */
	public static int flip(int input)
	{
		if (input == 0)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Returns max of the input.
	 * 
	 * @param a		An integer to be compared.
	 * @param b		An integer to be compared.
	 * @return	The maximum of {@value a} and {@value b}.
	 */
	public static int max(int a, int b)
	{
		if (b > a)
		{
			return b;
		}
		else
		{
			return a;
		}
	}
	
	/**
	 * Returns min of the input.
	 * 
	 * @param a		An integer to be compared.
	 * @param b		An integer to be compared.
	 * @return	The minimum of {@value a} and {@value b}.
	 */
	public static int min(int a, int b)
	{
		if (b < a)
		{
			return b;
		}
		else
		{
			return a;
		}
	}
}
