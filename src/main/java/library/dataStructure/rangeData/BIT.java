package library.dataStructure.rangeData;

public class BIT{
  public int n;
  private long[] bit;

  public BIT(int n){ this(new long[n]); }

  public BIT(long[] A){
    n = A.length;
    bit = new long[n +1];
    for (int i = 1;i <= n;i++) {
      bit[i] += A[i -1];
      if (i +(i &-i) <= n)
        bit[i +(i &-i)] += bit[i];
    }
  }

  public void upd(int x,long v){
    for (x++;x <= n;x += x &-x)
      bit[x] += v;
  }

  public long sum(int x){
    long ret = 0;
    for (;x > 0;x -= x &-x)
      ret += bit[x];
    return ret;
  }

  public long get(int i){ return get(i,i +1); }

  public long get(int l,int r){ return sum(r) -sum(l); }
}