package library.graph.unionfind;

import library.dataStructure.collection.*;

/**
 * rollback可能UnionFind
 * @author yuuki_n
 *
 */
public class RollbackUnionFind extends UnionFind{
  private MyList<int[]> hst = new MyList<>();

  public RollbackUnionFind(int n){ super(n); }

  @Override
  public int root(int x){ return dat[x] < 0 ? x : root(dat[x]); }

  @Override
  public boolean unite(int u,int v){
    if ((u = root(u)) == (v = root(v)))
      return false;

    hst.add(new int[]{u, dat[u]});
    hst.add(new int[]{v, dat[v]});
    hst.add(new int[]{u, nxt[u]});
    hst.add(new int[]{v, nxt[v]});
    return super.unite(u,v);
  }

  public void undo(){
    int[] h;
    for (int i = 0;i < 2;i++)
      nxt[(h = hst.pollLast())[0]] = h[1];
    for (int i = 0;i < 2;i++)
      dat[(h = hst.pollLast())[0]] = h[1];
    num++;
  }
}