package logic;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class BetterDiGraph implements EditableDiGraph {

	private int vertices; 
	private int edges;

	private LinkedList<Integer>[] adj; // all of the vertices 
	private HashMap<Integer, Boolean> allVertex; 
	private HashMap<Integer, Character> table; // table of Hanzi characters
	
	// eager tracking of zero degrees in order to prevent costly searching in list 
	private HashMap<Integer, Integer> degrees; // tracks which vertices have a indegree=0
	
	public BetterDiGraph(int v) {
		this.vertices = v;  
		this.edges = 0; 
		adj = new LinkedList[v]; 
		
		allVertex = new HashMap<Integer, Boolean>();
		
		// set up linked list array 
		for(int i = 0; i<v; i++) {
			// initialize adjacent nodes
			adj[i] = new LinkedList<Integer>(); 
			
			// initialize vertex list 
			// no vertex therefore all start as false 
			allVertex.put(Integer.valueOf(i), false);
			
		}
		
		degrees = new HashMap<Integer, Integer>(); 
		
		// all vertices start with degree=0
		for(int i = 0; i<v; i++) {
			degrees.put(i, -1);
		}
	}
	
	
	public HashMap getDegrees() {
		return degrees; 
	}
	
	@Override
	public void addEdge(int v, int w) {
		
		// check that both vertices are in the graph 
		// if a vertex is an actual member of the graph 
		// then it will have an in-degree of at least 0
		// see addVertex(int v) 
		if(degrees.get(v) < 0 || degrees.get(w) < 0)
			return; 
		
		adj[v].add(w);
		
		// Increment the degree of w 
		Integer degree = degrees.get(w) + 1;
		degrees.put(w, degree);
		
		edges++;
	}

	// TODO implement this
	@Override
	public void addVertex(int v) {
		// TODO Auto-generated method stub
		allVertex.put(v, true);

		
		// increment degree
		degrees.put(v, 0);
	}

	@Override
	public Iterable<Integer> getAdj(int v) {
		// returns a linked list containing all of the 
		// adjacent nodes to v 
		return adj[v]; 
	}

	@Override
	public int getEdgeCount() {
		// TODO Auto-generated method stub
		return edges;
	}

	@Override
	public int getIndegree(int v) throws NoSuchElementException {
		
		return degrees.get(v);
	
		
	}

	@Override
	public int getVertexCount() {
		// TODO Auto-generated method stub
		return vertices; 
	}

	@Override
	public void removeEdge(int v, int w) {
		
		
	}

	@Override
	public void removeVertex(int v) {
		// TODO 
		// determine if this node exists
		
		// decrement this degree making it -1 
		// taking it out of the pool of nodes we 
		// look for when sorting
		degrees.put(v, -1);
		
		// decrement all of the adjacent nodes degrees
		// taking eager approach
		for(int w: getAdj(v)) {
			// decrement degree 
			Integer degree = degrees.get(w)-1;
			
			// update 
			degrees.put(w, degree);
			
			// decrement vertices 
			vertices--;
		}
		
		// reset this nodes linked list 
		// TODO test that after the node 
		// has been removed, that it is 
		// now an empty list at this 
		// indes in the array
		//
		adj[v] = new LinkedList<Integer>(); 
		
		
		
		
	}

	@Override
	public Iterable<Integer> vertices() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		String returnString  = ""; 
		
		// TODO loop over all of the elements in the graph
		
		return returnString; 
	}

}
