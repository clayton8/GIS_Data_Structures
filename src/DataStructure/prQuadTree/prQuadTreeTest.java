package DataStructure.prQuadTree;


import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import DataStructure.Client.Point;
import DataStructure.prQuadTree.prQuadTree.prQuadInternal;
import DataStructure.prQuadTree.prQuadTree.prQuadNode;


public class prQuadTreeTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testInsert() {
		prQuadTree<Point> t = new prQuadTree<Point>(0, 100, 0, 100);
		Point newPoint = new Point(1,1);
		t.insert(newPoint);
		/*newPoint = new Point(10,10,2);
		t.insert(newPoint);
		newPoint = new Point(10,10,3);
		t.insert(newPoint);
		newPoint = new Point(10,10,4);
		t.insert(newPoint);
		newPoint = new Point(10,10,5);
		t.insert(newPoint);
		newPoint = new Point(10,10,6);*/
		//t.insert(newPoint);
		/*newPoint = new Point(99,99);
		t.insert(newPoint);
		newPoint = new Point(1,99);
		t.insert(newPoint);
		newPoint = new Point(99,1);
		t.insert(newPoint);
		newPoint = new Point(51,51);
		t.insert(newPoint);
		newPoint = new Point(51,51);
		t.insert(newPoint);
		newPoint = new Point(51,51);
		t.insert(newPoint);
		newPoint = new Point(51,51);
		t.insert(newPoint);
		newPoint = new Point(51,51);
		t.insert(newPoint);
		newPoint = new Point(51,51);
		t.insert(newPoint);
		newPoint = new Point(51,90);
		t.insert(newPoint);
		newPoint = new Point(70,7);
		t.insert(newPoint);
		newPoint = new Point(80,51);
		t.insert(newPoint);
		*/
		
		//t.printTree();
		
		
		
	}
	
	@Test
	public void testDeletion() {
		prQuadTree<Point> t = new prQuadTree<Point>(0, 100, 0, 100);
		Point newPoint = new Point(1,1);
		t.insert(newPoint);
		//t.insert(newPoint);
		newPoint = new Point(99,99);
		t.insert(newPoint);
		newPoint = new Point(1,99);
		t.insert(newPoint);
		newPoint = new Point(99,1);
		t.insert(newPoint);
		newPoint = new Point(60,60);
		t.insert(newPoint);
		newPoint = new Point(60,51);
		t.insert(newPoint);
		newPoint = new Point(51,60);
		t.insert(newPoint);
		newPoint = new Point(55,55);
		t.insert(newPoint);
		newPoint = new Point(50,50);
		t.insert(newPoint);
		newPoint = new Point(51,90);
		
		t.insert(newPoint);
		newPoint = new Point(70,70);
		t.insert(newPoint);
		newPoint = new Point(80,51);
		t.insert(newPoint);
		
		
		boolean deleted = false;
		newPoint = new Point(1,1);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);

		System.out.println("------------------------------------------");
		newPoint = new Point(99,99);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(1,99);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(99,1);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(60,60);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(60,51);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(51,60);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(55,55);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(50,50);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(50,50);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(false, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(51,90);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(70,70);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);
		System.out.println("------------------------------------------");
		
		newPoint = new Point(80,51);
		deleted = t.delete(newPoint);
		t.printTree();
		assertEquals(true, deleted);		
		

	}
	
	
	@Test
	public void testFind() {
		prQuadTree<Point> t = new prQuadTree<Point>(0, 100, 0, 100);
		Point newPoint = new Point(1,1);
		t.insert(newPoint);
		//t.insert(newPoint);
		newPoint = new Point(99,99);
		t.insert(newPoint);
		newPoint = new Point(1,99);
		t.insert(newPoint);
		newPoint = new Point(99,1);
		t.insert(newPoint);
		newPoint = new Point(60,60);
		t.insert(newPoint);
		newPoint = new Point(60,51);
		t.insert(newPoint);
		newPoint = new Point(51,60);
		t.insert(newPoint);
		newPoint = new Point(55,55,1);
		t.insert(newPoint);
		newPoint = new Point(50,50);
		t.insert(newPoint);
		newPoint = new Point(51,90);
		
		t.insert(newPoint);
		newPoint = new Point(70,70);
		t.insert(newPoint);
		newPoint = new Point(80,51);
		t.insert(newPoint);
		
		Point found = t.find(newPoint);
		assertEquals(newPoint, found);
		newPoint = new Point(55,55,1);
		found = t.find(newPoint);
		assertEquals(newPoint, found);
		newPoint = new Point(55,55);
		found = t.find(newPoint);
		assertEquals(null, found);
		
	}
	
	
	@Test
	public void testFindArea() {
		prQuadTree<Point> t = new prQuadTree<Point>(0, 100, 0, 100);
		Point newPoint = new Point(1,1);
		t.insert(newPoint);
		//t.insert(newPoint);
		newPoint = new Point(99,99);
		t.insert(newPoint);
		newPoint = new Point(1,99);
		t.insert(newPoint);
		newPoint = new Point(99,1);
		t.insert(newPoint);
		newPoint = new Point(60,60);
		t.insert(newPoint);
		newPoint = new Point(60,51);
		t.insert(newPoint);
		newPoint = new Point(51,60);
		t.insert(newPoint);
		newPoint = new Point(55,55);
		t.insert(newPoint);
		newPoint = new Point(50,50);
		t.insert(newPoint);
		newPoint = new Point(51,90);
		
		t.insert(newPoint);
		newPoint = new Point(70,70);
		t.insert(newPoint);
		newPoint = new Point(80,50);
		t.insert(newPoint);
		
		System.out.println("FOUND----------------\n");
		Vector<Point> found = t.find(50,99,50,99);
		for(Point x: found){
			System.out.println(x.toString());
		}
	}
	
	
	
	
	

}
