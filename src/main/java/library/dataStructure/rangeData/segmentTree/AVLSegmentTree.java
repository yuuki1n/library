package library.dataStructure.rangeData.segmentTree;

import static java.lang.Math.*;

import java.util.function.*;

import library.dataStructure.rangeData.base.*;
import library.util.*;

public abstract class AVLSegmentTree<V extends BaseV, F> {
  private V e = e();
  private Node root;
  private V[] ret = Util.cast(new BaseV[2]);
  private int ri;

  public AVLSegmentTree(int n){
    this();
    root = new Node(e(),n);
  }

  public AVLSegmentTree(){
    ret[ri] = e();
    ri = 1;
  }

  public void build(int n,IntFunction<V> init){ root = build(0,n,init); }

  private Node build(int l,int r,IntFunction<V> init){
    if (r -l == 1)
      return new Node(init.apply(l),1);
    var ret = new Node(e(),r -l);
    ret.lft = build(l,l +r >>1,init);
    ret.rht = build(l +r >>1,r,init);
    return ret.update();
  }

  public void add(V v){ add(v,1); }

  public void add(V v,int k){ ins(size(),v,k); }

  public void ins(int i,V v){ ins(i,v,1); }

  public void ins(int i,V v,int k){ root = root == null ? new Node(v,k) : ins(root,i,v,k); }

  private Node ins(Node nd,int i,V v,int k){
    if (nd.lft == null && (i == 0 || i == nd.sz))
      split(nd,i == 0 ? 1 : -1,v,k,nd.sz +k);
    else {
      if (nd.lft == null)
        split(nd,1,ag(e(),e,nd.val),i,nd.sz);
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

  public void upd(int i,F f){ upd(i,i +1,f); }

  public void upd(int l,int r,F f){
    if (size() < r)
      add(e(),r -size());
    root = upd(root,l,r,f);
  }

  private Node upd(Node nd,int l,int r,F f){
    if (l == 0 && r == nd.sz)
      nd.prop(f);
    else if (l < r) {
      if (nd.lft == null)
        split(nd,1,ag(e(),e,nd.val),0 < l ? l : r,nd.sz);
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
    if (0 < l) {
      split(nd,l);
      nd = merge(nd.lft,nd,toggle(nd.rht,0,r -l));
    } else if (r < nd.sz) {
      split(nd,r);
      nd = merge(toggle(nd.lft,l,r),nd,nd.rht);
    } else
      nd.toggle();
    return nd;
  }

  private void split(Node nd,int i){
    if (nd.lft == null)
      split(nd,1,ag(e(),e,nd.val),i,nd.sz);
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

  public V get(int i){ return get(root,i); }

  private V get(Node nd,int i){
    if (nd.lft == null)
      return nd.val;
    nd.push();
    return i < nd.lft.sz ? get(nd.lft,i) : get(nd.rht,i -nd.lft.sz);
  }

  public V get(int l,int r){
    ret[ri] = e();
    ri ^= 1;
    if (root != null)
      get(root,l,min(r,size()));
    return ret[ri ^= 1];
  }

  private void get(Node nd,int l,int r){
    if (0 == l && r == nd.sz)
      ag(ret[ri],ret[ri ^= 1],nd.val());
    else if (nd.lft == null)
      ag(ret[ri],ret[ri ^= 1],pw(nd.val,r -l));
    else {
      nd.push();
      if (l < nd.lft.sz)
        get(nd.lft,l,min(nd.lft.sz,r));
      if (nd.lft.sz < r)
        get(nd.rht,max(0,l -nd.lft.sz),r -nd.lft.sz);
    }
  }

  public V all(){ return root == null ? e : root.val(); }

  public int size(){ return root == null ? 0 : root.sz; }

  protected abstract V e();
  protected abstract void agg(V v,V a,V b);
  protected abstract void map(V v,F f);
  protected abstract F comp(F f,F g);
  protected abstract void tog(V v);

  private V ag(V v,V a,V b){
    agg(v,a,b);
    v.sz = a.sz +b.sz;
    return v;
  }

  protected V pow(V a,int n){
    V ret = e();
    for (V t = ag(e(),e,a);0 < n;n >>= 1,t = ag(e(),t,t))
      if (0 < (n &1))
        ret = ag(e(),ret,t);
    return ret;
  }

  private V pw(V a,int n){
    V ret = pow(a,n);
    ret.sz = n;
    return ret;
  }

  private void split(Node nd,int c,V vl,int i,int sz){
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
    private V val;
    private F laz;
    private Node lft,rht;

    private Node(V val,int sz){
      this.sz = sz;
      this.val = val;
      val.sz = 1;
    }

    private Node update(){
      bis = rht.rnk -lft.rnk;
      rnk = max(lft.rnk,rht.rnk) +1;
      ag(val,lft.val(),rht.val());
      sz = val.sz;
      return this;
    }

    private void push(){
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
    }

    private void prop(F f){
      map(val,f);
      if (lft != null)
        laz = laz == null ? f : comp(laz,f);
    }

    private Node toggle(){
      bis *= -1;
      var tn = lft;
      lft = rht;
      rht = tn;
      tog(val);
      if (lft != null)
        tog ^= 1;
      return this;
    }

    private Node cld(int c){ return c < 0 ? lft : rht; }

    private void cld(int c,Node nd){ nd = c < 0 ? (lft = nd) : (rht = nd); }

    private V val(){ return lft == null && 1 < sz ? pw(val,sz) : val; }
  }
}