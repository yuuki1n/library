package test.solver;

import java.io.InputStream;
import java.io.OutputStream;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.segmentTree.LazySegmentTree;
import library.io.MyWriter;

public class LazySeg extends BaseSolver{
  public LazySeg(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    var seg = new LazySegmentTree<Data, Long>(N){
      @Override
      protected void agg(Data v,Data a,Data b){ v.v = a.v +b.v; }

      @Override
      protected Long comp(Long f,Long g){ return f +g; }

      @Override
      protected Data e(){ return new Data(0); }

      @Override
      protected void map(Data v,Long f){ v.v += v.sz *f; }
    };
    //    seg.upd(2,15,3L);
    //    seg.log(log);
    //    System.out.println();
    //    seg.upd(17,18,8L);
    //    seg.log(log);
    //    System.out.println();
    //    seg.upd(0,9,1L);
    //    seg.log(log);
    //    System.out.println();
    //    seg.upd(1,18,9L);
    //    seg.log(log);
    //    System.out.println();

    return null;
  }

  class Data extends BaseV{
    long v;

    public Data(long v){ this.v = v; }

    @Override
    public String toString(){ return "" +v; }
  }
}
//public void log(MyWriter log){
//  var arr = new Object[val.length];
//  for (int i = 0;i < val.length;i++)
//    arr[i] = val[i] == null ? "null" : val[i];
//  log.println(arr);
//  arr = new Object[lazy.length];
//  for (int i = 0;i < lazy.length;i++)
//    arr[i] = lazy[i] == null ? "null" : lazy[i];
//  log.println(arr);
//}
//
