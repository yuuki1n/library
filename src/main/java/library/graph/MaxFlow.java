package library.graph;

import static java.lang.Math.*;

import java.util.*;

import library.util.*;

public class MaxFlow extends Graph<Long>{
  private long maxCap;
  private int[] ei,level;
  private int s = -1,t = -1;

  public MaxFlow(int n){ super(n,false); }

  @Override
  protected Long inv(Long l){ return 0L; }

  @Override
  public void addEdge(int u,int v,Long l){
    maxCap = max(maxCap,l);
    super.addEdge(u,v,l);
  }

  public long maxFlow(int s,int t){
    assert this.s < 0 && this.t < 0;
    this.s = s;
    this.t = t;
    long flow = 0L;
    for (long base = Long.highestOneBit(maxCap);base > 0;base >>= 1)
      while (true) {
        bfs(base);
        if (level[t] == 0)
          break;
        ei = new int[n];
        for (long f;0 < (f = dfs(t,Util.infL,base));)
          flow += f;
      }
    return flow;
  }

  public List<Edge<Long>> useEgdes(){
    int[] len = Util.arrI(n,i -> Util.infI),q = new int[n];
    int qi = 0,qj = 0;
    len[s] = 0;
    q[qj++] = s;
    for (int u;qi < qj;)
      for (var e:go(u = q[qi++]))
        if (0 < e.val && len[e.v] > len[u] +1)
          len[q[qj++] = e.v] = len[u] +1;

    return es.stream().filter(e -> len[e.u] < Util.infI && len[e.v] == Util.infI).toList();
  }

  private void bfs(long base){
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

  private long dfs(int u,long flow,long base){
    if (u == s)
      return flow;
    long ret = 0;
    var list = bk(u);
    for (int eM = list.length;ei[u] < eM;ei[u]++) {
      Edge<Long> re = list[ei[u]],e = re.re;
      if (level[u] <= level[e.u] || e.val < base)
        continue;
      long f = dfs(e.u,min(flow -ret,e.val),base);
      e.val -= f;
      re.val += f;
      ret += f;
      if (ret == flow)
        break;
    }
    return ret;
  }
}