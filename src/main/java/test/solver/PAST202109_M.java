package test.solver;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.io.*;
import java.util.*;

import library.graph.unionfind.*;
import test.tester.*;

public class PAST202109_M extends BaseTester{
  public PAST202109_M(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int M = in.it();

    var uf = new RelationalUnionFind<long[]>(N){
      @Override
      protected long[] id(){ return new long[]{1, 0}; }

      @Override
      protected long[] comp(long[] a,long[] b){
        long[] ret = {b[0] *a[0], b[0] *a[1] +b[1]};
        return ret;
      }

      @Override
      protected long[] inv(long[] v){ return new long[]{v[0], -v[1] *v[0]}; }

      @Override
      protected boolean eq(long[] a,long[] b){ return Arrays.equals(a,b); }
    };

    long[] min = new long[N];
    long[] max = new long[N];
    fill(max,infL);

    while (M-- > 0) {
      int a = in.idx();
      int b = in.idx();
      int c = in.it();

      long[] v = {-1, c};
      if (!uf.valid(a,b,v)) {
        var s = uf.comp(uf.dist(a),v);
        var t = uf.dist(b);
        if (s[0] == t[0])
          return -1;

        if (s[0] < 0) {
          var aa = s;
          s = t;
          t = aa;
        }

        long x2 = t[1] -s[1];
        if (x2 < 0 || x2 %2 == 1)
          return -1;

        int rt = uf.root(a);
        long x = x2 /2;
        if (x < min[rt] || max[rt] < x)
          return -1;
        min[rt] = x;
        max[rt] = x;
      } else
        uf.unite(a,b,v);
    }

    for (int i = 0;i < N;i++)
      if (uf.root(i) == i) {
        for (var j:uf.getGroup(i)) {
          var t = uf.dist(j);
          if (t[0] == 1)
            min[i] = max(min[i],-t[1]);
          else
            max[i] = min(max[i],t[1]);
        }
        if (max[i] < min[i])
          return -1;
      }

    long[] ans = new long[N];
    for (int i = 0;i < N;i++)
      if (uf.root(i) == i) {
        ans[i] = min[i];
        for (var j:uf.getGroup(i)) {
          var t = uf.dist(j);
          ans[j] = t[0] *ans[i] +t[1];
        }
      }
    return ans;
  }

  @Override
  public boolean verify(MyReader in,Scanner subIn,Scanner extIn){
    if (extIn.nextInt() == -1)
      return subIn.nextInt() == -1 && !subIn.hasNext();
    int N = in.it();
    int M = in.it();
    int[] A = arrI(N,i -> subIn.nextInt());
    while (M-- > 0) {
      int a = in.idx();
      int b = in.idx();
      int c = in.it();
      if (A[a] +A[b] != c)
        return false;
    }

    return true;
  }
}