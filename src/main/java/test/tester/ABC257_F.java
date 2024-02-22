package test.tester;

import static java.lang.Math.*;

import java.io.*;

import library.graph.*;
import test.*;
import test.base.*;

public class ABC257_F extends BaseTester{
  public ABC257_F(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int M = in.it();

    Dijkstra<Integer, Integer> g = new Dijkstra<>(N +1,false){
      @Override
      protected Integer zero(){ return 0; }

      @Override
      protected Integer inf(){ return infI; }

      @Override
      protected Integer f(Integer l,Edge<Integer> e){ return l +e.val; }
    };

    for (int m = 0;m < M;m++) {
      int u = in.it();
      int v = in.it();
      g.addEdge(u,v,1);
    }
    var g1 = g.calc(1);
    var gn = g.calc(N);
    for (int i = 1;i <= N;i++) {
      int ans = g1[N];
      ans = min(ans,g1[i] +gn[0]);
      ans = min(ans,g1[0] +gn[i]);
      out.println(ans == infI ? -1 : ans);
    }
    return null;
  }
}