package structures;

/**
 * Graph.java
 * @author Jesse Lutan
 * @author Jesse Friaz
 * @author Varun Kiragi
 * @author Tommy Le
 * @author Y Pham
 * @author Rohit Pokuri 
 * CIS 22C, Final Project
 */
import java.util.ArrayList;

public class Graph {

    private int vertices;
    private int edges;
    private ArrayList<List<Integer>> adj;
    private ArrayList<Character> color;
    private ArrayList<Integer> distance;
    private ArrayList<Integer> parent;

    /**
     * Constructors
     */
    /**
     * initializes an empty graph, with n vertices and 0 edges
     *
     * @param n the number of vertices in the graph
     */
    public Graph(int n) { //tested
        vertices = n;
        edges = 0;
        adj = new ArrayList<List<Integer>>();
        color = new ArrayList<Character>();
        distance = new ArrayList<Integer>();
        parent = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            adj.add(new List<Integer>());//initialize vertices as list objects in adj[]
            parent.add(null);//initialized parent list. Each index represent a vertice
            distance.add(-1); //initialized distance list. Each index represent a vertice
            color.add(' ');//initialized color list. Each index represent a vertice
        }
    }

    /**
     * * Accessors **
     */
    
    /**
     * Returns the number of edges in the graph
     *
     * @return the number of edges
     */
    public int getNumEdges() {
        return edges;
    }

    /**
     * Returns the number of vertices in the graph
     *
     * @return the number of vertices
     */
    public int getNumVertices() {
        return vertices;
    }

    /**
     * returns whether the graph is empty (no edges)
     *
     * @return whether the graph is empty
     */
    public boolean isEmpty() {
        return edges == 0;
    }

    /**
     * Returns the value of the distance[v]
     *
     * @param v a vertex in the graph
     * @precondition 0 < v <= vertices @re turn the distance of vertex v @throws
     * IndexOutOfBoundsException when the p recondition is violated
     */
    public Integer getDistance(Integer v) throws IndexOutOfBoundsException {
        if (!(0 < v && v <= vertices)) {
            throw new IndexOutOfBoundsException("getDistance: index out of bounds");
        }
        return distance.get(v - 1);
    }

    /**
     * Returns the value of the parent[v]
     *
     * @param v a vertex in the graph
     * @precondition 0 < v <= vertices @re turn the parent of vertex v @throws
     * IndexOutOfBoundsException when the precondition is violated
     */
    public Integer getParent(Integer v) throws IndexOutOfBoundsException {
        if (!(0 < v && v <= vertices)) {
            throw new IndexOutOfBoundsException("getParent: index out of bounds");
        }
        return parent.get(v - 1);
    }

    /**
     * Returns the value of the color[v]
     *
     * @param v a vertex in the graph
     * @precondition 0< v <= vertices @re turn the color of vertex v @throws
     * IndexOutOfBoundsException when th e precondition is violated
     */
    public Character getColor(Integer v) throws IndexOutOfBoundsException {
        if (!(0 < v && v <= vertices)) {
            throw new IndexOutOfBoundsException("getColor: index out of bounds");
        }
        return color.get(v - 1);
    }

    /**
     * * Mutators **
     */
    
    /**
     * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into
     * the list at index u)
     *
     * @precondition, 0 < u, v <= vertices @th rows IndexOutOfBounds exception
     * when the precondition is violated
     */
    public void addDirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException { //tested
        if (!(0 < u && u <= vertices && 0 < v && v <= vertices)) {
            throw new IndexOutOfBoundsException("addDirectedEdge: index out of bounds");
        }
        // only allow traversal of edge in one direction
        adj.get(u - 1).addLast(v);

        /* The following part is for testing purpose
        parent.set(v - 1, u);
        if (u == 1) {//if u is original vertice
            distance.set(v - 1, 1);//u is original vertice and v is one edge away from it.
        } else if (distance.get(u - 1) != -1) {//if u is connected to the original vertice
            if (distance.get(v - 1) == -1) {//prevent override distance from addUndirectedEdge
                distance.set(v - 1, distance.get(u - 1) + 1);
            }
        }*/
        edges++;//increment number of edges
    }

    /**
     * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into
     * the list at index u) and inserts u into the adjacent vertex list of v
     *
     * @precondition, 0 < u, v <= vertices 
     * @throws IndexOutOfBounds exception
     * when the precondition is violated
     */
    public void addUndirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException { //tested
        if (!(0 < u && u <= vertices && 0 < v && v <= vertices)) {
            throw new IndexOutOfBoundsException("addUndirectedEdge: index out of bounds");
        }
        // allow edge to be traversed in both directions
        addDirectedEdge(u, v);
        addDirectedEdge(v, u);
    }
    
    /**
     * Removes all occurrences of vertex v from the adjacency list of vertex u and
     * removes all occurrences u from the adjacency list of vertex v
     *
     * @precondition, 0 < u, v <= vertices 
     * @throws IndexOutOfBounds exception
     * when the precondition is violated
     */
    public void removeAdjacency(Integer u, Integer v) throws IndexOutOfBoundsException { 
    	if (!(0 < u && u <= vertices && 0 < v && v <= vertices)) {
            throw new IndexOutOfBoundsException("addUndirectedEdge: index out of bounds");
        }
    	List<Integer> uList = adj.get(u - 1),
    			vList = adj.get(v - 1);
    	uList.placeIterator();
    	while (!uList.offEnd()) {
    		if (uList.getIterator().equals(v)) {
    			uList.removeIterator();
    		} else {
    			uList.advanceIterator();
    		}
    	}
    	vList.placeIterator();
    	while (!vList.offEnd()) {
    		if (vList.getIterator().equals(u)) {
    			vList.removeIterator();
    		} else {
    			vList.advanceIterator();
    		}
    	}
    }

    /**
     * * Additional Operations **
     */
    
    /**
     * Creates a String representation of the Graph Prints the adjacency list of
     * each vertex in the graph, vertex:
     * <space separated list of adjacent vertices>
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < vertices; i++) {
            result += i + "\t" + adj.get(i).toString()+"\n";
        }
        return result + "\n";
    }

    /**
     * Prints the current values in the parallel ArrayLists after executing BFS.
     * First prints the heading: v <tab> c <tab> p <tab> d 
     * Then, prints out this
     * information for each vertex in the graph 
     * Note that this method is
     * intended purely to help you debug your code
     */
    public void printBFS() {
        System.out.println("v\tc\tp\td");
        for (int i = 1; i <= vertices; i++) { 
            System.out.println(i + "\t" + getColor(i) + "\t"
                    + getParent(i) + "\t" + getDistance(i));
        }
    }

    /**
     * Performs breath first search on this Graph give a source vertex
     *
     * @param source
     * @precondition graph must not be empty
     * @precondition source is a vertex in the graph
     * @throws IllegalStateException if the graph is empty
     * @throws IndexOutOfBoundsException when the source vertex is not a vertex
     * in the graph
     */
    public void BFS(Integer source) throws IllegalStateException, IndexOutOfBoundsException {
        // handle precondition violations
        if (isEmpty()) {
            throw new IllegalStateException("BFS: graph cannot be empty");
        } else if (!(source > 0 && source <= vertices)) {
            throw new IndexOutOfBoundsException("BFS: source is not in graph");
        }

        for (int i = 0; i < vertices; i++) {
            color.set(i, 'W');
            distance.set(i, -1);
            parent.set(i, null);
        }
        /* 
    	 * index at source-1 because vertices are 1-indexed
    	 * while ArrayList is 0 indexed
         */
        color.set(source - 1, 'G');
        distance.set(source - 1, 0);
        Queue<Integer> Q = new Queue<>();
        Q.enqueue(source - 1);
        while (!Q.isEmpty()) {
            int x = Q.getFront();
            Q.dequeue();
            //List<Integer> adjacents = adj.get(x - 1);//Error. Must call copy constructor
            List<Integer> adjacents = new List<>(adj.get(x));//no need to call x-1 because first item in Q is already source-1 
            adjacents.placeIterator();
            for (int i = 0; i < adjacents.getLength(); i++) {
                int y = adjacents.getIterator() - 1;//decrement y cause iterator will not return a value based on arrayList 0-based index
                // check if vertex y has not been processed yet
                if (color.get(y).equals('W')) { //comparing string
                    color.set(y, 'G');
                    distance.set(y, distance.get(x) + 1);
                    parent.set(y, x);
                    Q.enqueue(y);
                }
                adjacents.advanceIterator();//going through adj list.
            }
            color.set(x, 'B');//move this out of for loop
        }
    }

}
