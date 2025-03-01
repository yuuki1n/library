package library.dataStructure.rangeData;

public class PrefixSum{
  private long[] sum;
  private int i;

  public PrefixSum(int n){ sum = new long[n +1]; }

  public PrefixSum(long[] a){
    this(a.length);
    for (int i = 0;i < a.length;i++)
      sum[i +1] = sum[i] +a[i];
  }

  public void add(long a){ sum[i +1] = sum[i++] +a; }

  public long get(int l,int r){ return sum[r] -sum[l]; }

  public long get(int i){ return get(i,i +1); }
}
