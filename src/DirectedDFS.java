//-----------------------------------------------------
// Title: Directed depth-first search class
// Author: Kayra Arı, Elçin Karagül
// ID:
// Section: 4
// Assignment: 2
// Description: This class finds a route that starts at X, ends
// at Y and uses every one-way street exactly once. If several
// routes exist, the lexicographically smallest one is found.
// First the degrees are checked. Then a DFS-based search walks
// along unused streets, always taking the smallest neighbour,
// and adds an intersection to the route when it has no unused
// street left. Finally it is checked that the route really
// uses all streets.
//-----------------------------------------------------

public class DirectedDFS {
    private Stack<Integer> route;   // the route from x to y (null if none)
    private final int x;            // start intersection
    private final int y;            // end intersection

    public DirectedDFS(Digraph G, int x, int y) {
        //--------------------------------------------------------
        // Summary: Searches for the route from x to y that uses
        // every street of G exactly once.
        // Precondition: 0 <= x, y < G.V() and x != y. G has no
        // self-loops and no duplicate streets.
        // Postcondition: route holds the lexicographically smallest
        // valid route, or null if there is no valid route.
        //--------------------------------------------------------
        this.x = x;
        this.y = y;
        route = null;

        // Without correct degrees no route can exist (answer is -1)
        if (!degreesAreValid(G)) return;

        Stack<Integer> found = search(G);

        // Correct degrees are not enough: if the streets are not all
        // connected to x, some of them are never used, so the route
        // must contain exactly E + 1 intersections
        if (found.size() == G.E() + 1)
            route = found;
    }

    private boolean degreesAreValid(Digraph G) {
        //--------------------------------------------------------
        // Summary: Checks the degree condition of a route that
        // uses every street once: x has one more leaving street
        // than entering streets, y has one more entering street,
        // and for every other intersection the two numbers are
        // equal.
        // Precondition: None.
        // Postcondition: Returns true if all intersections satisfy
        // the condition.
        //--------------------------------------------------------
        for (int v = 0; v < G.V(); v++) {
            int difference = G.outdegree(v) - G.indegree(v);
            int expected = 0;
            if (v == x) expected = 1;
            if (v == y) expected = -1;
            if (difference != expected) return false;
        }
        return true;
    }

    private Stack<Integer> search(Digraph G) {
        //--------------------------------------------------------
        // Summary: Depth-first search over the streets, done with
        // an explicit Stack instead of recursion because the route
        // can be as long as 20000 streets. From the intersection on
        // top of the stack the smallest unused street is followed.
        // When an intersection has no unused street left, it is
        // removed and pushed onto the route. So the route is built
        // from its end to its start and the Stack returns it in
        // the correct order.
        // Precondition: The degree condition holds.
        // Postcondition: Returns the intersections of the route
        // from x in visiting order (every street reachable from x
        // is used exactly once).
        //--------------------------------------------------------
        int[][] sorted = new int[G.V()][];      // sorted[v] = neighbours of v, ascending
        for (int v = 0; v < G.V(); v++)
            sorted[v] = sortedNeighbors(G, v);

        int[] next = new int[G.V()];            // next[v] = first unused street of v
        Stack<Integer> trail = new Stack<Integer>();
        Stack<Integer> finished = new Stack<Integer>();
        trail.push(x);

        while (!trail.isEmpty()) {
            int v = trail.peek();
            if (next[v] < sorted[v].length) {
                // Follow the smallest unused street v -> w
                int w = sorted[v][next[v]];
                next[v]++;
                trail.push(w);
            } else {
                // No unused street left: v is finished
                finished.push(trail.pop());
            }
        }
        return finished;
    }

    private int[] sortedNeighbors(Digraph G, int v) {
        //--------------------------------------------------------
        // Summary: Returns the intersections pointing from v in
        // ascending order.
        // Precondition: 0 <= v < G.V().
        // Postcondition: An array with the sorted neighbours of v
        // is returned.
        //--------------------------------------------------------
        // Mark which intersections are neighbours of v
        boolean[] isNeighbor = new boolean[G.V()];
        for (int w : G.adj(v))
            isNeighbor[w] = true;

        // Collect them from the smallest to the largest
        int[] sorted = new int[G.outdegree(v)];
        int count = 0;
        for (int w = 0; w < G.V(); w++)
            if (isNeighbor[w]) sorted[count++] = w;
        return sorted;
    }

    public boolean hasRoute() {
        //--------------------------------------------------------
        // Summary: Tells whether a valid route was found.
        // Precondition: None.
        // Postcondition: Returns true if route is not null.
        //--------------------------------------------------------
        return route != null;
    }

    public Iterable<Integer> route() {
        //--------------------------------------------------------
        // Summary: Returns the route that was found.
        // Precondition: hasRoute() is true.
        // Postcondition: The intersections of the route are
        // returned in visiting order, starting with x and ending
        // with y.
        //--------------------------------------------------------
        return route;
    }
}