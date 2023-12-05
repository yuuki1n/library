package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.base.BaseV;

/**
 * 1点作用区間取得のセグメント木
 * @author yuuki_n
 *
 * @param <V> 値の型
 * @param <F> 作用の型
 */
public abstract class SegmentTree<V extends BaseV, F> extends Seg<V, F>{
  public SegmentTree(int n){ super(n); }

  @Override
  protected abstract void agg(V v,V a,V b);

  @Override
  public void upd(int i,F f){
    super.upd(i,f);
    up(i,i +1);
  }
}