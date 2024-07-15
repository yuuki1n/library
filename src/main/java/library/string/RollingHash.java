package library.string;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.concurrent.*;

import library.util.*;

/**
 * ローリングハッシュ
 * @author yuuki_n
 *
 */
public class RollingHash{
  private static long MOD = (1L <<61) -1,MASK30 = (1L <<30) -1,MASK31 = (1L <<31) -1,
      base = ThreadLocalRandom.current().nextLong(Util.infI,MOD);
  private static long[] pow = {1};
  int n;
  private long[] hash,S;
  private boolean updatable;
  private RollingHash rev;

  public RollingHash(char[] S,boolean updatable){ this(Util.arrL(S.length,i -> S[i]),updatable); }

  public RollingHash(int[] S,boolean updatable){ this(Util.arrL(S.length,i -> S[i]),updatable); }

  public RollingHash(long[] S,boolean updatable){
    this.S = new long[n = S.length];
    this.updatable = updatable;
    hash = new long[n +1];
    if (pow.length <= n) {
      int s = pow.length;
      pow = copyOf(pow,max(pow.length <<1,n +1));
      for (int i = s;i < pow.length;i++)
        pow[i] = mul(pow[i -1],base);
    }
    for (int i = 0;i < n;i++)
      set(i,S[i]);
  }

  public long get(int i){ return get(i,i +1); }

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
      hash[i +1] = mod(mul(hash[i],base) +v);
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

  private RollingHash rev(){
    long[] s = new long[n];
    for (int i = 0;i < n;i++)
      s[i] = S[n -1 -i];
    return new RollingHash(s,updatable);
  }

  private static long mul(long a,long b){
    long au = a >>31;
    long ad = a &MASK31;
    long bu = b >>31;
    long bd = b &MASK31;
    long mid = ad *bu +au *bd;
    return mod((au *bu <<1) +(mid >>30) +((mid &MASK30) <<31) +ad *bd);
  }

  private static long mod(long val){
    return val < 0 ? val +MOD : (val = (val &MOD) +(val >>61)) < MOD ? val : val -MOD;
  }
}