/**
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * 
 * Data Structure used to act a s a buffer between a file.
 * 
 * Contains a linked list of a specified size to contain GISRecords for quick access.
 * 
 * Uses MRU method.
 */

package DataStructure.BufferPool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;

import GISRecord.GISRecord;

public class BufferPool {

	int maxSize;
	LinkedList<GISRecord> buffer;
	RandomAccessFile file;
	
	public BufferPool(){
		maxSize = 10;
		buffer = new LinkedList<GISRecord>();
	}
	

	public BufferPool(int buffSize, String filePath) throws FileNotFoundException, SecurityException{
		maxSize = buffSize;
		buffer = new LinkedList<GISRecord>();
		file = new RandomAccessFile(filePath, "r");
	}
	
	public BufferPool(int buffSize, RandomAccessFile raFile) throws FileNotFoundException, SecurityException{
		maxSize = buffSize;
		buffer = new LinkedList<GISRecord>();
		file = raFile;
	}
	
	/**
	 * Returns the GISRecord for the given offset. If it is in the buffer pool
	 * it will not go to the file to retrieve it.
	 * @param offset The number of the start of the GIS record in the database file.
	 * @return	The GIS record if found. If not found return null.
	 * @throws IOException 
	 */
	public GISRecord getRecord(long offset) throws IOException{
		// Cecks to see if the record is in the buffer
		GISRecord newRecord = checkBuffer(offset);
		// If not it goes to the file to find the record
		if(newRecord == null){
			newRecord = getRecordFile(offset);	
		}
		
		return newRecord;
	}
	
	/**
	 * Checks the buffer for a particular offset to see if it is contained and if it is
	 * then it returns the GISRecord and then reorders the buffer to move that buffer to the top of the
	 * buffer.
	 * @param offset
	 * @return	GISRecord or null
	 */
	private GISRecord checkBuffer(long offset){
		GISRecord foundRecord = null;
		for(GISRecord x: buffer){
			if(x.getOffset() == offset){
				foundRecord = x;
				reorderBuffer(x, true);
				break;
			}
		}
		return foundRecord;
	}
	
	/**
	 * Appends GIS object to the front of the buffer
	 * @param newRecord record to be added to the buffer
	 * @param inBuffer whether the record was already in the buffer or not
	 */
	private void reorderBuffer(GISRecord newRecord, boolean inBuffer){
		if(inBuffer){
			recordInBufferReorder(newRecord);
		}else{
			newRecordReorder(newRecord);
		}
	}
	
	/**
	 * Inserts reorder the buffer to have the found record in the front of the record.
	 * @param record Record to be placed in the front of the buffer pool
	 */
	private void recordInBufferReorder(GISRecord record){
		for(int i = 0; i < buffer.size(); i++){
			GISRecord bufRecord = buffer.get(i);
		    if (bufRecord.getOffset() == record.getOffset()) {
		        buffer.remove(i);
		        buffer.addFirst(bufRecord);
		        break;
		    }
		}
	}
	
	/**
	 * Adds in the new GISRecord to the start of the linked list
	 * and if the buffer > maxSize then it deletes the last
	 * GISRecord.
	 * @param newRecord GISRecord to be added to the buffer
	 */
	private void newRecordReorder(GISRecord newRecord){
		buffer.addFirst(newRecord);
		if(buffer.size() > maxSize){
			buffer.removeLast();
		}
	}

	/**
	 * Searches though the saved file for the record at a particular offset
	 * @param offset	Start of like offset for GISRecord
	 * @return			Either a GISRecord or an ErrorGISRecord if it wasn't found.
	 * @throws IOException	If there was an issue reading the file.
	 */
	private GISRecord getRecordFile(long offset) throws IOException{
		
		file.seek(offset);
		String line = file.readLine();
		GISRecord recordFound = new GISRecord(offset, line);
		reorderBuffer(recordFound, false);
		return recordFound;
		
	}
	
	/**
	 * Returns a nicely formated string of what is in the buffer from MRU to LRU
	 */
	@Override
	public String toString(){
		String outputString = "MRU\n";
		for(GISRecord x: buffer){
			outputString += x.toStringWithOffset() + "\n";
		}
		outputString += "LRU";
		
		return outputString;
		
	}
	
}
