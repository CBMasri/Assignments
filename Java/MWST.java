/* MWST.java
   CSC 226 - Spring 2015
   Assignment 1 - Minimum Weight Spanning Tree
*/

import java.util.*;
import java.io.File;


public class MWST
{	
	static int MWST(int[][] G)
	{
		int numVerts = G.length;

		// edges will hold the set of all edges weights in G
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		// this loop fills the edge set list from the given graph
		for (int i = 0; i < numVerts; i++)
		{
			for (int j = 0; j < numVerts; j++)
			{
				// if there is no edge, keep going. also removes duplicate edges (ex. 1 > 2, 2 > 1)
				if (j < i || G[i][j] == 0)
					continue;
				// else add edge to edge list
				edges.add(new Edge(i,j, G[i][j]));
				
			}
		}
		
		// sort the edges
		Collections.sort(edges);
		
		// create a Union-Find data structure
		UnionFind unionFind = new UnionFind(numVerts);

		// counts the number of edges added into the MST
		int numEdges = 0;
		// counts the total edge weight of the MST
		int totalWeight = 0;
		
 		// iterate through the sorted edge list
		for (Edge edge : edges)
		{
			// if the vertices are connected already, skip this edge
			if (unionFind.connected(edge.vertex1, edge.vertex2))
				continue;
			
			// else add edge weight to total and link the vertices together
			totalWeight += edge.weight;
			unionFind.union(edge.vertex1, edge.vertex2);
			numEdges++;
			
			// if n-1 edges have been added, we're done
			if (numEdges == numVerts)
				break;
		}
		
		return totalWeight;
		
	}
	
		
	/* main()
	   Contains code to test the MWST function.
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
			
			int totalWeight = MWST(G);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}

/*
	This class creates Edge objects, containing two vertexes and the
	weight of the edge that connects them.
*/
class Edge implements Comparable<Edge>
{
	int vertex1, vertex2;
	int weight;
	
	public Edge(int vertex1, int vertex2, int weight)
	{
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.weight = weight;
	}
	
	public int getVertex1()
	{
		return vertex1;
	}
	
	public int getVertex2()
	{
		return vertex2;
	}
	
	public int getWeight()
	{
		return weight;
	}
	
	public int compareTo(Edge next)
	{
		return (this.weight - next.weight);
	}
	
	public String toString()
	{
		return vertex1 + " > " + vertex2 + " weight=" + weight;
	}
}

/*
	This class implements a version of the Union-Find data structure
*/
class UnionFind
{
	int[] parent;     	// parent[i] = parent of i
	byte[] rank;  		// rank[i] = rank of sub-tree rooted at i
	int count;    		// number of components

	public UnionFind(int n) 
	{
		count = n;
		parent = new int[n];
		rank = new byte[n];
		
		for (int i = 0; i < n; i++)
		{
			parent[i] = i;
			rank[i] = 0;
		}
	}

    public int find(int p)
	{
		while (p != parent[p])
		{
			// path compression by halving
			parent[p] = parent[parent[p]];
			p = parent[p];
		}
		return p;
	}

    public int count()
	{
		return count;
	}

	public boolean connected(int p, int q)
	{
		return find(p) == find(q);
	}

	public void union(int p, int q)
	{
		int i = find(p);
		int j = find(q);
		if (i == j) return;

		if (rank[i] < rank[j])
			parent[i] = j;
		else if (rank[i] > rank[j])
			parent[j] = i;
		else
		{
			parent[j] = i;
			rank[i]++;
		}
		count--;
	}
}