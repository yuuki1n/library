package library.dataStructure.rangeData;

public class DualBIT{
  private int n;
  private long[] val;

  public DualBIT(int n){
    this.n = n +1;
    val = new long[n +2];
  }

  public long agg(long a,long b){ return a +b; }

  public long inv(long v){ return -v; }

  public long get(int i){ return sum(i +1); }

  public void upd(int l,int r,long v){
    upd(l,v);
    upd(r,inv(v));
  }

  private void upd(int x,long v){
    for (x++;x <= n;x += x &-x)
      val[x] = agg(val[x],v);
  }

  private long sum(int x){
    long ret = 0;
    for (;x > 0;x -= x &-x)
      ret = agg(ret,val[x]);
    return ret;
  }
}