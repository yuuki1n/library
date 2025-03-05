package library.graph;

import static java.util.Arrays.*;

import library.dataStructure.collection.*;
import library.util.*;

/**
 * グラフ
 * @author yuuki_n
 *
 * @param <L>
 */
public class Graph<L> {
  protected int n;
  protected MyList<Edge<L>> es;
  private Edge<L>[][] go,bk;
  private int[] cntgo,cntbk;
  private boolean built;

  public Graph(int n,boolean dir){
    this.n = n;
    go = Util.cast(new Edge[n][]);
    bk = dir ? Util.cast(new Edge[n][]) : go;
    fill(go,new Edge[0]);
    fill(bk,new Edge[0]);
    es = new MyList<>();
    cntgo = new int[n];
    cntbk = dir ? new int[n] : cntgo;
  }

  public int size(){ return n; }

  protected L inv(L l){ return l; }

  public void addEdge(int u,int v){ addEdge(u,v,null); }

  public void addEdge(int u,int v,L l){
    var e = new Edge<>(es.size(),u,v,l);
    var re = new Edge<>(e.id,e.v,e.u,inv(e.val));
    es.add(e);
    re.re = e;
    e.re = re;
  }

  public Edge<L>[] go(int u){
    if (!built)
      build();
    return go[u];
  }

  public Edge<L>[] bk(int u){
    if (!built)
      build();
    return bk[u];
  }

  private void build(){
    for (var e:es) {
      cntgo[e.u]++;
      cntbk[e.v]++;
    }
    for (var e:es) {
      if (go[e.u].length == 0)
        go[e.u] = Util.cast(new Edge[cntgo[e.u]]);
      if (bk[e.v].length == 0)
        bk[e.v] = Util.cast(new Edge[cntbk[e.v]]);
    }
    for (var e:es) {
      go[e.u][--cntgo[e.u]] = e;
      bk[e.v][--cntbk[e.v]] = e.re;
    }
    built = true;
  }

  public void clear(){
    Edge<L>[] empty = Util.cast(new Edge[0]);
    for (var e:es)
      go[e.u] = bk[e.v] = empty;
    es.clear();
    built = false;
  }
}