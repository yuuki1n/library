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
public abstract class Seg<V extends BaseV, F> extends RangeData<V, F>{
  private int n,log;
  private V[] val;
  private F[] lazy;

  @SuppressWarnings("unchecked")
  protected Seg(int n){
    this.n = n;
    while (1 <<log <= n)
      log++;
    val = (V[]) new BaseV[n <<1];
    lazy = (F[]) new Object[n];

    for (int i = -1;++i < n;)
      (val[i +n] = init(i)).sz = 1;
    for (int i = n;--i > 0;merge(i))
      (val[i] = e()).sz = val[i <<1].sz +val[i <<1 |1].sz;
  }

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
      if ((l &1) == 1)
        ag(vl,vl,val[l++]);
      if ((r &1) == 1)
        ag(vr,val[--r],vr);
    }
    ag(vl,vl,vr);
    return vl;
  }

  protected abstract V e();

  protected V init(int i){ return e(); }

  protected void agg(V v,V a,V b){}

  private void ag(V v,V a,V b){
    agg(v,a,b);
    v.sz = a.sz +b.sz;
  }

  protected abstract void map(V v,F f);

  protected F comp(F f,F g){ return null; }

  protected void up(int l,int r){
    for (l = oddPart(l +n),r = oddPart(r +n);l != r;)
      merge(l > r ? (l >>= 1) : (r >>= 1));
    while (1 < l)
      merge(l >>= 1);
  }

  protected void down(int l,int r){
    int i = log;
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
    if (i < n)
      lazy[i] = lazy[i] == null ? f : comp(lazy[i],f);
  }

  private int oddPart(int i){ return i /(i &-i); }
}
