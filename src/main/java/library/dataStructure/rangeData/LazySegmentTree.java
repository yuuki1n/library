package library.dataStructure.rangeData;

/**
 * 区間作用区間取得のセグメント木
 * @author yuuki_n
 *
 * @param <V>
 * @param <F>
 */
abstract class LazySegmentTree<V, F> extends Seg<V, F>{

  LazySegmentTree(int n){ super(n); }

  @Override
  protected abstract V agg(V v0,V v1);

  @Override
  protected abstract F comp(F f0,F f1);

  @Override
  public void upd(int i,F f){ upd(i,i +1,f); }

  /**
   * 更新する前に遅延分を降ろす
   * 更新後に親ノード達を再計算する
   */
  @Override
  public void upd(int l,int r,F f){
    down(l,r);
    super.upd(l,r,f);
    up(l,r);
  }

  @Override
  public V get(int i){ return get(i,i +1); }

  /**
   * 取得する前に遅延分を降ろす
   */
  @Override
  public V get(int l,int r){
    down(l,r);
    return super.get(l,r);
  }
}