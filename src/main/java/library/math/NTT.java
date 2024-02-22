package library.math;

import static java.lang.Math.*;
import static java.util.Arrays.*;

public class NTT{
  private static long[][] sum = sum();

  public static long[] convolution(long[] a,long[] b,int l){ return convolution(a,0,a.length,b,0,b.length,l); }

  public static long[] convolution(long[] a,long[] b){
    return convolution(a,0,a.length,b,0,b.length,a.length +b.length -1);
  }

  public static long[] convolution(long[] a,int al,int ar,long[] b,int bl,int br,int l){
    return copyOf(convolution(a,al,ar,b,bl,br),l);
  }

  public static long[] convolution(long[] a,int al,int ar,long[] b,int bl,int br){
    int n = ar -al;
    int m = br -bl;
    int z = max(1,Integer.highestOneBit(n +m -2) <<1);
    long[] ta = new long[z];
    long[] tb = new long[z];
    System.arraycopy(a,al,ta,0,ar -al);
    System.arraycopy(b,bl,tb,0,br -bl);
    a = ta;
    b = tb;

    butterfly(a,sum[0]);
    butterfly(b,sum[0]);
    for (int i = 0;i < z;i++)
      a[i] = a[i] *b[i] %998_244_353;
    butterflyInv(a,sum[1]);

    long iz = pow(z,998_244_351);
    for (int i = 0;i < a.length;i++)
      a[i] = a[i] *iz %998_244_353;
    return a;
  }

  private static void butterflyInv(long[] a,long[] sum){
    int n = a.length;
    int h = 32 -Integer.numberOfLeadingZeros(n -1);

    for (int ph = h;ph >= 1;ph--) {
      int w = 1 <<ph -1,p = 1 <<h -ph;
      long now = 1;
      for (int s = 0;s < w;s++) {
        int offset = s <<h -ph +1;
        for (int i = 0;i < p;i++) {
          long l = a[i +offset];
          long r = a[i +offset +p];
          a[i +offset] = (l +r) %998_244_353;
          a[i +offset +p] = (998_244_353 +l -r) *now %998_244_353;
        }
        int x = Integer.numberOfTrailingZeros(~s);
        now = now *sum[x] %998_244_353;
      }
    }
  }

  private static void butterfly(long[] a,long[] sum){
    int n = a.length;
    int h = 32 -Integer.numberOfLeadingZeros(n -1);
    long ADD = 998_244_351L *998_244_353;

    for (int ph = 1;ph <= h;ph++) {
      int w = 1 <<ph -1,p = 1 <<h -ph;
      long now = 1;
      for (int s = 0;s < w;s++) {
        int offset = s <<h -ph +1;
        for (int i = 0;i < p;i++) {
          long l = a[i +offset];
          long r = a[i +offset +p] *now;
          a[i +offset] = (l +r) %998_244_353;
          a[i +offset +p] = (l -r +ADD) %998_244_353;
        }
        int x = Integer.numberOfTrailingZeros(~s);
        now = now *sum[x] %998_244_353;
      }
    }
  }

  private static long[][] sum(){
    long[][] sum = new long[2][21];
    long[] es = new long[22];
    long[] ies = new long[22];
    long e = 15311432;
    long ie = 469870224;
    for (int i = 22;i-- > 0;) {
      es[i] = e;
      ies[i] = ie;
      e = e *e %998_244_353;
      ie = ie *ie %998_244_353;
    }
    long se = 1;
    long sie = 1;
    for (int i = 0;i < 21;i++) {
      sum[0][i] = es[i] *se %998_244_353;
      se = se *ies[i] %998_244_353;
      sum[1][i] = ies[i] *sie %998_244_353;
      sie = sie *es[i] %998_244_353;
    }
    return sum;
  }

  private static long pow(long x,long n){
    x %= 998_244_353;
    long ret = 1;
    do {
      if ((n &1) == 1)
        ret = ret *x %998_244_353;
      x = x *x %998_244_353;
    } while (0 < (n >>= 1));

    return ret;
  }
}