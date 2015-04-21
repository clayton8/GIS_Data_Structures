


/**
 * Creator: Clayton Kuchta
 * Project: GIS System
 * Last Modified: April. 15th, 2015
 * 
 * Description:
 * 
 *  BST<> provides a generic implementation of a PR Quadtree
 * 
 *   BST<> implementation constraints:
 *   - PR Quadtree does not allow duplicates according to equals
 *   - The element must adhere to the Compare2D API
 *   - All tree traversals are performed recursively.
 * 
 */
package DataStructure.prQuadTree;
import java.awt.geom.Rectangle2D;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Vector;

//The test harness will belong to the following package; the quadtree
//implementation must belong to it as well.  In addition, the quadtree
//implementation must specify package access for the node types and tree
//members so that the test harness may have access to it.
//

public class prQuadTree< T extends Compare2D<? super T> > {

	// You must use a hierarchy of node types with an abstract base
	// class.  You may use different names for the node types if
	// you like (change displayHelper() accordingly).
	abstract class prQuadNode { }

	class prQuadLeaf extends prQuadNode {
		Vector<T> Elements;

		public prQuadLeaf() {
			Elements = new Vector<T>();
		}

		public prQuadLeaf(T elem){
			Elements = new Vector<T>();
			Elements.add(elem);
		}

		public int numberIndexes(){
			Hashtable<String,T> indexElements = new Hashtable<String,T>();
			int numIndex = 0;
			for(T x: Elements){
				
				if(!indexElements.containsKey(x.coordinatedToString())){
					numIndex++;
					indexElements.put(x.coordinatedToString(), x);
				}
			}
			return numIndex;
		}
	}

	class prQuadInternal extends prQuadNode {
		prQuadNode NW, NE, SE, SW;

		public prQuadInternal() { 
		}
	}

	prQuadNode root;
	double xMin, xMax, yMin, yMax;
	boolean insertFlag;
	boolean deleteFlag;
	static int bucketSize = 4;

