/**
 * 
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * This is the class that is the layer between the conductor and the hash table.
 * 
 * It makes it easy for the conductor to pass in the arguments from the command and
 * this will correctly modify the permeates to search the HashTable.
 * 
 * For the key it is concatenated in this form:
 * 
 * <Feature Name>:<State Abbreviation>
 * 
 * The data stored in the hash table is found in the DataStructure.BufferPool package.
 */

import java.util.Vector;

import DataStructure.Client.HashNameIndex;
import DataStructure.HashTable.HashTable;
import GISRecord.GISRecord;


public class nameIndex {
	HashTable<String, HashNameIndex> hashTable;
	int maxProbes;
	
	nameIndex(){
		hashTable = new HashTable<String, HashNameIndex>();
		maxProbes = 0;
	}
	
	/**
	 * Called to add a GIS record to the hash table.
	 * @param record	A valid GISrecord that is not null.
	 * @return			The max amount of probes since the nameIndex has been created.
	 */
	public long add(GISRecord record){
		// Concaticate the feature name and state abbreviation for the key
		String key = record.getFeatureName() + ":" + record.getStateAbriviation();
		
		HashNameIndex value = new HashNameIndex(record.getFeatureName(), record.getStateAbriviation(), record.getOffset());
		// INsert into the hash table
		int numProbes = hashTable.addElement(key, value);
		if(maxProbes < numProbes){
			maxProbes = numProbes;
		}
		
		return maxProbes;
	}
	
	/**
	 * Called to find a particular feature name and state abbreviation in the hash table.
	 * It concaticates the two strings together appropriately for a key
	 * @param fName	Valid feature name
	 * @param sAbriv	Valid state abriviation
	 * @return		A vector of HashNameIndexes that have the matching key. Returns null if nothing is found.
	 */
	public Vector<HashNameIndex> find(String fName, String sAbriv){
		String key = fName + ":" + sAbriv;
		return hashTable.find(key);
	}
	
	/**
	 * Returns a string that shows what the hashtable looks like.
	 */
	@Override
	public String toString(){
		return hashTable.toString();
	}
}
