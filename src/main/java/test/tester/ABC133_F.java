package test.tester;

import java.io.*;
import java.util.*;

import library.dataStructure.rangeData.base.*;
import library.graph.*;
import library.graph.tree.*;

public class ABC133_F extends BaseTester{
  public ABC133_F(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int Q = in.it();
    var seg = new HLD<int[], Data, int[]>(N,true){
      @Override
      protected Data e(){ return new Data(new int[3]); }

      @Override
      protected void agg(Data v,Data a,Data b){
        for (int i = 0;i < 3;i++)
          v.v[i] = a.v[i] +b.v[i];
      }

      @Override
      protected void map(Data v,int[] f){
        for (int i = 0;i < 3;i++)
          v.v[i] += f[i];
      }
    };
    for (int i = 1;i < N;i++)
      seg.addEdge(in.idx(),in.idx(),new int[]{in.idx(), in.it()});

    seg.makeTree(0);
    int[][] T = addId(in.it(Q,4));
    for (var t:T) {
      t[0]--;
      t[2]--;
      t[3]--;
    }
    List<Queue<Edge<int[]>>> list = new ArrayList<>();
    for (int i = 0;i < N;i++)
      list.add(new ArrayDeque<>());
    for (var e:seg.es) {
      list.get(e.val[0]).add(e);
      seg.upd(e,new int[]{0, e.val[1], 0});
    }

    Arrays.sort(T,Comparator.comparing(t -> t[0]));
    int preCol = N -1;
    int[] ans = new int[Q];
    for (var t:T) {
      int x = t[0];
      int y = t[1];
      if (x != preCol) {
        for (var e:list.get(preCol))
          seg.upd(e,new int[]{e.val[1], 0, -1});
        for (var e:list.get(x))
          seg.upd(e,new int[]{0, -e.val[1], 1});
      }

      int[] tmp = seg.getPath(t[2],t[3],0).v;
      ans[t[4]] = tmp[0] +tmp[1] +tmp[2] *y;
      preCol = x;
    }

    return ans;
  }

  class Data extends BaseV{
    int[] v;

    public Data(int[] v){ this.v = v; }
  }
}