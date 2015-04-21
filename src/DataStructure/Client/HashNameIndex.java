/**
 * 
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * 
 * Data structure stored inside of a Hash Table
 * 
 * It contains a feature name, state abbreviation, and the offset.
 */

package DataStructure.Client;

public class HashNameIndex {
	String featureName;
	String stateAbriviation;
	long offset;
	
	public HashNameIndex(String featureN, String stateA,long offset){
		featureName = featureN;
		stateAbriviation = stateA;
		this.offset = offset;
	}
	
	/**
	 * Gets the offset stored
	 * @return long of the offset stored
	 */
	public long getOffset(){
		return offset;
	}
	
	/**
	 * returns nicely formated string of all the private members.
	 */
	@Override
	public String toString(){
		return featureName + ":" + stateAbriviation + ", [" + offset + "]";
	}
}
