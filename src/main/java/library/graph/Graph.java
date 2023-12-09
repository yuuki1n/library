package library.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * グラフ
 * @author yuuki_n
 *
 * @param <L>
 */
public class Graph<L> {
  public int n;
  public List<Edge<L>> es;
  private List<Edge<L>>[] go,bk;

  @SuppressWarnings("unchecked")
  public Graph(int n,boolean dir){
    this.n = n;
    go = new List[n];
    bk = dir ? new List[n] : go;
    for (int i = 0;i < n;i++) {
      go[i] = new ArrayList<>();
      bk[i] = new ArrayList<>();
    }
    es = new ArrayList<>();
  }

  public void addEdge(int u,int v){ addEdge(u,v,null); }

  public void addEdge(int u,int v,L l){
    var e = new Edge<>(es.size(),u,v,l);
    var re = new Edge<>(e.id,e.v,e.u,inv(e.val));
    es.add(e);
    go[u].add(re.re = e);
    bk[v].add(e.re = re);
  }

  protected L inv(L l){ return l; }

  public List<Edge<L>> go(int ui){ return go[ui]; }

  public List<Edge<L>> back(int ui){ return bk[ui]; }
}
