package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.rangeData.RangeData;

public abstract class DynamicSegmentTree<V, F> extends RangeData<V, F>{
  private Node root;
  private int log;

  DynamicSegmentTree(int log){ root = new Node(null,0,1 <<(this.log = log)); }

  protected abstract V e();

  protected abstract V agg(V a,V b);

  protected abstract V map(V v,F f);

  @Override
  public void upd(int i,F f){
    var nd = root;
    for (int j = 0;j < log;j++) {
      int c = nd.l +nd.r >>1;
      nd = i < c ? nd.lft() : nd.rht();
    }
    nd.val = map(nd.val,f);
    for (int j = 0;j < log;j++) {
      nd = nd.p;
      nd.val = nd.lft == null ? nd.rht.val
          : nd.rht == null ? nd.lft.val : agg(nd.lft.val,nd.rht.val);
    }
  }

  @Override
  public V get(int l,int r){ return get(root,l,r); }

  @Override
  public V get(int i){ return get(i,i +1); }

  private V get(Node nd,int l,int r){
    if (nd == null || nd.r <= l || r <= nd.l)
      return e();

    if (l <= nd.l && nd.r <= r)
      return nd.val;

    return agg(get(nd.lft,l,r),get(nd.rht,l,r));
  }

  private class Node{
    private Node p,lft,rht;
    private V val;
    private int l,r;

    private Node(Node p,int l,int r){
      this.p = p;
      this.l = l;
      this.r = r;
      val = e();
    }

    private Node lft(){ return lft == null ? lft = new Node(this,l,l +r >>1) : lft; }

    private Node rht(){ return rht == null ? rht = new Node(this,l +r >>1,r) : rht; }
  }
}