package library.dataStructure.rangeData;

import static java.lang.Math.*;

public abstract class SparseTable{
  private int n;
  private long[] tbl;

  public SparseTable(int n){
    int K = max(1,32 -Integer.numberOfLeadingZeros(n -1));
    this.n = 1 <<K;

    tbl = new long[K *this.n];
    for (int i = 0;i < this.n;i++)
      tbl[i] = i < n ? init(i) : e();
    for (int k = 1;k < K;k++)
      for (int s = 1 <<k;s < this.n;s += 2 <<k) {
        int b = k *this.n;
        tbl[b +s] = s < n ? init(s) : e();
        tbl[b +s -1] = s < n ? init(s -1) : e();
        for (int i = 1;i < 1 <<k;i++) {
          tbl[b +s +i] = agg(tbl[b +s +i -1],tbl[s +i]);
          tbl[b +s -1 -i] = agg(tbl[b +s -i],tbl[s -1 -i]);
        }
      }
  }

  protected abstract long e();
  protected abstract long init(int i);
  protected abstract long agg(long a,long b);

  public long get(int l,int r){
    r--;
    if (l == r)
      return tbl[l];
    int k = 31 -Integer.numberOfLeadingZeros(l ^r);
    long ret = e();
    ret = agg(tbl[k *n +l],tbl[k *n +r]);
    return ret;
  }
}