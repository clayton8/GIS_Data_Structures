/**
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * 
 * Generic HashTable
 * 
 * Probing:
 * 		Quadratic: (n^2 + n)/2
 * 
 * Hashing:
 * 		elfhash
 * 
 * This can contain multiple objects with the same key. It is in essence a bucket HashTable
 * 		
 */

package DataStructure.HashTable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Vector;

public class HashTable<K extends Comparable<? super K>, V> {

	class HashNode<K extends Comparable<? super K>, V>{
		K key;
		Vector<V> data;
		boolean tombstone;
		
		HashNode(){
			key = null;
			data = new Vector<V>();
		}
		HashNode(K newKey, V newValue){
			key = newKey;
			data = new Vector<V>();
			data.add(newValue);
			tombstone = false;
		}
		
		HashNode(K newKey, Vector<V> newValues){
			key = newKey;
			data = newValues;
			tombstone = false;
		}
	}
	
	HashNode<K, V>[] table;
	int tableSizeIndex;
	static int tableSizes[] = {1019, 2027, 4079, 8123, 16267, 32503, 65011, 130027, 260111, 520279, 1040387, 2080763, 4161539, 8323151, 16646323};
	int numElements;
	
	/**
	 * Constructor for HashTable
	 */
	public HashTable(){
		tableSizeIndex = 0;
		numElements = 0;
		table = new HashNode[tableSizes[tableSizeIndex]];
	}
	
	/**
	 * Adds an item into the HashTable and will increase table size if it is more than 70%
	 * full.
	 * @param key	A object able to be converted into a string.
	 * @param value
	 * @return Number of probes used to place item.
	 */
	public int addElement(K key, V value){
		Vector<V> values = new Vector<V>();
		values.add(value);
		int numProbes = addElementHelper(key, values, true);
		
		double percentFull = (double)numElements / (double)tableSizes[tableSizeIndex];
		if(percentFull >= .70){
			resizeTableHelper();
		}
		return numProbes;
	}
	
	/**
	 * Resized the table to the next size and rehash
	 */
	private void resizeTableHelper(){
		// Checks to see if there is another size it can expand to
		if(tableSizeIndex < 14){
			ArrayList<HashNode<K, V>> oldTable = new ArrayList<HashNode<K, V>>();
			
			// Copy all the elements into a table so we can resize the table contained in the data structure
			for(int i = 0; i < tableSizes[tableSizeIndex]; i++){
				int checkNode = checkTypeNode(table[i]);
				if(checkNode == 2){
					oldTable.add(table[i]);
				}
			}
			// Create new table for the function
			tableSizeIndex++;
			table = new HashNode[tableSizes[tableSizeIndex]];
			
			// Rehash all the elements into the new table
			for(HashNode<K, V> x: oldTable){
				int checkNode = checkTypeNode(x);
				if(checkNode == 2){
					addElementHelper(x.key, x.data, false);
				}
			}
		}else{
			System.out.println("TABLE WWWWWAAAAAAYYYYY TTTTOOOO BIG!!");
		}
	}
	
	/**
	 * Adds an element to the HashTable. If if it finds a it will not add another value.
	 * @param key	Key to hash off of.
	 * @param value Element to be stored
	 * @return		Number of probes used to insert element
	 */
	private int addElementHelper(K key, Vector<V> value, boolean increment){
		
		
		// Search for a new place to add the new item.
		for(int probeCount = 0; probeCount < tableSizes[tableSizeIndex]; probeCount++){
			
			int index = (int)getIndex(key, probeCount, tableSizes[tableSizeIndex]);
			
			
			int checkNode = checkTypeNode(table[index]);
			
			if(checkNode == 0){
				// is an empty node
				table[index] = new HashNode<K,V>(key, value);
				if(increment)
					numElements++;
				return probeCount;
			}else if(checkNode == 1){
				// is a tumbstone node
				table[index].data = value;
				table[index].key = key;
				if(increment)
					numElements++;
				return probeCount;
			}else if(table[index].key.equals(key)){
				table[index].data.addAll(value);
				return probeCount;
			}
		}
		return -1;
	}
	
	/**
	 * Does quadratic probing according to the key and the probe count.
	 * @param key	Key to be hashed
	 * @param probeCount	Number of probes done
	 * @param tableSize		Size of the table to mod off of
	 * @return	The index in the table to place the value
	 */
	long getIndex(K key, int probeCount, int tableSize){
		return (elfHash((String) key) + (probeCount*probeCount + probeCount)/2) % tableSize;
	}
	
	/**
	 * Checks to see if a node is null, zombie or filled
	 * @param node is a HashNode you want to check
	 * @return 0 if null, 1 if zombie and 2 if filled
	 */
	private int checkTypeNode(HashNode<K, V> node){
		if(node == null){
			return 0;
		}
		if(node.tombstone){
			return 1;
		}
		
		return 2;
	}
	
	/**
	 * Hashes a string to a long
	 * @param toHash String to be hashed
	 * @return The hash value
	 */
	private long elfHash(String toHash){
		// From notes
		long hashValue = 0;
		for (int Pos = 0; Pos < toHash.length(); Pos++) { // use all elements 
			hashValue = (hashValue << 4) + toHash.charAt(Pos); // shift/mix
			long hiBits = hashValue & 0xF000000000000000L; 		// get high nybble
			
			if (hiBits != 0){
				hashValue ^= hiBits >> 56; // xor high nybble with the second nybble
			}
			hashValue &= ~hiBits; 	// clear high nybble
		}
		return hashValue;

	}
	
	/**
	 * Returns a value that corresponds to a particular key
	 * @param key	That you are searching for.
	 * @return	Returns the value associated with the key.
	 */
	public Vector<V> find(K key){
		// Go through the table and if null is found or the equivalent is found
		// return that. If the entire table is searched then it will break out and
		// return null.
		for(int i = 0; i < tableSizes[tableSizeIndex]; i++){
			int index = (int) getIndex(key, i, tableSizes[tableSizeIndex]);
			int checkNode = checkTypeNode(table[index]);
			if(checkNode == 0){
				return null;
			}
			if(checkNode == 2 && table[index].key.equals(key)){
				return table[index].data;
			}
			
		}
		return null;
	}
	
	/**
	 * Deletes the particular key that is inserted. Will set both the key and data to null
	 * indicating a tombstone slot.
	 * @param key	Wanting to delete
	 * @return		Whether a deletion occurred
	 */
	public boolean remove(K key){

		for(int i = 0; i < tableSizes[tableSizeIndex]; i++){
			int index = (int)getIndex(key, i, tableSizes[tableSizeIndex]);

			int checkNode = checkTypeNode(table[index]);
			if(checkNode == 0){
				return false;
			}
			if(checkNode == 2 && table[index].key.equals(key)){
				table[index].tombstone = true;
				numElements--;
				return true;
			}	
		}

		return false;
	}
	
	/**
	 * returns a string containing all informaiton needed about the hashtable, and what slots
	 * are filled and what is in the slot.
	 */
	@Override
	public String toString(){
		String stringHashTable = "Current table size is " + tableSizes[tableSizeIndex] 
								+"\nNumber of elements in table is " + numElements + "\n\n";
		for(int i = 0; i < tableSizes[tableSizeIndex]; i++){
			int checkNode = checkTypeNode(table[i]);
			if(checkNode == 2){
				stringHashTable += i + ":   " + table[i].data.toString() + "\n";
			}else if(checkNode == 1){
				stringHashTable += i + ":   TOMBSTONE\n";
			}
		}
		return stringHashTable;
	}

}















