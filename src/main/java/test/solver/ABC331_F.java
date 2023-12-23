package test.solver;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.io.InputStream;
import java.io.OutputStream;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.segmentTree.AVLSegmentTree;

public class ABC331_F extends BaseSolver{
  public ABC331_F(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int Q = in.it();
    char[] S = in.ch();
    long m = rd.nextInt(infI);
    long[] pow = new long[N +1];
    pow[0] = 1;
    for (int i = 0;i < N;i++)
      pow[i +1] = pow[i] *m %mod;
    var seg = new AVLSegmentTree<Data, Character>(){
      @Override
      protected void agg(Data v,Data a,Data b){
        if (a.sz > N || b.sz > N)
          return;
        var ahas = a.has;
        v.has = (ahas *pow[b.sz] +b.has) %mod;
      }

      @Override
      protected Data e(){ return new Data(0); }

      @Override
      protected void map(Data v,Character f){ v.has = f; }

      @Override
      protected Character comp(Character f,Character g){ return g; }
    };
    seg.build(N,i -> new Data(S[i]));
    while (Q-- > 0)
      if (in.it() == 1) {
        int x = in.idx();
        char c = in.ch()[0];
        seg.upd(x,c);
      } else {
        int l = in.idx();
        int r = in.it();
        var data = seg.get(l,r);
        //        seg.toggle(l,r);
        var rev = seg.getRev(l,r);
        //        seg.toggle(l,r);
        out.println(data.has == rev.has);
      }

    return null;
  }

  class Data extends BaseV{
    long has;

    public Data(long has){ this.has = has; }
  }
}