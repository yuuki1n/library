package library.dataStructure.rangeData;

import library.dataStructure.rangeData.base.RangeData;

public abstract class PrefixSum extends RangeData<Long, Long>{
  long[] sum;

  public PrefixSum(int n){
    sum = new long[n +1];
    for (int i = 0;i < n;i++)
      sum[i +1] = sum[i] +a(i);
  }

  abstract Long a(int i);

  @Override
  public Long get(int l,int r){ return sum[r] -sum[l]; }
}
