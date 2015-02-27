/* ShortestPath.java
   CSC 226 - Fall 2014
   Assignment 3
*/

import java.util.*;
import java.io.File;

public class ShortestPath{	
	/* ShortestPath(G)
		Given an adjacency matrix for graph G, return the total weight
		of a minimum weight path from vertex 0 to vertex 1
	*/
	static int ShortestPath(int[][] G){
	
		int numVerts = G.length;
		int totalWeight = 0;
		HeapPriorityQueue pq = new HeapPriorityQueue(numVerts);
		Vertex[] vertices = new Vertex[numVerts];
		
		// first we need to initialize all the vertices
		for (int i = 0; i < numVerts; i++) {
			
			Vertex v = new Vertex();
			vertices[i] = v;
			
			// distance from source vertex to source is zero
			if (i == 0) {
				v.minDistance = 0;
				pq.insert(v);
			// all other vertices are added to the priority queue
			} else {
				pq.insert(v);
			}
		}
		
		// now we can initialize all the edges
		for (int i = 0; i < numVerts; i++) {
			for (int j = 0; j < numVerts; j++) {
				if (G[i][j] != 0) {
					Vertex currentVertex = vertices[i];
					Vertex targetVertex = vertices[j];
					Edge e = new Edge(targetVertex, G[i][j]);
					currentVertex.adjacencies.add(e);
				}
			}
		}
		
		// everything is ready, start dijkstras
		
		while ( !pq.isEmpty() ) {		
			// u <-- get min from priority queue
			Vertex u = pq.removeMin();
			
			// for each neighbour v of u
			for (Edge e : u.adjacencies) {
				Vertex v = e.targetVertex;
				double edgeWeight = e.weight;
				double altDistance = u.minDistance + edgeWeight;
				
				if (altDistance < v.minDistance) {
					v.minDistance = altDistance;
					v.parent = u;
					
					Vertex addBack = new Vertex();
					addBack.minDistance = altDistance;
					addBack.adjacencies = v.adjacencies;
					addBack.parent = u;
					pq.insert(addBack);
				}
			}
		}
		
		// return the distance of the shortest path from
		// vertex 0 (source) to vertex 1 (target)
		totalWeight = (int)vertices[1].minDistance;
		return totalWeight;
	}
	
		
	/*
	main()
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		// Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			
			int totalWeight = ShortestPath(G);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}

/*
	This class creates Vertex objects
*/
class Vertex implements Comparable<Vertex> {
	ArrayList<Edge> adjacencies = new ArrayList<Edge>();
	double minDistance = Double.POSITIVE_INFINITY;
    Vertex parent = null;
	
    public int compareTo(Vertex otherVertex) {
        return Double.compare(minDistance, otherVertex.minDistance);
    }
}

/*
	This class creates Edge objects to be used by the Vertex class
*/
class Edge {
	Vertex targetVertex;
	double weight;
	
	public Edge (Vertex targetVertex, int weight) {
		this.targetVertex = targetVertex;
		this.weight = weight;
	}
}

/*
	Heap Priority Queue class (written in csc 115)
*/
class HeapPriorityQueue {
	
	protected Vertex storage[];

	protected int currentSize;

	public HeapPriorityQueue(int size) {
		storage = new Vertex[size*2];
		currentSize = 0;
	}
	
	public int size () {
		return currentSize;
	}
	
	public boolean isEmpty () {
		return currentSize == 0;
	}
	
	public Vertex removeMin () {
		
		Vertex min = storage[1];
		
		storage[1] = storage[currentSize];
		storage[currentSize] = null;
		currentSize--;
		
		bubbleDown();
		
		return min;
	}
	
	public void insert ( Vertex k  ) {
		
		if (currentSize == 0) {
			storage[1] = k;
			currentSize++;
		} else {
			currentSize++;
			storage[currentSize] = k;
			bubbleUp();
		}
	}

	public void bubbleUp()  {
		int index = currentSize;
		
		while (hasParent(index) && (storage[parent(index)].compareTo(storage[index]) > 0)) {
			swapElement(index, parent(index));
			index = parent(index);
		}
	}

	public void bubbleDown() {
		int index = 1;
		
		while(hasLeft(index)) {
			int smaller = leftChild(index);
			
			if (hasRight(index)) {
				if (storage[leftChild(index)].compareTo(storage[rightChild(index)]) > 0) {
					smaller = rightChild(index);
				}
			}
			
			if (storage[index].compareTo(storage[smaller]) > 0) {
				swapElement(index, smaller);
			}
			
			index = smaller;
		}
	}	
	
	private void swapElement ( int p1, int p2 ) {
		Vertex temp = storage[p1];
		storage[p1] = storage[p2];
		storage[p2] = temp;
	}
	
	private int parent ( int pos ) {
		return pos/2;
	}
	
	private int leftChild ( int pos ) {
		return pos*2;
	}
	
	private int rightChild ( int pos ) {
		return pos*2+1;
	}
	
	private boolean hasParent ( int pos ) {
		return (storage[pos/2] != null);
	}
	
	private boolean hasLeft ( int pos ) {
		return (leftChild(pos) <= currentSize);
	}

	private boolean hasRight ( int pos ) {
		return (rightChild(pos) <= currentSize);
	}
}