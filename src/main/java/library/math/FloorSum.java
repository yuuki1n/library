package library.math;

public class FloorSum{
  public static long calc(long n,long m,long a,long b){
    if (a < 0) {
      b += (n -1) *a;
      a *= -1;
    }

    long ret = 0;
    if (a >= m) {
      ret += a /m *n *(n -1) >>1;
      a %= m;
    }
    if (b >= m) {
      ret += b /m *n;
      b %= m;
    }

    long last = a *n +b;
    if (last >= m)
      ret += calc(last /m,a,m,last %m);

    return ret;
  }
}
