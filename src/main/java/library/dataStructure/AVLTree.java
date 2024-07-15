package library.dataStructure;

import static java.lang.Math.*;

public class AVLTree{
  private Node root;

  public void add(long v){ add(v,1); }

  public void add(long v,int k){ root = root == null ? new Node(v,k) : add(root,v,k); }

  private Node add(Node nd,long v,int k){
    if (nd.lft == null) {
      int c = Long.compare(nd.val,v);
      if (c == 0) {
        nd.sz += k;
        return nd;
      } else {
        var ret = new Node(0,0);
        ret.cld(-c,new Node(v,k));
        ret.cld(c,nd);
        nd = ret;
      }
    } else {
      int c = Long.compare(v,nd.rht.l);
      nd.lft = c < 0 ? add(nd.lft,v,k) : nd.lft;
      nd.rht = c < 0 ? nd.rht : add(nd.rht,v,k);
    }
    return balance(nd);
  }

  public void del(long v){ del(v,1); }

  public void del(long v,int k){ root = del(root,v,k); }

  private Node del(Node nd,long v,int k){
    if (nd.lft == null) {
      int c = Long.compare(nd.val,v);
      if (c == 0)
        nd.sz -= k;
      return c != 0 || 0 < nd.sz ? nd : null;
    }
    int c = Long.compare(v,nd.rht.l) *2 +1;
    Node del = del(c < 0 ? nd.lft : nd.rht,v,k);
    if (del == null)
      return nd.cld(-c);
    nd.cld(c,del);
    return balance(nd);
  }

  public long get(int i){ return get(root,i); }

  private long get(Node nd,int i){
    return nd.lft == null ? nd.val : i < nd.lft.sz ? get(nd.lft,i) : get(nd.rht,i -nd.lft.sz);
  }

  public long get(int l,int r){ return root == null ? 0 : get(root,l,min(r,size())); }

  private long get(Node nd,int l,int r){
    if (0 == l && r == nd.sz)
      return nd.val();
    else if (nd.lft == null)
      return nd.val *(r -l);
    else {
      long ret = 0;
      if (l < nd.lft.sz)
        ret += get(nd.lft,l,min(nd.lft.sz,r));
      if (nd.lft.sz < r)
        ret += get(nd.rht,max(0,l -nd.lft.sz),r -nd.lft.sz);
      return ret;
    }
  }

  public int size(){ return root == null ? 0 : root.sz; }

  private Node balance(Node nd){ return (1 < abs(nd.bis = nd.rht.rnk -nd.lft.rnk) ? (nd = rotate(nd)) : nd).update(); }

  private Node rotate(Node u){
    var v = u.cld(u.bis);
    if (u.bis *v.bis < -1)
      v = rotate(v);
    u.cld(u.bis,v.cld(-u.bis));
    v.cld(-u.bis,u);
    u.update();
    return v;
  }

  private class Node{
    private int sz,bis,rnk;
    private long val,l;
    private Node lft,rht;

    private Node(long val,int sz){
      this.sz = sz;
      this.val = l = val;
    }

    private Node update(){
      sz = lft.sz +rht.sz;
      bis = rht.rnk -lft.rnk;
      rnk = max(lft.rnk,rht.rnk) +1;
      val = lft.val() +rht.val();
      l = lft.l;
      return this;
    }

    private Node cld(int c){ return c < 0 ? lft : rht; }

    private void cld(int c,Node nd){ nd = c < 0 ? (lft = nd) : (rht = nd); }

    private long val(){ return lft == null && 1 < sz ? val *sz : val; }
  }
}