package library.util;

import static java.util.Arrays.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class Util{
  public static String yes = "Yes",no = "No";
  public static int infI = (1 <<30) -1;
  public static long infL = (1L <<60 |1 <<30) -1,
      mod = 998244353;
  public static Random rd = ThreadLocalRandom.current();
  private long st = System.currentTimeMillis();

  protected long elapsed(){ return System.currentTimeMillis() -st; }

  protected void reset(){ st = System.currentTimeMillis(); }

  public int[] arrI(int N,IntUnaryOperator f){
    int[] ret = new int[N];
    setAll(ret,f);
    return ret;
  }

  public long[] arrL(int N,IntToLongFunction f){
    long[] ret = new long[N];
    setAll(ret,f);
    return ret;
  }

  public double[] arrD(int N,IntToDoubleFunction f){
    double[] ret = new double[N];
    setAll(ret,f);
    return ret;
  }

  public <T> T[] arr(T[] arr,IntFunction<T> f){
    setAll(arr,f);
    return arr;
  }

  public int[][] addId(int[][] T){
    return arr(new int[T.length][],i -> {
      int[] t = copyOf(T[i],T[i].length +1);
      t[t.length -1] = i;
      return t;
    });
  }

  @SuppressWarnings("unchecked")
  public static <T> T cast(Object obj){ return (T) obj; }
}
