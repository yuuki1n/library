package test.solver;

import static java.lang.Math.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.segmentTree.AVLSegmentTree;

public class ABC327_F extends BaseSolver{
  public ABC327_F(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int D = in.it();
    int W = in.it();
    int[][] S = in.it(N,2);

    var seg = new AVLSegmentTree<Data, Integer>(){
      @Override
      protected Data e(){ return new Data(0); }

      @Override
      protected void agg(Data v,Data a,Data b){ v.v = max(a.v,b.v); }

      @Override
      protected void map(Data v,Integer f){ v.v += f; }

      @Override
      protected Integer comp(Integer f,Integer g){ return f +g; }
    };

    List<int[]> events = new ArrayList<>();
    for (var s:S) {
      events.add(new int[]{s[0], s[1], 1});
      events.add(new int[]{s[0] +D, s[1], -1});
    }
    events.sort(Comparator.comparing(t -> t[0] *4 +t[2]));
    long ans = 0;
    for (var s:events) {
      seg.upd(s[1],min(s[1] +W,1 <<18),s[2]);
      if (0 < s[2])
        ans = max(ans,seg.get(0,1 <<18).v);
    }

    return ans;
  }

  class Data extends BaseV{
    long v;

    public Data(long v){ this.v = v; }
  }
}
