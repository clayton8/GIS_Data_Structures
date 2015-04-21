/** 
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 */


/**
 * Command:
 * This class contains all information about a particular command
 */

package gisTools;

import java.util.Vector;

public class Command {
	Vector<String> comments;
	String cmd;
	Vector<String> commandInfo;
	int number;

	public Command(Vector<String> cmdInfo, int cmdNumb){
		
		comments = cmdInfo;
		cmd = cmdInfo.lastElement();
		comments.remove(comments.size() -1 );
		commandInfo = new Vector<String>();
		number = cmdNumb;
		
		
		String[] splitCmd = cmd.split("\t");
		for(int i = 0; i < splitCmd.length; i++){
			commandInfo.add(splitCmd[i]);
		}	
	}
	
	public String  getCommand(){
		if(commandInfo.firstElement().equals("what_is_in") && commandInfo.size() > 5){
			return commandInfo.firstElement() + "\t" + commandInfo.get(1);
		}
		return commandInfo.firstElement();
	}
	
	public Vector<String> getCommandInfo(){
		return commandInfo;
	}
	
	public Vector<String> getComments(){
		return comments;
	}
	
	@Override
	public String toString(){
		String output = "";
		for(String x: comments){
			output += x + "\n";
		}
		output += "Command " + number + ":  " + cmd;
		return output;
	}
}
