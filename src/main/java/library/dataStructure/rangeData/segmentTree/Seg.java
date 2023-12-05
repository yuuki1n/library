package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.base.RangeData;

/**
 * セグメント木の抽象クラス
 * @author yuuki_n
 *
 * @param <V> 値の型
 * @param <F> 作用の型
 */
abstract class Seg<V extends BaseV, F> extends RangeData<V, F>{
  private int n,log;
  private V[] val;
  private F[] lazy;

  @SuppressWarnings("unchecked")
  Seg(int n){
    this.n = n;
    while (1 <<log <= n)
      log++;
    val = (V[]) new BaseV[n <<1];
    lazy = (F[]) new Object[n];

    for (int i = -1;++i < n;) {
      V v = val[i +n] = init(i);
      v.l = i;
      v.r = i +1;
    }
    for (int i = n;--i > 0;merge(i)) {
      V v = val[i] = e();
      v.l = val[i <<1].l;
      v.r = val[i <<1 |1].r;
    }
  }

  protected abstract V e();

  protected V init(int i){ return e(); }

  protected void agg(V v,V a,V b){}

  protected abstract void map(V v,F f);

  protected void rangeMap(int i){ map(val[i],lazy[i]); }

  protected F comp(F f,F g){ return null; }

  private V eval(int i){
    if (i < n && lazy[i] != null) {
      rangeMap(i);
      prop(i <<1,lazy[i]);
      prop(i <<1 |1,lazy[i]);
      lazy[i] = null;
    }
    return val[i];
  }

  private void merge(int i){
    if (val[i].l < val[i].r)
      agg(val[i],eval(i <<1),eval(i <<1 |1));
  }

  private void prop(int i,F f){
    if (i < n)
      lazy[i] = lazy[i] == null ? f : comp(lazy[i],f);
    else
      map(val[i],f);
  }

  protected void up(int l,int r){
    l = oddPart(l +n);
    r = oddPart(r +n);
    while (1 < l)
      merge(l >>= 1);
    while (1 < r)
      merge(r >>= 1);
  }

  protected void down(int l,int r){
    l = oddPart(l +n);
    r = oddPart(r +n);
    for (int i = log;i > 0;i--) {
      if (l >>i > 0)
        eval(l >>i);
      if (r >>i > 0)
        eval(r >>i);
    }
  }

  private int oddPart(int i){ return i /(i &-i); }

  @Override
  public void upd(int i,F f){ prop(i +n,f); }

  @Override
  public void upd(int l,int r,F f){
    l += n;
    r += n;
    do {
      if ((l &1) == 1)
        prop(l++,f);
      if ((r &1) == 1)
        prop(--r,f);
    } while ((l >>= 1) < (r >>= 1));
  }

  @Override
  public V get(int i){ return val[i +n]; }

  @Override
  public V get(int l,int r){
    l += n;
    r += n;
    V vl = e(),vr = e();
    while (l < r) {
      if ((l &1) == 1)
        vl = agg(vl,eval(l++));
      if ((r &1) == 1)
        vr = agg(eval(--r),vr);
      l >>= 1;
      r >>= 1;
    }
    return agg(vl,vr);
  }

  private V agg(V vl,V vr){
    V t = e();
    agg(t,vl,vr);
    return t;
  }
}