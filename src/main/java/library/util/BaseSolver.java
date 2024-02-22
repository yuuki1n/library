package library.util;

import static java.lang.Math.*;

import java.util.function.*;

import library.io.*;

public class BaseSolver extends Util{
  public MyReader in;
  public MyWriter out,log;
  public boolean multi = false;

  public BaseSolver(MyReader in,MyWriter out,MyWriter log){
    this.in = in;
    this.out = out;
    this.log = log;
  }

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
    if (!judge.test(o))
      return o -Integer.signum(n -o);
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
    for (double m,c = 0;c < 100;c++)
      m = judge.test(m = (o +n) /2) ? (o = m) : (n = m);
    return o;
  }

  protected long ceil(long a,long b){ return (a +b -1) /b; }
}