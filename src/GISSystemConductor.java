/**
 * 
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * This is what executes the program. 
 * 
 * It contains:
 * 
 * commandBufferReader:
 * 		How the conductor reads in the commands to execute.
 * 
 * logFile:
 * 		What the conductor writes its output to.
 * 
 * nameInx:
 * 		Layer that contains the Hash Table of the gis records
 * 
 * coordIndex:
 * 		Layer that contains the PR Quad Tree
 * 
 * buffPool:
 * 		Layer between the database file and the conductor.
 * 
 * When the run() is called it goes through the commandBufferReader
 * and grabs a command one at a time and then executes that command and prints it 
 * off in the logFile.
 */
import gisTools.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.Vector;

import DataStructure.BufferPool.BufferPool;
import DataStructure.Client.HashNameIndex;
import DataStructure.Client.Point;
import DataStructure.prQuadTree.prQuadTree;
import GISRecord.GISRecord;
import GISRecord.Latitude;
import GISRecord.Longitude;


public class GISSystemConductor {

	RandomAccessFile databaseFile;
	BufferedReader commandBuffReader;
	FileWriter logFile;
	nameIndex  nameInx;
	CoordinateIndex coordIndex;
	BufferPool buffPool;
	boolean imported;
	int commandNumber;
	
	GISSystemConductor(RandomAccessFile databaseFile, FileReader commandFile, FileWriter logFile) throws FileNotFoundException, SecurityException{
		this.databaseFile = databaseFile;
		commandBuffReader = new BufferedReader(commandFile);
		this.logFile = logFile;
		buffPool = new BufferPool(10, databaseFile);
		imported = false;
		commandNumber = 0;
	}
	
	/**
	 * Uses the getCommand(int cmdNumber) function to grab a new command
	 * if the command is not empty it then calls executeCommand(Command) to 
	 * execute and print the output of the command to the log file.
	 * @throws IOException	If there was an issue reading the file.
	 */
	public void run() throws IOException{
		Command cmd = null;
		int cmdNumber = 1;
		
		while((cmd = getCommand(cmdNumber)) != null)
		{
			cmdNumber++;
			executeCommand(cmd);	
		}	
		
		
	}
	
