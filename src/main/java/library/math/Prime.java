package library.math;

import static java.util.Arrays.*;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import library.util.Util;

public class Prime{
  private Random rd = ThreadLocalRandom.current();
  private int[] spf;
  private long[] AInt;
  private BigInteger[] ALng;

  Prime(){ this(10_000_000); }

  Prime(int n){
    spf = Util.arrI(n +1,i -> i);
    for (int p = 2;p *p <= n;p++)
      if (spf[p] == p)
        for (int l = p *p;l <= n;l += p)
          spf[l] = p;
    AInt = new long[]{2, 7, 61};
    ALng = IntStream.of(2,325,9375,28178,450775,9780504,1795265022)
        .mapToObj(BigInteger::valueOf)
        .toArray(l -> new BigInteger[l]);
  }

  boolean isPrime(long n){
    if (n < spf.length)
      return 1 < n && spf[(int) n] == n;
    if ((n &1) == 0)
      return false;
    long lsb = Long.lowestOneBit(n -1);
    if (n < 1 <<30) {
      long m = (n -1) /lsb;
      a:for (var a:AInt) {
        long z = pow(a,m,n);
        if (z < 2)
          continue;
        for (long k = 1;k <= lsb;k <<= 1)
          if (z == n -1)
            continue a;
          else
            z = z *z %n;
        return false;
      }
    } else {
      BigInteger bn = BigInteger.valueOf(n),m = BigInteger.valueOf((n -1) /lsb);
      a:for (var ba:ALng) {
        var z = ba.modPow(m,bn);
        if (z.longValue() < 2)
          continue;
        for (long k = 1;k <= lsb;k <<= 1)
          if (z.longValue() == n -1)
            continue a;
          else
            z = z.multiply(z).mod(bn);
        return false;
      }
    }
    return true;
  }

  long[] factorize(long n){
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

  long[] divisors(long n){
    long[] fs = factorize(n);
    int l = 2,id = 0;
    for (int i = 1,sz = 1;i < fs.length;i++,l += sz)
      if (fs[i -1] < fs[i])
        sz = l;

    long[] ret = new long[l];
    ret[id++] = 1;
    ret[id++] = fs[0];
    for (int i = 1,s = 0,sz = 1;i < fs.length;i++,s += sz) {
      if (fs[i -1] < fs[i]) {
        sz = id;
        s = 0;
      }
      for (int j = s;j < s +sz;j++)
        ret[id++] = ret[j] *fs[i];
    }
    return ret;
  }

  private long rho(long n){
    if (n < spf.length)
      return spf[(int) n];
    if (n %2 == 0)
      return 2;
    BigInteger bn = BigInteger.valueOf(n);
    for (long t;;) {
      long x = 0,y = x,q = 1,c = rd.nextLong(n -1) +1;
      a:for (int k = 1;;k <<= 1,y = x,q = 1)
        for (int i = k;i-- > 0;) {
          x = mul(x,x,bn) +c;
          if (n <= x)
            x -= n;
          q = mul(q,Math.abs(x -y),bn);
          if ((i &127) == 0 && (t = gcd(q,n)) > 1)
            break a;
        }
      if (t < n)
        return t;
    }
  }

  public long mul(long a,long b,BigInteger bn){
    return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(bn).longValue();
  }

  private long pow(long x,long n,long mod){
    long ret = 1;
    for (x %= mod;0 < n;x = x *x %mod,n >>= 1)
      if ((n &1) == 1)
        ret = ret *x %mod;
    return ret;
  }

  private long gcd(long a,long b){
    while (0 < b) {
      long t = a;
      a = b;
      b = t %b;
    }
    return a;
  }
}