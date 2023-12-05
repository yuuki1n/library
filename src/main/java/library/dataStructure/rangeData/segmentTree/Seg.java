package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.RangeData;
import library.dataStructure.rangeData.base.BaseF;
import library.dataStructure.rangeData.base.BaseV;

/**
 * セグメント木の抽象クラス
 * @author yuuki_n
 *
 * @param <VT> 値の型
 * @param <FT> 作用の型
 */
abstract class Seg<VT extends BaseV, FT extends BaseF> extends RangeData<VT, FT>{
  private int n,log;
  private VT[] val;
  private FT[] lazy;

  @SuppressWarnings("unchecked")
  Seg(int n){
    this.n = n;
    while (1 <<log <= n)
      log++;
    val = (VT[]) new BaseV[n <<1];
    lazy = (FT[]) new BaseF[n];

    for (int i = -1;++i < n;) {
      VT v = val[i +n] = init(i);
      v.l = i;
      v.r = i +1;
    }
    for (int i = n;--i > 0;merge(i)) {
      VT v = val[i] = e();
      v.l = val[i <<1].l;
      v.r = val[i <<1 |1].r;
    }
  }

  /**
   * @return VTの単位元
   */
  protected abstract VT e();

  /**
   * @param i
   * @return i番目の初期値
   */
  protected VT init(int i){ return e(); }

  /**
   * @param v
   * @param f
   * @param l
   * @param r
   * @return [l,r)を集約した値vにfを作用させた値
   */
  protected abstract void map(VT v,FT f);

  protected void rangeMap(int i){ map(val[i],lazy[i]); }

  /**
   * xにaとbを集約した値を設定する
   * @param x
   * @param a
   * @param b
   */
  protected void agg(VT x,VT a,VT b){}

  /**
  * @param f
  * @param g
  * @return f,gの合成g(f(x))
  */
  protected FT comp(FT f,FT g){ return null; }

  /**
   * 遅延分があれば位置iのノードに作用させる
   * 子ノードに伝播させる
   * @param i
   * @return
   */
  private VT eval(int i){
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
  private void merge(int i){
    if (val[i].l < val[i].r)
      agg(val[i],eval(i <<1),eval(i <<1 |1));
  }

  /**
   * 位置iのノードにfの作用を置く
   * @param i
   * @param f
   */
  private void prop(int i,FT f){
    if (i < n)
      lazy[i] = lazy[i] == null ? f : comp(lazy[i],f);
    else
      map(val[i],f);
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
  public void upd(int i,FT f){ prop(i +n,f); }

  @Override
  public void upd(int l,int r,FT f){
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
  public VT get(int i){ return val[i +n]; }

  @Override
  public VT get(int l,int r){
    l += n;
    r += n;
    VT vl = e(),vr = e();
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

  private VT agg(VT vl,VT vr){
    var t = e();
    agg(t,vl,vr);
    return t;
  }

}
