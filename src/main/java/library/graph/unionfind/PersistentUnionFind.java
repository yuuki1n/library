package library.graph.unionfind;

import library.dataStructure.*;

public class PersistentUnionFind{
  int num;
  protected PersistentArray dat,nxt;

  public PersistentUnionFind(int n){
    dat = new PersistentArray(n);
    nxt = new PersistentArray(n);
    for (int i = 0;i < n;i++) {
      dat.set(i,-1,-1);
      nxt.set(i,i,-1);
    }
    num = n;
  }

  public int root(int x,int t){
    int d = dat.get(x,t);
    return d < 0 ? x : dat.set(x,root(d,t),t);
  }

  public boolean same(int u,int v,int t){ return root(u,t) == root(v,t); }

  public boolean unite(int u,int v,int t,int t2){
    if ((u = root(u,t)) == (v = root(v,t)))
      return false;

    if (dat.get(u,t) > dat.get(v,t)) {
      u ^= v;
      v ^= u;
      u ^= v;
    }
    dat.set(u,dat.get(u,t) +dat.get(v,t),t2);
    dat.set(v,u,t2);
    num--;
    var nu = nxt.get(u,t);
    var nv = nxt.get(v,t);
    nxt.set(u,nv,t2);
    nxt.set(v,nu,t2);
    return true;
  }

  public int size(int x,int t){ return -dat.get(root(x,t),t); }

  public int[] getGroup(int x,int t){
    int[] ret = new int[size(x,t)];
    for (int i = 0,c = root(x,t);i < ret.length;i++)
      ret[i] = c = nxt.get(c,t);
    return ret;
  }
}
