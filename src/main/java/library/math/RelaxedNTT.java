package library.math;

import static java.util.Arrays.*;

public class RelaxedNTT{
  private int n;
  private long[] f,g,h;

  public RelaxedNTT(){ this(16); }

  public RelaxedNTT(int n){
    f = new long[n];
    g = new long[n];
    h = new long[n];
  }

  public long append(long a,long b){
    f = grow(f,n);
    g = grow(g,n);
    f[n] = a;
    g[n] = b;
    n++;
    int m = n +1 &-(n +1);
    if (n < m) {
      m = m -1 >>1;
      calc(n >>1,n,n >>1,n);
    }

    int s = 0;
    for (int i = 1;i <= m;s += i,i <<= 1) {
      calc(n -i,n,s,s +i);
      calc(s,s +i,n -i,n);
    }
    return h[n -1];
  }

  private void calc(int l1,int r1,int l2,int r2){
    h = grow(h,r1 +r2 -1);
    long[] conv = NTT.convolution(f,l1,r1,g,l2,r2);
    for (int i = 0;i < conv.length;i++)
      h[i +l1 +l2] = (h[i +l1 +l2] +conv[i]) %998_244_353;
  }

  private long[] grow(long[] arr,int i){ return i < arr.length ? arr : copyOf(arr,i <<1); }
}