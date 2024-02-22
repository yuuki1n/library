package library.graph.tree;

import static java.util.Arrays.*;

import library.dataStructure.collection.*;
import library.dataStructure.rangeData.base.*;
import library.dataStructure.rangeData.segmentTree.*;
import library.graph.*;

public abstract class HLD<L, V extends BaseV, F> extends Graph<L>{
  private int[] p,hp,l,r;
  private Seg<V, F> rev,seg;

  public HLD(int n,boolean reversible){
    super(n,false);
    p = new int[n];
    hp = new int[n];
    l = new int[n];
    r = new int[n];
    seg = new LazySegmentTree<>(n){
      @Override
      protected void agg(V v,V a,V b){ HLD.this.agg(v,a,b); }

      @Override
      protected F comp(F f,F g){ return HLD.this.comp(f,g); }

      @Override
      protected V e(){ return HLD.this.e(); }

      @Override
      protected void map(V v,F f){ HLD.this.map(v,f); }
    };

    rev = reversible ? new LazySegmentTree<>(n){
      @Override
      protected void agg(V v,V a,V b){ HLD.this.agg(v,b,a); }

      @Override
      protected F comp(F f,F g){ return HLD.this.comp(f,g); }

      @Override
      protected V e(){ return HLD.this.e(); }

      @Override
      protected void map(V v,F f){ HLD.this.map(v,f); }
    } : seg;

  }

  public void updEdge(int e,F f){ upd(es.get(e).v,f); }

  public void upd(Edge<L> e,F f){ upd(l[e.u] > l[e.v] ? e.u : e.v,f); }

  public void upd(int u,F f){
    rev.upd(l[u],f);
    if (rev != seg)
      seg.upd(l[u],f);
  }

  public void updSub(int u,F f){
    rev.upd(l[u],r[u],f);
    if (rev != seg)
      seg.upd(l[u],r[u],f);
  }

  public void updPath(int u,int v,int incLca,F f){
    while (true) {
      if (l[u] > l[v]) {
        var t = u;
        u = v;
        v = t;
      }

      var h = hp[v];
      if (l[h] <= l[u]) {
        rev.upd(l[u] +1 -incLca,l[v] +1,f);
        if (rev != seg)
          seg.upd(l[u] +1 -incLca,l[v] +1,f);
        return;
      }

      rev.upd(l[h],l[v] +1,f);
      if (rev != seg)
        seg.upd(l[h],l[v] +1,f);
      v = p[h];
    }
  }

  public V get(int u){ return rev.get(l[u]); }

  public V getSub(int u,int incLca){ return rev.get(l[u] +1 -incLca,r[u]); }

  public V getPath(int u,int v,int incLca){
    V vl = e(),vr = e();
    boolean tog = false;
    while (true) {
      if (l[u] > l[v]) {
        var t = u;
        u = v;
        v = t;
        tog ^= true;
      }

      var h = hp[v];
      if (l[h] <= l[u]) {
        if (tog)
          ag(vl,vl,rev.get(l[u] +1 -incLca,l[v] +1));
        else
          ag(vr,seg.get(l[u] +1 -incLca,l[v] +1),vr);
        ag(vl,vl,vr);
        return vl;
      }

      if (tog)
        ag(vl,vl,rev.get(l[h],l[v] +1));
      else
        ag(vr,seg.get(l[h],l[v] +1),vr);
      v = p[h];
    }
  }

  int lca(int u,int v){
    while (true) {
      if (l[u] > l[v]) {
        var t = u;
        u = v;
        v = t;
      }

      var h = hp[v];
      if (l[h] <= l[u])
        return u;

      v = p[h];
    }
  }

  public void makeTree(int s){
    MyStack<Integer> stk = new MyStack<>();
    fill(hp,-1);
    p[s] = s;
    stk.add(s);
    stk.add(s);
    while (!stk.isEmpty()) {
      var u = stk.pop();
      if (r[u] < 1) {
        r[u] = 1;
        for (var e:go(u)) {
          if (e.v == p[u])
            continue;
          es.set(e.id,e);
          p[e.v] = u;
          stk.add(e.v);
          stk.add(e.v);
        }
      } else if (u != s)
        r[p[u]] += r[u];
    }

    for (int u = 0;u < n;u++) {
      var go = go(u);
      for (int i = 1;i < go.size();i++)
        if (r[u] < r[go.get(0).v] || r[go.get(0).v] < r[go.get(i).v] && r[go.get(i).v] < r[u])
          go.swap(0,i);
    }

    stk.add(s);
    for (int hid = 0;!stk.isEmpty();) {
      var u = stk.pop();
      r[u] += l[u] = hid++;
      if (hp[u] < 0)
        hp[u] = u;
      var go = go(u);
      for (int i = go.size();i-- > 0;) {
        var v = go.get(i).v;
        if (v == p[u])
          continue;
        if (i == 0)
          hp[v] = hp[u];
        stk.add(v);
      }
    }

  }

  protected abstract V e();
  protected abstract void agg(V v,V a,V b);
  protected abstract void map(V v,F f);

  protected F comp(F f,F g){ return null; }

  private void ag(V v,V a,V b){
    agg(v,a,b);
    v.sz = a.sz +b.sz;
  }
}