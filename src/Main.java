//-----------------------------------------------------
// Title: Main class
// Author: Kayra Arı, Elçin Karagül
// ID:
// Section: 4
// Assignment: 2
// Description: This class reads the street network from
// standard input, builds the digraph and searches for a route
// that starts at X, ends at Y and uses every one-way street
// exactly once. It prints the number of intersections on the
// route and the route itself, or -1 if no such route exists.
//-----------------------------------------------------

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //--------------------------------------------------------
        // Summary: Reads the input, finds the route and prints the
        // result.
        // Precondition: The input follows the format given in the
        // assignment (N M, M streets, X Y).
        // Postcondition: The answer is printed to standard output.
        //--------------------------------------------------------
        Scanner keyboard = new Scanner(System.in);

        // First line: number of intersections and one-way streets
        int N = keyboard.nextInt();
        int M = keyboard.nextInt();

        // Intersections are numbered 0..N-1
        Digraph graph = new Digraph(N);
        for (int i = 0; i < M; i++) {
            int u = keyboard.nextInt();
            int v = keyboard.nextInt();
            graph.addEdge(u, v);
        }

        // Last line: start intersection X and end intersection Y
        int x = keyboard.nextInt();
        int y = keyboard.nextInt();

        // Search for the lexicographically smallest valid route
        DirectedDFS dfs = new DirectedDFS(graph, x, y);
        if (!dfs.hasRoute()) {
            System.out.println(-1);
            return;
        }

        // Print K = M + 1 and the intersections separated by single spaces
        StringBuilder output = new StringBuilder();
        String separator = "";
        for (int v : dfs.route()) {
            output.append(separator).append(v);
            separator = " ";
        }
        System.out.println(M + 1);
        System.out.println(output);
    }
}