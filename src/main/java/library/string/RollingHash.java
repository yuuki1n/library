package library.string;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.concurrent.ThreadLocalRandom;

import library.util.Mod61;
import library.util.Util;

/**
 * ローリングハッシュ
 * @author yuuki_n
 *
 */
public class RollingHash{
  private static long m;
  private static long[] pow;
  static {
    for (int k = 1;m < Util.infI;m = Mod61.pow(37,k))
      while (!Mod61.isPrimeRoot(k))
        k = ThreadLocalRandom.current().nextInt(Util.infI);
    pow = new long[]{1};
  }

  int n;
  private long[] hash,S;
  private boolean updatale;
  private RollingHash rev;

  public RollingHash(char[] S,boolean updatable){ this(Util.arrL(S.length,i -> S[i]),updatable); }

  public RollingHash(int[] S,boolean updatable){ this(Util.arrL(S.length,i -> S[i]),updatable); }

  public RollingHash(long[] S,boolean updatale){
    this.S = new long[S.length];
    this.updatale = updatale;
    n = this.S.length;
    hash = new long[n +1];
    setPow(n);
    for (int i = 0;i < n;i++)
      upd(i,S[i]);
  }

  /**
   * [l,r)部分のハッシュ値を返す
   * l>rの時は(l,r]部分を逆順にしたもののハッシュ値を返す
   * @param l
   * @param r
   * @return
   */
  public long get(int l,int r){
    if (l > r)
      return (rev == null ? rev = rev() : rev).get(n -l,n -r);
    return Mod61.mod(hash(r) -Mod61.mul(hash(l),pow[r -l]));
  }

  public void set(int i,long v){
    assert updatale;
    upd(i,v);
    if (rev != null)
      rev.upd(n -i -1,v);
  }

  public static boolean equal(RollingHash rhS,int sl,int sr,RollingHash rhT,int tl,int tr){
    return rhS.get(sl,sr) == rhT.get(tl,tr);
  }

  private void upd(int i,long v){
    if (updatale)
      for (int x = i +1;x <= n;x += x &-x)
        hash[x] = Mod61.mod(hash[x] +Mod61.mul(v -S[i],pow[x -i -1]));
    else
      hash[i +1] = Mod61.mod(Mod61.mul(hash[i],m) +v);
    S[i] = v;
  }

  private long hash(int i){
    long ret = 0;
    if (updatale)
      for (int x = i;x > 0;x -= x &-x)
        ret = Mod61.mod(ret +Mod61.mul(hash[x],pow[i -x]));
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
      pow[i] = Mod61.mul(pow[i -1],m);
  }

  private RollingHash rev(){
    long[] s = new long[n];
    for (int i = 0;i < n;i++)
      s[i] = S[n -1 -i];
    return new RollingHash(s,updatale);
  }
}