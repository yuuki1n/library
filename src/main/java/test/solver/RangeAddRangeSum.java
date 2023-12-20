package test.solver;

import static java.lang.Math.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.base.RangeData;
import library.dataStructure.rangeData.segmentTree.AVLSegmentTree;
import library.dataStructure.rangeData.segmentTree.LazySegmentTree;

public class RangeAddRangeSum extends BaseSolver{
  public RangeAddRangeSum(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int seed = rd.nextInt();
    System.out.println(seed);
    int N = 100000;
    rd = new Random(1);
    AVLSegmentTree<Data, Long> seg = avl(N);
    RangeData<Data, Long> seg2 = lazy(N);

    int cnt = 0;
    while (elapsed() < 1000) {
      cnt++;
      int i,j;
      do {
        i = rd.nextInt(N);
        j = rd.nextInt(N) +1;
      } while (i >= j);
      long x = rd.nextInt(10) +1;
      if (rd.nextBoolean()) {
        log.println(i +"\t" +j +"\t" +x);
        seg.upd(i,j,x);
        seg2.upd(i,j,x);
        //        assert seg.check();
      } else {
        var a = seg.get(i,j);
        var b = seg2.get(i,j);
        //        assert seg.check();
        var d = max(j,seg.size()) -max(i,seg.size());
        assert a.v == b.v && a.sz +d == b.sz : cnt +"," +i +"," +j;
      }
    }

    cnt = 0;
    seg = avl(N);
    reset();
    while (elapsed() < 1000) {
      cnt++;
      int i,j;
      do {
        i = rd.nextInt(N);
        j = rd.nextInt(N) +1;
      } while (i >= j);
      long x = rd.nextInt(10) +1;
      if (rd.nextBoolean())
        seg.upd(i,j,x);
      else
        seg.get(i,j);
    }

    return cnt;
  }

  private RangeData<Data, Long> lazy(int N){
    RangeData<Data, Long> seg2 = new LazySegmentTree<>(N){
      @Override
      protected Data e(){ return new Data(0); }

      @Override
      protected void map(Data v,Long f){ v.v += v.sz *f; }

      @Override
      protected Long comp(Long f,Long g){ return f +g; }

      @Override
      protected void agg(Data v,Data a,Data b){ v.v = a.v +b.v; }
    };
    return seg2;
  }

  private AVLSegmentTree<Data, Long> avl(int N){
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
    return seg;
  }

  class Data extends BaseV{
    long v;

    public Data(long v){ this.v = v; }

    @Override
    public String toString(){ return "" +v; }
  }
}
//@Override
//public String toString(){
//  List<String> list = new ArrayList<>();
//  dfs(nl.rht,list,0);
//  return list.toString();
//}
//
//private void dfs(Node nd,List<String> list,int dpt){
//  list.add("(" +dpt +"," +nd.sz +"," +nd.val() +")");
//  if (!nd.leaf) {
//    dfs(nd.lft,list,dpt +1);
//    dfs(nd.rht,list,dpt +1);
//  }
//}
//
//public boolean check(){ return dfs(nl.rht); }
//
//private boolean dfs(AVLSegmentTree<V, F>.Node nd){
//  if (abs(nd.bis) > 1)
//    return false;
//  if (!nd.leaf)
//    return dfs(nd.lft) && dfs(nd.rht);
//  return true;
//}
