package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.base.*;

/**
 * 区間作用区間取得のセグメント木
 * @author yuuki_n
 *
 * @param <V> 値の型
 * @param <F> 作用の型
 */
public abstract class LazySegmentTree<V extends BaseV, F> extends Seg<V, F>{
  public LazySegmentTree(int n){ super(n); }

  @Override
  public void upd(int i,F f){ upd(i,i +1,f); }

  @Override
  public void upd(int l,int r,F f){
    down(l,r);
    super.upd(l,r,f);
    up(l,r);
  }

  @Override
  public V get(int i){ return get(i,i +1); }

  @Override
  public V[] getList(int l,int r){
    down(l,r);
    return super.getList(l,r);
  }
}