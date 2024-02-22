package library.graph;

import static java.lang.Math.*;

import library.util.*;

public class MaxFlow extends Graph<Long>{
  private long maxCap = 0;
  private int[] ei,level;

  public MaxFlow(int n){ super(n,false); }

  @Override
  protected Long inv(Long l){ return 0L; }

  @Override
  public void addEdge(int u,int v,Long l){
    maxCap = max(maxCap,l);
    super.addEdge(u,v,l);
  }

  public long maxFlow(int s,int t){
    long flow = 0L;

    for (long base = Long.highestOneBit(maxCap);base > 0;base >>= 1)
      while (true) {
        bfs(s,t,base);
        if (level[t] == 0)
          break;
        ei = new int[n];
        for (long f;0 < (f = dfs(s,t,Util.infL,base));)
          flow += f;
      }
    return flow;
  }

  private void bfs(int s,int t,long base){
    int[] que = new int[n];
    level = new int[n];
    int hd = 0,tl = 0;
    level[que[tl++] = s] = 1;
    while (hd < tl) {
      int u = que[hd++];
      for (var e:go(u)) {
        int v = e.v;
        if (e.val < base || level[v] > 0)
          continue;
        level[v] = level[u] +1;
        if ((que[tl++] = v) == t)
          break;
      }
    }
  }

  private long dfs(int s,int u,long flow,long base){
    if (u == s)
      return flow;
    long ret = 0;
    var list = bk(u);
    for (int eM = list.size();ei[u] < eM;ei[u]++) {
      Edge<Long> re = list.get(ei[u]),e = re.re;
      if (level[u] <= level[e.u] || e.val < base)
        continue;
      long f = dfs(s,e.u,min(flow -ret,e.val),base);
      e.val -= f;
      re.val += f;
      ret += f;
      if (ret == flow)
        break;
    }
    return ret;
  }
}