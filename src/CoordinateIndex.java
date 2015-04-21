/**
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * 
 * This is the class that is the layer between the GISSystemConductor and the prquadtree.
 */

import java.util.Vector;

import DataStructure.Client.Point;
import DataStructure.prQuadTree.prQuadTree;
import GISRecord.GISRecord;
import GISRecord.Latitude;
import GISRecord.Longitude;


public class CoordinateIndex {

	prQuadTree<Point> tree;
	
	CoordinateIndex(){
		tree = new prQuadTree<Point>(0, 100, 0, 100);
	}

	CoordinateIndex(double xMin, double xMax, double yMin, double yMax){
		tree = new prQuadTree<Point>(xMin, xMax, yMin, yMax);
	}
	
	/**
	 * Adds a GIS record to the tree and returns if it was successful or not. It gets the correct
	 * Information from the GISRecord to create a Point to be stored in the tree.
	 * @param record	Valid GISRecord
	 * @return		Whether the recored was stored or not.
	 */
	public boolean add(GISRecord record){
		Point GISPoint = new Point(record.getX(), record.getY(), record.getOffset());
		return tree.insert(GISPoint);
	}
	
	/**
	 * Executes the what_is_at command and is passed in coordinates. It constructs
	 * a latitude and longitude and uses that to find anything that is in that particular location
	 * @param lat	Latitude in DDMMSS format
	 * @param long	Longitude in DDDMMSS format 
	 * @return		A vector containing all the Points that are at that lat and long.
	 */
	public Vector<Point> find(String lat, String longitude){
		
		Latitude newLat = new Latitude(lat);
		Longitude  newLong = new Longitude(longitude);
		
		return tree.find(newLong.get(), newLong.get(), newLat.get(), newLat.get());
	}
	
	/**
	 * Finds any points within a particular box. It constructs a box and then finds the 
	 * max and min x and y coordinates to search inside the tree
	 * @param lat 		Center of the box to be searched
	 * @param longitude	Center of the box to be searched
	 * @param halfHeight	Half the height of the box
	 * @param halfLength	Half of the length of the box
	 * @return	A Vector containing all the points inside that box
	 */
	public Vector<Point> find(String lat, String longitude, String halfHeight, String halfLength){

		Latitude newLat= new Latitude(lat);
		Longitude newLong = new Longitude(longitude);
		int hHeight = Integer.parseInt(halfHeight);
		int hLength = Integer.parseInt(halfLength);
		// Find the max and min values for the box to be searched
		double xMin = newLong.get() - hLength;
		double xMax = newLong.get() + hLength;
		double yMin = newLat.get() - hHeight;
		double yMax = newLat.get() + hHeight;
		
		return tree.find(xMin, xMax, yMin, yMax);
	}
	
	/**
	 * return A nicely formated string for what the tree looks like.
	 */
	@Override
	public String toString(){
		return tree.toString();
	}
}
