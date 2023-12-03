package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.RangeData;

/**
 * セグメント木の抽象クラス
 * @author yuuki_n
 *
 * @param <V>
 * @param <F>
 */
public abstract class Seg<V, F> extends RangeData<V, F>{
  protected int n,log;
  private V[] val;
  private F[] lazy;
  private int[] l,r;

  @SuppressWarnings("unchecked")
  Seg(int n){
    this.n = n;
    while (1 <<log <= n)
      log++;
    val = (V[]) new Object[n <<1];
    lazy = (F[]) new Object[n];
    l = new int[n <<1];
    r = new int[n <<1];

    for (int i = -1;++i < n;l[i +n] = i,r[i +n] = i +1)
      val[i +n] = init(i);
    for (int i = n;--i > 0;l[i] = l[i <<1],r[i] = r[i <<1 |1])
      merge(i);
  }

  /**
   * @return Vの単位元
   */
  protected abstract V e();

  /**
   * @param i
   * @return i番目の初期値
   */
  protected V init(int i){ return e(); }

  /**
   * @param v
   * @param f
   * @param l
   * @param r
   * @return [l,r)を集約した値vにfを作用させた値
   */
  protected abstract V map(V v,F f,int l,int r);

  protected void rangeMap(int i){ val[i] = map(val[i],lazy[i],l[i],r[i]); }

  /**
   * @param a
   * @param b
   * @return aとbの集約
   */
  protected V agg(V a,V b){ return null; }

  /**
   *
   * @param a
   * @param b
   * @return a,bの合成
   */
  protected F comp(F a,F b){ return null; }

  /**
   * 遅延分があれば位置iのノードに作用させる
   * 子ノードに伝播させる
   * @param i
   * @return
   */
  protected V eval(int i){
    if (i < n && lazy[i] != null) {
      rangeMap(i);
      prop(i <<1,lazy[i]);
      prop(i <<1 |1,lazy[i]);
      lazy[i] = null;
    }
    return val[i];
  }

  /**
   * 位置iのノードの値を子から計算する
   * @param i
   */
  private void merge(int i){ val[i] = agg(eval(i <<1),eval(i <<1 |1)); }

  /**
   * 位置iのノードにfの作用を置く
   * @param i
   * @param f
   */
  protected void prop(int i,F f){
    if (i < n)
      lazy[i] = lazy[i] == null ? f : comp(lazy[i],f);
    else
      val[i] = map(val[i],f,l[i],r[i]);
  }

  /**
   * [l,r)をカバーするノード達を再計算する
   * @param l
   * @param r
   */
  protected void up(int l,int r){
    l = oddPart(l +n);
    r = oddPart(r +n);
    while (1 < l)
      merge(l >>= 1);
    while (1 < r)
      merge(r >>= 1);
  }

  /**
   * [l,r)をカバーするノード達まで遅延分を降ろす
   * @param l
   * @param r
   */
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
      vl = (l &1) == 0 ? vl : agg(vl,eval(l++));
      vr = (r &1) == 0 ? vr : agg(eval(--r),vr);
      l >>= 1;
      r >>= 1;
    }

    return agg(vl,vr);
  }

}