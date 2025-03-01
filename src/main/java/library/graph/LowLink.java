package library.graph;

import static java.util.Arrays.*;

import java.util.*;

import library.util.*;

public class LowLink extends Graph<Long>{
  private int[] low,ord,inc;

  public LowLink(int n){ super(n,false); }

  public void build(){
    low = new int[n];
    ord = new int[n];
    inc = new int[n];
    fill(ord,-1);
    Iterator<Edge<Long>>[] itr = Util.cast(new Iterator[n]);
    for (int v = 0;v < n;v++)
      itr[v] = go(v).iterator();
    int[] stk = new int[n +2];
    stk[0] = -1;
    for (int root = 0,s = 0,count = 0;root < n;root++) {
      if (ord[root] != -1)
        continue;
      ord[root] = low[root] = count++;
      inc[root]--;
      stk[s = 1] = root;
      while (0 < s) {
        int v = stk[s],p = stk[s -1];
        if (itr[v].hasNext()) {
          int w = itr[v].next().v;
          if (ord[w] == -1) {
            ord[w] = low[w] = count++;
            stk[++s] = w;
          } else if (w != p && low[v] > ord[w])
            low[v] = ord[w];
        } else {
          s--;
          if (p != -1) {
            if (low[p] > low[v])
              low[p] = low[v];
            if (ord[p] <= low[v])
              inc[p]++;
          }
        }
      }
    }
  }

  public boolean isBridge(int x,int y){ return ord[x] < low[y] || ord[y] < low[x]; }

  public boolean isArticulation(int x){ return inc[x] > 0; }

  public int articulationValue(int x){ return inc[x]; }
}