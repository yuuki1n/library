package library.graph.unionfind;

import static java.util.Arrays.*;

public abstract class RelationalUnionFind<F> extends UnionFind{
  private F[] dist;

  @SuppressWarnings("unchecked")
  public RelationalUnionFind(int n){
    super(n);
    dist = (F[]) new Object[n];
    setAll(dist,i -> id());
  }

  protected abstract F id();
  protected abstract F comp(F a,F b);
  protected abstract F inv(F v);
  protected abstract boolean eq(F a,F b);

  @Override
  public int root(int x){
    if (dat[x] < 0)
      return x;

    int r = root(dat[x]);
    dist[x] = comp(dist[dat[x]],dist[x]);

    return dat[x] = r;
  }

  public boolean valid(int u,int v,F c){ return !same(u,v) || eq(dist(u,v),c); }

  @Deprecated
  @Override
  public boolean unite(int u,int v){ return unite(u,v,id()); }

  public boolean unite(int u,int v,F f){
    if (!valid(u,v,f))
      return false;
    if (same(u,v))
      return true;
    f = comp(dist(u),f);
    f = comp(f,inv(dist(v)));
    super.unite(u = root(u),v = root(v));
    if (dat[u] > dat[v])
      dist[u] = inv(f);
    else
      dist[v] = f;
    return true;
  }

  public F dist(int x){
    root(x);
    return dist[x];
  }

  public F dist(int u,int v){ return !same(u,v) ? null : comp(inv(dist(u)),dist(v)); }
}