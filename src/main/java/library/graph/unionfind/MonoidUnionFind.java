package library.graph.unionfind;

import static java.util.Arrays.*;

/**
 * マージ時に総和も計算する
 * 可換モノイドがのる
 * @author yuuki_n
 *
 * @param <V>
 */
public abstract class MonoidUnionFind<V> extends UnionFind{
  private V[] val;

  @SuppressWarnings("unchecked")
  public MonoidUnionFind(int n){
    super(n);
    val = (V[]) new Object[n];
    setAll(val,this::init);
  }

  protected abstract V init(int i);
  protected abstract V agg(V a,V b);

  @Override
  public boolean unite(int u,int v){
    if (same(u,v))
      return false;

    if (dat[u = root(u)] > dat[v = root(v)]) {
      u ^= v;
      v ^= u;
      u ^= v;
    }
    val[u] = agg(val[u],val[v]);

    return super.unite(u,v);
  }

  public V val(int i){ return val[root(i)]; }
}