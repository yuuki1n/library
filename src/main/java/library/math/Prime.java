package library.math;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.*;

import library.util.*;

public class Prime{
  private long[] spf,
      arrI = {2, 7, 61},
      arrL = {2, 325, 9375, 28178, 450775, 9780504, 1795265022};

  public Prime(){ this(1_000_000); }

  public Prime(int n){
    spf = new long[n +1];
    Arrays.setAll(spf,i -> i);
    for (int p = 2;p *p <= n;p++)
      if (spf[p] == p)
        for (int l = p *p;l <= n;l += p)
          spf[l] = p;
  }

  public long[] divisors(long n){
    long[] fs = factorize(n);
    int l = fs.length > 0 ? 2 : 1,id = 0;
    for (int i = 1,sz = 1;i < fs.length;i++,l += sz)
      if (fs[i -1] < fs[i])
        sz = l;

    long[] ret = new long[l];
    ret[id++] = 1;
    for (int i = 0,s = 0,sz = 1;i < fs.length;i++,s += sz) {
      if (0 < i && fs[i -1] < fs[i]) {
        sz = id;
        s = 0;
      }
      for (int j = s;j < s +sz;j++)
        ret[id++] = ret[j] *fs[i];
    }
    sort(ret);
    return ret;
  }

  public long[] factorize(long n){
    if (n < 2)
      return new long[0];
    long[] ret = new long[64];
    int h = 0,t = 0;
    ret[t++] = n;
    while (h < t) {
      long cur = ret[h++];
      if (!isPrime(cur)) {
        var p = rho(cur);
        ret[--h] = p;
        ret[t++] = cur /p;
      }
    }
    sort(ret,0,t);
    return copyOf(ret,t);
  }

  public boolean isPrime(long n){
    if (n < spf.length)
      return 1 < n && spf[(int) n] == n;
    if ((n &1) == 0)
      return false;
    ModInt bm = new ModInt(n);
    long lsb = n -1 &-n +1;
    long m = (n -1) /lsb;
    a:for (var a:n < 1 <<30 ? arrI : arrL) {
      long z = bm.pow(a %n,m);
      if (z < 2)
        continue;
      for (long k = 1;k <= lsb;k <<= 1)
        if (z == n -1)
          continue a;
        else
          z = bm.mul(z,z);
      return false;
    }
    return true;
  }

  private long rho(long n){
    if (n < spf.length)
      return spf[(int) n];
    if ((n &1) == 0)
      return 2;
    ModInt bm = new ModInt(n);
    for (long t;;) {
      long x = 0,y = x,q = 1,c = Util.rd.nextLong(n -1) +1;
      a:for (int k = 1;;k <<= 1,y = x,q = 1)
        for (int i = k;i-- > 0;) {
          x = bm.mul(x,x) +c;
          if (n <= x)
            x -= n;
          q = bm.mul(q,abs(x -y));
          if ((i &127) == 0 && (t = gcd(q,n)) > 1)
            break a;
        }
      if (t < n)
        return t;
    }
  }

  private long gcd(long a,long b){
    while (0 < b) {
      long t = a;
      a = b;
      b = t %b;
    }
    return a;
  }

  class ModInt{
    private long n,r2,ni;

    public ModInt(long n){
      this.n = n;
      r2 = (1L <<62) %n;
      for (int i = 0;i < 66;i++) {
        r2 <<= 1;
        if (r2 >= n)
          r2 -= n;
      }
      ni = n;
      for (int i = 0;i < 5;++i)
        ni *= 2 -n *ni;
    }

    private static long high(long x,long y){ return multiplyHigh(x,y) +(x >>63 &y) +(y >>63 &x); }

    private long mr(long x,long y){ return high(x,y) +high(-ni *x *y,n) +(x *y == 0 ? 0 : 1); }

    public long mod(long x){ return x < n ? x : x -n; }

    public long mul(long x,long y){ return mod(mr(mr(x,r2),y)); }

    public long pow(long x,long y){
      long z = mr(x,r2);
      long r = 1;
      while (y > 0) {
        if ((y &1) == 1)
          r = mr(r,z);
        z = mr(z,z);
        y >>= 1;
      }
      return mod(r);
    }
  }
}