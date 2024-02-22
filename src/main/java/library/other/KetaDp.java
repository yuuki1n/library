package library.other;

import static java.util.Arrays.*;

import library.util.*;

public abstract class KetaDp<T> {
  private int B;
  private int[] N;
  private T[] dp;

  public KetaDp(char[] N){ this(N,10); }

  public KetaDp(char[] N,int B){
    this.N = new int[N.length];
    for (int i = 0;i < N.length;i++)
      this.N[i] = N[i] -'0';
    dp = Util.cast(new Object[N.length +1 <<1]);
    this.B = B;
    setAll(dp,i -> init());
  }

  protected abstract T init();
  protected abstract void f(T pd,T dp,int n,int k);

  protected void mod(T dp){}

  public T get(int i,int same){ return dp[i *2 +same]; }

  public void calc(){
    for (int i = 0;i < N.length;i++) {
      int t = N[i];
      for (int n = 0;n < B;n++) {
        if (n == t)
          f(get(i +1,1),get(i,1),n,N.length -1 -i);
        if (n < t)
          f(get(i +1,0),get(i,1),n,N.length -1 -i);
        if (0 < i)
          f(get(i +1,0),get(i,0),n,N.length -1 -i);
      }
      mod(get(i +1,0));
      mod(get(i +1,1));
    }
  }
}