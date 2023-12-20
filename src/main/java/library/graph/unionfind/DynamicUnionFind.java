package library.graph.unionfind;

import java.util.HashMap;
import java.util.Map;

/**
 * 動的UnionFind
 * @author yuuki_n
 *
 * @param <K>
 */
public abstract class DynamicUnionFind<K> {
  int num;
  private Map<K, K> par;
  private Map<K, Integer> size;

  public DynamicUnionFind(){
    par = new HashMap<>();
    size = new HashMap<>();
    num = 0;
  }

  public boolean add(K x){
    if (par.containsKey(x))
      return false;
    par.put(x,x);
    size.put(x,1);
    num++;
    return true;
  }

  public K root(K x){
    add(x);

    if (x.equals(par.get(x)))
      return x;

    K r = root(par.get(x));
    par.put(par.get(x),r);
    return r;
  }

  public boolean same(K u,K v){ return root(u).equals(root(v)); }

  public boolean unite(K u,K v){
    if ((u = root(u)).equals(v = root(v)))
      return false;

    par.put(v,u);
    size.merge(u,size.get(v),Integer::sum);
    num--;
    return true;
  }

  public int size(K x){ return size.get(root(x)); }
}