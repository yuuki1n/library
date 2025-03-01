package library.dataStructure;

import java.lang.reflect.*;

import library.dataStructure.collection.*;
import library.util.*;

public abstract class Trie<V> {
  private int b;
  public Node root;

  public Trie(){ this('z' +1); }

  public Trie(int b){
    this.b = b;
    root = new Node();
  }

  abstract V e();

  public MyList<Node> path(char[] s){
    MyList<Node> q = new MyList<>(s.length +1);
    var nd = root;
    q.add(nd);
    for (char c:s) {
      if (nd.ch[c] == null)
        nd.ch[c] = new Node();
      q.add(nd = nd.ch[c]);
    }
    return q;
  }

  class Node{
    private Node[] ch = Util.cast(Array.newInstance(this.getClass(),b));
    public V v = e();
  }
}