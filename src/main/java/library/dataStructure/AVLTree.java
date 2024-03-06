package library.dataStructure;

import static java.lang.Math.*;

import java.util.*;

import library.dataStructure.rangeData.base.*;
import library.util.*;

public abstract class AVLTree<V extends BaseV> {
  private V e = e(),t = e();
  private Node root;
  private Comparator<V> cmp;

  public AVLTree(){ this(Util.cast(Comparator.naturalOrder())); }

  public AVLTree(Comparator<V> cmp){ this.cmp = cmp; }

  public void add(V v){
    if (root == null)
      root = new Node(v,1);
    else
      root = add(root,v);
  }

  private Node add(Node nd,V v){
    if (nd.lft == null) {
      int c = cmp.compare(nd.val,v);
      if (c == 0) {
        nd.sz++;
        return nd;
      } else {
        var ret = new Node(e(),0);
        ret.cld(-c,new Node(v,1));
        ret.cld(c,nd);
        nd = ret;
      }
    } else {
      int c = cmp.compare(v,nd.rht.l);
      nd.cld(-1,c < 0 ? add(nd.lft,v) : nd.lft);
      nd.cld(1,c < 0 ? nd.rht : add(nd.rht,v));
    }
    return balance(nd);
  }

  public V del(V v){
    var ret = e();
    root = del(ret,root,v);
    return ret;
  }

  private Node del(V ret,Node nd,V v){
    if (nd.lft == null) {
      int c = cmp.compare(nd.val,v);
      if (c == 0) {
        nd.sz--;
        ag(ret,e,nd.val);
      }
      return c != 0 || 0 < nd.sz ? nd : null;
    }
    int c = cmp.compare(v,nd.rht.l) *2 +1;
    Node del = del(ret,c < 0 ? nd.lft : nd.rht,v);
    if (del == null)
      return nd.cld(-c);
    nd.cld(c,del);
    return balance(nd);
  }

  public V get(int i){ return get(i,i +1); }

  public V get(int l,int r){
    V ret = e();
    if (root != null)
      get(ret,root,l,min(r,size()));
    return ret;
  }

  private void get(V ret,Node nd,int l,int r){
    if (l == 0 && r == nd.sz)
      ag(ret,ret,nd.val());
    else if (nd.lft == null)
      ag(ret,ret,pw(nd.val,r -l));
    else {
      if (l < nd.lft.sz)
        get(ret,nd.lft,l,min(nd.lft.sz,r));
      if (nd.lft.sz < r)
        get(ret,nd.rht,max(0,l -nd.lft.sz),r -nd.lft.sz);
    }
  }

  public V all(){ return root == null ? e : root.val(); }

  public int size(){ return root == null ? 0 : root.sz; }

  protected abstract V e();
  protected abstract void agg(V v,V a,V b);

  private V ag(V v,V a,V b){
    agg(v,a,b);
    v.sz = a.sz +b.sz;
    return v;
  }

  protected void pow(V v,V a,int n){
    for (ag(t,e,a);0 < n;n >>= 1,ag(t,t,t))
      if (0 < (n &1))
        ag(v,v,t);
  }

  private V pw(V a,int n){
    V ret = e();
    pow(ret,a,n);
    ret.sz = n;
    return ret;
  }

  private Node balance(Node nd){ return (1 < abs(nd.bis = nd.rht.rnk -nd.lft.rnk) ? (nd = rotate(nd)) : nd).merge(); }

  private Node rotate(Node u){
    var v = u.cld(u.bis);
    if (u.bis *v.bis < -1)
      v = rotate(v);
    u.cld(u.bis,v.cld(-u.bis));
    v.cld(-u.bis,u);
    u.merge();
    return v;
  }

  private class Node{
    private int sz,bis,rnk;
    private V val,l;
    private Node lft,rht;

    private Node(V val,int sz){
      this.sz = sz;
      this.val = l = val;
      val.sz = 1;
    }

    private Node merge(){
      bis = rht.rnk -lft.rnk;
      rnk = max(lft.rnk,rht.rnk) +1;
      ag(val,lft.val(),rht.val());
      l = lft.l;
      sz = val.sz;
      return this;
    }

    private Node cld(int c){ return c < 0 ? lft : rht; }

    private void cld(int c,Node nd){ nd = c < 0 ? (lft = nd) : (rht = nd); }

    private V val(){ return lft == null && 1 < sz ? pw(val,sz) : val; }
  }
}