package library.dataStructure.rangeData.segmentTree;

import static java.lang.Math.*;

import java.util.function.IntFunction;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.base.RangeData;

public abstract class AVLSegmentTree<V extends BaseV, F> extends RangeData<V, F>{
  private V e = e();
  private Node root;

  public AVLSegmentTree(int n){ root = new Node(e(),n); }

  public AVLSegmentTree(){}

  public void build(int n,IntFunction<V> init){ root = build(0,n,init); }

  private Node build(int i,int n,IntFunction<V> init){
    if (n < 2)
      return n < 1 ? null : new Node(init.apply(i),1);
    var ret = new Node(e(),n);
    ret.leaf = false;
    ret.cld(-1,build(i,n /2,init));
    ret.cld(1,build(i +n /2,n -n /2,init));
    return ret.merge();
  }

  public void add(V v){ add(v,1); }

  public void add(V v,int k){ ins(size(),v,k); }

  public void ins(int i,V v){ ins(i,v,1); }

  public void ins(int i,V v,int k){ root = root == null ? new Node(v,k) : ins(root,i,v,k); }

  private Node ins(Node nd,int i,V v,int k){
    if (nd.leaf && (i == 0 || i == nd.sz)) {
      split(nd,i == 0 ? 1 : -1,v,k,nd.sz +k);
      return nd.merge();
    }

    if (nd.leaf)
      split(nd,1,ag(e(),e,nd.val),i,nd.sz);
    else
      nd.push();

    if (i < nd.lft.sz)
      nd.cld(-1,ins(nd.lft,i,v,k));
    else
      nd.cld(1,ins(nd.rht,i -nd.lft.sz,v,k));

    return balance(nd);
  }

  public V del(int i){
    var ret = e();
    root = del(ret,root,i);
    return ret;
  }

  private Node del(V ret,Node nd,int i){
    if (nd.leaf) {
      nd.sz--;
      ag(ret,e,nd.val);
      return 0 < nd.sz ? nd : null;
    }
    nd.push();
    int c = i < nd.lft.sz ? -1 : 1;
    Node del = c < 0 ? del(ret,nd.lft,i) : del(ret,nd.rht,i -nd.lft.sz);
    if (del == null)
      return nd.cld(-c);
    nd.cld(c,del);
    return balance(nd);
  }

  @Override
  public void upd(int i,F f){ upd(i,i +1,f); }

  @Override
  public void upd(int l,int r,F f){
    if (size() < r)
      add(e(),r -size());
    root = upd(root,l,r,f);
  }

  private Node upd(Node nd,int l,int r,F f){
    if (l == 0 && r == nd.sz)
      return nd.prop(f);

    if (nd.leaf)
      split(nd,1,ag(e(),e,nd.val),0 < l ? l : r,nd.sz);
    else
      nd.push();

    if (l < nd.lft.sz)
      nd.cld(-1,upd(nd.lft,l,min(nd.lft.sz,r),f));
    if (nd.lft.sz < r)
      nd.cld(1,upd(nd.rht,max(0,l -nd.lft.sz),r -nd.lft.sz,f));
    return balance(nd);
  }

  public void toggle(int l,int r){ root = l < r ? toggle(root,l,r) : root; }

  private Node toggle(Node nd,int l,int r){
    nd.push();
    if (l == 0 && r == nd.sz)
      return nd.toggle();
    else if (r < nd.sz) {
      split(nd,r);
      return merge(toggle(nd.lft,l,r),nd,nd.rht);
    } else {
      split(nd,l);
      return merge(nd.lft,nd,toggle(nd.rht,0,r -l));
    }
  }

  private void split(Node nd,int i){
    if (nd.leaf)
      split(nd,1,ag(e(),e,nd.val),i,nd.sz);
    else {
      nd.push();
      if (i < nd.lft.sz) {
        split(nd.lft,i);
        var lft = nd.lft;
        nd.cld(-1,lft.lft);
        nd.cld(1,merge(lft.rht,lft,nd.rht));
      } else if (nd.lft.sz < i) {
        split(nd.rht,i -nd.lft.sz);
        var rht = nd.rht;
        nd.cld(1,rht.rht);
        nd.cld(-1,merge(nd.lft,rht,rht.lft));
      }
    }
  }

  private Node merge(Node lft,Node nd,Node rht){
    if (abs(lft.rnk -rht.rnk) < 2) {
      nd.cld(-1,lft);
      nd.cld(1,rht);
    } else if (lft.rnk > rht.rnk) {
      lft.push().cld(1,merge(lft.rht,nd,rht));
      nd = lft;
    } else if (lft.rnk < rht.rnk) {
      rht.push().cld(-1,merge(lft,nd,rht.lft));
      nd = rht;
    }
    return balance(nd);
  }

