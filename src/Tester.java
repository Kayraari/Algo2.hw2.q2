//-----------------------------------------------------
// Title: Tester class
// Author: Kayra Arı, Elçin Karagül
// ID:
// Section: 4
// Assignment: 2
// Description: This class tests the solution of Question 2.
// It runs Main with fixed key test cases (sample inputs, a
// single street, wrong start and end, correct degrees but
// disconnected streets, a case where the smallest neighbour
// must not be taken first, two-digit numbers, opposite
// streets, unused intersections) and a large network for
// running time. It also generates random networks and
// compares the output with an independent brute-force method
// that tries every possible route.
//-----------------------------------------------------

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class Tester {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        //--------------------------------------------------------
        // Summary: Runs all fixed tests, the large test and the
        // random tests, then prints a summary.
        // Precondition: Main, Digraph and DirectedDFS classes are
        // compiled in the same folder.
        // Postcondition: Each test is printed as PASS or FAIL and
        // the totals are printed.
        //--------------------------------------------------------
        System.out.println("=== Key test points ===");

        // 1. First sample input given in the assignment
        check("Sample input 1",
                "5 7\n0 1\n1 2\n2 0\n0 3\n3 4\n4 0\n0 2\n0 2\n",
                "8\n0 1 2 0 3 4 0 2\n");

        // 2. Second sample input: two branches leave 0, no way back
        check("Sample input 2",
                "4 3\n0 1\n1 2\n0 3\n0 2\n",
                "-1\n");

        // 3. Smallest possible network: one street
        check("Single street",
                "2 1\n0 1\n0 1\n",
                "2\n0 1\n");

        // 4. The street goes from 0 to 1 but the route must go from 1 to 0
        check("Street in the wrong direction",
                "2 1\n0 1\n1 0\n",
                "-1\n");

        // 5. Degrees are correct, but the cycle 2 -> 3 -> 2 cannot be
        //    reached, so checking only the degrees would be wrong
        check("Correct degrees but disconnected streets",
                "4 3\n0 1\n2 3\n3 2\n0 1\n",
                "-1\n");

        // 6. Going to 1 first is a dead end; the route must be 0 2 0 1
        check("Smallest neighbour is a dead end",
                "3 3\n0 1\n0 2\n2 0\n0 1\n",
                "4\n0 2 0 1\n");

        // 7. 9 is smaller than 10: numbers are compared as numbers
        check("Lexicographic tie with two-digit numbers",
                "11 5\n0 9\n9 0\n0 10\n10 0\n0 1\n0 1\n",
                "6\n0 9 0 10 0 1\n");

        // 8. Streets in opposite directions are separate streets
        check("Opposite streets",
                "3 3\n0 1\n1 0\n0 2\n0 2\n",
                "4\n0 1 0 2\n");

        // 9. Sample network, but a different end is required
        check("Wrong end intersection",
                "5 7\n0 1\n1 2\n2 0\n0 3\n3 4\n4 0\n0 2\n0 3\n",
                "-1\n");

        // 10. Intersections 0, 1 and 2 have no street at all
        check("Unused intersections",
                "5 1\n3 4\n3 4\n",
                "2\n3 4\n");

        // 11. Start is not the smallest vertex, route passes it twice
        check("Start in the middle",
                "3 4\n2 0\n0 1\n1 2\n2 1\n2 1\n",
                "5\n2 0 1 2 1\n");

        // 12. Largest allowed network (2000 intersections, 20000 streets)
        largeTest(2000, 20000);

        System.out.println();
        System.out.println("=== Randomly generated tests ===");
        randomTests(3000, 2026);

        System.out.println();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }

    private static void check(String name, String input, String expected) {
        //--------------------------------------------------------
        // Summary: Runs Main with the input and compares the output
        // with the expected output.
        // Precondition: input is valid for Question 2.
        // Postcondition: PASS or FAIL is printed and counters are
        // updated.
        //--------------------------------------------------------
        String actual = runMain(input);
        if (actual.equals(expected)) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name);
            System.out.println("  Input:\n" + input);
            System.out.println("  Expected:\n" + expected);
            System.out.println("  Actual:\n" + actual);
        }
    }

    private static String runMain(String input) {
        //--------------------------------------------------------
        // Summary: Runs Main.main with the given text as standard
        // input and returns what Main prints.
        // Precondition: input is a valid input string.
        // Postcondition: System.in and System.out are restored.
        //--------------------------------------------------------
        java.io.InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(buffer));
            Main.main(new String[0]);
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
        return buffer.toString().replace("\r\n", "\n");
    }

    private static int randomWalk(Random random, int n, int m, int start,
                                  boolean[][] street, int[] from, int[] to) {
        //--------------------------------------------------------
        // Summary: Builds a network by walking randomly from start
        // and adding every street that is walked. So a route that
        // uses every street once is known to exist.
        // Precondition: n >= 2, from and to have length m.
        // Postcondition: Returns the number of streets added; the
        // walk ends at to[result - 1].
        //--------------------------------------------------------
        int v = start;
        int count = 0;
        int tries = 0;
        while (count < m && tries < 50 * m) {
            tries++;
            int w = random.nextInt(n);
            if (w != v && !street[v][w]) {
                street[v][w] = true;
                from[count] = v;
                to[count] = w;
                count++;
                v = w;
            }
        }
        return count;
    }

    private static void largeTest(int n, int m) {
        //--------------------------------------------------------
        // Summary: Builds a random walk with m streets, shuffles
        // the streets and checks that Main prints a valid route
        // within 2 seconds.
        // Precondition: 2 <= n and m <= n(n-1).
        // Postcondition: PASS or FAIL is printed.
        //--------------------------------------------------------
        Random random = new Random(224);
        boolean[][] street = new boolean[n][n];
        int[] from = new int[m];
        int[] to = new int[m];
        int count = randomWalk(random, n, m, 0, street, from, to);
        int x = 0;
        int y = to[count - 1];
        if (x == y) {                   // X and Y must be different
            count--;
            street[from[count]][to[count]] = false;
            y = to[count - 1];
        }
        String input = buildInput(random, n, count, from, to, x, y);

        long start = System.currentTimeMillis();
        String out = runMain(input);
        long time = System.currentTimeMillis() - start;

        String[] lines = out.trim().split("\n");
        boolean ok = lines.length == 2
                && Integer.parseInt(lines[0].trim()) == count + 1
                && validRoute(lines[1].trim().split(" "), street, count, x, y)
                && time < 2000;
        String name = "Large network " + n + " / " + count + " (" + time + " ms)";
        if (ok) { passed++; System.out.println("PASS  " + name); }
        else { failed++; System.out.println("FAIL  " + name); }
    }

    private static boolean validRoute(String[] parts, boolean[][] street,
                                      int m, int x, int y) {
        //--------------------------------------------------------
        // Summary: Checks that the printed sequence is a valid
        // route: it starts at x, ends at y, has m + 1 entries and
        // uses only existing streets, each of them exactly once.
        // Precondition: parts contains the printed intersections.
        // Postcondition: Returns true if the route is valid.
        //--------------------------------------------------------
        if (parts.length != m + 1) return false;
        int[] r = new int[parts.length];
        for (int i = 0; i < parts.length; i++) r[i] = Integer.parseInt(parts[i]);
        if (r[0] != x || r[m] != y) return false;
        boolean[][] used = new boolean[street.length][street.length];
        for (int i = 0; i < m; i++) {
            if (!street[r[i]][r[i + 1]] || used[r[i]][r[i + 1]]) return false;
            used[r[i]][r[i + 1]] = true;
        }
        return true;
    }

    private static void randomTests(int count, long seed) {
        //--------------------------------------------------------
        // Summary: Generates random networks and compares Main with
        // the brute-force expected output. Some networks are random
        // walks (a route exists), some have a wrong end, and some
        // are completely random (usually no route).
        // Precondition: count > 0.
        // Postcondition: Number of passed random tests is printed;
        // every failing case is printed.
        //--------------------------------------------------------
        Random random = new Random(seed);
        int ok = 0;
        int withRoute = 0;

        for (int test = 0; test < count; test++) {
            int n = 2 + random.nextInt(5);
            int m = 1 + random.nextInt(Math.min(11, n * (n - 1)));
            boolean[][] street = new boolean[n][n];
            int[] from = new int[m];
            int[] to = new int[m];
            int x;
            int y;

            if (random.nextInt(3) > 0) {
                // Random walk: a route from x to the end of the walk exists
                x = random.nextInt(n);
                m = randomWalk(random, n, m, x, street, from, to);
                y = to[m - 1];
                if (random.nextInt(6) == 0) y = random.nextInt(n);  // wrong end
            } else {
                // Completely random streets
                int added = 0;
                while (added < m) {
                    int u = random.nextInt(n);
                    int v = random.nextInt(n);
                    if (u != v && !street[u][v]) {
                        street[u][v] = true;
                        from[added] = u;
                        to[added] = v;
                        added++;
                    }
                }
                x = random.nextInt(n);
                y = random.nextInt(n);
            }
            if (x == y) { test--; continue; }   // X and Y must be different

            String input = buildInput(random, n, m, from, to, x, y);
            String expected = bruteForce(n, m, street, x, y);
            String actual = runMain(input);
            if (!expected.equals("-1\n")) withRoute++;

            if (actual.equals(expected)) {
                ok++;
                passed++;
            } else {
                failed++;
                System.out.println("FAIL  random test " + (test + 1));
                System.out.println("  Input:\n" + input);
                System.out.println("  Expected:\n" + expected);
                System.out.println("  Actual:\n" + actual);
            }
        }
        System.out.println("Random tests passed: " + ok + " / " + count
                + " (" + withRoute + " of them have a route)");
    }

    private static String buildInput(Random random, int n, int m,
                                     int[] from, int[] to, int x, int y) {
        //--------------------------------------------------------
        // Summary: Writes a network in the input format of the
        // assignment, with the streets in random order.
        // Precondition: from[i] -> to[i] are the m streets.
        // Postcondition: The input text is returned.
        //--------------------------------------------------------
        int[] order = new int[m];
        for (int i = 0; i < m; i++) order[i] = i;
        for (int i = m - 1; i > 0; i--) {       // shuffle
            int j = random.nextInt(i + 1);
            int t = order[i]; order[i] = order[j]; order[j] = t;
        }
        StringBuilder input = new StringBuilder();
        input.append(n).append(" ").append(m).append("\n");
        for (int i = 0; i < m; i++)
            input.append(from[order[i]]).append(" ").append(to[order[i]]).append("\n");
        input.append(x).append(" ").append(y).append("\n");
        return input.toString();
    }

    // Route found by the brute-force search
    private static int[] bestRoute;

    private static String bruteForce(int n, int m, boolean[][] street, int x, int y) {
        //--------------------------------------------------------
        // Summary: Computes the expected output by trying every
        // possible route from x with backtracking.
        // Precondition: street[u][v] is true if street u -> v exists.
        // Postcondition: Returns the expected output text.
        //--------------------------------------------------------
        bestRoute = null;
        int[] route = new int[m + 1];
        route[0] = x;
        boolean[][] used = new boolean[n][n];
        search(n, m, street, used, y, route, 0);
        if (bestRoute == null) return "-1\n";
        StringBuilder out = new StringBuilder();
        out.append(m + 1).append("\n");
        for (int i = 0; i <= m; i++) {
            if (i > 0) out.append(" ");
            out.append(bestRoute[i]);
        }
        return out.append("\n").toString();
    }

    private static void search(int n, int m, boolean[][] street, boolean[][] used,
                               int y, int[] route, int length) {
        //--------------------------------------------------------
        // Summary: Tries every unused street from the last
        // intersection in ascending order and undoes the choice if
        // it does not lead to a complete route (backtracking). The
        // first complete route found is the lexicographically
        // smallest one.
        // Precondition: route[0..length] uses length different
        // streets.
        // Postcondition: bestRoute holds the route if one exists.
        //--------------------------------------------------------
        if (bestRoute != null) return;
        int last = route[length];
        if (length == m) {
            if (last == y) bestRoute = route.clone();
            return;
        }
        for (int w = 0; w < n; w++) {
            if (street[last][w] && !used[last][w]) {
                used[last][w] = true;
                route[length + 1] = w;
                search(n, m, street, used, y, route, length + 1);
                used[last][w] = false;
            }
        }
    }
}