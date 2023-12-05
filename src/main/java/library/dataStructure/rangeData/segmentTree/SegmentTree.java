package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.base.BaseF;
import library.dataStructure.rangeData.base.BaseV;

/**
 * 1点作用区間取得のセグメント木
 * @author yuuki_n
 *
 * @param <VT> 値の型
 * @param <FT> 作用の型
 */
public abstract class SegmentTree<VT extends BaseV, FT extends BaseF> extends Seg<VT, FT>{

  public SegmentTree(int n){ super(n); }

  @Override
  protected abstract void agg(VT x,VT a,VT b);

  /**
   * 更新後に親ノード達を再計算する
   */
  @Override
  public void upd(int i,FT f){
    super.upd(i,f);
    up(i,i +1);
  }

}
