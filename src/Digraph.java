import java.util.*;

public class Digraph {
     private int n;
     private ArrayList<Integer>[] adj;
     private int[] indeg, outdeg;

     Digraph(int n){
         this.n=n;
         adj=(ArrayList<Integer>[]) new ArrayList[n];
         for(int i=0; i<n; i++) adj[i] = new ArrayList<>();
         indeg= new int[n];
         outdeg= new int[n];
     }

     void addEdge(int u, int v){
         //adds edge u->v and updates degree count
         adj[u].add(v);
         outdeg[u]++;
         indeg[v]++;
     }
     int v(){
         return n;
     }
     int inDegree(int v){
         return indeg[v];
     }
     int outDegree(int v){
         return outdeg[v];
     }
     void sortAdjacency(){
         for(int i=0; i<n;i++)
             Collections.sort(adj[i]); //??
     }
    ArrayList<Integer> adjList(int v) {
         return adj[v]; }
    boolean degreesOk(int x, int y)
    {
        for (int v = 0; v < n; v++) {
            int d = outdeg[v] - indeg[v];
            int need = (v == x) ? 1 : (v == y) ? -1 : 0;
            if (d != need) return false;
        }
        return true;
    }
    ArrayList<Integer> eulerPath(int x)
    {
        int[] ptr = new int[n];
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        ArrayList<Integer> path = new ArrayList<>();
        stack.push(x);
        while (!stack.isEmpty()) {
            int v = stack.peek();
            if (ptr[v] < adj[v].size()) {
                stack.push(adj[v].get(ptr[v]++));
            } else {
                path.add(stack.pop());
            }
        }
        Collections.reverse(path);
        return path;
    }
}