	/**
	 * This function figures out what type of command is passed in and executes
	 * it accordingly. It constructs a string and then prints it to the logFile.
	 * @param cmd		Command object to be executed. It must not be null
	 * @throws IOException	If there is an error reading or writing to a file.
	 */
	private void executeCommand(Command cmd) throws IOException{
		String outputString = "";
		if(cmd.getCommand().equals("world")){
			
			
			// Initialize the world
			Longitude xMin = new Longitude(cmd.getCommandInfo().get(1));
			Longitude xMax = new Longitude(cmd.getCommandInfo().get(2));
			Latitude yMin = new Latitude(cmd.getCommandInfo().get(3));
			Latitude yMax = new Latitude(cmd.getCommandInfo().get(4));
			
			// Construct the nameIndex and the coordinateIndex with the appropriate values
			nameInx = new nameIndex();
			coordIndex = new CoordinateIndex( xMin.get(), xMax.get(), yMin.get(), yMax.get());
			
			// Creates a nicely formated string for the output to show what has been created for the
			// project
			outputString += cmd.toString();
			outputString += "\n\n\nGIS Program\n\n";
			outputString += "Start time:\t" + LocalDateTime.now() + "\n\n";
			outputString += "---------------------------------------------------------\n\n";
			outputString += "The world created looks like this (it has been constructed into seconds):\n\n";
			outputString += "\t\t\t" + yMax.get() + "\n\n\t"+ xMin.get() + "\t\t" + xMax.get() + "\n\n\t\t\t" + yMin.get() + "\n\n";
			
		}else if(cmd.getCommand().equals("import")){
			// Creates a nicely formated string for the output
			outputString += cmd.toString() + "\n\n";
			
			// Imports every file that is contained within the specified world 
			// into the nameIndex, CoordinateIndex, and the dataBase file. It bypasses the
			// BufferPool for this
			outputString += importFile(cmd.getCommandInfo().get(1));
			
			// Must note that an import has taken place so if you import another file
			// the first line in the database file will not be imported because it is 
			// an example line, not actual data.
			imported = true;
		}else if(cmd.getCommand().equals("what_is_at")){
			// Use the coodIndex to find all of the features at a particular point.
			Vector<Point> foundFeatures = coordIndex.find(cmd.getCommandInfo().get(1), cmd.getCommandInfo().get(2));
			
			// Creates a nicely formatted string for the log file
			outputString += cmd.toString() + "\n\n\t";
			
			if(foundFeatures.size() > 0){
				// If there were features found we do this format
				outputString += "The following features were found at " +  cmd.getCommandInfo().get(1) + "\t" +   
							cmd.getCommandInfo().get(2) + "\n\n";
				
				// Add all the found items to the string to print off, go to the bufferPool to get
				// the needed record info for this command.
				for(Point x: foundFeatures){
					GISRecord newRecord = buffPool.getRecord(x.getOffset());
					
					outputString += x.getOffset() + ":\t" + newRecord.getFeatureName() + "\t" 
									+ newRecord.getCountyName() + "\t"
									+ newRecord.getStateAbriviation() + "\n";
				}
			}
			else{
				// If nothing was found we say nothing was found
				outputString += "No features found at " +  cmd.getCommandInfo().get(1) + "\t" +   
						cmd.getCommandInfo().get(2) + "\n";
			}
			
		}else if(cmd.getCommand().equals("what_is")){
			// Use the NameIndex to find all the features with a particular name and state abbreviation
			Vector<HashNameIndex> infoFound = nameInx.find(cmd.getCommandInfo().get(1), cmd.getCommandInfo().get(2));
			
			// Create a nicely formated string for the output
			outputString += cmd.toString() + "\n\n\t";
			if(infoFound != null){
				// Feature found
				outputString += infoFound.size() + " features were found that matched " + cmd.getCommandInfo().get(1)
								+ " " +  cmd.getCommandInfo().get(2) + ":\n\n";
				// Go to buffer pool to get record and get the offset, county name, lat and long for the logFile
				for(HashNameIndex x : infoFound){
					GISRecord newRecord = buffPool.getRecord(x.getOffset());
					outputString += x.getOffset() + ":\t" + newRecord.getCountyName() + "\t"+ newRecord.getLat() + "\t"
									+ newRecord.getLong()+"\n";
				}
			}
			else{
				// If nothing found say no records match
				outputString += "No record found matching " + cmd.getCommandInfo().get(1) + " " + cmd.getCommandInfo().get(2);
			}
			
			
		}else if(cmd.getCommand().equals("what_is_in")){
			// Use the quad tree to find all of the points within a particular region
			Vector<Point> infoFound = coordIndex.find(cmd.getCommandInfo().get(1), cmd.getCommandInfo().get(2),cmd.getCommandInfo().get(3),cmd.getCommandInfo().get(4));

			// Create the nicely formated string for the log file
			outputString += cmd.toString() + "\n\n\t";
			if(infoFound.size() > 0){
				// A record was found
				outputString += "The following " + infoFound.size() + " features were found in ("
						+ cmd.getCommandInfo().get(1) + " +/- " + cmd.getCommandInfo().get(3) + ", " + cmd.getCommandInfo().get(2) + " +/- " 
						+ cmd.getCommandInfo().get(4) + ")\n\n";
				
				// Go to the bufferPool for each record and get the feature name, state abbreviation, lat, and long.
				for(Point x: infoFound){
					GISRecord newRecord = buffPool.getRecord(x.getOffset());
					outputString += x.getOffset() + ":\t" + newRecord.getFeatureName() + "\t" + newRecord.getStateAbriviation()
									+ "\t" + newRecord.getLat() + "\t" + newRecord.getLong() + "\n";
				}
			}
			else{
				// No record was found
				outputString += "No Records were found in ("
						+ cmd.getCommandInfo().get(1) + " +/- " + cmd.getCommandInfo().get(3) + ", " 
						+ cmd.getCommandInfo().get(2) + " +/- " 
						+ cmd.getCommandInfo().get(4) + ")\n\n";
			}
			
		}else if(cmd.getCommand().equals("what_is_in\t-l")){
			// Find all the features 
			Vector<Point> infoFound = coordIndex.find(cmd.getCommandInfo().get(2), cmd.getCommandInfo().get(3),cmd.getCommandInfo().get(4),cmd.getCommandInfo().get(5));
			
			// Create nicely formated string for logFile.
			outputString += cmd.toString() + "\n\n\t";
			if(infoFound.size() > 0){
				// record found
				outputString += "The following " + infoFound.size() + " features were found in ("
						+ cmd.getCommandInfo().get(2) + " +/- " + cmd.getCommandInfo().get(4) + ", " + cmd.getCommandInfo().get(3) + " +/- " 
						+ cmd.getCommandInfo().get(5) + ")\n\n";
				
				// Go to the database file to get the the GIS record and get all the important fields.
				for(Point x: infoFound){
					GISRecord newRecord = buffPool.getRecord(x.getOffset());
					outputString += newRecord.toStringUsedFields() + "\n";
				}
			}
			else{
				outputString += "No records were found in ("
						+ cmd.getCommandInfo().get(2) + " +/- " + cmd.getCommandInfo().get(4) + ", " 
						+ cmd.getCommandInfo().get(3) + " +/- " 
						+ cmd.getCommandInfo().get(5) + ")\n\n";
			}
			
		}else if(cmd.getCommand().equals("what_is_in\t-c")){
			Vector<Point> infoFound = coordIndex.find(cmd.getCommandInfo().get(2), cmd.getCommandInfo().get(3),cmd.getCommandInfo().get(4),cmd.getCommandInfo().get(5));
		
			// Create nicely formated string for the logFile
			outputString += cmd.toString() + "\n\n\t";
			if(infoFound.size() > 0){
				// record found
				outputString += infoFound.size() + " features were found in ("
					+ cmd.getCommandInfo().get(2) + " +/- " + cmd.getCommandInfo().get(4) 
					+ ", " + cmd.getCommandInfo().get(3) + " +/- " 
					+ cmd.getCommandInfo().get(5) + ")\n\n";
			}
			else{
				// no records found
				outputString += "No records were found in ("
						+ cmd.getCommandInfo().get(2) + " +/- " + cmd.getCommandInfo().get(4) + ", " 
						+ cmd.getCommandInfo().get(3) + " +/- " 
						+ cmd.getCommandInfo().get(5) + ")\n\n";
			}
			
			
			
		}else if(cmd.getCommand().equals("debug")){

			// Put the command down and then print out one of three of the
			// data structures depending on the second argument.
			outputString += cmd.toString() + "\n\n";
			
			if(cmd.getCommandInfo().get(1).equals("hash")){
				outputString += nameInx.toString();
			}
			else if(cmd.getCommandInfo().get(1).equals("pool")){
				outputString += buffPool.toString();
			}
			else{
				outputString += coordIndex.toString();
			}
		}else if(cmd.getCommand().equals("quit")){
			
			// Terminate the program and print out the finished time for the logFile.
			outputString += cmd.toString() + "\n\nTerminating execution of commands."
					+ "\nEnd Time: " + LocalDateTime.now();
		}else{
			outputString += "\n\n\nERROR\n\n\n";
		}
		
		
		outputString += "\n\n---------------------------------------------------------\n\n";
		logFile.write(outputString);
		
	}
	
