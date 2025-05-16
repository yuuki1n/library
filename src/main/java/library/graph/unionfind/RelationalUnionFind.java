package library.graph.unionfind;

import static java.util.Arrays.*;

public abstract class RelationalUnionFind<F> extends UnionFind{
  private F[] rel;

  @SuppressWarnings("unchecked")
  public RelationalUnionFind(int n){
    super(n);
    rel = (F[]) new Object[n];
    setAll(rel,i -> id());
  }

  protected abstract F id();
  protected abstract F comp(F a,F b);
  protected abstract F inv(F v);
  protected abstract boolean valid(F a,F b);

  public boolean valid(int u,int v,F c){ return !same(u,v) || valid(rel(u,v),c); }

  @Override
  public int root(int x){
    if (dat[x] < 0)
      return x;
    int r = root(dat[x]);
    rel[x] = comp(rel[dat[x]],rel[x]);
    return dat[x] = r;
  }

  @Deprecated
  @Override
  public boolean unite(int u,int v){ throw new UnsupportedOperationException(); }

  public boolean unite(int u,int v,F f){
    if (same(u,v))
      return valid(rel(u,v),f);

    f = comp(rel(u),f);
    f = comp(f,inv(rel(v)));
    super.unite(u = root(u),v = root(v));
    if (dat[u] > dat[v])
      rel[u] = inv(f);
    else
      rel[v] = f;
    return true;
  }

  public F rel(int x){
    root(x);
    return rel[x];
  }

  public F rel(int u,int v){ return !same(u,v) ? null : comp(inv(rel(u)),rel(v)); }
}