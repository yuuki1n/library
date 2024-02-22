package library.graph.tree;

import java.lang.reflect.*;

import library.dataStructure.collection.*;
import library.graph.*;
import library.util.*;

/**
 * 抽象化全方位木dp
 * @author yuuki_n
 *
 * @param <L>グラフの辺に持つ値の型
 * @param <D>dpでメモする値の型
 * @param <A>解の型
 */
public abstract class ReRootingDp<L, D, A> extends Graph<L>{
  private D[] dp;
  private A[] ans;

  public ReRootingDp(int N){
    super(N,false);
    dp = Util.cast(new Object[2 *N]);
    ans = Util.cast(Array.newInstance(ans(0,e()).getClass(),n));
  }

  /**
   * @return dpで持つ値の単位元
   */
  protected abstract D e();
  /**
   * @return dpで持つ値の集約
   */
  protected abstract D agg(D a,D b);
  /**
   * @param v
   * @param e
   * @return 頂点e.vに集めたdpの値にe.u方向の辺を足した値
   */
  protected abstract D adj(D v,Edge<L> e);
  /**
   * @param u
   * @param sum 頂点uに集めたdpの値
   * @return 頂点uについての解
   */
  protected abstract A ans(int u,D sum);

  /**
   * ans(u,sum)内で使用することを想定
   * @param u
   * @return 頂点uの割のdpの値list
   */
  protected MyList<D> sur(int u){ return go(u).map(e -> dp[e.id]); }

  /**
   * 計算する
   */
  public A[] calc(){
    for (var e:es)
      e.re.id = e.id +n;

    var stk = new MyStack<Edge<L>>();
    var se = new Edge<L>(n -1,-1,0,null);
    stk.add(se);
    while (!stk.isEmpty()) {
      var e = stk.pop();
      if (dp[e.id] == null) {
        dp[e.id] = e();
        for (var ee:go(e.v))
          if (ee != e.re) {
            stk.add(ee);
            stk.add(ee);
          }
      } else {
        for (var ee:go(e.v))
          if (ee != e.re)
            dp[e.id] = agg(dp[e.id],dp[ee.id]);
        if (e.u > -1)
          dp[e.id] = adj(dp[e.id],e);
      }
    }
    stk.add(se);
    while (!stk.isEmpty()) {
      var e = stk.pop();
      var es = go(e.v);
      int n = es.size();
      D[] pre = Util.cast(new Object[n +1]),suf = Util.cast(new Object[n +1]);
      pre[0] = e();
      suf[n] = e();
      for (int i = 0;i < n;i++) {
        pre[i +1] = agg(pre[i],dp[es.get(i).id]);
        suf[n -1 -i] = agg(dp[es.get(n -1 -i).id],suf[n -i]);
      }

      ans[e.v] = ans(e.v,suf[0]);

      for (int i = 0;i < n;i++) {
        Edge<L> ee = es.get(i);
        if (ee != e.re) {
          dp[ee.re.id] = adj(agg(pre[i],suf[i +1]),ee.re);
          stk.add(ee);
        }
      }
    }
    return ans;
  }
}