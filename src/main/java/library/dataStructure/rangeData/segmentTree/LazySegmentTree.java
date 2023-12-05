package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.base.BaseF;
import library.dataStructure.rangeData.base.BaseV;

/**
 * 区間作用区間取得のセグメント木
 * @author yuuki_n
 *
 * @param <VT> 値の型
 * @param <FT> 作用の型
 */

abstract class LazySegmentTree<VT extends BaseV, FT extends BaseF> extends Seg<VT, FT>{

  public LazySegmentTree(int n){ super(n); }

  @Override
  protected abstract void agg(VT v,VT a,VT b);

  @Override
  protected abstract FT comp(FT a,FT b);

  @Override
  public void upd(int i,FT f){ upd(i,i +1,f); }

  /**
   * 更新する前に遅延分を降ろす
   * 更新後に親ノード達を再計算する
   */
  @Override
  public void upd(int l,int r,FT f){
    down(l,r);
    super.upd(l,r,f);
    up(l,r);
  }

  @Override
  public VT get(int i){ return get(i,i +1); }

  /**
   * 取得する前に遅延分を降ろす
   */
  @Override
  public VT get(int l,int r){
    down(l,r);
    return super.get(l,r);
  }
}