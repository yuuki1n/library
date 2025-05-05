package library.dataStructure.rangeData.segmentTree;

import static java.lang.Math.*;

import java.util.function.*;

public abstract class AVLSegmentTreeLong{
  private long e = e();
  private Node root;

  public AVLSegmentTreeLong(int n){ root = new Node(e(),n); }

  public AVLSegmentTreeLong(){}

  public void build(int n,IntToLongFunction init){ root = n == 0 ? null : build(0,n,init); }

  private Node build(int l,int r,IntToLongFunction init){
    if (r -l == 1)
      return new Node(init.applyAsLong(l),1);
    var ret = new Node(e(),r -l);
    ret.lft = build(l,l +r >>1,init);
    ret.rht = build(l +r >>1,r,init);
    return ret.update();
  }

  public void add(long v){ add(v,1); }

  public void add(long v,int k){ ins(size(),v,k); }

  public void ins(int i,long v){ ins(i,v,1); }

  public void ins(int i,long v,int k){ root = root == null ? new Node(v,k) : ins(root,i,v,k); }

  private Node ins(Node nd,int i,long v,int k){
    if (nd.lft == null && (i == 0 || i == nd.sz))
      split(nd,i == 0 ? 1 : -1,v,k,nd.sz +k);
    else {
      if (nd.lft == null)
        split(nd,1,nd.val,i,nd.sz);
      else
        nd.push();

      if (i < nd.lft.sz)
        nd.lft = ins(nd.lft,i,v,k);
      else
        nd.rht = ins(nd.rht,i -nd.lft.sz,v,k);
    }
    return balance(nd);
  }

  public void del(int i){ root = del(root,i); }

  private Node del(Node nd,int i){
    if (nd.lft == null)
      return 0 < --nd.sz ? nd : null;
    nd.push();
    int c = i < nd.lft.sz ? -1 : 1;
    Node del = c < 0 ? del(nd.lft,i) : del(nd.rht,i -nd.lft.sz);
    if (del == null)
      return nd.cld(-c);
    nd.cld(c,del);
    return balance(nd);
  }

  public void del(int l,int r){ root = l < r ? del(root,l,r) : root; }

  private Node del(Node nd,int l,int r){
    if (r -l == nd.sz)
      return null;
    nd.push();
    return 0 < l && r < nd.sz
        ? merge(split(nd,l).lft,nd,del(nd.rht,0,r -l))
        : 0 < l ? split(nd,l).lft : split(nd,r).rht;
  }

  public void upd(int i,long f){ upd(i,i +1,f); }

  public void upd(int l,int r,long f){
    if (size() < r)
      add(e(),r -size());
    root = upd(root,l,r,f);
  }

  private Node upd(Node nd,int l,int r,long f){
    if (l == 0 && r == nd.sz)
      nd.prop(f);
    else if (l < r) {
      if (nd.lft == null)
        split(nd,1,nd.val,0 < l ? l : r,nd.sz);
      else
        nd.push();

      if (l < nd.lft.sz)
        nd.lft = upd(nd.lft,l,min(nd.lft.sz,r),f);
      if (nd.lft.sz < r)
        nd.rht = upd(nd.rht,max(0,l -nd.lft.sz),r -nd.lft.sz,f);
      nd = balance(nd);
    }
    return nd;
  }

  public void toggle(int l,int r){ root = l < r ? toggle(root,l,r) : root; }

  private Node toggle(Node nd,int l,int r){
    nd.push();
    return 0 < l ? merge(split(nd,l).lft,nd,toggle(nd.rht,0,r -l))
        : r < nd.sz ? merge(toggle(split(nd,r).lft,l,r),nd,nd.rht)
            : nd.toggle();
  }

  public void shift(int l,int r,int k){ root = 0 < (k %= r -l) ? shift(root,l,r,k) : root; }

  private Node shift(Node nd,int l,int r,int k){
    nd.push();
    return 0 < l ? merge(split(nd,l).lft,nd,shift(nd.rht,0,r -l,k))
        : r < nd.sz ? merge(shift(split(nd,r).lft,l,r,k),nd,nd.rht)
            : merge(split(nd,k).rht,nd,nd.lft);
  }

