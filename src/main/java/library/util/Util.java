package library.util;

import static java.util.Arrays.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

public class Util{
  public static String yes = "Yes",no = "No";
  public static int infI = (1 <<30) -1;
  public static long infL = (1L <<61 |1 <<30) -1,
      mod = 998244353;
  public static Random rd = ThreadLocalRandom.current();
  private long st = System.currentTimeMillis();

  protected long elapsed(){ return System.currentTimeMillis() -st; }

  protected void reset(){ st = System.currentTimeMillis(); }

  public static int[] arrI(int N,IntUnaryOperator f){ return IntStream.range(0,N).map(f).toArray(); }

  public static long[] arrL(int N,IntToLongFunction f){ return IntStream.range(0,N).mapToLong(f).toArray(); }

  public static double[] arrD(int N,IntToDoubleFunction f){ return IntStream.range(0,N).mapToDouble(f).toArray(); }

  public static <T> T[] arr(T[] arr,IntFunction<T> f){
    setAll(arr,f);
    return arr;
  }

  @SuppressWarnings("unchecked")
  public static <T> T cast(Object obj){ return (T) obj; }

  public static <T> void swap(T[] arr,int i,int j){
    T t = arr[i];
    arr[i] = arr[j];
    arr[j] = t;
  }

  public static int min(int... arr){
    int ret = arr[0];
    for (int i = 1;i < arr.length;i++)
      ret = Math.min(ret,arr[i]);
    return ret;
  }

  public static long min(long... arr){
    long ret = arr[0];
    for (int i = 1;i < arr.length;i++)
      ret = Math.min(ret,arr[i]);
    return ret;
  }

  public static double min(double... arr){
    double ret = arr[0];
    for (int i = 1;i < arr.length;i++)
      ret = Math.min(ret,arr[i]);
    return ret;
  }

  public static int max(int... arr){
    int ret = arr[0];
    for (int i = 1;i < arr.length;i++)
      ret = Math.max(ret,arr[i]);
    return ret;
  }

  public static long max(long... arr){
    long ret = arr[0];
    for (int i = 1;i < arr.length;i++)
      ret = Math.max(ret,arr[i]);
    return ret;
  }

  public static double max(double... arr){
    double ret = arr[0];
    for (int i = 1;i < arr.length;i++)
      ret = Math.max(ret,arr[i]);
    return ret;
  }

  public static double sum(double... A){
    double ret = 0;
    for (var a:A)
      ret += a;
    return ret;
  }

  public static long sum(int... A){
    long ret = 0;
    for (var a:A)
      ret += a;
    return ret;
  }

  public static long sum(long... A){
    long ret = 0;
    for (var a:A)
      ret += a;
    return ret;
  }
}