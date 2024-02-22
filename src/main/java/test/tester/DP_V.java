package test.tester;

import java.io.*;

import library.graph.*;
import library.graph.tree.*;

public class DP_V extends BaseTester{
  public DP_V(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int M = in.it();
    var g = new ReRootingDp<Long, Long, Long>(N){
      @Override
      protected Long agg(Long d0,Long d1){ return d0 *d1 %M; }

      @Override
      protected Long e(){ return 1L; }

      @Override
      protected Long adj(Long v,Edge<Long> e){ return v +1; }

      @Override
      protected Long ans(int u,Long sum){ return sum; }
    };
    for (int i = 1;i < N;i++)
      g.addEdge(in.idx(),in.idx());

    return g.calc();
  }
}