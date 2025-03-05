package library.graph;

import static java.util.Arrays.*;

import library.dataStructure.collection.*;

public class SCC extends Graph<Object>{
  private int[][] scc;
  private int[] label;
  private boolean[] seen;

  public SCC(Graph<?> g){
    super(g.n,true);
    scc = new int[n][];
    label = new int[n];
    seen = new boolean[n];
    int counter = n;

    MyList<Integer> stk = new MyList<>(n <<1);
    for (int i = 0;i < n;i++) {
      stk.add(~i);
      stk.add(i);
      while (!stk.isEmpty()) {
        int u = stk.pollLast();
        if (u >= 0) {
          if (seen[u]) {
            stk.pollLast();
            continue;
          }
          seen[u] = true;
          for (var e:g.go(u)) {
            stk.add(~e.v);
            stk.add(e.v);
          }
        } else
          label[--counter] = ~u;
      }
    }

    seen = new boolean[n];
    int si = 0;
    for (var t:label) {
      if (seen[t])
        continue;
      MyList<Integer> list = new MyList<>();

      stk.add(t);
      while (!stk.isEmpty()) {
        int u = stk.pollLast();
        if (seen[u])
          continue;
        seen[u] = true;
        list.add(u);
        for (var e:g.bk(u))
          stk.add(e.v);
      }
      scc[si++] = list.toIntArray(a -> a);
    }

    scc = copyOf(scc,n = si);
    int[] map = new int[n],arr = new int[g.es.size()];
    for (int i = n;i-- > 0;) {
      int cnt = 0;
      for (var j:scc[i]) {
        map[j] = i;
        for (var e:g.go(j))
          arr[cnt++] = map[e.v];
      }
      sort(arr,0,cnt);
      for (int k = 0,p = 0;k < cnt;p = arr[k++])
        if (i != arr[k] && arr[k] != p)
          addEdge(i,arr[k]);
    }
  }

  public int[] us(int u){ return scc[u]; }
}