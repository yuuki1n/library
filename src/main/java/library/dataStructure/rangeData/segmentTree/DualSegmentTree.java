package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.base.BaseF;
import library.dataStructure.rangeData.base.BaseV;

/**
 * 区間作用1点取得のセグメント木
 * @author yuuki_n
 *
 * @param <VT> 値の型
 * @param <FT> 作用の型
 */
abstract class DualSegmentTree<VT extends BaseV, FT extends BaseF> extends Seg<VT, FT>{

  public DualSegmentTree(int n){ super(n); }

  @Override
  protected abstract FT comp(FT a,FT b);

  @Override
  protected void rangeMap(int i){}

  @Override
  public void upd(int i,FT f){ upd(i,i +1,f); }

  /**
   * 更新する前に遅延分を降ろす
   */
  @Override
  public void upd(int l,int r,FT f){
    down(l,r);
    super.upd(l,r,f);
  }

  /**
   * 取得する前に遅延分を降ろす
   */
  @Override
  public VT get(int i){
    down(i,i +1);
    return super.get(i);
  }

}