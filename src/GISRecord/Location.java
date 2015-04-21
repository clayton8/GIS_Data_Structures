/** 
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 */


/**
 * Location:
 * This class contains a Latitude and Longitude object and makes it easy to get an X and Y point for a
 * particular object.
 */


package GISRecord;

public class Location  {

   private Latitude ycoord;
   private Longitude xcoord;
   
   public Location() {
      xcoord = new Longitude();
      ycoord = new Latitude();
   }

   public Location(Longitude x, Latitude y) {
      xcoord = x;
      ycoord = y;
   }

   public double getX() {
      return xcoord.get();
   }

   public double getY() {
	   return ycoord.get();
   }
   
   @Override
   public String toString() {
      return "(" + Double.toString(xcoord.get()) + ", " + Double.toString(ycoord.get()) + ")";
   }
   
   
   public boolean equals(Object o) { 
	   // Make sure there really IS another object:
	   if ( o == null ) return false;
	   // Make sure it's of the correct type:
	   if ( !this.getClass().equals(o.getClass()) ){
		   return false;
	   }
	   Location handle = (Location) o;
	   if(xcoord.get() == handle.getX() && ycoord.get() == handle.getY()){
		   return true;
	   }
	   return false;
   }
}

