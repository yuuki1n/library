package library.graph;

import static java.util.Arrays.*;

import java.lang.reflect.*;
import java.util.*;

import library.util.*;

public abstract class Dijkstra<E, L> extends Graph<E>{
  private Comparator<L> cmp;
  private L[] len;
  private int[] hep,idx;
  private Edge<E>[] pre;
  private int sz;

  public Dijkstra(int n,boolean dir){
    super(n,dir);
    hep = new int[n];
    idx = new int[n];
    cmp = cmp();
  }

  protected abstract L zero();
  protected abstract L inf();
  protected abstract L f(L l,Edge<E> e);

  protected Comparator<L> cmp(){ return Util.cast(Comparator.naturalOrder()); }

  public L[] calc(int s){ return calc(s,-1); }

  public L[] calc(int s,int g){
    len = Util.cast(Array.newInstance(zero().getClass(),sz = n));
    pre = Util.cast(new Edge[n]);
    fill(len,inf());
    setAll(hep,i -> i);
    setAll(idx,i -> i);
    set(s,zero());
    for (int cur;0 < sz && (cur = poll()) != g;)
      for (var e:go(cur))
        set((pre[e.v] = e).v,f(len[cur],e));
    return len;
  }

  public L get(int t){ return len[t]; }

  public Deque<Edge<E>> path(int t){
    Deque<Edge<E>> ret = new ArrayDeque<>();
    while (pre[t] != null) {
      ret.addFirst(pre[t]);
      t = pre[t].u;
    }

    return ret;
  }

  private void set(int i,L l){
    if (idx[i] < sz && cmp.compare(l,len[i]) < 0) {
      len[i] = l;
      heapfy(idx[i]);
    }
  }

  private int poll(){
    int ret = hep[0];
    heapfy(swap(0,--sz));
    return ret;
  }

  private void heapfy(int k){
    int p = k -1 >>1;
    if (0 <= p && cmp.compare(len[hep[p]],len[hep[k]]) > 0) {
      heapfy(swap(p,k));
      return;
    }

    int c = k <<1 |1;
    if (sz <= c)
      return;

    if (c +1 < sz && cmp.compare(len[hep[c]],len[hep[c +1]]) > 0)
      c++;

    if (cmp.compare(len[hep[c]],len[hep[k]]) < 0)
      heapfy(swap(c,k));
  }

  private int swap(int i,int j){
    hep[i] ^= hep[j];
    hep[j] ^= hep[i];
    hep[i] ^= hep[j];
    idx[hep[i]] = i;
    idx[hep[j]] = j;
    return i;
  }
}