package library.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 *mod 1^61-1での計算をする
 * @author yuuki_n
 *
 */
public class Mod61{
  private static long MASK30 = (1L <<30) -1;
  private static long MASK31 = (1L <<31) -1;
  private static long MOD = (1L <<61) -1;

  public static long mul(long a,long b){
    long lu = a >>31;
    long ld = a &MASK31;
    long ru = b >>31;
    long rd = b &MASK31;
    long mid = ld *ru +lu *rd;
    return mod((lu *ru <<1) +ld *rd +((mid &MASK30) <<31) +(mid >>30));
  }

  public static long mod(long val){
    while (val < 0)
      val += MOD;
    val = (val &MOD) +(val >>61);
    return val > MOD ? val -MOD : val;
  }

  public static long pow(long x,long n){
    long ret = 1;
    do {
      if ((n &1) == 1)
        ret = mul(ret,x);
      x = mul(x,x);
    } while (0 < (n >>= 1));
    return ret;
  }

  public static long base(){
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