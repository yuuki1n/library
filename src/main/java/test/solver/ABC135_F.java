package test.solver;

import static java.lang.Math.*;

import java.io.*;
import java.util.*;

import library.dataStructure.collection.*;
import library.string.*;
import test.tester.*;

public class ABC135_F extends BaseTester{
  public ABC135_F(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    String S = in.str();
    String T = in.str();
    int sl = S.length();
    int tl = T.length();
    S = S.repeat((sl +tl) /sl +1);
    MyList<MyList<Integer>> go = new MyList<>();
    int[] bk = new int[sl];
    for (int i = 0;i < sl;i++)
      go.add(new MyList<Integer>());
    RollingHash rhS = new RollingHash(S.toCharArray(),false);
    RollingHash rhT = new RollingHash(T.toCharArray(),false);

    for (int i = 0;i < sl;i++) {
      if (i == 55611) {
        log.printlns(S.substring(i,i +tl));
        log.printlns(T);
        log.printlns(rhS.get(i,i +tl),rhT.get(0,tl));
      }
      if (rhS.get(i,i +tl) == rhT.get(0,tl)) {
        go.get(i).add((i +tl) %sl);
        bk[(i +tl) %sl]++;
      }
    }
    int[] len = new int[sl];
    Queue<Integer> que = new ArrayDeque<>();

    for (int i = 0;i < sl;i++)
      if (bk[i] == 0)
        que.add(i);

    while (!que.isEmpty()) {
      var u = que.poll();
      MyList<Integer> goo = go.get(u);
      for (int i = 0;i < goo.size();i++) {
        var v = goo.get(i);
        len[v] = max(len[v],len[u] +1);
        bk[v]--;
        if (bk[v] == 0)
          que.add(v);
      }
    }

    for (var b:bk)
      if (b > 0)
        return -1;
    int ans = 0;
    for (var l:len)
      ans = max(ans,l);

    return ans;
  }
}