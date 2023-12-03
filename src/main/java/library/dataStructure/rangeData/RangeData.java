package library.dataStructure.rangeData;

/**
 *
 * @author yuuki_n
 *
 * 区間処理用のデータ構造の抽象クラス
 *
 * @param <V> 管理するデータ型
 * @param <F> 作用のデータ型
 */
public abstract class RangeData<V, F> {

  /**
   * i番目の値にfを作用させる
   * @param i
   * @param f
   */
  public void upd(int i,F f){}

  /**
   * [l,r)番目にfを作用させる
   * @param l
   * @param r
   * @param f
   */
  public void upd(int l,int r,F f){}

  /**
   * i番目の値を取得する
   * @param i
   * @return
   */
  public V get(int i){ return null; }

  /**
   * [l,r)番目を集約した値を取得する
   * @param l
   * @param r
   * @return
   */
  public V get(int l,int r){ return null; }
}
