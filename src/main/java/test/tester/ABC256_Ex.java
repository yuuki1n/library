package test.tester;

import static java.lang.Math.*;

import java.io.*;

import library.dataStructure.rangeData.base.*;
import library.dataStructure.rangeData.segmentTree.*;
import library.util.*;
import test.*;
import test.base.*;

public class ABC256_Ex extends BaseTester{
  public ABC256_Ex(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int Q = in.it();
    long[] A = in.lg(N);
    LazySegmentTree<Data, long[]> seg = new LazySegmentTree<>(N){
      @Override
      protected void agg(Data v,Data a,Data b){
        v.sum = a.sum +b.sum;
        v.min = min(a.min,b.min);
        v.max = max(a.max,b.max);
      }

      @Override
      protected Data e(){ return new Data(); }

      @Override
      protected Data init(int i){ return new Data(A[i]); }

      @Override
      protected void map(Data v,long[] f){
        long x = f[0];
        long y = f[1];
        if (0 < x && v.min /x != v.max /x) {
          v.fail = true;
          return;
        }
        v.fail = false;
        if (0 < x) {
          v.min /= x;
          v.max /= x;
        } else
          v.min = v.max = y;
        v.sum = v.min *v.sz;
      }

      @Override
      protected long[] comp(long[] f,long[] g){
        if (g[0] == 0)
          return g;
        if (0 < f[0]) {
          long l = f[0] *g[0];
          return new long[]{l /g[0] == f[0] ? l : infL, 0};
        }
        if (0 < f[1])
          return new long[]{0, f[1] /g[0]};
        return null;
      }
    };

    //    log(seg,N);
    while (Q-- > 0) {
      int t = in.it();
      int l = in.idx();
      int r = in.it();
      if (t == 1)
        seg.upd(l,r,new long[]{in.lg(), 0});
      if (t == 2)
        seg.upd(l,r,new long[]{0, in.lg()});
      if (t == 3) {
        long ans = seg.get(l,r).sum;
        out.println(ans);
      }
      //      log(seg,N);
    }
    return null;
  }

  class Data extends BaseV{
    long sum;
    long min,max;

    public Data(){
      min = Util.infI;
      max = 0;
    }

    public Data(long v){ sum = min = max = v; }

    @Override
    public String toString(){ return "" +sum; }
  }
}