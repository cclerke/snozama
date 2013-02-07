package snozama.runtime.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import snozama.amazons.global.GlobalFunctions;

public class GlobalFunctionsTest {

	@Test
	public void testMax() {
		assertEquals(GlobalFunctions.max(1, 2), 2);
		assertEquals(GlobalFunctions.max(6, 3), 6);
		assertEquals(GlobalFunctions.max(-7, -8), -7);
	}
	
	@Test
	public void testDualQuickSortAsc() {
		int[] a = {0, 3, 2, 6, 5, 8, 2, 2, 4, 5, 6, 7};
		int[] b = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		
		int[] atest = a.clone();
		int[] btest = b.clone();
		
		GlobalFunctions.dualQuickSort(btest, atest, 0, a.length-1, (byte)1);
		
		for (int i = 0; i < atest.length; i++)
		{
			System.out.println(atest[i]);
		}
		
		for (int i = 0; i < atest.length-1; i++)
		{
			assertTrue(atest[i] <= atest[i+1]);
		}
	}
	

	@Test
	public void testDualQuickSortDesc() {
		int[] a = {0, 3, 2, 6, 5, 8, 2, 2, 4, 5, 6, 7};
		int[] b = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		
		int[] atest = a.clone();
		int[] btest = b.clone();
		
		GlobalFunctions.dualQuickSort(btest, atest, 0, a.length-1, (byte)0);
		
		for (int i = 0; i < atest.length; i++)
		{
			System.out.println(atest[i]);
		}
		
		for (int i = 0; i < atest.length-1; i++)
		{
			assertTrue(atest[i] >= atest[i+1]);
		}
	}

}
