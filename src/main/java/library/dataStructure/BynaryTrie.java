package library.dataStructure;

import static java.lang.Math.*;

public class BynaryTrie{
  private int k,sz,laz;
  private BynaryTrie lft,rht;

  public BynaryTrie(){ this(30); }

  public BynaryTrie(int k){ this.k = k; }

  public void add(int x){ update(x,1); }

  public void remove(int x){ update(x,-1); }

  public boolean contains(int x){
    if (k < 0)
      return sz > 0;
    eval();
    BynaryTrie cld = (x >>k &1) == 0 ? lft : rht;
    return cld != null && cld.contains(x &~(1 <<k));
  }

  public int kth(int k){
    if (k < 0 || sz <= k)
      return -1;
    if (this.k < 0)
      return 0;
    eval();
    if (lft != null) {
      if (k < lft.sz)
        return lft.kth(k);
      k -= lft.sz;
    }
    return rht.kth(k) |1 <<this.k;
  }

  public void xor(int v){ laz ^= v; }

  public int size(){ return sz; }

  private int update(int x,int v){
    if (k < 0)
      return sz = min(1,max(0,sz +v));
    eval();
    BynaryTrie cld = (x >>k &1) == 0
        ? lft == null ? lft = new BynaryTrie(k -1) : lft
        : rht == null ? rht = new BynaryTrie(k -1) : rht;
    return sz += -cld.sz +cld.update(x,v);
  }

  private void eval(){
    if ((laz >>k &1) == 1) {
      var tmp = lft;
      lft = rht;
      rht = tmp;
      laz ^= 1 <<k;
    }
    if (lft != null)
      lft.laz ^= laz;
    if (rht != null)
      rht.laz ^= laz;
    laz = 0;
  }
}