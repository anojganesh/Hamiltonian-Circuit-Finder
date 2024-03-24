/**
 * The Hamilton class contains static methods to find Hamiltonian circuits in
 * Graph objects.
 * This class cannot be instantiated.
 * <BR>
 * THIS CLASS SHOULD BE MODIFIED
 * 
 * @author Anoj Ganeshalingam
 * @author
 *
 */
public class Hamilton {

    /**
     * Stops class from being instantiated.
     */
    private Hamilton() {
    }

    /**
     * graph for which a Hamiltonian circuit is sought
     */
    private static Graph g = null;

    /**
     * Total number of vertices in graph
     */
    private static int totalV;

    /**
     * Hamiltonian circuit being built
     */
    private static Walk hamiltonian = null;

    /**
     * Finds a Hamiltonian circuit in graph if there is one.
     * <br>
     * DO NOT MODIFY THE METHOD SIGNATURE AND RETURN TYPE OF THIS METHOD
     * 
     * @param graph graph for which a Hamiltonian circuit is sought
     * @return A Hamiltonian circuit for the graph if there is one, or null
     *         otherwise.
     */
    public static Walk getHamiltonian(Graph graph) {

        // Store static information
        g = graph;
        totalV = g.getTotalVertices();
        hamiltonian = new Walk(totalV + 1);

        // Continue the Implementation

        // starting with base cases
        /*
         * if (g.isEmpty()) {
         * return hamiltonian;
         * }
         * if (g.getTotalVertices() == 1) {
         * return hamiltonian;
         * }
         * if (g.getTotalEdges() == 0) {
         * return null;
         * }
         */

        // int[][] matrix = g.getAllEdges();
        for (int curVertex = 0; curVertex < totalV; curVertex++) { // try every vertex as a
            hamiltonian.addVertex(curVertex); // possible first vertex
            // System.out.println("path: " + hamiltonian.toString());
            Walk res = findPath(g, hamiltonian);
            if (res != null) {
                // System.out.println("res: " + res);
                return res;
            }
            /*
             * System.out.println(hamiltonian.toString());
             * hamiltonian.addVertex(curVertex); // until solution is found
             * System.out.println(hamiltonian.toString());
             * int[] fromRow = matrix[i];
             * System.out.println("Current vertex " + curVertex);
             * for (int connectedTo = 0; connectedTo < fromRow.length; connectedTo++) {
             * if (fromRow[connectedTo] > 0 && connectedTo != i) {
             * System.out.println("Connected to " + connectedTo);
             * return findPath(g, hamiltonian);
             * }
             * }
             */
        }
        // System.out.println("res: " + null);
        return null;
    }

    public static Walk findPath(Graph graph, Walk path) {
        int[][] matrix = graph.getAllEdges();
        int prevNode = path.getVertex(path.getLength());
        // System.out.println("prevNode: " + prevNode);
        int[] fromRow = matrix[prevNode];
        int rowLength = fromRow.length;
        for (int connectedTo = 0; connectedTo < rowLength; connectedTo++) {
            int pathLength = path.getLength() + 1;
            if ((fromRow[connectedTo] > 0) && (!isIn(path, connectedTo))) {
                if (pathLength < rowLength) {
                    //System.out.println("1: " + path.toString());
                    path.addVertex(connectedTo);
                    //System.out.println("2: " + path.toString());
                    Walk res = findPath(g, path);
                    if (res != null) {
                        return res;
                    }
                }
            } else if ((fromRow[connectedTo] > 0) && (pathLength == rowLength)) {
                int firstNode = path.getVertex(0);
                if (connectedTo == firstNode) {
                    path.addVertex(firstNode);
                    return path;
                }
            }
        }
        return null;
    }

    // returns true if vertex is in path.
    public static boolean isIn(Walk path, int vertex) {
        for (int i = 0; i < path.getLength() + 1; i++) {
            int current = path.getVertex(i);
            if (current == vertex) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a Hamiltonian circuit in graph if there is one.
     * 
     * @param vertex       current vertex in the circuit
     * @param totalvisited total number of vertices visited so far in the graph
     * @return true if a Hamiltonian path for the graph has been found
     *         starting at the current vertex and returning to the first vertex in
     *         the walk
     */

    private static boolean foundHamiltonian(int vertex, int totalvisited) {
        return true;
    }

}
