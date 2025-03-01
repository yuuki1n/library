package library.graph.unionfind;

import java.lang.reflect.*;
import java.util.*;

import library.util.*;

/**
 * 動的UnionFind
 * @author yuuki_n
 *
 * @param <K>
 */
public class DynamicUnionFind<K> {
  int num;
  private Map<K, K> par,nxt;
  private Map<K, Integer> size;

  public DynamicUnionFind(){
    par = new HashMap<>();
    nxt = new HashMap<>();
    size = new HashMap<>();
    num = 0;
  }

  public boolean add(K x){
    if (par.containsKey(x))
      return false;
    par.put(x,x);
    nxt.put(x,x);
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

  boolean same(K u,K v){ return root(u).equals(root(v)); }

  boolean unite(K u,K v){
    if ((u = root(u)).equals(v = root(v)))
      return false;

    if (size.get(u) < size.get(v)) {
      var t = u;
      u = v;
      v = t;
    }

    par.put(v,u);
    size.merge(u,size.get(v),Integer::sum);
    var nu = nxt.get(u);
    nxt.put(u,nxt.get(v));
    nxt.put(v,nu);

    num--;
    return true;
  }

  int size(K x){ return size.get(root(x)); }

  public K[] getGroup(K x){
    x = root(x);
    K[] ret = Util.cast(Array.newInstance(x.getClass(),size.get(x)));
    for (int i = 0;i < ret.length;i++)
      ret[i] = x = nxt.get(x);
    return ret;
  }
}