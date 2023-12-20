package library.util;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoublePredicate;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongPredicate;

import library.io.MyReader;
import library.io.MyWriter;

public class Util{
  public static String yes = "Yes",no = "No";
  public static int infI = (1 <<30) -1;
  public static long infL = (1L <<60 |1 <<30) -1;
  private long st = System.currentTimeMillis();
  public static Random rd = ThreadLocalRandom.current();
  public static long mod = 998244353;
  public MyReader in;
  public MyWriter out;
  public MyWriter log;

  public Util(InputStream in,OutputStream out,OutputStream log){
    this.in = new MyReader(in);
    this.out = new MyWriter(out);
    this.log = new MyWriter(log){
      @Override
      public void println(Object obj){ super.println(obj == null ? "null" : obj); }

      @Override
      protected void ln(){
        super.ln();
        flush();
      }
    };
  }

  protected long elapsed(){ return System.currentTimeMillis() -st; }

  protected void reset(){ st = System.currentTimeMillis(); }

  protected long inv(long x,long mod){ return pow(x,mod -2,mod); }

  protected long pow(long x,long n){ return pow(x,n,Util.mod); }

  protected long pow(long x,long n,long mod){
    long ret = 1;
    for (x %= mod;0 < n;x = x *x %mod,n >>= 1)
      if ((n &1) == 1)
        ret = ret *x %mod;
    return ret;
  }

  protected int bSearchI(int o,int n,IntPredicate judge){
    for (int m = 0;1 < abs(n -o);)
      m = judge.test(m = o +n >>1) ? (o = m) : (n = m);
    return o;
  }

  protected long bSearchL(long o,long n,LongPredicate judge){
    for (long m = 0;1 < abs(n -o);)
      m = judge.test(m = o +n >>1) ? (o = m) : (n = m);
    return o;
  }

  protected double bSearchD(double o,double n,DoublePredicate judge){
    for (double m;1e-9 < abs(n -o);)
      m = judge.test(m = (o +n) /2) ? (o = m) : (n = m);
    return o;
  }

  public static int[] arrI(int N,IntUnaryOperator f){
    int[] ret = new int[N];
    setAll(ret,f);
    return ret;
  }

  public static long[] arrL(int N,IntToLongFunction f){
    long[] ret = new long[N];
    setAll(ret,f);
    return ret;
  }

  public static double[] arrD(int N,IntToDoubleFunction f){
    double[] ret = new double[N];
    setAll(ret,f);
    return ret;
  }

  public static <T> T[] arr(T[] arr,IntFunction<T> f){
    setAll(arr,f);
    return arr;
  }
}