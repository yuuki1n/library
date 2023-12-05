package library.dataStructure.rangeData.segmentTree;

import java.util.Stack;

import library.dataStructure.rangeData.RangeData;
import library.dataStructure.rangeData.base.BaseF;
import library.dataStructure.rangeData.base.BaseV;

public abstract class DynamicSegmentTree<VT extends BaseV, FT extends BaseF> extends RangeData<VT, FT>{
  private Node root,nl;

  public DynamicSegmentTree(int log){
    root = new Node(null,0,1 <<log);
    nl = new Node(null,0,0);
  }

  protected abstract VT e();

  protected abstract void agg(VT x,VT a,VT b);

  protected abstract void map(VT v,FT f);

  @Override
  public void upd(int i,FT f){
    var nd = root;
    while (nd.l() +1 < nd.r())
      nd = i < nd.l() +nd.r() >>1 ? nd.lft() : nd.rht();
    map(nd.val,f);
    while (nd.p != null) {
      nd = nd.p;
      int l = nd.l(),r = nd.r();
      agg(nd.val,(nd.lft != null ? nd.lft : nl).val,(nd.rht != null ? nd.rht : nl).val);
      nd.val.l = l;
      nd.val.r = r;
    }
  }

  @Override
  public VT get(int l,int r){
    VT ret = e();
    ret.l = ret.r = l;
    Stack<Node> stk = new Stack<>();
    stk.add(root);
    for (Node nd;!stk.isEmpty();)
      if ((nd = stk.pop()) == null || nd.r() <= l || r <= nd.l())
        continue;
      else if (l <= nd.l() && nd.r() <= r)
        ret = agg(ret,nd.val);
      else {
        stk.add(nd.rht);
        stk.add(nd.lft);
      }

    return ret;
  }

  @Override
  public VT get(int i){ return get(i,i +1); }

  private VT agg(VT vl,VT vr){
    var t = e();
    agg(t,vl,vr);
    return t;
  }

  private class Node{
    private Node p,lft,rht;
    private VT val;

    private Node(Node p,int l,int r){
      this.p = p;
      val = e();
      val.l = l;
      val.r = r;
    }

    private int l(){ return val.l; }

    private int r(){ return val.r; }

    private Node lft(){ return lft == null ? lft = new Node(this,l(),l() +r() >>1) : lft; }

    private Node rht(){ return rht == null ? rht = new Node(this,l() +r() >>1,r()) : rht; }
  }
}