	/**
	 * This is what reads through the command file to get a new command
	 * It grabs all of the comments before a command then once it finds a 
	 * command it constructs a command object with the comments and the command and
	 * returns it.
	 * @param cmdNumber	The command number for the file. This does not keep track of the command number it must be passed in.
	 * @return	Command object containing a valid command.
	 * @throws IOException	If there is a error reading the command file.
	 */
	private Command getCommand(int cmdNumber) throws IOException {
		String line = new String();
		Vector<String> cmdInfo = new Vector<String>();
		while (commandBuffReader.ready()) {
			  line = commandBuffReader.readLine();
			  cmdInfo.add(line);
			  if(line.charAt(0) != ';'){
				  return new Command(cmdInfo, cmdNumber);
			  }
			}
		return null;
	}
	
	/**
	 * Executes the command of importing a file and returns the output
	 * information when importing. ie max number of 
	 * @param fileName	String containing a file name to be imported
	 * @return	A string of containing information about when it imported the file, or if there was an error.
	 */
	private String importFile(String fileName){
		try {
			FileReader fileIn = new FileReader(fileName);
			BufferedReader fileBuffer = new BufferedReader(fileIn);
			String line = new String();
			
			long locationsFound = 0;
			long longProbeSeq = 0;
			long validLocations = 0;
			
			// Read in first line not containing any GIS data.
			line = fileBuffer.readLine();
			// If we have not already imported a record we write this line to the database file.
			if(imported == false){
				databaseFile.writeBytes(line + "\n");
			}
			
			// Go through every file in the file and check to see if it fits in the 
			// world and if it does then add it to the nameIndex and the database file.
			while(fileBuffer.ready()){
				locationsFound++;
				line = fileBuffer.readLine();
				long offset = databaseFile.getFilePointer();
				GISRecord newRecord = new GISRecord(offset,line);
				// Check to see if the record fit in the world.
				if(coordIndex.add(newRecord)){
					validLocations++;
					databaseFile.writeBytes(newRecord.toString() + "\n");
					long probe = nameInx.add(newRecord);
					// Update the longest probe sequence.
					if(probe > longProbeSeq){
						longProbeSeq = probe;
					}
				}
			}
			
			String outputString = "Locations Found:\t" + locationsFound +"\nLongest Probe:\t\t" + longProbeSeq + "\nValid Locations:\t" + validLocations;
			return outputString;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return "Error: Could not find file "  + fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "Error: IO Exception with "  + fileName;
		}
	}
	
}
