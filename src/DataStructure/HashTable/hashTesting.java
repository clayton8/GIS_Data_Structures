package DataStructure.HashTable;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class hashTesting {

	@Before
	public void setUp() throws Exception {
	}
/*
	@Test
	public void testInstantiation() {
		HashTable<String, String> ht = new HashTable<String, String>();
		for(HashTable.HashNode x: ht.table){
			assertEquals(null, x);
		}
		assertEquals(0, ht.tableSizeIndex);
		assertEquals(1019, ht.tableSizes[ht.tableSizeIndex]);
		assertEquals(0, ht.numElements);
	}
	
	@Test
	public void testInsertion(){
		HashTable<String, String> ht = new HashTable<String, String>();
		int key = 0;
		int value = 0; 
		for(;key < 4000; key++, value++){
			ht.addElement(Integer.toString(key), Integer.toString(value));
		}
		assertEquals(4000, ht.numElements);
		assertEquals(3, ht.tableSizeIndex);
		assertEquals(8123, ht.tableSizes[ht.tableSizeIndex]);
		
		int realNum = 0;
		for(int i = 0; i < ht.tableSizes[ht.tableSizeIndex]; i++){
			if(ht.table[i] != null){
				realNum++;
			}
		}
		assertEquals(4000, realNum);
		
	}
	
	@Test
	public void testInsertionDuplicate(){
		HashTable<String, String> ht = new HashTable<String, String>();
		int key = 0;
		int value = 0; 
		boolean added;
		added = ht.addElement(Integer.toString(key), Integer.toString(value));
		assertEquals(true, added);
		assertEquals(1, ht.numElements);
		assertEquals(0, ht.tableSizeIndex);
		assertEquals(1019, ht.tableSizes[ht.tableSizeIndex]);
		ht.displayHashTable();
		
		added = ht.addElement(Integer.toString(key), Integer.toString(value));
		assertEquals(false, added);
		assertEquals(1, ht.numElements);
		assertEquals(0, ht.tableSizeIndex);
		assertEquals(1019, ht.tableSizes[ht.tableSizeIndex]);
		//ht.displayHashTable();
		
	}
	
	@Test
	public void testSearch(){
		HashTable<String, String> ht = new HashTable<String, String>();
		int key = 0;
		int value = 0; 
		for(;key < 4000; key++, value++){
			Random generator = new Random();
			int randNum = generator.nextInt();
			boolean placed = false;
			ht.addElement(Integer.toString(randNum), Integer.toString(value));
		}
		
		String newval =  ht.find("0");
		assertEquals("0", newval);
		newval =  ht.find("100");
		assertEquals("100", newval);
		newval =  ht.find("400");
		assertEquals("400", newval);
		newval =  ht.find("090909090909");
		assertEquals(null, newval);
		
	}
	
	@Test
	public void testDeletion(){
		HashTable<String, String> ht = new HashTable<String, String>();
		int key = 0;
		int value = 0; 
		for(;key < 4000; key++, value++){
			if(key == 788){
				ht.displayHashTable();
			}
			ht.addElement(Integer.toString(key), Integer.toString(value));
		}
		
		boolean removed =  ht.remove("0");
		assertEquals(true, removed);
		assertEquals(3999, ht.numElements);
		assertEquals(3, ht.tableSizeIndex);
		assertEquals(8123, ht.tableSizes[ht.tableSizeIndex]);
		
		removed =  ht.remove("0");
		assertEquals(false, removed);
		String found = ht.find("0");
		assertEquals(null, found);
		assertEquals(3999, ht.numElements);
		assertEquals(3, ht.tableSizeIndex);
		assertEquals(8123, ht.tableSizes[ht.tableSizeIndex]);
		
		for(int i = 1; i < 4000; i++){
			removed =  ht.remove(Integer.toString(i));
			assertEquals(true, removed);
			
			assertEquals(4000 - i-1, ht.numElements);
			assertEquals(3, ht.tableSizeIndex);
			assertEquals(8123, ht.tableSizes[ht.tableSizeIndex]);
		}
		*/
		
		
	//}
	
	
	
	
	
	
	
	

}
