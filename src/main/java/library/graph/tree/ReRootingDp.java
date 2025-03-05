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

  protected abstract D e();
  protected abstract D agg(D a,D b);
  protected abstract D adj(D v,Edge<L> e);
  protected abstract A ans(int u,D sum);

  protected D[] sur(int u){
    Edge<L>[] arr = go(u);
    D[] ret = Util.cast(new Object[arr.length]);
    for (int i = 0;i < arr.length;i++)
      ret[i] = dp[arr[i].id];
    return ret;
  }

  public A[] calc(){
    for (var e:es)
      e.re.id += n;

    var stk = new MyList<Edge<L>>();
    var se = new Edge<L>(n -1,-1,0,null);
    stk.add(se);
    while (!stk.isEmpty()) {
      var e = stk.pollLast();
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
      var e = stk.pollLast();
      var es = go(e.v);
      int n = es.length;
      D[] pre = Util.cast(new Object[n +1]),suf = Util.cast(new Object[n +1]);
      pre[0] = e();
      suf[n] = e();
      for (int i = 0;i < n;i++) {
        pre[i +1] = agg(pre[i],dp[es[i].id]);
        suf[n -1 -i] = agg(dp[es[n -1 -i].id],suf[n -i]);
      }

      ans[e.v] = ans(e.v,suf[0]);

      for (int i = 0;i < n;i++) {
        Edge<L> ee = es[i];
        if (ee != e.re) {
          dp[ee.re.id] = adj(agg(pre[i],suf[i +1]),ee.re);
          stk.add(ee);
        }
      }
    }
    return ans;
  }

  @Override
  public void clear(){
    dp[n -1] = null;
    for (var e:es)
      dp[e.id] = dp[e.re.id] = null;
    super.clear();
  }
}