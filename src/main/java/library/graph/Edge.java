package library.graph;

/**
 * グラフ構造体用の辺クラス
 * @author yuuki_n
 *
 * @param <L> 辺に持つ値
 */
public class Edge<L> {
  public int id;
  public int u;
  public int v;
  public L val;
  public Edge<L> re;

  public Edge(int id,int u,int v,L val){
    this.id = id;
    this.u = u;
    this.v = v;
    this.val = val;
  }
}