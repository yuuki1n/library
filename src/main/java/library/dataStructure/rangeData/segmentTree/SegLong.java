package library.dataStructure.rangeData.segmentTree;

public abstract class SegLong{
  private int n;
  private long[] val,lazy;
  private boolean[] lazflg;
  private int[] sz;

  protected SegLong(int n){
    this.n = n;
    val = new long[n <<1];
    lazy = new long[n];
    lazflg = new boolean[n];
    sz = new int[n <<1];

    for (int i = -1;++i < n;)
      val[i +n] = init(i);
    for (int i = n;--i > 0;)
      merge(i);

    for (int i = n;i < val.length;i++)
      sz[i] = 1;
    for (int i = n -1;--i > 0;)
      sz[i] = sz[i <<1] +sz[i <<1 |1];
  }

  public void upd(int i,long f){ prop(i +n,f); }

  public void upd(int l,int r,long f){
    for (l += n,r += n;l < r;l >>= 1,r >>= 1) {
      if ((l &1) == 1)
        prop(l++,f);
      if ((r &1) == 1)
        prop(--r,f);
    }
  }

  public long get(int i){ return val[i +n]; }

  public long get(int l,int r){
    long ret = e();
    for (l += n,r += n;l < r;l >>= 1,r >>= 1) {
      if ((l &1) > 0)
        ret = agg(ret,val[l++]);
      if ((r &1) > 0)
        ret = agg(ret,val[--r]);
    }
    return ret;
  }

  protected long init(int i){ return e(); }

  protected abstract long e();
  protected abstract long agg(long a,long b);
  protected abstract long map(long v,long f,int sz);
  protected abstract long comp(long f,long g);

  protected void up(int l,int r){
    for (l = oddPart(l +n),r = oddPart(r +n);l != r;)
      merge(l > r ? (l >>= 1) : (r >>= 1));
    while (1 < l)
      merge(l >>= 1);
  }

  protected void down(int l,int r){
    int i = 32 -Integer.numberOfLeadingZeros(n);
    for (l = oddPart(l +n),r = oddPart(r +n);i > 0;i--) {
      push(l >>i);
      push(r >>i);
    }
  }

  private void merge(int i){ val[i] = agg(val[i <<1],val[i <<1 |1]); }

  private void push(int i){
    if (lazflg[i]) {
      prop(i <<1,lazy[i]);
      prop(i <<1 |1,lazy[i]);
      lazflg[i] = false;
    }
  }

  private void prop(int i,long f){
    val[i] = map(val[i],f,sz[i]);
    if (i < n)
      lazy[i] = lazflg[i] ? comp(lazy[i],f) : f;
  }

  private int oddPart(int i){ return i /(i &-i); }
}