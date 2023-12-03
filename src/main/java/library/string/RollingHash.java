package library.string;

import static java.util.Arrays.*;

import java.util.concurrent.ThreadLocalRandom;

import library.util.Mod61;
import library.util.Util;

/**
 * ローリングハッシュ
 * 基本はRHashクラスの内部で使う
 * @author yuuki_n
 *
 */
public class RollingHash{
  private static long m;
  static {
    for (int k = 1;m < Util.infI;m = Mod61.pow(37,k))
      while (!Mod61.isPrimeRoot(k))
        k = ThreadLocalRandom.current().nextInt(Util.infI);
  }

  int n;
  private long[] hash,pow,S;
  private boolean updatale;

  public RollingHash(char[] S,boolean updatable){ this(Util.arrL(S.length,i -> S[i]),updatable); }

  public RollingHash(int[] S,boolean updatable){ this(Util.arrL(S.length,i -> S[i]),updatable); }

  public RollingHash(long[] S,boolean updatale){
    //    m = 10;
    S = copyOf(S,S.length <<1);
    for (int i = S.length >>1;i < S.length;i++)
      S[i] = S[S.length -i -1];
    this.S = new long[S.length];
    this.updatale = updatale;
    n = this.S.length;
    hash = new long[n +1];
    pow = new long[n +1];
    pow[0] = 1;
    for (int i = 0;i < n;i++)
      pow[i +1] = Mod61.mul(pow[i],m);
    for (int i = 0;i < n;i++)
      upd(i,S[i]);
  }

  public long get(int l,int r){
    if (l > r) {
      l = n -l;
      r = n -r;
    }
    return Mod61.mod(hash(r) -Mod61.mul(hash(l),pow[r -l]));
  }

  public void set(int i,long v){
    upd(i,v);
    i = n -i -1;
    upd(i,v);
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

}