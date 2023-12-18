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
      v.sz = 1;
    }
    for (int i = n;--i > 0;merge(i)) {
      V v = val[i] = e();
      v.sz = val[i <<1].sz +val[i <<1 |1].sz;
    }
  }

  protected abstract V e();

  protected V init(int i){ return e(); }

  protected void agg(V v,V a,V b){}

  protected abstract void map(V v,F f);

  protected void rangeMap(int i){ map(val[i],lazy[i]); }

  protected F comp(F f,F g){ return null; }

  private V eval(int i){
    if (0 < i && i < n && lazy[i] != null) {
      rangeMap(i);
      prop(i <<1,lazy[i]);
      prop(i <<1 |1,lazy[i]);
      lazy[i] = null;
    }
    return val[i];
  }

  private void merge(int i){ agg(val[i],eval(i <<1),eval(i <<1 |1)); }

  private void prop(int i,F f){
    if (i < n)
      lazy[i] = lazy[i] == null ? f : comp(lazy[i],f);
    else
      map(val[i],f);
  }

  protected void up(int l,int r){
    for (l = oddPart(l +n),r = oddPart(r +n);l != r;)
      merge(l > r ? (l >>= 1) : (r >>= 1));
    while (1 < l)
      merge(l >>= 1);
  }

  protected void down(int l,int r){
    int i = log;
    for (l = oddPart(l +n),r = oddPart(r +n);i > 0;i--) {
      eval(l >>i);
      eval(r >>i);
    }
  }

  private int oddPart(int i){ return i /(i &-i); }

  @Override
  public void upd(int i,F f){ prop(i +n,f); }

  @Override
  public void upd(int l,int r,F f){
    for (l += n,r += n;l < r;l >>= 1,r >>= 1) {
      if ((l &1) == 1)
        prop(l++,f);
      if ((r &1) == 1)
        prop(--r,f);
    }
  }

  @Override
  public V get(int i){ return val[i +n]; }

  @Override
  public V get(int l,int r){
    V vl = e(),vr = e();
    for (l += n,r += n;l < r;l >>= 1,r >>= 1) {
      if ((l &1) == 1) {
        var t = eval(l++);
        agg(vl,vl,t);
        vl.sz += t.sz;
      }
      if ((r &1) == 1) {
        var t = eval(--r);
        agg(vr,t,vr);
        vr.sz += t.sz;
      }
    }
    agg(vl,vl,vr);
    vl.sz += vr.sz;
    return vl;
  }
}
