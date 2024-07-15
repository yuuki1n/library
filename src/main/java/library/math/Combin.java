package library.math;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import library.util.*;

public class Combin{
  int n = 2;
  long[] f,fi;
  long mod = Util.mod;

  public Combin(int n){
    this();
    grow(n);
  }

  public Combin(){ f = fi = new long[]{1, 1}; }

  public void grow(int n){
    n = min((int) mod,n);
    f = copyOf(f,n);
    fi = copyOf(fi,n);
    for (int i = this.n;i < n;i++)
      f[i] = f[i -1] *i %mod;
    fi[n -1] = pow(f[n -1],mod -2);
    for (int i = n;--i > this.n;)
      fi[i -1] = fi[i] *i %mod;
    this.n = n;
  }

  private long pow(long x,long n){
    long ret = 1;
    for (x %= mod;0 < n;x = x *x %mod,n >>= 1)
      if ((n &1) == 1)
        ret = ret *x %mod;
    return ret;
  }

  public long nHr(int n,int r){ return r < 0 ? 0 : nCr(n +r -1,r); }

  public long nCr(int n,int r){
    if (r < 0 || n -r < 0)
      return 0;

    if (this.n <= n)
      grow(max(this.n <<1,n +1));
    return f[n] *(fi[r] *fi[n -r] %mod) %mod;
  }
}