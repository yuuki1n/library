package library.dataStructure.rangeData.segmentTree;

import static java.lang.Math.*;

import java.util.function.IntFunction;

import library.dataStructure.rangeData.base.BaseV;
import library.dataStructure.rangeData.base.RangeData;

public abstract class AVLSegmentTree<V extends BaseV, F> extends RangeData<V, F>{
  private V e = e(),e1 = e();
  private Node nl = new Node(0);

  public AVLSegmentTree(int n){
    this();
    nl.cld(1,new Node(n));
  }

  public AVLSegmentTree(){ e1.sz = 1; }

  public void build(int n,IntFunction<V> init){ nl.cld(1,build(0,n,init)); }

  private Node build(int i,int n,IntFunction<V> init){
    if (n == 1)
      return new Node(init.apply(i),1);
    var ret = new Node(n);
    ret.leaf = false;
    ret.cld(-1,build(i,n /2,init));
    ret.cld(1,build(i +n /2,n -n /2,init));
    ret.merge();
    return ret;
  }

  public void add(V v){ add(v,1); }

  public void add(V v,int k){ ins(size(),v,k); }

  public void ins(int i,V v){ ins(i,v,1); }

  public void ins(int i,V v,int k){
    if (nl.rht == null)
      nl.cld(1,new Node(v,k));
    else
      ins(nl.rht,i,v,k);
  }

  private void ins(Node nd,int i,V v,int k){
    if (nd.leaf && (i == 0 || i == nd.sz)) {
      int c = i == 0 ? 1 : -1;
      nd.leaf = false;
      nd.cld(-c,new Node(v,k));
      nd.cld(c,new Node(nd.val,nd.sz));
      nd.val = e();
      nd.merge();
      return;
    }

    if (nd.leaf)
      split(nd,i);
    else
      nd.push();

    if (i < nd.lft.sz)
      ins(nd.lft,i,v,k);
    else
      ins(nd.rht,i -nd.lft.sz,v,k);

    if (abs(nd.bis()) > 1)
      nd.par.cld(nd.par.lft == nd ? -1 : 1,nd = rotate(nd));
    nd.merge();
  }

  public V del(int i){ return i < size() ? del(nl.rht,i) : e; }

  private V del(Node nd,int i){
    if (nd.leaf) {
      nd.sz--;
      return nd.val;
    }

    nd.push();
    var ret = i < nd.lft.sz ? del(nd.lft,i) : del(nd.rht,i -nd.lft.sz);

    int c = nd.par.lft == nd ? -1 : 1;
    if (nd.lft.sz == 0)
      nd.par.cld(c,nd.rht);
    else if (nd.rht.sz == 0)
      nd.par.cld(c,nd.lft);

    if (abs(nd.bis()) > 1)
      nd.par.cld(c,nd = rotate(nd));
    nd.merge();
    return ret;
  }

  @Override
  public void upd(int i,F f){ upd(i,i +1,f); }

  @Override
  public void upd(int l,int r,F f){
    if (size() < r)
      add(e(),r -size());
    upd(nl.rht,l,r,f);
  }

  private void upd(Node nd,int l,int r,F f){
    if ((l = max(0,l)) == 0 && (r = min(nd.sz,r)) == nd.sz) {
      nd.prop(f);
      return;
    }

    if (nd.leaf)
      split(nd,0 < l ? l : r);
    else
      nd.push();

    if (l < nd.lft.sz)
      upd(nd.lft,l,r,f);
    if (nd.lft.sz < r)
      upd(nd.rht,l -nd.lft.sz,r -nd.lft.sz,f);

    if (abs(nd.bis()) > 1)
      nd.par.cld(nd.par.lft == nd ? -1 : 1,nd = rotate(nd));
    nd.merge();
  }

  @Override
  public V get(int i){ return get(i,i +1); }

  @Override
  public V get(int l,int r){
    V ret = e();
    if (nl.rht != null)
      get(ret,nl.rht,l,r);
    int max = max(0,r -max(size(),l));
    if (0 < max)
      pow(ret,e1,max);
    return ret;
  }

  private void get(V ret,Node nd,int l,int r){
    if ((l = max(0,l)) >= (r = min(nd.sz,r)))
      return;
    if (l == 0 && r == nd.sz) {
      ag(ret,ret,nd.val());
      return;
    }
    if (nd.leaf) {
      pow(ret,nd.val,r -l);
      return;
    }

    nd.push();
    get(ret,nd.lft,l,r);
    get(ret,nd.rht,l -nd.lft.sz,r -nd.lft.sz);
  }

  public V all(){ return nl.rht == null ? e : nl.rht.val(); }

  public int size(){ return nl.rht == null ? 0 : nl.rht.sz; }

  protected abstract V e();
  protected abstract void agg(V v,V a,V b);
  protected abstract void map(V v,F f);
  protected abstract F comp(F f,F g);

  private void ag(V v,V a,V b){
    agg(v,a,b);
    v.sz = a.sz +b.sz;
  }

  protected void pow(V v,V a,int n){
    var x = e();
    ag(x,e,a);
    while (0 < n) {
      if ((n &1) == 1)
        ag(v,v,x);
      n >>= 1;
      if (0 < n)
        ag(x,x,x);
    }
  }

  private void split(Node nd,int c){
    nd.leaf = false;
    nd.cld(-1,new Node(nd.val,c));
    nd.cld(1,new Node(nd.sz -c));
    agg(nd.rht.val,e,nd.val);
    nd.val = e();
  }

  private Node rotate(Node u){
    var v = u.cld(u.bis);
    v.push();
    if (u.bis *v.bis < 0)
      v = rotate(v);
    u.cld(u.bis,v.cld(-u.bis));
    v.cld(-u.bis,u);
    u.merge();
    return v;
  }

  private class Node{
    private int rnk,bis,sz;
    private V val;
    private F laz;
    private Node par,lft,rht;
    private boolean leaf = true;

    private Node(int sz){ this(e(),sz); }

    private Node(V val,int sz){
      this.sz = sz;
      this.val = val;
      val.sz = 1;
    }

    private Node cld(int c){ return c < 0 ? lft : rht; }

    private void cld(int c,Node nd){
      if (c < 0)
        lft = nd;
      else
        rht = nd;
      nd.par = this;
    }

    private void merge(){
      rnk = max(lft.rnk,rht.rnk) +1;
      bis();
      ag(val,lft.val(),rht.val());
      sz = val.sz;
    }

    private int bis(){ return bis = rht.rnk -lft.rnk; }

    private V val(){
      if (leaf && 1 < sz) {
        var ret = e();
        pow(ret,val,sz);
        ret.sz = sz;
        return ret;
      }
      return val;
    }

    private void push(){
      if (laz != null) {
        lft.prop(laz);
        rht.prop(laz);
        laz = null;
      }
    }

    private void prop(F f){
      map(val,f);
      if (!leaf)
        laz = laz == null ? f : comp(laz,f);
    }
  }
}