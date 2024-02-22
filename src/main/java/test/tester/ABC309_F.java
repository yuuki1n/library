package test.tester;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.io.*;
import java.util.*;

import library.dataStructure.rangeData.base.*;
import library.dataStructure.rangeData.segmentTree.*;
import test.*;
import test.base.*;

public class ABC309_F extends BaseTester{
  public ABC309_F(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int[][] T = in.it(N,3);
    for (var t:T)
      sort(t);

    var seg = new AVLSegmentTree<Data, Long>(){
      @Override
      protected void agg(Data v,Data a,Data b){ v.v = min(a.v,b.v); }

      @Override
      protected Data e(){ return new Data(infI); }

      @Override
      protected void map(Data v,Long f){ v.v = min(v.v,f); }

      @Override
      protected void pow(Data v,Data a,int n){ v.v = min(v.v,a.v); }

      @Override
      protected Long comp(Long f,Long g){ return g; }

      @Override
      protected void tog(Data v){}
    };

    sort(T,Comparator.comparing(t -> 1L *infI *t[0] -t[1]));
    for (var t:T) {
      long v = seg.get(0,t[1]).v;
      if (v < t[2])
        return true;
      seg.upd(t[1],t[1] +1,1L *t[2]);
    }

    return false;
  }

  class Data extends BaseV{
    long v;

    public Data(long v){ this.v = v; }
  }
}