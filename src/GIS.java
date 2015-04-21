//On my honor:
//I have not discussed the Java language code in my program with anyone other than my instructor or the teaching assistants assigned to this course.
//I have not used Java language code obtained from another student, or any other unauthorized source, either modified or unmodified.
//If any Java language code or documentation used in my program was obtained from another source, such as a text book or course notes, that has been clearly noted with a proper citation in the comments of my program.
//I have not designed this program in such a way as to defeat or interfere with the normal operation of the Curator System.
//Clayton Kuchta

/**
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * 
 * This is the main class for the GIS Project. This project takes in three 
 * arguments:
 * 
 * <database file name> <command script file name> <log file name>
 * 
 * Where the database file is where you store imported data. The command script
 * bides by the commands specified in the project description
 * and the log file is the output of this project. 
 * 
 * 
 * Overall Design:
 * 		The overall design is that the main checks and opens all the inputed files. Then 
 * 		it constructs a GISSystemConductor object and then it runs.
 * 
 * nameIndex:
 * 		The nameIndex is the layer between the condutorin the hash table.
 * 
 * CoordinateIndex:
 * 		The CoordinateIndex is the layer between the conductor and the pr quad tree.
 * 
 * BufferPool:
 * 		There is also i buffer pool data object that is the layer between the database file
 * 		and the conductor.
 * 
 * DataStructure Element Wrappers:
 * 		All wrapper classes that are stored in the hash table and the quad tree can be found in the package
 * 		DataStructures.Client. 
 * 
 * Point:
 * 		This is what is stored in a quad tree.
 * 
 * HashNameIndex:
 * 		This is what is stored in the hash table.
 * 
 * GISRecord:
 * 		When a GIS record is read in it is converted into a GIS Record to easily get all the information
 * 		from the record without having to split the string every time you want data.
 * 
 * Package gisTools:
 * 		This package contains the Command class which holds the information about a command. It makes any information about
 * 		a command easily accessible for the conductor.
 * 
 */


import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import error.ErrorMessage;


public class GIS {
	
	
	private static final int RandomAccessFile = 0;

	public static void main(String[] args) throws IOException{		
		
		// Check the correct amount of arguments
		if (args.length < 3){
			ErrorMessage.errorMessage("Not enough arguments");
			return;
		}
		
		// Make database file
		File databaseFile = new File(args[0]);
		RandomAccessFile databaseRAF;
		if(!databaseFile.createNewFile()){
			databaseRAF = new RandomAccessFile(databaseFile, "rw");
			databaseRAF.setLength(0);
		}else{
			databaseRAF = new RandomAccessFile(databaseFile, "rw");
		}
		
		
		// Check for command file
		File commandFile = new File(args[1]);
		if(!commandFile.exists()){
			ErrorMessage.errorMessage("Command file not found " + commandFile.getName());
			return;
		}
		FileReader commandFR = new FileReader(commandFile);
		
		
		// Check and create a logFile
		File logFile	= new File(args[2]);
		FileWriter logRAF;
		if(!databaseFile.createNewFile()){
			logRAF = new FileWriter(logFile);
			logRAF.write("");
		}else{
			logRAF = new FileWriter(logFile);
		}
		
		// Construct the conductor
		GISSystemConductor conductor = new GISSystemConductor(databaseRAF, commandFR, logRAF);
		
		
		// Execute the program.
		try{
			conductor.run();
		}catch(IOException e){
			System.out.println("ERROR:\t" + e.getMessage());
		}
		
		logRAF.close();
		
		
	}
}
