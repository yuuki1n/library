package library.dataStructure.rangeData.segmentTree;

public abstract class LazySegmentTreeLong extends SegLong{
  public LazySegmentTreeLong(int n){ super(n); }

  @Override
  public void upd(int i,long f){ upd(i,i +1,f); }

  @Override
  public void upd(int l,int r,long f){
    down(l,r);
    super.upd(l,r,f);
    up(l,r);
  }

  @Override
  public long get(int i){
    down(i,i +1);
    return super.get(i);
  }

  @Override
  public long get(int l,int r){
    down(l,r);
    return super.get(l,r);
  }
}
