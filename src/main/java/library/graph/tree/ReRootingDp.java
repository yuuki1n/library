package library.graph.tree;

import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

import library.graph.Edge;
import library.graph.Graph;

/**
 * 抽象化全方位木dp
 * @author yuuki_n
 *
 * @param <L>グラフの辺に持つ値の型
 * @param <D>dpでメモする値の型
 * @param <A>解の型
 */
@SuppressWarnings("unchecked")
abstract class ReRootingDp<L, D, A> extends Graph<L>{
  private D[] dp;
  private A[] ans;
  private Edge<L>[][] g;

  public ReRootingDp(int N){
    super(N,false);
    dp = (D[]) new Object[2 *N];
    ans = (A[]) new Object[n];
  }

  /**
   * @return dpで持つ値の単位元
   */
  abstract D e();

  /**
   * @return dpで持つ値の集約
   */
  abstract D agg(D a,D b);

  /**
   * @param v
   * @param e
   * @return 頂点e.vに集めたdpの値にe.u方向の辺を足した値
   */
  abstract D adj(D v,Edge<L> e);

  /**
   * @param u
   * @param sum 頂点uに集めたdpの値
   * @return 頂点uについての解
   */
  abstract A ans(int u,D sum);

  /**
   * ans(u,sum)内で使用することを想定
   * @param u
   * @return 頂点uの割のdpの値list
   */
  protected List<D> sur(int u){ return go(u).stream().map(e -> dp[e.id]).toList(); }

  /**
   * 事前にcalcを呼ぶ必要あり
   * @param u
   * @return　頂点uの解
   */
  public A get(int u){ return ans[u]; }

  /**
   * 事前にcalcを呼ぶ必要あり
   * @return 全頂点の解list
   */
  public List<A> anses(){ return IntStream.range(0,n).mapToObj(this::get).toList(); }

  /**
   * 計算する
   */
  public void calc(){
    for (var e:es)
      e.re.id = e.id +n;
    g = new Edge[n][];
    for (int i = 0;i < n;i++)
      g[i] = go(i).toArray(new Edge[0]);
    var stk = new Stack<Edge<L>>();
    var se = new Edge<L>(n -1,-1,0,null);
    stk.add(se);
    while (!stk.isEmpty()) {
      var e = stk.pop();
      if (dp[e.id] == null) {
        dp[e.id] = e();
        for (var ee:g[e.v])
          if (ee != e.re) {
            stk.add(ee);
            stk.add(ee);
          }
      } else {
        for (var ee:g[e.v])
          if (ee != e.re)
            dp[e.id] = agg(dp[e.id],dp[ee.id]);
        if (e.u > -1)
          dp[e.id] = adj(dp[e.id],e);
      }
    }
    stk.add(se);
    while (!stk.isEmpty()) {
      var e = stk.pop();
      var es = g[e.v];
      int n = g[e.v].length;
      D[] pre = (D[]) new Object[n +1],suf = (D[]) new Object[n +1];
      pre[0] = e();
      suf[n] = e();
      for (int i = 0;i < n;i++) {
        pre[i +1] = agg(pre[i],dp[es[i].id]);
        suf[n -1 -i] = agg(dp[es[n -1 -i].id],suf[n -i]);
      }

      ans[e.v] = ans(e.v,suf[0]);

      for (int i = 0;i < n;i++)
        if (es[i] != e.re) {
          dp[es[i].re.id] = adj(agg(pre[i],suf[i +1]),es[i].re);
          stk.add(es[i]);
        }
    }
  }
}