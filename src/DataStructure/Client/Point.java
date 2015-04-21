/**
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * 
 * The object stored inside of a prquadtree.
 * 
 * Information for this can be found in the Compare2D class
 */
package DataStructure.Client;
import DataStructure.prQuadTree.*;

public class Point implements Compare2D<Point> {

   private double xcoord;
   private double ycoord;
   private long	  offset;
   
   public Point() {
      xcoord = 0;
      ycoord = 0;
      offset = 0;
   }

   public Point(double x, double y) {
	      xcoord = x;
	      ycoord = y;
	      offset = 0;
   }
   
   public Point(double x, double y, long offset) {
      xcoord = x;
      ycoord = y;
      this.offset = offset;
   }

   public double getX() {
      return xcoord;
   }

   public double getY() {
      return ycoord;
   }
   
   public long getOffset(){
	   return offset;
   }
   
   public Direction directionFrom(double X, double Y) { 
      if((X < xcoord && Y <= ycoord) || 
    		  (X == xcoord && Y == ycoord)){
    	  return Direction.NE;
      }
      else if(X >= xcoord && Y < ycoord){
    	  return Direction.NW;
      }
      else if(X > xcoord && Y >= ycoord ){
    	  return Direction.SW;
      }
      else{
    	 return Direction.SE; 
      }
   }
   
   public Direction inQuadrant(double xLo, double xHi, 
                               double yLo, double yHi) { 
      if(!inBox(xLo, xHi, yLo, yHi)){
    	  return Direction.NOQUADRANT;
      }
      
	  double centerX = (xLo + xHi) / 2;
      double centerY =  (yLo + yHi) / 2;
      return directionFrom(centerX, centerY);
   }

   public boolean inBox(double xLo, double xHi, 
                          double yLo, double yHi) { 
	   if(xLo <= xcoord && xHi >= xcoord && yLo <= ycoord && yHi >= ycoord){
		   return true;
	   }
	   else{
		   return false;
	   }
   }

   public String toString() {
      return "(" + Double.toString(xcoord) + ", " + Double.toString(ycoord) + "), " + Long.toString(offset);
   }
   
   // prints out only the coordinates for the Point
   public String coordinatedToString(){
	   return "(" + Double.toString(xcoord) + ", " + Double.toString(ycoord) + ")";
   }
    
   
   public boolean equals(Object o) { 
	   // Make sure there really IS another object:
	   if ( o == null ) return false;
	   // Make sure it's of the correct type:
	   if ( !this.getClass().equals(o.getClass()) ){
		   return false;
	   }
	   Point handle = (Point) o;
	   if(xcoord == handle.getX() && ycoord == handle.getY() && offset == handle.getOffset()){
		   return true;
	   }
	   return false;
   }
}

