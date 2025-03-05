package library.graph;

import static java.lang.Math.*;

import library.util.*;

public class MinCostFlow extends Dijkstra<long[], Long>{
  private long[] h;

  public MinCostFlow(int n){
    super(n,false);
    h = new long[n];
  }

  @Override
  protected Long zero(){ return 0L; }

  @Override
  protected Long inf(){ return Util.infL; }

  @Override
  protected Long f(Long l,Edge<long[]> e){ return e.val[0] == 0 ? inf() : l +e.val[1] +h[e.u] -h[e.v]; }

  @Override
  protected long[] inv(long[] l){ return new long[]{0, -l[1]}; }

  public void addEdge(int u,int v,long cap,long cost){ addEdge(u,v,new long[]{cap, cost}); }

  public long[] flow(int s,int t,long flow){
    long[] ret = new long[2];
    while (0 < flow) {
      calc(s);
      var path = path(t);
      if (path.isEmpty())
        break;

      long cost = 0;
      long f = flow;
      for (var e:path)
        f = min(f,e.val[0]);
      for (var e:path) {
        cost += e.val[1];
        e.val[0] -= f;
        e.re.val[0] += f;
      }
      flow -= f;
      ret[0] += cost *f;
      ret[1] += f;
      for (int i = 0;i < n;i++)
        h[i] += get(i);
    }
    return ret;
  }
}