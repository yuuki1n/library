package library.dataStructure.rangeData.segmentTree;

import java.lang.reflect.*;

import library.dataStructure.rangeData.base.*;
import library.util.*;

/**
 * セグメント木の抽象クラス
 * @author yuuki_n
 *
 * @param <V> 値の型
 * @param <F> 作用の型
 */
public abstract class Seg<V extends BaseV, F> {
  private int n;
  private V[] ret,val;
  private F[] lazy;

  protected Seg(int n){
    this.n = n;
    ret = Util.cast(new BaseV[]{e(), e()});
    val = Util.cast(new BaseV[n <<1]);
    lazy = Util.cast(new Object[n]);

    for (int i = -1;++i < n;)
      (val[i +n] = init(i)).sz = 1;
    for (int i = n;--i > 0;merge(i))
      (val[i] = e()).sz = val[i <<1].sz +val[i <<1 |1].sz;
  }

  public void upd(int i,F f){ prop(i +n,f); }

  public void upd(int l,int r,F f){
    for (l += n,r += n;l < r;l >>= 1,r >>= 1) {
      if ((l &1) == 1)
        prop(l++,f);
      if ((r &1) == 1)
        prop(--r,f);
    }
  }

  public V get(int i){ return val[i +n]; }

  public V get(int l,int r){
    int i = 0;
    ret[i] = e();
    i ^= 1;
    for (var v:getList(l,r)) {
      agg(ret[i],ret[i ^1],v);
      ret[i].sz = ret[i ^= 1].sz +v.sz;
    }
    return ret[i ^1];
  }

  public V[] getList(int l,int r){
    int sz = 0;
    for (int li = l += n,ri = r += n;li < ri;li = li +1 >>1,ri >>= 1)
      sz += (li &1) +(ri &1);
    V[] arr = Util.cast(Array.newInstance(e().getClass(),sz));
    for (int i = 0;l < r;l >>= 1,r >>= 1) {
      if ((l &1) > 0)
        arr[i++] = val[l++];
      if ((r &1) > 0)
        arr[--sz] = val[--r];
    }
    return arr;
  }

  public V[] getPath(int i){
    int sz = 32 -Integer.numberOfLeadingZeros(i +n);
    V[] arr = Util.cast(Array.newInstance(e().getClass(),sz));
    for (i += n;0 < i;i >>= 1)
      arr[--sz] = val[i];
    return arr;
  }

  protected V init(int i){ return e(); }

  protected abstract V e();
  protected abstract void agg(V v,V a,V b);
  protected abstract void map(V v,F f);
  protected abstract F comp(F f,F g);

  protected void up(int l,int r){
    for (l = oddPart(l +n),r = oddPart(r +n);l != r;)
      merge(l > r ? (l >>= 1) : (r >>= 1));
    while (1 < l)
      merge(l >>= 1);
  }

  protected void down(int l,int r){
    int i = 32 -Integer.numberOfLeadingZeros(n);
    for (l = oddPart(l +n),r = oddPart(r +n);i > 0;i--) {
      push(l >>i);
      push(r >>i);
    }
  }

  private void merge(int i){ agg(val[i],val[i <<1],val[i <<1 |1]); }

  private void push(int i){
    if (lazy[i] != null) {
      prop(i <<1,lazy[i]);
      prop(i <<1 |1,lazy[i]);
      lazy[i] = null;
    }
  }

  private void prop(int i,F f){
    map(val[i],f);
    if (i < n) {
      lazy[i] = lazy[i] == null ? f : comp(lazy[i],f);
      if (val[i].fail) {
        push(i);
        merge(i);
      }
    }
  }

  private int oddPart(int i){ return i /(i &-i); }
}