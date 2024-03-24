import java.util.Scanner;
import java.util.Random;

/**
 * Graph objects can be used to work with undirected graphs.
 * <br>
 * Graphs are internally represented using adjacency matrices.
 * <br>
 * This class provides facilities to record visits of vertices and edges.
 * <BR>
 * ONLY MODIFY METHODS DFSVISIT AND ISCONNECTED IN THIS CLASS
 * 
 * @author Anoj Ganeshalingam
 *
 */
public class Graph {

    /**
     * Used to generate edges randomly
     */
    static Random rand = new Random(999);

    // -----------------------------------------------------------------------
    // The following instance variables are private and immutable

    /**
     * Total number of vertices in graph
     */
    private int totalV = 0;

    /**
     * Total number of edges in graph
     */
    private int totalE = 0;

    /**
     * Adjacency matrix of graph.
     * <br>
     * edges[x][y] is the number of edges from vertex x to vertex y.
     */
    private int[][] edges = null;

    /**
     * Degree of each vertex.
     */
    private int[] degrees = null;

    // -----------------------------------------------------------------------
    // The following instance variables are public and can be used directly
    // by external graph visitors, or indirectly using methods provided.

    /**
     * Used by graph visitors to keep track of visited vertices.
     */
    public boolean[] visitedV = null;

    /**
     * Used by graph visitors to keep track of the degree of each vertex
     * if unvisited edges are counted.
     */
    public int[] unvisitedVDegree = null;

    /**
     * Used by graph visitors to keep track of number of visited edges
     * as an alternative to using unvisitedE.
     */
    public int[][] visitedE = null;

    /**
     * Used by graph visitors to keep track of number of unvisited edges
     * as an alternative to using visitedE.
     */
    public int[][] unvisitedE = null;

    /**
     *
     * Creates a new undirected Graph whose content will be read from the Scanner.
     * <br>
     * Input format consists of non-negative integers separated by white space as
     * follows:
     * <ul>
     * <li>First non-negative integer specifies the number of vertices n
     * <li>Next nxn integers specify the edges, listed in adjacency matrix order
     * </ul>
     * 
     * @throws InstantiationException if incorrect data is read
     * @param in Scanner used to read graph description
     */
    public Graph(Scanner in) throws InstantiationException {

        // Read number of totalV and handle empty graph
        totalV = in.nextInt();

        if (totalV < 0) {
            throw new InstantiationException("Number of vertices must be positive");
        }
        if (totalV == 0)
            return;

        // Read adjacency matrix
        // If mistakes are found in edges, the entire matrix is read
        // before the exception is thrown,
        int i, j;
        boolean inputMistake = false;
        edges = new int[totalV][totalV];
        for (i = 0; i < totalV; i++) {
            for (j = 0; j < totalV; j++) {
                edges[i][j] = in.nextInt();
                if (edges[i][j] < 0) {
                    inputMistake = true;
                }
            }
        }
        if (inputMistake)
            throw new InstantiationException("Number of edges cannot be negative");

        // Verify that adjacency matrix is symmetric
        for (i = 0; i < totalV; i++)
            for (j = i + 1; j < totalV; j++)
                if (edges[i][j] != edges[j][i]) {
                    throw new InstantiationException("Adjacency matrix is not symmetric");
                }

        // Initialize remainder of new Graph fields
        initializeGraphFields();
    }

    /**
     * Creates a randomly generated Graph according to specifications.
     * 
     * @param vertices         Number of vertices in graph - defaults to 0
     * @param maxParallelEdges Maximum number of parallel edges between any two
     *                         vertices - defaults to 0
     */
    public Graph(int vertices, int maxParallelEdges) {

        // set totalV and handle empty graph
        totalV = (vertices < 0) ? 0 : vertices;
        if (totalV == 0)
            return;

        // Add edges to graph randomly
        edges = new int[totalV][totalV];
        if (maxParallelEdges > 0) {
            int randmax = maxParallelEdges + 1;
            for (int i = 0; i < totalV; i++)
                for (int j = i; j < totalV; j++) {
                    edges[i][j] = rand.nextInt(randmax);
                    edges[j][i] = edges[i][j];
                }
        }

        // Initialize remainder of new Graph fields
        initializeGraphFields();
    }

