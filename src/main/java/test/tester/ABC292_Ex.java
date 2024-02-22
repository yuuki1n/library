package test.tester;

import static java.lang.Math.*;

import java.io.*;
import java.util.*;

import library.dataStructure.rangeData.base.*;
import library.dataStructure.rangeData.segmentTree.*;
import library.io.*;

public class ABC292_Ex extends BaseTester{
  public ABC292_Ex(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    long B = in.it();
    int Q = in.it();
    long[] A = in.lg(N);

    var seg = new AVLSegmentTree<Data, Long>(N){
      @Override
      protected Data e(){ return new Data(-infL); }

      @Override
      protected void agg(Data v,Data a,Data b){ v.v = max(a.v,b.v); }

      @Override
      protected void map(Data v,Long f){ v.v += f; }

      @Override
      protected Long comp(Long f,Long g){ return f +g; }

      @Override
      protected void tog(Data v){}
    };
    seg.build(N,i -> new Data(0));
    for (int i = 0;i < N;i++)
      seg.upd(i,N,A[i] -B);

    while (Q-- > 0) {
      final int c = in.idx();
      final long x = in.lg();
      long d = x -A[c];
      A[c] = x;
      seg.upd(c,N,d);
      int ans = bSearchI(N,0,x1 -> seg.get(0,x1).v >= 0);
      out.println(B +(double) seg.get(ans -1).v /ans);
    }

    return null;
  }

  class Data extends BaseV{
    long v;

    public Data(long v){ this.v = v; }
  }

  @Override
  public boolean verify(MyReader in,Scanner subIn,Scanner extIn){
    while (subIn.hasNext() && extIn.hasNext()) {
      double sub = subIn.nextDouble();
      double ext = extIn.nextDouble();
      double dlt = 1e-9;
      if (!gosaCheck(sub,ext,dlt))
        return false;
    }

    return super.verify(in,subIn,extIn);
  }
}