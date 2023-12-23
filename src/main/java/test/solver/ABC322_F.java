package test.solver;

import static java.lang.Math.*;

import java.io.InputStream;
import java.io.OutputStream;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.segmentTree.AVLSegmentTree;

public class ABC322_F extends BaseSolver{
  public ABC322_F(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int Q = in.it();
    char[] S = in.ch();

    var seg = new AVLSegmentTree<Data, Integer>(){
      @Override
      protected Data e(){ return new Data(0,0,0,0,0,0); }

      @Override
      protected void agg(Data v,Data l,Data r){
        int l0 = l.l0;
        if (l.l0 == l.sz)
          l0 += r.l0;
        int l1 = l.l1;
        if (l.l1 == l.sz)
          l1 += r.l1;
        int r0 = r.r0;
        if (r.r0 == r.sz)
          r0 += l.r0;
        int r1 = r.r1;
        if (r.r1 == r.sz)
          r1 += l.r1;
        int n0 = max(max(max(l.n0,r.n0),max(l0,r0)),l.r0 +r.l0);
        int n1 = max(max(max(l.n1,r.n1),max(l1,r1)),l.r1 +r.l1);
        v.l0 = l0;
        v.r0 = r0;
        v.n0 = n0;
        v.l1 = l1;
        v.r1 = r1;
        v.n1 = n1;
      }

      @Override
      protected void map(Data v,Integer f){
        if (f == 0)
          return;
        v.l0 ^= v.l1;
        v.l1 ^= v.l0;
        v.l0 ^= v.l1;

        v.r0 ^= v.r1;
        v.r1 ^= v.r0;
        v.r0 ^= v.r1;

        v.n0 ^= v.n1;
        v.n1 ^= v.n0;
        v.n0 ^= v.n1;
      }

      @Override
      protected Integer comp(Integer f,Integer g){ return f ^g; }
    };

    seg.build(N,i -> S[i] == '0' ? new Data(1,1,1,0,0,0) : new Data(0,0,0,1,1,1));
    while (Q-- > 0) {
      int c = in.it();
      int l = in.idx();
      int r = in.it();
      if (c == 1)
        seg.upd(l,r,1);
      else {
        Data ans = seg.get(l,r);
        out.println(ans.n1);
      }
    }

    return null;
  }

  class Data extends BaseV{
    int l0,r0,n0,l1,r1,n1;

    public Data(int l0,int r0,int n0,int l1,int r1,int n1){
      this.l0 = l0;
      this.r0 = r0;
      this.n0 = n0;
      this.l1 = l1;
      this.r1 = r1;
      this.n1 = n1;
    }
  }
}