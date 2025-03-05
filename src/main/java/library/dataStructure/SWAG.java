package library.dataStructure;

import library.dataStructure.collection.*;

public abstract class SWAG<V> {
  private Stk front,back;
  private MyList<V> tmp = new MyList<>();

  public SWAG(){
    front = new Stk(){
      @Override
      V agg(V sum,V v){ return SWAG.this.agg(v,sum); }
    };
    back = new Stk(){
      @Override
      V agg(V sum,V v){ return SWAG.this.agg(sum,v); }
    };
  }

  protected abstract V e();
  protected abstract V agg(V a,V b);

  public void addFirst(V v){ front.add(v); }

  public void addLast(V v){ back.add(v); }

  public V pollFirst(){
    if (front.size() == 0)
      f(front,back);
    return front.pop();
  }

  public V pollLast(){
    if (back.size() == 0)
      f(back,front);
    return back.pop();
  }

  public V sum(){ return agg(front.peekSum(),back.peekSum()); }

  private void f(Stk a,Stk b){
    while (b.size() -1 > tmp.size())
      tmp.add(b.pop());
    while (b.size() > 0)
      a.add(b.pop());
    while (tmp.size() > 0)
      b.add(tmp.pollLast());
  }

  public int size(){ return front.size() +back.size(); }

  abstract class Stk{
    private MyList<V> val,sum;

    private Stk(){
      val = new MyList<>();
      sum = new MyList<>();
      sum.add(e());
    }

    private int size(){ return val.size(); }

    private void add(V v){
      val.add(v);
      sum.add(agg(sum.peekLast(),v));
    }

    abstract V agg(V sum,V v);

    private V pop(){
      sum.pollLast();
      return val.pollLast();
    }

    private V peekSum(){ return sum.peekLast(); }
  }
}