  @Override
  public V get(int i){ return get(i,i +1); }

  @Override
  public V get(int l,int r){
    V ret = e();
    if (root != null)
      get(ret,root,l,r,false);
    return ret;
  }

  public V getRev(int l,int r){
    V ret = e();
    if (root != null)
      get(ret,root,l,r,true);
    return ret;
  }

  private void get(V ret,Node nd,int l,int r,boolean rev){
    if (l == 0 && r == nd.sz)
      ag(ret,rev ? nd.rev() : ret,rev ? ret : nd.val());
    else if (nd.leaf)
      ag(ret,rev ? pw(nd.rev,r -l) : ret,rev ? ret : pw(nd.val,r -l));
    else {
      nd.push();
      if (l < nd.lft.sz)
        get(ret,nd.lft,l,min(nd.lft.sz,r),rev);
      if (nd.lft.sz < r)
        get(ret,nd.rht,max(0,l -nd.lft.sz),r -nd.lft.sz,rev);
    }
  }

  public V all(){ return root == null ? e : root.val(); }

  public int size(){ return root == null ? 0 : root.sz; }

  protected abstract V e();
  protected abstract void agg(V v,V a,V b);
  protected abstract void map(V v,F f);
  protected abstract F comp(F f,F g);

  private V ag(V v,V a,V b){
    agg(v,a,b);
    v.sz = a.sz +b.sz;
    return v;
  }

  protected void pow(V v,V a,int n){
    V x = e();
    for (ag(x,e,a);0 < n;n >>= 1,ag(x,x,x))
      if (0 < (n &1))
        ag(v,v,x);
  }

  private V pw(V a,int n){
    var ret = e();
    pow(ret,a,n);
    ret.sz = n;
    return ret;
  }

  private void split(Node nd,int c,V vl,int i,int sz){
    nd.leaf = false;
    nd.cld(-c,new Node(vl,i));
    nd.cld(c,new Node(nd.val,sz -i));
    nd.val = e();
  }

  private Node balance(Node nd){ return (1 < abs(nd.bis = nd.rht.rnk -nd.lft.rnk) ? (nd = rotate(nd)) : nd).merge(); }

  private Node rotate(Node u){
    var v = u.cld(u.bis).push();
    if (u.bis *v.bis < -1)
      v = rotate(v);
    u.cld(u.bis,v.cld(-u.bis));
    v.cld(-u.bis,u);
    u.merge();
    return v;
  }

  private class Node{
    private int rnk,bis,sz,tog;
    private V val,rev;
    private F laz;
    private Node lft,rht;
    private boolean leaf = true;

    private Node(V val,int sz){
      this.sz = sz;
      this.val = val;
      val.sz = 1;
    }

    private Node merge(){
      bis = rht.rnk -lft.rnk;
      rnk = max(lft.rnk,rht.rnk) +1;
      ag(val,lft.val(),rht.val());
      if (rev != null)
        ag(rev,rht.rev(),lft.rev());
      sz = val.sz;
      return this;
    }

    private Node push(){
      if (laz != null) {
        lft.prop(laz);
        rht.prop(laz);
        laz = null;
      }
      if (0 < tog) {
        lft.toggle();
        rht.toggle();
        tog = 0;
      }
      return this;
    }

    private Node prop(F f){
      map(val,f);
      if (rev != null)
        map(rev,f);
      if (!leaf)
        laz = laz == null ? f : comp(laz,f);
      return this;
    }

    private Node toggle(){
      bis *= -1;
      if (rev == null)
        ag(rev = e(),leaf ? e : rht.rev(),leaf ? val : lft.rev());
      var tn = lft;
      lft = rht;
      rht = tn;
      var tv = val;
      val = rev;
      rev = tv;
      if (!leaf)
        tog ^= 1;
      return this;
    }

    private Node cld(int c){ return c < 0 ? lft : rht; }

    private void cld(int c,Node nd){ nd = c < 0 ? (lft = nd) : (rht = nd); }

    private V val(){ return leaf && 1 < sz ? pw(val,sz) : val; }

    private V rev(){
      return rev == null
          ? ag(rev = e(),leaf ? e : rht.rev(),leaf ? val : lft.rev())
          : leaf && 1 < sz ? pw(rev,sz) : rev;
    }
  }
}
