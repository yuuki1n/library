package library.dataStructure;

/**
 * 昇順に管理するやつ
 * 途中
 */
import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;

public abstract class AVLTree<V> {
  private V e = e();
  private Node root;
  private Comparator<V> cmp;

  public AVLTree(Comparator<V> cmp){ this.cmp = cmp; }

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

  public void add(V v,long k){ root = root == null ? new Node(v,k) : add(root,v,k); }

  private Node add(Node nd,V v,long k){
    if (nd.leaf) {
      int c = cmp.compare(v,nd.val);
      if (c == 0) {
        nd.sz++;
        return nd;
      }
      split(nd,c < 0 ? 1 : -1,v,k,nd.sz +k);
      return nd.merge();
    }

    if (cmp.compare(v,nd.rht.endL) < 0)
      nd.cld(-1,add(nd.lft,v,k));
    else
      nd.cld(1,add(nd.rht,v,k));

    return balance(nd);
  }

  public void del(int i){ root = del(root,i); }

  private Node del(Node nd,long i){
    if (nd.leaf) {
      nd.sz--;
      return 0 < nd.sz ? nd : null;
    }
    int c = i < nd.lft.sz ? -1 : 1;
    Node del = c < 0 ? del(nd.lft,i) : del(nd.rht,i -nd.lft.sz);
    if (del == null)
      return nd.cld(-c);
    nd.cld(c,del);
    return balance(nd);
  }

  public V get(long i){ return i < size() ? get(root,i) : e(); }

  private V get(Node nd,long i){
    if (nd.leaf)
      return nd.val;
    if (i < nd.lft.sz)
      return get(nd.lft,i);
    else
      return get(nd.rht,i -nd.lft.sz);
  }

  public V get(long l,long r){
    V ret = e();
    if (root != null)
      get(ret,root,l,min(r,size()));
    return ret;
  }

  private void get(V ret,Node nd,long l,long r){
    if (l == 0 && r == nd.sz)
      ret = agg(ret,nd.val());
    else if (nd.leaf)
      ret = agg(ret,pow(nd.val,r -l));
    else {
      if (l < nd.lft.sz)
        get(ret,nd.lft,l,min(nd.lft.sz,r));
      if (nd.lft.sz < r)
        get(ret,nd.rht,max(0,l -nd.lft.sz),r -nd.lft.sz);
    }
  }

  public V all(){ return root == null ? e : root.val(); }

  public long size(){ return root == null ? 0 : root.sz; }

  protected abstract V e();
  protected abstract V agg(V a,V b);

  protected V pow(V a,long n){
    V v = e();
    for (V x = agg(e,a);0 < n;n >>= 1,x = agg(x,x))
      if (0 < (n &1))
        v = agg(v,x);
    return v;
  }

  private void split(Node nd,int c,V vl,long i,long sz){
    nd.leaf = false;
    nd.cld(-c,new Node(vl,i));
    nd.cld(c,new Node(nd.val,sz -i));
    nd.val = e();
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
    private int rnk,bis;
    private long sz;
    private V val,endL,endR;
    private Node lft,rht;
    private boolean leaf = true;

    private Node(V val,long sz){
      this.sz = sz;
      this.val = endL = endR = val;
    }

    private Node merge(){
      bis = rht.rnk -lft.rnk;
      rnk = max(lft.rnk,rht.rnk) +1;
      val = agg(lft.val(),rht.val());
      sz = lft.sz +rht.sz;
      endL = lft.endL;
      endR = rht.endR;
      return this;
    }

    private Node cld(int c){ return c < 0 ? lft : rht; }

    private void cld(int c,Node nd){ nd = c < 0 ? (lft = nd) : (rht = nd); }

    private V val(){ return leaf && 1 < sz ? pow(val,sz) : val; }
  }
}