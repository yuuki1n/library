package library.util;

import static java.util.Arrays.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

import library.io.MyReader;
import library.io.MyWriter;

public class Util{
  public static String yes = "Yes",no = "No";
  public static int infI = (1 <<30) -1;
  public static long infL = (1L <<60 |1 <<30) -1;
  private long st = System.currentTimeMillis();
  public static Random rd = ThreadLocalRandom.current();
  static long mod = 998244353;
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

  long inv(long x,long mod){ return pow(x,mod -2,mod); }

  long pow(long x,long n){ return pow(x,n,Util.mod); }

  long pow(long x,long n,long mod){
    x %= mod;
    long ret = 1;
    while (0 < n) {
      if ((n &1) == 1)
        ret = ret *x %mod;
      x = x *x %mod;
      n >>= 1;
    }

    return ret;
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