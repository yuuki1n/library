package test.solver;

import static java.util.Arrays.*;

import java.io.*;
import java.lang.reflect.*;

import test.tester.*;

public class ARC147_C extends BaseTester{
  public ARC147_C(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int[] L = new int[N];
    int[] R = new int[N];
    for (int i = 0;i < N;i++) {
      L[i] = in.it();
      R[i] = in.it();
    }
    sort(L);
    reverse(L);
    sort(R);
    long ans = 0;
    for (int i = 0;i < N;i++)
      ans += Math.max(0L,L[i] -R[i]) *(N -2 *i -1);
    return ans;
  }

  void reverse(Object arr){
    if (!arr.getClass().isArray())
      throw new UnsupportedOperationException("reverse");

    int l = 0;
    int r = Array.getLength(arr) -1;

    while (l < r) {
      Object t = Array.get(arr,l);
      Array.set(arr,l,Array.get(arr,r));
      Array.set(arr,r,t);
      l++;
      r--;
    }
  }
}