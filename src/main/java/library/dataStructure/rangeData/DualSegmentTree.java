package library.dataStructure.rangeData;

/**
 * 区間作用1点取得のセグメント木
 * @author yuuki_n
 *
 * @param <V>
 * @param <F>
 */
abstract class DualSegmentTree<V, F> extends Seg<V, F>{

  DualSegmentTree(int n){ super(n); }

  @Override
  protected abstract F comp(F a,F b);

  @Override
  protected void rangeMap(int i){}

  @Override
  public void upd(int i,F f){ upd(i,i +1,f); }

  /**
   * 更新する前に遅延分を降ろす
   */
  @Override
  public void upd(int l,int r,F f){
    down(l,r);
    super.upd(l,r,f);
  }

  /**
   * 取得する前に遅延分を降ろす
   */
  @Override
  public V get(int i){
    down(i,i +1);
    return super.get(i);
  }

}