    /**
     * Once vertices and edges are known, initialize remainder of Graph fields.
     */
    private void initializeGraphFields() {

        // ...............This is the section that was corrected on March 5
        // ..........................

        // Record degrees of vertices in degrees array and count total edges
        degrees = new int[totalV];
        for (int i = 0; i < totalV; i++) {
            degrees[i] = 0;
            for (int j = 0; j < totalV; j++) {
                degrees[i] += edges[i][j];
            }
            // loops count twice
            degrees[i] += edges[i][i];
            totalE += degrees[i];
        }
        // Number of edges = graph degree/2
        totalE = totalE / 2;
        // ............................................................................................

        // Prepare visitation status arrays
        unvisitedVDegree = new int[totalV];
        visitedV = new boolean[totalV];
        visitedE = new int[totalV][totalV];
        unvisitedE = new int[totalV][totalV];
        resetVisitation();
    }

    /**
     * Returns a String representation of the graph:
     * a 2-D representation of the adjacency matrix of that graph.
     * 
     * @return The 2-D representation of the adjacency matrix of that graph.
     *
     */
    @Override
    public String toString() {
        return matrixtoString(edges);
    }

    /**
     * Returns a String representation of 2 dimensional matrix
     * of size totalV x totalV.
     * This can be used to visualize edges, visitedE, and unvisitedE
     * 
     * @param matrix matrix to be represented
     * @return 2D string representation of matrix
     */
    private String matrixtoString(int[][] matrix) {
        String result = "";
        for (int i = 0; i < totalV; i++) {
            for (int j = 0; j < totalV; j++) {
                result += matrix[i][j];
                result += " ";
            }
            result += "\n";
        }
        return result;

    }

    /**
     * Verifies whether graph is empty (with no vertices).
     * 
     * @return True iff graph is empty
     */
    public boolean isEmpty() {
        return (totalV == 0);
    }

    /**
     * Gets the number of vertices in the graph.
     * 
     * @return The number of vertices in the graph
     *
     */
    public int getTotalVertices() {
        return totalV;
    }

    /**
     * Gets the degree of a specific vertex.
     * 
     * @param vertex the vertex whose degree is sought
     * @return The degree of vertex
     *
     */
    public int getDegree(int vertex) {
        return degrees[vertex];
    }

    /**
     * Gets the number of edges in the graph.
     * 
     * @return The number of edges in the graph
     *
     */
    public int getTotalEdges() {
        return totalE;
    }

    /**
     * Gets the number of edges from sourceV to destV.
     * 
     * @param sourceV The source vertex
     * @param destV   The destination vertex
     * @return The number of edges from sourceV to destV
     */
    public int getEdgeCount(int sourceV, int destV) {
        if (sourceV >= 0 && sourceV < totalV && destV >= 0 && destV < totalV)
            return edges[sourceV][destV];
        else
            return 0;
    }

    /**
     * Gets the adjacency matrix of the graph.
     * 
     * @return The adjacency matrix of the graph
     */
    public int[][] getAllEdges() {
        return edges;
    }

    /**
     * Resets unvisitedVDegree, visitedV, visitedE, and unvisitedE matrices for a
     * new visitation.
     */
    public void resetVisitation() {
        for (int i = 0; i < totalV; i++) {
            unvisitedVDegree[i] = degrees[i];
            visitedV[i] = false;
            for (int j = 0; j < totalV; j++) {
                visitedE[i][j] = 0;
                unvisitedE[i][j] = edges[i][j];
            }
        }
    }

    /**
     * Check whether vertex has been visited.
     * 
     * @param vertex vertex whose visited status is checked
     * @return True iff vertex has been visited
     */
    public boolean isVisitedV(int vertex) {
        return (visitedV[vertex] == true);
    }

    /**
     * Visit vertex.
     * <br>
     * Side-effect: visitedV is modified.
     * 
     * @param vertex vertex being visited
     */
    public void visitV(int vertex) {
        if (vertex < 0 || vertex >= totalV)
            return;
        visitedV[vertex] = true;
    }

