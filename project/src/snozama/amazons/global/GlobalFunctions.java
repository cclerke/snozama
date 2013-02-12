package snozama.amazons.global;

/**
 * Class containing functions that are needed throughout code base.
 * 
 * @author Graeme Douglas
 * @author Alex Yakovlev
 *
 */
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
	
	// QuickSort sorting code.  See CLRS or http://codereview.stackexchange.com/questions/4022/java-implementation-of-quick-sort
	/**
	 * A quick sort algorithm that sorts input arrays toSort and sortBy based on
	 * values in sortBy.  Can be sorted in any order.
	 * 
	 * @param toSort	Primary array to be sorted.
	 * @param sortBy	Secondary array to be sorted, determines the sort order.
	 * @param a			Index of first item to sort from.
	 * @param b			Index of last item to sort to.
	 * @param order		<code> 1 </code> to be sorted in ascending order,
	 * 					<code> -1 </code> (or otherwise) to sort in descending
	 * 					order.
	 */
	public static void dualQuickSort(int[] toSort, int[] sortBy, int a, int b, byte order)
    {
        if(a < b)
        {
        	int c = partition(toSort, sortBy, a, b, order);
        	
            dualQuickSort(toSort, sortBy, a, c, order);
            dualQuickSort(toSort, sortBy, c+1, b, order);
        }
    }
	
	/**
	 * @param toSort	Primary array to be sorted.
	 * @param sortBy	Secondary array to be sorted, determines the sort order.
	 * @param a			Index of first item to sort from.
	 * @param b			Index of last item to sort to.
	 * @param order		<code> 1 </code> to be sorted in ascending order,
	 * 					<code> -1 </code> (or otherwise) to sort in descending
	 * 					order.	
	 * @return			The index of the item between <code> a </code> and
	 * 					<code> b </code> that divides the arrays into partitions.
	 */
	private static int partition(int[] toSort, int[] sortBy, int a, int b, byte order) {

        int x = sortBy[a];
        int i = a-1 ;
        int j = b+1 ;

        while (true)
        {
            i++;
            
            while (i < b && ((order == 1 && sortBy[i] < x) || (order != 1  && sortBy[i] > x)))
            {
                i++;
            }
            
            j--;
            
            while (j > a && ((order == 1 && sortBy[j] > x) || (order != 1 && sortBy[j] < x)))
            {
                j--;
            }
            
            if (i < j)
            {
                swap(sortBy, i, j);
                swap(toSort, i, j);
            }
            else
            {
                return j;
            }
        }
    }
	
	/**
	 * Swap values at indices <code> i </code> and <code> j </code> in array
	 * <code> a </code>.
	 * 
	 * @param a		The array whose items need to be swapped.
	 * @param i		The index of the first item to swap.
	 * @param j		The index of the second item to swap.
	 */
	public static void swap(int[] a, int i, int j)
	{
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
}
