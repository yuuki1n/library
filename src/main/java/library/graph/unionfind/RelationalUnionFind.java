package library.graph.unionfind;

import static java.util.Arrays.*;

import library.util.*;

public abstract class RelationalUnionFind<F> extends UnionFind{
  private F[] dist;

  public RelationalUnionFind(int n){
    super(n);
    dist = Util.cast(new Object[n]);
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

  @Override
  public boolean unite(int u,int v){ return unite(u,v,id()); }

  /**
   * A[v]=f(A[u])となる関係fを追加する
   * @param u
   * @param v
   * @param f
   * @return
   */
  public boolean unite(int u,int v,F f){
    assert valid(u,v,f);
    f = comp(dist(u),f);
    f = comp(f,inv(dist(v)));
    if (!super.unite(u = root(u),v = root(v)))
      return false;
    if (dat[u] > dat[v])
      dist[u] = inv(f);
    else
      dist[v] = f;
    return true;
  }

  /**
   * @param x
   * @return　A[x]=f(A[root(x)])となる関係f
   */
  public F dist(int x){
    root(x);
    return dist[x];
  }

  /**
   * @param u
   * @param v
   * @return　A[v]=f(A[u])となる関係f
   */
  public F dist(int u,int v){ return !same(u,v) ? null : comp(inv(dist(u)),dist(v)); }
}