//-----------------------------------------------------
// Title: Digraph class
// Author: Kayra Arı, Elçin Karagül
// ID:
// Section: 4
// Assignment: 2
// Description: This class represents the city as a directed
// graph. Intersections are integer vertices 0..V-1 and one-way
// streets are directed edges. Adjacency lists are stored in an
// array of Bags. The indegree of every intersection is also
// kept, because the route needs the number of streets entering
// and leaving each intersection.
//-----------------------------------------------------

public class Digraph {
    private final int V;            // number of intersections
    private int E;                  // number of one-way streets
    private Bag<Integer>[] adj;     // adj[v] = intersections pointing from v
    private int[] indegree;         // indegree[v] = number of streets entering v

    public Digraph(int V) {
        //--------------------------------------------------------
        // Summary: Creates a digraph with V intersections and no
        // streets.
        // Precondition: V >= 0.
        // Postcondition: An empty Bag is created for every
        // intersection and all indegrees are 0.
        //--------------------------------------------------------
        this.V = V;
        this.E = 0;
        indegree = new int[V];
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }

    public void addEdge(int v, int w) {
        //--------------------------------------------------------
        // Summary: Adds a one-way street from v to w.
        // Precondition: 0 <= v, w < V.
        // Postcondition: w is added to the adjacency list of v
        // (only in this direction), the indegree of w and E are
        // increased by one.
        //--------------------------------------------------------
        adj[v].add(w);
        indegree[w]++;
        E++;
    }

    public Iterable<Integer> adj(int v) {
        //--------------------------------------------------------
        // Summary: Returns the intersections pointing from v.
        // Precondition: 0 <= v < V.
        // Postcondition: An iterable over the vertices w with a
        // street v -> w is returned (in no particular order).
        //--------------------------------------------------------
        return adj[v];
    }

    public int outdegree(int v) {
        //--------------------------------------------------------
        // Summary: Returns the number of streets leaving v.
        // Precondition: 0 <= v < V.
        // Postcondition: The outdegree of v is returned.
        //--------------------------------------------------------
        return adj[v].size();
    }

    public int indegree(int v) {
        //--------------------------------------------------------
        // Summary: Returns the number of streets entering v.
        // Precondition: 0 <= v < V.
        // Postcondition: The indegree of v is returned.
        //--------------------------------------------------------
        return indegree[v];
    }

    public int V() {
        //--------------------------------------------------------
        // Summary: Returns the number of intersections.
        // Precondition: None.
        // Postcondition: V is returned.
        //--------------------------------------------------------
        return V;
    }

    public int E() {
        //--------------------------------------------------------
        // Summary: Returns the number of one-way streets.
        // Precondition: None.
        // Postcondition: E is returned.
        //--------------------------------------------------------
        return E;
    }
}