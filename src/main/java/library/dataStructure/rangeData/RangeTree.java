package library.dataStructure.rangeData;

import library.dataStructure.collection.*;

/**
 * 領域木
 * 途中
 * @author yuuki_n
 *
 * @param <S>
 */
public abstract class RangeTree<S> {
  private Node root;

  public RangeTree(int log){ root = new Node(0,1 <<log); }

  protected abstract S e();

  public MyList<S> path(int i){
    MyList<S> ret = new MyList<>();
    ret.add(root.val);
    for (var nd = root;nd.l +1 < nd.r;)
      ret.add((nd = i < nd.l +nd.r >>1 ? nd.lft() : nd.rht()).val);
    return ret;
  }

  public MyList<S> list(int l,int r){
    MyList<S> ret = new MyList<>();
    MyStack<Node> stk = new MyStack<>();
    stk.add(root);
    for (Node nd;!stk.isEmpty();)
      if ((nd = stk.pop()) == null || nd.r <= l || r <= nd.l)
        continue;
      else if (l <= nd.l && nd.r <= r)
        ret.add(nd.val);
      else {
        stk.add(nd.rht);
        stk.add(nd.lft);
      }
    return ret;
  }

  private class Node{
    private Node lft,rht;
    private S val;
    private int l,r;

    private Node(int l,int r){
      this.l = l;
      this.r = r;
      val = e();
    }

    private Node lft(){ return lft == null ? lft = new Node(l,l +r >>1) : lft; }

    private Node rht(){ return rht == null ? rht = new Node(l +r >>1,r) : rht; }
  }
}