	// Initialize quadtree to empty state, representing the specified region.
	public prQuadTree(double xMin, double xMax, double yMin, double yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	// Pre:   elem != null
	// Post:  If elem lies within the tree's region, and elem is not already 
	//        present in the tree, elem has been inserted into the tree.
	// Return true iff elem is inserted into the tree. 
	public boolean insert(T elem) {
		
		insertFlag = false;
		if(elem.inBox(xMin, xMax, yMin, yMax) && elem != null)
		{

			root = insertHelper(elem, root, xMin, xMax, yMin, yMax);
		}
		return insertFlag;
	}

	// Pre:   elem != null, xMin, xMax, yMin, yMax create a square inside the quadtree region.
	// Post:  If element is not a duplicate it will insert the element into the tree.
	// Return prQuadNode for recursion. 
	private prQuadNode insertHelper(T elem, prQuadNode node, double xMin, double xMax, double yMin, double yMax){
		
		if(node == null){
			// There was an empty leaf we can put the element into
			insertFlag = true;
			return new prQuadLeaf(elem);
		}


		if( node.getClass().equals( new prQuadLeaf().getClass() ) ){
			// We need to split the node

			prQuadLeaf leafHandle = (prQuadLeaf) node;


			if(leafHandle.Elements.contains(elem)){
				// Failed to insert because there is a duplicate element in the tree		   
				return node;
			}

			leafHandle.Elements.addElement(elem);
			insertFlag = true;
			
			if(leafHandle.numberIndexes() > bucketSize){
				// Needs to split the bucket
				return splitHelper(leafHandle.Elements, xMin, xMax, yMin, yMax);
			}
		}else{

			// We have an internal node and need to keep searching		   
			prQuadInternal internalHandle = (prQuadInternal) node;
			Direction newDirect = elem.inQuadrant(xMin, xMax, yMin, yMax);
			if(newDirect == Direction.NE){
				Rectangle2D.Double newQuadrant = getNewQuadrant(newDirect, xMin, xMax, yMin, yMax);
				internalHandle.NE = insertHelper(elem, internalHandle.NE, newQuadrant.getMinX() , newQuadrant.getMaxX(), newQuadrant.getMinY(), newQuadrant.getMaxY());
			}
			else if(newDirect == Direction.NW){
				Rectangle2D.Double newQuadrant = getNewQuadrant(newDirect, xMin, xMax, yMin, yMax);
				internalHandle.NW = insertHelper(elem, internalHandle.NW, newQuadrant.getMinX() , newQuadrant.getMaxX(), newQuadrant.getMinY(), newQuadrant.getMaxY());
			}
			else if(newDirect == Direction.SE){
				Rectangle2D.Double newQuadrant = getNewQuadrant(newDirect, xMin, xMax, yMin, yMax);
				internalHandle.SE = insertHelper(elem, internalHandle.SE, newQuadrant.getMinX() , newQuadrant.getMaxX(), newQuadrant.getMinY(), newQuadrant.getMaxY());
			}
			else{
				Rectangle2D.Double newQuadrant = getNewQuadrant(newDirect, xMin, xMax, yMin, yMax);
				internalHandle.SW = insertHelper(elem, internalHandle.SW, newQuadrant.getMinX() , newQuadrant.getMaxX(), newQuadrant.getMinY(), newQuadrant.getMaxY());
			}


		}
		return node;
	}

	// Pre:   nodeElements != null, xMin, xMax, yMin, yMax are a rectangle within the quadtree.
	// Post:  Will split the quad if needed.
	// Return prQuadNode for recursion.
	private prQuadNode splitHelper(Vector<T> nodeElements, double xMin, double xMax, double yMin, double yMax){

		prQuadInternal internalNode = new prQuadInternal();

		prQuadLeaf neLeaf = new prQuadLeaf();
		prQuadLeaf  seLeaf = new prQuadLeaf();;
		prQuadLeaf  nwLeaf = new prQuadLeaf();;
		prQuadLeaf  swLeaf = new prQuadLeaf();;
		Direction elemDirection;

		// Go through all the nodes passed in and see what quadrand of the current box it would go into.
		for(T x: nodeElements){
			elemDirection = x.inQuadrant(xMin, xMax, yMin, yMax);
			if(elemDirection == Direction.NE){
				neLeaf.Elements.add(x);
			}else if(elemDirection == Direction.NW){
				nwLeaf.Elements.add(x);
			}else if(elemDirection == Direction.SE){
				seLeaf.Elements.add(x);
			}else{
				swLeaf.Elements.add(x);
			}
		}

		// Now that we know how many elements in each quadrant we must see if there are too many in one bucket
		// and if so it needs to be split. Otherwise a new leaf is added to to the internal node and all the 
		// elements are added to that bucket's list.
		if(neLeaf.numberIndexes() > bucketSize){
			// NE needs to be split more because overflowing bucket
			Rectangle2D newQuad = getNewQuadrant(Direction.NE, xMin, xMax, yMin, yMax);
			
			internalNode.NE = splitHelper(neLeaf.Elements, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
		}else if( neLeaf.numberIndexes() > 0){
			// add to internal node pointer.
			internalNode.NE = neLeaf;	   
		}

		if(nwLeaf.numberIndexes() > bucketSize){
			// NW needs to be split more because overflowing bucket
			Rectangle2D newQuad = getNewQuadrant(Direction.NW, xMin, xMax, yMin, yMax);
			internalNode.NW = splitHelper(nwLeaf.Elements, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
			
		}else if( nwLeaf.numberIndexes() > 0){
			// add to internal node pointer.
			internalNode.NW = nwLeaf;
		}

		if(seLeaf.numberIndexes() > bucketSize){
			// SE needs to be split more because overflowing bucket
			Rectangle2D newQuad = getNewQuadrant(Direction.SE, xMin, xMax, yMin, yMax);
			internalNode.SE = splitHelper(seLeaf.Elements, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
		}else if( seLeaf.numberIndexes() > 0){
			// add to internal node pointer.
			internalNode.SE = seLeaf;
		}

		if(swLeaf.numberIndexes() > bucketSize){
			// SW needs to be split more because overflowing bucket
			Rectangle2D newQuad = getNewQuadrant(Direction.SW, xMin, xMax, yMin, yMax);
			internalNode.SW = splitHelper(swLeaf.Elements, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
		}else if( swLeaf.numberIndexes() > 0){
			// add to internal node pointer.
			internalNode.SW = swLeaf;
		}  
		return internalNode;
	}

	// Pre:  elem != null
	// Post: If elem lies in the tree's region, and a matching element occurs
	//       in the tree, then that element has been removed.
	// Returns true iff a matching element has been removed from the tree.
	public boolean delete(T Elem) {

		deleteFlag = false;
		if(Elem.inBox(xMin, xMax, yMin, yMax)){
			root = deleteHelper(Elem, root, xMin, xMax, yMin, yMax);
		}
		return deleteFlag;
	}

	// Pre:  elem != null, xMin, xMax, yMin, and yMax are a quadrant in the quadtree.
	// Post: If the node passed in is internal it will figure out which quadrant to go to
	//		next and then call deleteHelper with that new quadrant and assign the correct
	//		quadrant node the returned value of deletedHelper. It will then check to see if 
	// 		the internal node contains all null characters for its quadrants and if it does it will return 
	//       null to delete the node. Now if the node passed in is a leaf it will search the leaf to see
	//		if the element is contained and if it is then it will delete the element in the bucket and then check
	//		to see if the leaf is now empty, if it is empty it returns null and if it isn't empty it returns the leaf.
	// Returns Either the internal node, leaf node or null if that node needs to be deleted.
	private prQuadNode deleteHelper(T elem, prQuadNode node, double xMin, double xMax, double yMin, double yMax){
		if(node == null){
			return node;
		}

		// If check to see if it is an internal node
		if(node.getClass().equals(new prQuadInternal().getClass())){

			prQuadInternal internalHandle = (prQuadInternal) node;

			Direction newDirection = elem.inQuadrant(xMin, xMax, yMin, yMax);
			Rectangle2D newQuad = getNewQuadrant(newDirection, xMin, xMax, yMin, yMax);

			if(newDirection == Direction.NE){
				internalHandle.NE = deleteHelper(elem, internalHandle.NE, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
			}else if(newDirection == Direction.NW){
				internalHandle.NW = deleteHelper(elem, internalHandle.NW, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
			}else if(newDirection == Direction.SE){
				internalHandle.SE = deleteHelper(elem, internalHandle.SE, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
			}else{
				internalHandle.SW = deleteHelper(elem, internalHandle.SW, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
			}

			// Check to see if the internal node has empty quadrant nodes
			int numbLeafs = internalHandle.NE != null ? 1 : 0;
			numbLeafs += internalHandle.NW != null ? 1 : 0;
			numbLeafs += internalHandle.SE != null ? 1 : 0;
			numbLeafs += internalHandle.SW != null ? 1 : 0;

			if(numbLeafs == 0){
				return null;
			}else if(numbLeafs == 1){
				// going to check if the one node is a leaf and if so return that leaf node instead of an internal node
				if(internalHandle.NW != null && internalHandle.NW.getClass().equals(new prQuadLeaf().getClass())){
					return internalHandle.NW;
				}else if(internalHandle.NE != null && internalHandle.NE.getClass().equals(new prQuadLeaf().getClass())){
					return internalHandle.NE;
				}else if(internalHandle.SW != null && internalHandle.SW.getClass().equals(new prQuadLeaf().getClass())){
					return internalHandle.SW;
				}else if(internalHandle.SE != null && internalHandle.SE.getClass().equals(new prQuadLeaf().getClass())){
					return internalHandle.SE;
				}
			}

			// If there are more than one quadrant filled with leafs or if the quadrands filled are not leafs then return the internal
			// node 
			return internalHandle;

		}

		// If you reach here then it is a leaf node and you have check to see if it matches an element in a bucket
		prQuadLeaf leafHandle = (prQuadLeaf) node;

		for(int i = 0; i < leafHandle.Elements.size(); i++){   
			if(leafHandle.Elements.get(i).equals(elem)){
				deleteFlag = true;
				leafHandle.Elements.remove(i);
			}
		}

		// We have now removed the element

		// If the leaf is now completely empty of any elements return null
		if(leafHandle.Elements.size() < 1){
			return null;
		}

		// Leaf still contains elements in it so return the leaf with the elements
		return leafHandle;
	}

	// Pre:  elem != null
	// Returns reference to an element x within the tree such that 
	// elem.equals(x)is true, provided such a matching element occurs within
	// the tree; returns null otherwise.
	public T find(T Elem) {

		return findHelper(Elem, root, xMin, xMax, yMin, yMax);
	}

	// Pre:  elem != null, xMin, xMax, yMin, and yMax are a quadrant in the quadtree.
	// Recursive helper that returns reference to an element x within the tree such that 
	// elem.equals(x)is true, provided such a matching element occurs within
	// the tree; returns null otherwise.
	private T findHelper(T elem, prQuadNode node, double xMin, double xMax, double yMin, double yMax){
		if(node == null){
			return null;
		}

		if(node.getClass().equals( new prQuadInternal().getClass() )){
			// If it is still an internal node we must keep searching.
			prQuadInternal internalHandle = (prQuadInternal) node;
			Direction newDirection = elem.inQuadrant(xMin, xMax, yMin, yMax);
			Rectangle2D.Double newQuad = getNewQuadrant(newDirection , xMin, xMax, yMin, yMax);

			if(newDirection == Direction.NW){
				return findHelper(elem, internalHandle.NW, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
			}else if(newDirection == Direction.NE){
				return findHelper(elem, internalHandle.NE, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
			}else if(newDirection == Direction.SW){
				return findHelper(elem, internalHandle.SW, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
			}else{
				return findHelper(elem, internalHandle.SE, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
			}
		}

		// If we are here it must be a leaf node so we have to search for the element in the element.
		prQuadLeaf leafHandle = (prQuadLeaf) node;
		for (T x : leafHandle.Elements) {
			if(x.equals(elem)){
				return x;
			}
		}
		// A match was not found in the vector so return null
		return null;

	}

	// Pre:  xLo, xHi, yLo and yHi define a rectangular region
	// Returns a collection of (references to) all elements x such that x is 
	//in the tree and x lies at coordinates within the defined rectangular 
	// region, including the boundary of the region.
	public Vector<T> find(double xLo, double xHi, double yLo, double yHi) {
		Vector<T> vect = new Vector<T>();

		vect = findHelper(vect, root, xLo, xHi, yLo, yHi, this.xMin, this.xMax, this.yMin, this.yMax);

		return vect;      
	}

	// Pre:  xLoSearch, xHiSearch, yLoSearch and yHiSearch define a rectangular region.
	//		xLo, xHi, yLo, yHi define a quadrant in the tree.
	// Returns a collection of (references to) all elements x such that x is 
	// in the tree and x lies at coordinates within the defined rectangular 
	// region, including the boundary of the region.
	private Vector<T> findHelper(Vector<T> vect, prQuadNode node, double xLoSearch, double xHiSearch, double yLoSearch, double yHiSearch,double xLo, double xHi, double yLo, double yHi){
		if(node == null){
			return vect;
		}
		// If it is an internal node:
		if(node.getClass().equals(new prQuadInternal().getClass())){
			// Create Internal node handle
			prQuadInternal internalHandle = (prQuadInternal) node;
			// Create function to find quadrants to search
			Vector<Direction> vectQuadrants = getQuadrantsSearch(xLoSearch, xHiSearch, yLoSearch, yHiSearch, xLo, xHi, yLo, yHi);
			// Go to quadrants and populate your vector 
			for(Direction x: vectQuadrants){
				Rectangle2D.Double newQuad =  getNewQuadrant(x, xLo, xHi, yLo, yHi);
				if(x == Direction.NE){
					vect = findHelper(vect, internalHandle.NE, xLoSearch, xHiSearch, yLoSearch, yHiSearch, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
				}else if(x == Direction.NW){
					vect = findHelper(vect, internalHandle.NW, xLoSearch, xHiSearch, yLoSearch, yHiSearch, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
				}else if(x == Direction.SE){
					vect = findHelper(vect, internalHandle.SE, xLoSearch, xHiSearch, yLoSearch, yHiSearch, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
				}else if(x == Direction.SW){
					vect = findHelper(vect, internalHandle.SW, xLoSearch, xHiSearch, yLoSearch, yHiSearch, newQuad.getMinX(), newQuad.getMaxX(), newQuad.getMinY(), newQuad.getMaxY());
				}else{
					System.out.println("EEEEEEEERRRRRRRROOOOORRRRRRR");
				}
			}

			return vect;
		}

		// If you get here it is a leaf.
		prQuadLeaf leafHandle = (prQuadLeaf) node;
		for(T x: leafHandle.Elements){
			if(x.inBox(xLoSearch, xHiSearch, yLoSearch, yHiSearch)){
				vect.add(x);
			}
		}
		return vect;
	}

	// Pre:  xLoSearch, xHiSearch, yLoSearch and yHiSearch define a rectangular region.
	//		xLo, xHi, yLo, yHi define a quadrant in the tree.
	// Returns a collection of quadrants the given rectangle lives within.
	private Vector<Direction> getQuadrantsSearch(double xLoSearch, double xHiSearch, double yLoSearch, double yHiSearch, double xLo, double xHi, double yLo, double yHi){
		Vector<Direction> vectDirection = new Vector<Direction>();
		Rectangle2D.Double neQuad = getNewQuadrant(Direction.NE, xLo, xHi, yLo, yHi);
		Rectangle2D.Double nwQuad = getNewQuadrant(Direction.NW, xLo, xHi, yLo, yHi);
		Rectangle2D.Double seQuad = getNewQuadrant(Direction.SE, xLo, xHi, yLo, yHi);
		Rectangle2D.Double swQuad = getNewQuadrant(Direction.SW, xLo, xHi, yLo, yHi);



		if(checkInQuadrant(neQuad, xLoSearch, xHiSearch, yLoSearch, yHiSearch)){
			vectDirection.add(Direction.NE);
		}
		if(checkInQuadrant(nwQuad, xLoSearch, xHiSearch, yLoSearch, yHiSearch)){
			vectDirection.add(Direction.NW);
		}
		if(checkInQuadrant(seQuad, xLoSearch, xHiSearch, yLoSearch, yHiSearch)){
			vectDirection.add(Direction.SE);
		}
		if(checkInQuadrant(swQuad, xLoSearch, xHiSearch, yLoSearch, yHiSearch)){
			vectDirection.add(Direction.SW);
		}

		return vectDirection;
	}

	// Pre:  quadrant is a quadrant in the tree, xLo, xHi, yLo, yHi is a rectangular region
	// Returns whether or not the rectangular region lives within the quadrant
	private boolean checkInQuadrant(Rectangle2D.Double quadrant, double xLo, double xHi, double yLo, double yHi){
		if(( (xLo <= quadrant.getMinX() && xHi >= quadrant.getMinX() ) || (xHi >= quadrant.getMaxX() && xLo <= quadrant.getMaxX() ) || (xLo >= quadrant.getMinX() && xHi <= quadrant.getMaxX() ))
				&&
				((yLo <= quadrant.getMinY() && yHi >= quadrant.getMinY() ) || (yHi >= quadrant.getMaxY() && yLo <= quadrant.getMaxY() ) || (yLo >= quadrant.getMinY() && yHi <= quadrant.getMaxY()))){
			return true;
		}
		return false;
	}

	// Pre:  xMin, xMax, yMin and yMax define a rectangular region
	// Returns a 2D rectangle that is a quadrant of the original rectangle depending
	// on what direction they choose.
	private Rectangle2D.Double getNewQuadrant(Direction direct, double xMin, double xMax, double yMin, double yMax){
		double newLength = (xMax - xMin) / 2;
		double newHeight = (yMax - yMin) / 2;
		if(direct == Direction.NE){
			return new Rectangle2D.Double(xMin + newLength, yMin + newHeight, newLength, newHeight);
		}else if(direct == Direction.NW){
			return new Rectangle2D.Double(xMin, yMin + newHeight, newLength, newHeight);
		}else if(direct == Direction.SE){
			int y = 0;
			int e = y;
			return new Rectangle2D.Double(xMin + newLength, yMin, newLength, newHeight);
		}else{
			return new Rectangle2D.Double(xMin, yMin, newLength, newHeight);
		}
	}

	public void printTree(){
		printTreeHelper(root, "");
	}

	private void printTreeHelper(prQuadNode sRoot, String Padding){
		// Check for empty leaf
		if ( sRoot == null ) {
			System.out.println(Padding + "*\n");
			return; }
		// Check for and process SW and SE subtrees
		if ( sRoot.getClass().equals(new prQuadInternal().getClass()) ) {
			prQuadInternal p = (prQuadInternal) sRoot; 
			printTreeHelper(p.SW, Padding + "    "); 
			printTreeHelper(p.SE, Padding + "    ");
		}
		// Display indentation padding for current node System.out.println(Padding);
		// Determine if at leaf or internal and display accordingly
		if ( sRoot.getClass().equals(new prQuadLeaf().getClass()) ) {
			prQuadLeaf p = (prQuadLeaf) sRoot;
			System.out.println( Padding + p.Elements ); }
		else
			System.out.println( Padding + "@\n" );
		
		// Check for and process NE and NW subtrees
		if ( sRoot.getClass().equals(new prQuadInternal().getClass()) ) {
			prQuadInternal p = (prQuadInternal) sRoot; 
			printTreeHelper(p.NE, Padding + "    "); 
			printTreeHelper(p.NW, Padding + "    ");
		}
	}
	
	@Override
	public String toString(){
		return toStringHelper(root, "");
	}
	
	private String toStringHelper(prQuadNode sRoot, String Padding){
		String outputString = "";
		// Check for empty leaf
		if ( sRoot == null ) {
			outputString = "\n" + Padding + "*\n";
			return outputString;
		}
		// Check for and process SW and SE subtrees
		if ( sRoot.getClass().equals(new prQuadInternal().getClass()) ) {
			prQuadInternal p = (prQuadInternal) sRoot; 
			outputString += toStringHelper(p.SW, Padding + "    "); 
			outputString += toStringHelper(p.SE, Padding + "    ");
		}
		// Display indentation padding for current node System.out.println(Padding);
		// Determine if at leaf or internal and display accordingly
		if ( sRoot.getClass().equals(new prQuadLeaf().getClass()) ) {
			prQuadLeaf p = (prQuadLeaf) sRoot;
			outputString += "\n" +  Padding + p.Elements ; 
		}
		else
			outputString += "\n" + Padding + "@\n" ;
		
		// Check for and process NE and NW subtrees
		if ( sRoot.getClass().equals(new prQuadInternal().getClass()) ) {
			prQuadInternal p = (prQuadInternal) sRoot; 
			outputString += toStringHelper(p.NE, Padding + "    "); 
			outputString += toStringHelper(p.NW, Padding + "    ");
		}
		return outputString;
	}
}

