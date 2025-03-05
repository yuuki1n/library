package library.graph.tree;

import static java.util.Arrays.*;

import java.util.*;
import java.util.function.*;

import library.dataStructure.collection.*;
import library.graph.*;
import library.util.*;

public class HLD extends Graph<Object>{
  private int[] p,hp,l,r;

  public HLD(int n){
    super(n,false);
    p = new int[n];
    hp = new int[n];
    l = new int[n];
    r = new int[n];
  }

  public MyList<int[]> auxiliary(MyList<Integer> lis){
    MyList<int[]> ret = new MyList<>();
    lis = new MyList<>(lis);
    lis.add(0);
    lis.sort(Comparator.comparing(i -> l[i]));
    for (int i = lis.size() -1;i > 0;i--)
      lis.add(lca(lis.get(i -1),lis.get(i)));
    lis.sort(Comparator.comparing(i -> l[i]));
    MyList<Integer> stk = new MyList<>();
    stk.add(lis.get(0));
    for (var y:lis) {
      while (r[stk.peekLast()] <= l[y])
        stk.pollLast();
      if (!stk.peekLast().equals(y))
        ret.add(new int[]{stk.peekLast(), y});
      stk.add(y);
    }
    return ret;
  }

  public MyList<int[]> getPath(int u,int v,int incLca){
    MyList<int[]> ret = new MyList<>();
    var lst = itr(u,v,(a,b) -> {
      ret.add(new int[]{l[a], l[b] +1});
      return p[a];
    });
    ret.add(new int[]{l[lst[0]] +1 -incLca, l[lst[1]] +1});
    return ret;
  }

  public int lca(int u,int v){ return itr(u,v,(a,b) -> p[a])[0]; }

  private int[] itr(int u,int v,IntBinaryOperator f){
    while (true) {
      if (l[u] > l[v]) {
        var t = u;
        u = v;
        v = t;
      }

      var h = hp[v];
      if (l[h] <= l[u])
        return new int[]{u, v};

      v = f.applyAsInt(h,v);
    }
  }

  public int ei(int e){ return l[es.get(e).v]; }

  public int l(int u){ return l[u]; }

  public int r(int u){ return r[u]; }

  public void makeTree(int s){
    MyList<Integer> stk = new MyList<>();
    fill(hp,-1);
    p[s] = s;
    stk.add(s);
    stk.add(s);
    while (!stk.isEmpty()) {
      var u = stk.pollLast();
      if (r[u] < 1) {
        r[u] = 1;
        for (var e:go(u)) {
          if (e.v == p[u])
            continue;
          es.set(e.id,e);
          p[e.v] = u;
          stk.add(e.v);
          stk.add(e.v);
        }
      } else if (u != s)
        r[p[u]] += r[u];
    }

    for (int u = 0;u < n;u++) {
      var go = go(u);
      for (int i = 1;i < go.length;i++)
        if (r[u] < r[go[0].v] || r[go[0].v] < r[go[i].v] && r[go[i].v] < r[u])
          Util.swap(go,0,i);
    }

    stk.add(s);
    for (int hid = 0;!stk.isEmpty();) {
      var u = stk.pollLast();
      r[u] += l[u] = hid++;
      if (hp[u] < 0)
        hp[u] = u;
      var go = go(u);
      for (int i = go.length;i-- > 0;) {
        var v = go[i].v;
        if (v == p[u])
          continue;
        if (i == 0)
          hp[v] = hp[u];
        stk.add(v);
      }
    }
  }
}