package test.solver;

import java.io.*;

import library.dataStructure.rangeData.base.*;
import library.dataStructure.rangeData.segmentTree.*;
import test.tester.*;

public class RangeReverseRangeSum extends BaseTester{
  public RangeReverseRangeSum(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int Q = in.it();
    long[] A = in.lg(N);
    var seg = new AVLSegmentTree<Data, Long>(){
      @Override
      protected Data e(){ return new Data(0); }

      @Override
      protected void map(Data v,Long f){ v.v += v.sz *f; }

      @Override
      protected Long comp(Long f,Long g){ return f +g; }

      @Override
      protected void agg(Data v,Data a,Data b){ v.v = a.v +b.v; }

      @Override
      protected void pow(Data v,Data a,int n){ v.v = a.v *n; }

      @Override
      protected void tog(Data v){}
    };

    seg.build(N,i -> new Data(A[i]));
    //    log(seg,N);
    while (Q-- > 0) {
      int t = in.it();
      int l = in.it();
      int r = in.it();
      if (t == 0)
        seg.toggle(l,r);
      else {
        Data obj = seg.get(l,r);
        out.println(obj.v);
      }
      //      log(seg,N);
    }
    return null;
  }

  <T extends BaseV> void log(LazySegmentTree<T, ?> seg,int n){
    T[] a = (T[]) new BaseV[n];
    for (int i = 0;i < n;i++)
      a[i] = seg.get(i);
    log.println(a);
  }

  class Data extends BaseV{
    long v;

    public Data(long v){ this.v = v; }

    @Override
    public String toString(){ return "" +v; }
  }
}
