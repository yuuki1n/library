package test.solver;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.segmentTree.AVLSegmentTree;
import library.dataStructure.rangeData.segmentTree.DualSegmentTree;

public class ABC332_F extends BaseSolver{
  public ABC332_F(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int Q = in.it();
    long[] A = in.lg(N);

    var seg = new AVLSegmentTree<Data, long[]>(N){
      @Override
      protected long[] comp(long[] f,long[] g){ return new long[]{f[0] *g[0] %mod, (f[1] *g[0] +g[1]) %mod}; }

      @Override
      protected Data e(){ return new Data(0); }

      //      @Override
      //      protected Data init(int i){ return new Data(A[i] %mod); }

      @Override
      protected void map(Data v,long[] f){
        if (v.sz == 1)
          v.v = (v.v *f[0] +f[1]) %mod;
      }

      @Override
      protected void agg(Data v,Data a,Data b){ v.v = b.v; }
    };

    seg.build(N,i -> new Data(A[i] %mod));

    long[] ans = new long[N];

    while (Q-- > 0) {
      int l = in.idx();
      int r = in.it();

      long inv = inv(r -l,mod);

      long a = (r -l -1) *inv %mod;

      long x = in.lg();
      long b = x *inv %mod;
      seg.upd(l,r,new long[]{a, b});
    }

    for (int i = 0;i < N;i++)
      ans[i] = seg.get(i).v;
    return ans;
  }

  class Data extends BaseV{
    long v;

    public Data(long v){ this.v = v; }
  }
}