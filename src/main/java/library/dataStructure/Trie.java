package library.dataStructure;

import java.lang.reflect.*;
import java.util.function.*;

import library.dataStructure.collection.*;
import library.util.*;

public abstract class Trie<V> {
  private int b;
  public Node root;

  public Trie(){ this('z' +1); }

  public Trie(int b){
    this.b = b;
    root = new Node(null,-1);
  }

  abstract V e();

  public MyList<Node> add(char[] s){ return path(Util.arrI(s.length,i -> s[i]),true); }

  public MyList<Node> add(int[] s){ return path(s,true); }

  public MyList<Node> path(char[] s){ return path(Util.arrI(s.length,i -> s[i]),false); }

  public MyList<Node> path(int[] s){ return path(s,false); }

  private MyList<Node> path(int[] s,boolean add){
    MyList<Node> q = new MyList<>(s.length +1);
    var nd = root;
    q.add(nd);
    for (int c:s) {
      if (nd.ch[c] == null)
        if (add)
          nd.ch[c] = new Node(nd,c);
        else
          break;
      q.add(nd = nd.ch[c]);
    }
    return q;
  }

  public void dfs(Consumer<Node> preFunc,Consumer<Node> sufFunc){ dfs(root,preFunc,sufFunc); }

  public void dfs(Node nd,Consumer<Node> preFunc,Consumer<Node> sufFunc){
    if (preFunc != null)
      preFunc.accept(nd);
    for (var ch:nd.ch)
      if (ch != null)
        dfs(ch,preFunc,sufFunc);
    if (sufFunc != null)
      sufFunc.accept(nd);
  }

  public class Node{
    public Node par;
    public Node[] ch;
    public int c;
    public V v;

    public Node(Node par,int c){
      this.par = par;
      ch = Util.cast(Array.newInstance(this.getClass(),b));
      this.c = c;
      v = e();
    }
  }
}