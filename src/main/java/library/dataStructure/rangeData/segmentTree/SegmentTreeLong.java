package library.dataStructure.rangeData.segmentTree;

public abstract class SegmentTreeLong extends SegLong{
  public SegmentTreeLong(int n){ super(n); }

  @Override
  protected long comp(long f,long g){ return 0; }

  @Override
  public void upd(int i,long f){
    super.upd(i,f);
    up(i,i +1);
  }
}