    /**
     * Unvisit vertex.
     * <br>
     * Side-effect: visitedV is modified.
     * 
     * @param vertex vertex being unvisited
     */
    public void unvisitV(int vertex) {
        if (vertex < 0 || vertex >= totalV)
            return;
        visitedV[vertex] = false;
    }

    /**
     * Visit edge between two vertices.
     * <br>
     * Side-effect: visitedE and unvisitedE are are modified.
     * 
     * @param v1 Vertex incident on edge being visited
     * @param v2 Vertex incident on edge being visited
     */
    public void visitE(int v1, int v2) {
        if (v1 < 0 || v1 >= totalV || v2 < 0 || v2 >= totalV)
            return;
        visitedE[v1][v2] += 1;
        unvisitedE[v1][v2] -= 1;
        if (v1 != v2) {
            visitedE[v2][v1] += 1;
            unvisitedE[v2][v1] -= 1;
        }
    }

    /**
     * Unvisit edge between two vertices.
     * <br>
     * Side-effect: visitedE and unvisitedE are are modified.
     * 
     * @param v1 Vertex incident on edge being unvisited
     * @param v2 Vertex incident on edge being unvisited
     */
    public void unvisitE(int v1, int v2) {
        if (v1 < 0 || v1 >= totalV || v2 < 0 || v2 >= totalV)
            return;
        visitedE[v1][v2] -= 1;
        unvisitedE[v1][v2] += 1;
        if (v1 != v2) {
            visitedE[v2][v1] -= 1;
            unvisitedE[v2][v1] += 1;
        }
    }

    // ========================================================================================
    // LAB6: MODIFY THE TWO METHODS UNDERNEATH AND SUBMIT YOUR MODIFIED GRAPH.JAVA
    // ASSIGNMENT: USE GRAPH.JAVA YOU MODIFIED IN LAB6 BUT DO NOT SUBMIT IT
    // IN BOTH CASES: DO NOT MODIFY THE RETURN TYPE AND METHOD SIGNATURE OF
    // ISCONNECTED
    // DFSVISIT IS A HELPER FUNCTION FOR ISCONNECTED AND YOU CAN DO WHATEVER YOU
    // WANT WITH IT.

    /**
     * Verifies whether graph is connected.
     * 
     * @return True iff graph is connected
     */
    public boolean isConnected() {
        resetVisitation();

        System.out.println("started");

        int startingNode = -1;
        if (getTotalVertices() == 0 || getTotalVertices() == 1) {
            return true;
        }
        if (getTotalEdges() == 0) {
            return false;
        }

        outerloop: for (int i = 0; i < this.edges.length; i++) {
            for (int j = 0; j < this.edges[i].length; j++) {
                /* System.out.println("trying"); */
                if (this.edges[i][j] > 0) {
                    startingNode = i;/*
                                      * System.out.println("starting node: " + startingNode);
                                      */
                    break outerloop;
                }
            }
        }
        DFSvisit(startingNode);
        boolean[] visitedArray = this.visitedV;
        System.out.println(visitedArray);
        for (Boolean bool : visitedArray) {
            if (bool == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Conducts a Depth First Search visit of the unvisited vertices
     * of the graph starting at vertex.
     * <br>
     * Ties between vertices are broken in numeric order.
     * <br>
     * Side-effect: visitedV is modified.
     * 
     * @param vertex First vertex to be visited
     */
    private void DFSvisit(int vertex) { /*
                                         * System.out.println(this.visitedV);
                                         * System.out.println("Vertices: " + getTotalVertices());
                                         * System.out.println("starting1.5");
                                         * System.out.println("starting2");
                                         */
        System.out.println("vertex: " + vertex);/*
                                                 * System.out.println(Arrays.deepToString(edges));
                                                 * for (Boolean bool : visitedV) {
                                                 * System.out.println(Boolean.toString(bool));
                                                 * }
                                                 * 
                                                 * System.out.println(this.isVisitedV(1));
                                                 */
        if (this.isVisitedV(vertex)) {
            return;
        } else {
            visitV(vertex);
        }
        int[] nodeNeighbors = edges[vertex];
        for (int i = 0; i < nodeNeighbors.length; i++) {
            if (nodeNeighbors[i] > 0) {
                DFSvisit(i);
            }
        }
    }
}
