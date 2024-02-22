package library.dataStructure.rangeData.segmentTree;

import library.dataStructure.collection.*;
import library.dataStructure.rangeData.base.*;

@Deprecated
public abstract class DynamicSegmentTree<V extends BaseV, F> {
  private Node root,nl;

  public DynamicSegmentTree(int log){
    root = new Node(null,0,1 <<log);
    nl = new Node(null,0,0);
  }

  protected abstract V e();
  protected abstract void agg(V x,V a,V b);
  protected abstract void map(V v,F f);

  public void upd(int i,F f){
    var nd = root;
    while (nd.l +1 < nd.r)
      nd = i < nd.l +nd.r >>1 ? nd.lft() : nd.rht();
    map(nd.val,f);
    while (nd.p != null)
      if ((nd = nd.p).lft == null) {
        nl.l = nd.l;
        nl.r = nd.rht.l;
        agg(nd.val,nl.val,nd.rht.val);
      } else if (nd.rht == null) {
        nl.l = nd.lft.r;
        nl.r = nd.r;
        agg(nd.val,nd.lft.val,nl.val);
      } else
        agg(nd.val,nd.lft.val,nd.rht.val);
  }

  public V get(int l,int r){
    V ret = e();
    MyStack<Node> stk = new MyStack<>();
    stk.add(root);
    for (Node nd;!stk.isEmpty();)
      if ((nd = stk.pop()) == null || nd.r <= l || r <= nd.l)
        continue;
      else if (l <= nd.l && nd.r <= r) {
        var t = e();
        agg(t,ret,nd.val);
        ret = t;
      } else {
        stk.add(nd.rht);
        stk.add(nd.lft);
      }

    return ret;
  }

  public V get(int i){ return get(i,i +1); }

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
