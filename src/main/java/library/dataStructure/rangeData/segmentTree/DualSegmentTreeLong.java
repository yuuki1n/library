package library.dataStructure.rangeData.segmentTree;

public abstract class DualSegmentTreeLong extends SegLong{
  public DualSegmentTreeLong(int n){ super(n); }

  @Override
  protected long agg(long a,long b){ return 0; }

  @Override
  public void upd(int i,long f){ upd(i,i +1,f); }

  @Override
  public void upd(int l,int r,long f){
    down(l,r);
    super.upd(l,r,f);
  }

  @Override
  public long get(int i){
    down(i,i +1);
    return super.get(i);
  }
}