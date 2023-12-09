package library.graph;

import static java.util.Arrays.*;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

public abstract class Dijkstra<E, L> extends Graph<E>{
  private L[] len;
  private int[] arr,rev;
  private Edge<E>[] pre;
  private L zero = zero(),inf = inf();
  private Comparator<L> cmp = cmp();
  private int sz;

  public Dijkstra(int n,boolean dir){
    super(n,dir);
    arr = new int[n];
    rev = new int[n];
  }

  protected abstract L zero();
  protected abstract L inf();
  protected abstract L f(L l,Edge<E> val);

  @SuppressWarnings("unchecked")
  protected Comparator<L> cmp(){ return (Comparator<L>) Comparator.naturalOrder(); }

  public void calc(int s){ calc(s,-1); }

  @SuppressWarnings("unchecked")
  public void calc(int s,int g){
    len = (L[]) new Object[n];
    fill(len,inf);
    setAll(arr,i -> i);
    setAll(rev,i -> i);
    pre = new Edge[n];
    sz = n;
    set(s,zero);
    for (int cur;0 < sz && (cur = poll()) != g;) {
      L l = len[cur];
      for (var e:go(cur)) {
        L ll = f(l,e);
        if (cmp.compare(ll,len[e.v]) < 0)
          set((pre[e.v] = e).v,ll);
      }
    }
  }

  public L len(int t){ return len[t]; }

  public Deque<Edge<E>> path(int t){
    Deque<Edge<E>> ret = new ArrayDeque<>();
    while (pre[t] != null) {
      ret.addFirst(pre[t]);
      t = pre[t].u;
    }

    return ret;
  }

  private void set(int i,L l){
    if (sz <= rev[i] || cmp.compare(len[i],l) <= 0)
      return;
    len[i] = l;
    heapfy(rev[i]);
  }

  private int poll(){
    int ret = arr[0];
    heapfy(swap(0,--sz));
    return ret;
  }

  private void heapfy(int k){
    int p = k -1 >>1;
    if (0 <= p && cmp.compare(len[arr[p]],len[arr[k]]) > 0) {
      heapfy(swap(p,k));
      return;
    }

    int c = k <<1 |1;
    if (sz <= c)
      return;

    if (c +1 < sz && cmp.compare(len[arr[c +1]],len[arr[c]]) < 0)
      c++;

    if (cmp.compare(len[arr[c]],len[arr[k]]) < 0)
      heapfy(swap(c,k));
  }

  private int swap(int i,int j){
    int t = arr[i];
    arr[i] = arr[j];
    arr[j] = t;
    rev[arr[i]] = i;
    rev[arr[j]] = j;
    return i;
  }
}