package test.solver;

import java.io.*;

import library.dataStructure.collection.*;
import library.dataStructure.rangeData.base.*;
import test.tester.*;

public class ABC255_Ex extends BaseTester{
  public ABC255_Ex(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    long N = in.lg();
    int Q = in.it();
    DynamicSegmentTree seg = new DynamicSegmentTree(60);
    long pd = 0;
    while (Q-- > 0) {
      long d = in.lg();
      long l = in.lg() -1;
      long r = in.lg();
      seg.upd(0,1L <<60,1,(d -pd) %mod);
      out.println(seg.get(l,r));
      seg.upd(l,r,0,0);
      pd = d;
    }
    //    return elapsed();
    return null;
  }

  class Data extends BaseV{
    long v;

    public Data(long v){ this.v = v; }
  }

  class DynamicSegmentTree{
    private Node root;

    public DynamicSegmentTree(int log){ root = new Node(0,1L <<log); }

    public void upd(long l,long r,long fa,long fb){ upd(root,l,r,fa,fb); }

    private void upd(Node nd,long l,long r,long fa,long fb){
      if (nd.r <= l || r <= nd.l)
        return;

      if (l <= nd.l && nd.r <= r) {
        nd.prop(fa,fb);
        return;
      }
      nd.push();
      upd(nd.lft,l,r,fa,fb);
      upd(nd.rht,l,r,fa,fb);
      nd.merge();
    }

    public long get(long l,long r){
      long ret = 0;
      MyStack<Node> stk = new MyStack<>();
      stk.add(root);
      for (Node nd;!stk.isEmpty();)
        if ((nd = stk.pop()) == null || nd.r <= l || r <= nd.l)
          continue;
        else if (l <= nd.l && nd.r <= r)
          ret = (ret +nd.v) %998244353;
        else {
          nd.push();
          stk.add(nd.rht);
          stk.add(nd.lft);
        }
      return ret;
    }

    public long get(int i){ return get(i,i +1); }

    private class Node{
      private Node lft,rht;
      private int v,c;
      private long la = 1,lb;
      private long l,r;

      private Node(long l,long r){
        this.l = l;
        this.r = r;
        c = (int) ((l +1 +r) %998244353 *((r -l) %998244353) %998244353 *499122177 %998244353);
      }

      private Node merge(){
        v = (lft.v +rht.v) %998244353;
        c = (lft.c +rht.c) %998244353;
        return this;
      }

      private Node push(){
        lft().prop(la,lb);
        rht().prop(la,lb);
        la = 1;
        lb = 0;
        return this;
      }

      private Node prop(long fa,long fb){
        v = (int) ((fa *v +fb *c) %998244353);
        la = fa *la %998244353;
        lb = (fa *lb +fb) %998244353;
        return this;
      }

      private Node lft(){ return lft == null ? lft = new Node(l,l +r >>1) : lft; }

      private Node rht(){ return rht == null ? rht = new Node(l +r >>1,r) : rht; }
    }
  }
}