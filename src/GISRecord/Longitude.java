package GISRecord;

/** 
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 */


/**
 * Longitude:
 * This class contains all the information for longitude. It has the day, minute, second, 
 * cardinal direction, and whether the inputed longitude is known. It makes it easy to 
 * construct a longitude and then get any information you want with a simple function call.
 */
public class Longitude {
	private double value;
	private boolean known;
	
	
	public Longitude(){
		value = 0;
		known = false;
	}
	
	/**
	 * This is the constructor to make a new Longitude object. If the longitude is unknown
	 * then all the int values will be set to 0, the direction will be null and the known will be
	 * false. It is assumed that the input string will either read: 
	 * 
	 * UKNOWN or 'DDDMMSSd'
	 * 
	 * @param longitudeString 	This is the string that holds the longitude information of either UNKNOWN
	 * 							or 'DDMMSSd'
	 */
	public Longitude(String longitudeString)
	{
		if(!longitudeString.equals("Unknown")){
			known = true;
			
			int degree = Integer.parseInt(longitudeString.substring(0, 3));
			int minute = Integer.parseInt(longitudeString.substring(3, 5));
			int second = Integer.parseInt(longitudeString.substring(5, 7));
			if(longitudeString.contains("W"))
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
	 * This formats the information for longitude in the format:
	 * 
	 * DDd MMm SSs direction
	 * 
	 * or if the value of the longitude is unknown then it returns 'UNKNOWN'
	 * 
	 * @return	Returns a string of the information in the specified format from the project description.
	 */
	@Override
	public String toString(){
		String returnLongitude = null;
		/*if(known){
			returnLongitude = Integer.toString(day) + "d  " + Integer.toString(minute) + "m  " + 
					Integer.toString(second) + "s  " + direction; 
		}
		else{
			returnLongitude = "UNKNOWN";
		}*/
		return returnLongitude;
	}
	
}
