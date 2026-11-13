import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        int n = keyboard.nextInt(), m = keyboard.nextInt();
        Digraph g = new Digraph(n);
        for (int i = 0; i < m; i++) g.addEdge(keyboard.nextInt(), keyboard.nextInt());
        int x = keyboard.nextInt(), y = keyboard.nextInt();

        if (!g.degreesOk(x, y)) { System.out.println(-1); return; }

        g.sortAdjacency();
        ArrayList<Integer> path = g.eulerPath(x);
        if (path.size() != m + 1 || path.get(m) != y) {
            System.out.println(-1);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(m + 1).append("\n");
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(path.get(i));
        }
        System.out.println(sb);
    }
}
