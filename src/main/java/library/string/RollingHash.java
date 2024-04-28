package library.string;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.concurrent.*;
import java.util.function.*;

import library.util.*;

/**
 * ローリングハッシュ
 * @author yuuki_n
 *
 */
public class RollingHash{
  private static long MASK30 = (1L <<30) -1;
  private static long MASK31 = (1L <<31) -1;
  private static long MOD = (1L <<61) -1;
  private static long m = base();
  private static long[] pow = {1};
  int n;
  private long[] hash,S;
  private boolean updatable;
  private RollingHash rev;

  public RollingHash(char[] S,boolean updatable){ this(S.length,i -> S[i],updatable); }

  public RollingHash(int[] S,boolean updatable){ this(S.length,i -> S[i],updatable); }

  public RollingHash(long[] S,boolean updatable){ this(S.length,i -> S[i],updatable); }

  public RollingHash(int n,IntToLongFunction f,boolean updatale){
    S = new long[this.n = n];
    updatable = updatale;
    hash = new long[n +1];
    setPow(n);
    for (int i = 0;i < n;i++)
      set(i,f.applyAsLong(i));
  }

  public long get(int l,int r){
    if (l > r)
      return (rev == null ? rev = rev() : rev).get(n -l,n -r);
    return mod(hash(r) -mul(hash(l),pow[r -l]));
  }

  public void upd(int i,long v){
    assert updatable;
    set(i,v);
    if (rev != null)
      rev.set(n -i -1,v);
  }

  private void set(int i,long v){
    if (updatable)
      for (int x = i +1;x <= n;x += x &-x)
        hash[x] = mod(hash[x] +mul(v -S[i],pow[x -i -1]));
    else
      hash[i +1] = mod(mul(hash[i],m) +v);
    S[i] = v;
  }

  private long hash(int i){
    long ret = 0;
    if (updatable)
      for (int x = i;x > 0;x -= x &-x)
        ret = mod(ret +mul(hash[x],pow[i -x]));
    else
      ret = hash[i];
    return ret;
  }

  private void setPow(int n){
    if (n < pow.length)
      return;
    int s = pow.length;
    pow = copyOf(pow,max(pow.length <<1,n +1));
    for (int i = s;i < pow.length;i++)
      pow[i] = mul(pow[i -1],m);
  }

  private RollingHash rev(){
    long[] s = new long[n];
    for (int i = 0;i < n;i++)
      s[i] = S[n -1 -i];
    return new RollingHash(s,updatable);
  }

  private static long mul(long a,long b){
    long lu = a >>31;
    long ld = a &MASK31;
    long ru = b >>31;
    long rd = b &MASK31;
    long mid = ld *ru +lu *rd;
    return mod((lu *ru <<1) +ld *rd +((mid &MASK30) <<31) +(mid >>30));
  }

  private static long mod(long val){
    while (val < 0)
      val += MOD;
    val = (val &MOD) +(val >>61);
    return val > MOD ? val -MOD : val;
  }

  private static long pow(long x,long n){
    long ret = 1;
    do {
      if ((n &1) == 1)
        ret = mul(ret,x);
      x = mul(x,x);
    } while (0 < (n >>= 1));
    return ret;
  }

  private static long base(){
    long m = 0;
    for (int k = 1;m < Util.infI;m = pow(37,k))
      while (!isPrimeRoot(k))
        k = ThreadLocalRandom.current().nextInt(Util.infI);
    return m;
  }

  private static boolean isPrimeRoot(long a){
    long b = MOD -1;
    while (0 < b) {
      long t = a;
      a = b;
      b = t %b;
    }
    return a > 1;
  }
}