  private Node split(Node nd,int i){
    if (nd.lft == null)
      split(nd,1,nd.val,i,nd.sz);
    else {
      nd.push();
      if (i < nd.lft.sz) {
        split(nd.lft,i);
        var lft = nd.lft;
        nd.lft = lft.lft;
        nd.rht = merge(lft.rht,lft,nd.rht);
      } else if (nd.lft.sz < i) {
        split(nd.rht,i -nd.lft.sz);
        var rht = nd.rht;
        nd.rht = rht.rht;
        nd.lft = merge(nd.lft,rht,rht.lft);
      }
    }
    return nd;
  }

  private Node merge(Node lft,Node nd,Node rht){
    if (abs(lft.rnk -rht.rnk) < 2) {
      nd.lft = lft;
      nd.rht = rht;
    } else if (lft.rnk > rht.rnk) {
      lft.push();
      lft.rht = merge(lft.rht,nd,rht);
      nd = lft;
    } else if (lft.rnk < rht.rnk) {
      rht.push();
      rht.lft = merge(lft,nd,rht.lft);
      nd = rht;
    }
    return balance(nd);
  }

  public long get(int i){ return get(root,i); }

  private long get(Node nd,int i){
    if (nd.lft == null)
      return nd.val;
    nd.push();
    return i < nd.lft.sz ? get(nd.lft,i) : get(nd.rht,i -nd.lft.sz);
  }

  public long get(int l,int r){ return root != null ? get(root,l,min(r,size())) : e(); }

  private long get(Node nd,int l,int r){
    if (0 == l && r == nd.sz)
      return nd.val();
    else if (nd.lft == null)
      return pow(nd.val,r -l);
    else {
      nd.push();
      long ret = e();
      if (l < nd.lft.sz)
        ret = agg(ret,get(nd.lft,l,min(nd.lft.sz,r)));
      if (nd.lft.sz < r)
        ret = agg(ret,get(nd.rht,max(0,l -nd.lft.sz),r -nd.lft.sz));
      return ret;
    }
  }

  public long all(){ return root == null ? e : root.val(); }

  public int size(){ return root == null ? 0 : root.sz; }

  protected abstract long e();
  protected abstract long agg(long a,long b);
  protected abstract long map(long v,long f,int sz);
  protected abstract long comp(long f,long g);

  protected long pow(long a,int n){
    long ret = e();
    for (long t = a;0 < n;n >>= 1,t = agg(t,t))
      if (0 < (n &1))
        ret = agg(ret,t);
    return ret;
  }

  private void split(Node nd,int c,long vl,int i,int sz){
    nd.cld(-c,new Node(vl,i));
    nd.cld(c,new Node(nd.val,sz -i));
    nd.val = e();
  }

  private Node balance(Node nd){ return (1 < abs(nd.bis = nd.rht.rnk -nd.lft.rnk) ? (nd = rotate(nd)) : nd).update(); }

  private Node rotate(Node u){
    var v = u.cld(u.bis);
    v.push();
    if (u.bis *v.bis < -1)
      v = rotate(v);
    u.cld(u.bis,v.cld(-u.bis));
    v.cld(-u.bis,u);
    u.update();
    return v;
  }

  private class Node{
    private int sz,bis,rnk,tog;
    private long val,laz;
    private boolean lazflg;
    private Node lft,rht;

    private Node(long val,int sz){
      this.sz = sz;
      this.val = val;
    }

    private Node update(){
      bis = rht.rnk -lft.rnk;
      rnk = max(lft.rnk,rht.rnk) +1;
      val = agg(lft.val(),rht.val());
      sz = lft.sz +rht.sz;
      return this;
    }

    private void push(){
      if (lazflg && lft != null) {
        lft.prop(laz);
        rht.prop(laz);
        lazflg = false;
      }
      if (0 < tog) {
        lft.toggle();
        rht.toggle();
        tog = 0;
      }
    }

    private void prop(long f){
      val = map(val,f,sz);
      laz = lft == null || !lazflg ? f : comp(laz,f);
      lazflg = true;
    }

    private Node toggle(){
      bis *= -1;
      var tn = lft;
      lft = rht;
      rht = tn;
      if (lft != null)
        tog ^= 1;
      return this;
    }

    private Node cld(int c){ return c < 0 ? lft : rht; }

    private void cld(int c,Node nd){ nd = c < 0 ? (lft = nd) : (rht = nd); }

    private long val(){ return lft == null && 1 < sz ? pow(val,sz) : val; }
  }
}