package GISRecord;

/** 
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 */

/**
 * Latitude:
 * This class contains all the information for latitude. It has the day, minute, second, 
 * cardinal direction, and whether the inputed latitude is known. It makes it easy to 
 * construct a latitude and then get any information you want with a simple function call.
 */

public class Latitude {
	private double value;
	private boolean known;
	
	
	public Latitude(){
		value = 0;
		known = false;
	}
	
	/**
	 * This is the function that gives the day value of a Latitude object, it is expected to be 
	 * initialized.
	 * @return	Int containing the day value.
	 */
	public Latitude(String latitudeString){
			if(!latitudeString.equals("Unknown")){
				known = true;
				
				
				int degree = 0;
				try{
					degree = Integer.parseInt(latitudeString.substring(0, 2));
				}catch(NumberFormatException e){
					System.out.println(latitudeString);
				}
				int minute = Integer.parseInt(latitudeString.substring(2, 4));
				int second = Integer.parseInt(latitudeString.substring(4, 6));
				if(latitudeString.contains("S"))
				{
					value = - (second + minute*60 + degree*3600);
					
				}
				else
				{
					value = second + minute*60 + degree*3600;
				}
			}
			else{
				known = false;
				value = 0;
			}
		}
	
	public double get(){
		return value;
	}
	
	
	
	/**
	 * Function that returns a boolean to say if the latitude was UNKNOWN or if it was known.
	 * @return	If the input string to the constructor was 'UNKNOWN' then know is false, otherwise
	 * 			it is true.
	 */
	public boolean latitudeKnown(){
		return known;
	}
	
	/**
	 * This formats the information for latitude in the format:
	 * 
	 * DDd MMm SSs direction
	 * 
	 * or if the value of the latitude is unknown then it returns 'UNKNOWN'
	 * 
	 * @return	Returns a string of the information in the specified format from the project description.
	 */
	public String getAllLatitude(){
		String returnLatitude = null;
		/*if(known){
			returnLatitude = Integer.toString(day) + "d  " + Integer.toString(minute) + "m  " + 
					Integer.toString(second) + "s  " + direction; 
		}
		else{
			returnLatitude = "UNKNOWN";
		}*/
		return returnLatitude;
	}
}
