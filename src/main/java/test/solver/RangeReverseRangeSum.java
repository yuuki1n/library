package test.solver;

import static java.lang.Math.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.Scanner;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.base.RangeData;
import library.dataStructure.rangeData.segmentTree.AVLSegmentTree;
import library.dataStructure.rangeData.segmentTree.LazySegmentTree;
import library.io.MyReader;

public class RangeReverseRangeSum extends BaseSolver{
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

  <T> void log(RangeData<T, ?> seg,int n){
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
