package snozama.runtime.tests.mechanics;

import static org.junit.Assert.*;

import org.junit.Test;

import snozama.amazons.mechanics.killerheuristic.KillerTable;

public class KillerTableTest {

	@Test
	public void testCreate() {
		KillerTable table = new KillerTable();
		
		for (int i = 0; i < table.maxDepth; i++)
		{
			for (int j = 1; j <= table.movesPerDepth; j++)
			{
				assertEquals(0, table.get(table.movesPerDepth*i + j));
			}
		}
	}
	
	@Test
	public void testPutGetSimple()
	{
		KillerTable table = new KillerTable();
		
		for (int depth = 0; depth <= table.maxDepth; depth++)
		{
			int index = table.getStartingIndex(depth);

			assertEquals((depth*(table.movesPerDepth+1))+1, index);

			assertEquals(2, table.movesPerDepth);

			assertEquals(0, table.get(index));
			assertEquals(0, table.get(index+1));

			table.put(2, depth);
			table.put(2, depth);

			assertEquals(2, table.get(index));
			assertEquals(2, table.get(index+1));

			table.put(7, depth);

			assertEquals(7, table.get(index));
			assertEquals(2, table.get(index+1));
		}
	}

}
