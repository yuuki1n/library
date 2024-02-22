package library.graph.unionfind;

import library.dataStructure.collection.*;

/**
 * rollback可能UnionFind
 * @author yuuki_n
 *
 */
public class RollbackUnionFind extends UnionFind{
  private MyStack<int[]> hst = new MyStack<>();

  public RollbackUnionFind(int n){ super(n); }

  @Override
  public int root(int x){ return dat[x] < 0 ? x : root(dat[x]); }

  @Override
  public boolean unite(int u,int v){
    if (!super.unite(u = root(u),v = root(v)))
      return false;
    hst.add(new int[]{u, dat[u]});
    hst.add(new int[]{v, dat[v]});
    hst.add(new int[]{u, nxt[u]});
    hst.add(new int[]{v, nxt[v]});
    return true;
  }

  public void undo(){
    int[] h;
    for (int i = 0;i < 2;i++)
      nxt[(h = hst.pop())[0]] = h[1];
    for (int i = 0;i < 2;i++)
      dat[(h = hst.pop())[0]] = h[1];
    num++;
  }
}