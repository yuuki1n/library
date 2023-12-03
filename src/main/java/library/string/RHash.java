package library.string;

import static java.util.Arrays.*;

import java.util.concurrent.ThreadLocalRandom;

import library.util.Mod61;
import library.util.Util;

/**
 * ローリングハッシュ
 * 一致判定のテスト回数をnumで設定する
 * @author yuuki_n
 *
 */
public class RHash{
  private static int num = 3;
  private static long[] ms = new long[num];
  private RollingHash[] rhs;

  static {
    for (int i = 0,k;i < num;ms[i] = Mod61.pow(37,k),i++)
      do
        k = ThreadLocalRandom.current().nextInt(Util.infI);
      while (!Mod61.isPrimeRoot(k));
  }

  public RHash(char[] S,boolean updatable){ this(Util.arrL(S.length,i -> S[i]),true); }

  public RHash(int[] S,boolean updatable){ this(Util.arrL(S.length,i -> S[i]),true); }

  public RHash(long[] S,boolean updatable){
    S = copyOf(S,S.length <<1);
    for (int i = S.length >>1;i < S.length;i++)
      S[i] = S[S.length -i -1];
    rhs = new RollingHash[num];
    for (int m = 0;m < num;m++)
      rhs[m] = new RollingHash(S,ms[m],updatable);
  }

  public void upd(int i,char c){
    for (var rh:rhs) {
      rh.set(i,c);
      rh.set(rh.n -i -1,c);
    }
  }

  public static boolean equal(RHash rhS,int sl,int sr,RHash rhT,int tl,int tr){
    for (int i = 0;i < num;i++)
      if (get(rhS.rhs[i],sl,sr) != get(rhT.rhs[i],tl,tr))
        return false;

    return true;
  }

  public static boolean equal(RHash rhS,int[] s,RHash rhT,int[] t){
    for (int i = 0;i < num;i++)
      if (get(rhS.rhs[i],s) != get(rhT.rhs[i],t))
        return false;

    return true;
  }

  private static long get(RollingHash rh,int l,int r){ return l < r ? rh.get(l,r) : rh.get(rh.n -l,rh.n -r); }

  private static long get(RollingHash rh,int[] s){
    for (int i = 0;i < s.length;i += 2)
      if (s[i] > s[i +1]) {
        s[i] = rh.n -s[i];
        s[i +1] = rh.n -s[i +1];
      }
    return rh.get(s);
  }
}