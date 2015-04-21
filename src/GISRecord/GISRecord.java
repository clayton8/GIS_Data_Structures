/**
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * 
 * This contains all the information for a GIS record.
 * It makes it easy to access any information needed about a GIS record.
 */

package GISRecord;

public class GISRecord {
	
	long 		offset;
	String 		recordLine;
	
	int 		fid;
	String 		featureName;
	String		featureClass;
	String 		alphStateCode;
	String 		stateNumCode;
	String 		countyName;
	String		countyNum;
	Location 	primeLocation;
	String		primeLat;
	String 		primeLong;
	String		primeLatDec;
	String		primeLongDec;
	String 		sourceLat;
	String		sourceLong;
	String		sourceLatDec;
	String		sourceLongDec;
	String 		elevM;
	String		elevF;
	String		mapName;
	String		dateCreated;
	String 		dateEdit;
	
	
	int 	FeatureElevation;
	
	public GISRecord(long offset, String gisLine){
		this.offset = offset;
		recordLine = gisLine;
		setInformation(gisLine);
	}
	
	/**
	 * Fills in all the private variables for a GIS record except for the offset and recordLine.
	 * @param line	GIS record in the specified form.
	 */
	private void setInformation(String line){
		String[] info = line.split("\\|");
		fid 			= Integer.parseInt(info[RecordLocations.FID.getValue()]);
		featureName 	= info[RecordLocations.FName.getValue()];
		featureClass		= info[RecordLocations.FClass.getValue()];
		alphStateCode 	= info[RecordLocations.StateAlphCode.getValue()];
		stateNumCode 	= info[RecordLocations.StateNumCode.getValue()];
		countyName		= info[RecordLocations.CountyName.getValue()];
		countyNum		= info[RecordLocations.CountyNumCode.getValue()];
		Latitude lat 	= new Latitude(info[RecordLocations.PrimeLat.getValue()]);
		Longitude lon	= new Longitude(info[RecordLocations.PrimeLong.getValue()]);
		primeLocation	= new Location(lon, lat);
		primeLat		= info[RecordLocations.PrimeLat.getValue()];
		primeLong		= info[RecordLocations.PrimeLong.getValue()];
	
		primeLatDec 	= info[RecordLocations.PrimeLatDec.getValue()];
		primeLongDec	= info[RecordLocations.PrimeLongDec.getValue()];
 		sourceLat		= info[RecordLocations.SourceLat.getValue()];
		sourceLong		= info[RecordLocations.SourceLong.getValue()];
		sourceLatDec	= info[RecordLocations.SourceLatDec.getValue()];
		sourceLongDec 	= info[RecordLocations.SourceLongDec.getValue()];
 		elevM			= info[RecordLocations.ElevationM.getValue()];
		elevF			= info[RecordLocations.ElevationF.getValue()];
		mapName			= info[RecordLocations.MapName.getValue()];
		dateCreated		= info[RecordLocations.Created.getValue()];
		
		
		if(info.length == RecordLocations.Edited.getValue() + 1){
			dateEdit		= info[RecordLocations.Edited.getValue()];
		}else
		{
			dateEdit = null;
		}
 		

	}
	
	public long getOffset(){
		return offset;
	}
	
	public double getX(){
		return primeLocation.getX();
	}
	
	public double getY(){
		return primeLocation.getY();
	}
	
	public String getCountyName(){
		return countyName;
	}
	
	public String getLat(){
		return primeLat;
	}
	
	public String getLong(){
		return primeLong;
	}
	
	@Override
	public String toString(){
		return recordLine;
	}
	
	public String toStringWithOffset(){
		return offset + ":   " + recordLine;
	}
	
	public void setOffset(long newOffset){
		offset = newOffset;
	}
	
	public String getFeatureName(){
		return featureName;
	}
	
	public String getStateAbriviation(){
		return alphStateCode;
	}
	
	/**
	 * Give a nicely formated string with no empty fields included
	 * @return	Returns a string with none of the empty fields
	 */
	public String toStringUsedFields(){
		String outputString = "Feature ID\t\t\t:   "+ fid + "\n"
						+ "Feature Name\t\t:   "+ featureName + "\n"
						+ "Feature Class\t\t:   " + featureClass + "\n"
						+ "State\t\t\t\t:   "+ alphStateCode + "\n"
						+ "County Name\t\t\t:   "+ countyName + "\n"
						+ "Latitude\t\t\t:   " + primeLat + "\n"
						+ "Longitude\t\t\t:   " + primeLong + "\n";

		if(elevF != null && !elevF.equals("")){
			outputString += "Elevation in Feet\t:   " + elevF + "\n";
		}
		
		outputString += "Map Name\t\t\t:   " + mapName + "\n"
					+ "Date Created\t\t:   " + dateCreated + "\n";
		
		if(dateEdit != null){
			outputString += "Date Modified\t\t:   " + dateEdit + "\n";
		}
						
		return outputString;				
						
			
		
	}
}
