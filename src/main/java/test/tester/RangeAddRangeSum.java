package test.tester;

import java.io.*;
import java.util.*;

import library.dataStructure.rangeData.base.*;
import library.dataStructure.rangeData.segmentTree.*;
import library.io.*;
import test.base.*;

public class RangeAddRangeSum extends BaseTester{
  public RangeAddRangeSum(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int seed = rd.nextInt();
    System.out.println(seed);
    int N = 1000_000_000;
    //    rd = new Random(1);
    int cnt;
    AVLSegmentTree<Data, Long> seg = avl(N);
    //    LazySegmentTree<Data, Long> seg2 = lazy(N);
    //
    //    while (elapsed() < 1000) {
    //      cnt++;
    //      int i,j;
    //      do {
    //        i = rd.nextInt(N);
    //        j = rd.nextInt(N) +1;
    //      } while (i >= j);
    //      long x = rd.nextInt(infI) +1;
    //      if (rd.nextBoolean()) {
    //        seg.upd(i,j,x);
    //        seg2.upd(i,j,x);
    //      } else {
    //        var a = seg.get(i,j);
    //        var b = seg2.get(i,j);
    //        assert a.v == b.v && a.sz == b.sz : cnt +"," +i +"," +j;
    //      }
    //    }

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
      long x = rd.nextInt(infI) +1;
      if (rd.nextBoolean())
        seg.upd(i,j,x);
      else
        seg.get(i,j);
    }

    return cnt;
  }

  private LazySegmentTree<Data, Long> lazy(int N){
    LazySegmentTree<Data, Long> seg2 = new LazySegmentTree<>(N){
      @Override
      protected Data e(){ return new Data(0); }

      @Override
      protected void map(Data v,Long f){ v.v = (v.v +v.sz *f) %mod; }

      @Override
      protected Long comp(Long f,Long g){ return (f +g) %mod; }

      @Override
      protected void agg(Data v,Data a,Data b){ v.v = (a.v +b.v) %mod; }
    };
    return seg2;
  }

  private AVLSegmentTree<Data, Long> avl(int N){
    var seg = new AVLSegmentTree<Data, Long>(N){
      @Override
      protected Data e(){ return new Data(0); }

      @Override
      protected void map(Data v,Long f){ v.v = (v.v +v.sz *f) %mod; }

      @Override
      protected Long comp(Long f,Long g){ return (f +g) %mod; }

      @Override
      protected void agg(Data v,Data a,Data b){ v.v = (a.v +b.v) %mod; }

      @Override
      protected void pow(Data v,Data a,int n){ v.v = a.v *n %mod; }

      @Override
      protected void tog(Data v){}
    };
    return seg;
  }

  @Override
  public boolean verify(MyReader in,Scanner subIn,Scanner extIn){ return true; }
}

class Data extends BaseV{
  long v;

  public Data(long v){ this.v = v; }

  @Override
  public String toString(){ return "" +v; }
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
