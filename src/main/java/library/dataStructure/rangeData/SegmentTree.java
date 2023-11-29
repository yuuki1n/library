package library.dataStructure.rangeData;

/**
 * 1点作用区間取得のセグメント木
 * @author yuuki_n
 *
 * @param <V>
 * @param <F>
 */
abstract class SegmentTree<V, F> extends Seg<V, F>{

  public SegmentTree(int n){ super(n); }

  @Override
  protected abstract V agg(V a,V b);

  /**
   * 更新後に親ノード達を再計算する
   */
  @Override
  public void upd(int i,F f){
    super.upd(i,f);
    up(i,i +1);
  }

}