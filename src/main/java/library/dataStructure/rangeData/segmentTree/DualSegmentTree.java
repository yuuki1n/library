package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.base.*;

/**
 * 区間作用1点取得のセグメント木
 * @author yuuki_n
 *
 * @param <V> 値の型
 * @param <F> 作用の型
 */
public abstract class DualSegmentTree<V extends BaseV, F> extends Seg<V, F>{
  public DualSegmentTree(int n){ super(n); }

  @Override
  protected void agg(V v,V a,V b){}

  @Override
  public void upd(int i,F f){ upd(i,i +1,f); }

  @Override
  public void upd(int l,int r,F f){
    down(l,r);
    super.upd(l,r,f);
  }

  @Override
  public V get(int i){
    down(i,i +1);
    return super.get(i);
  }
}