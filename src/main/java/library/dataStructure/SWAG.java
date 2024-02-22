package library.dataStructure;

import library.dataStructure.collection.*;

public abstract class SWAG<V> {
  private Stk front,back;
  private MyStack<V> tmp = new MyStack<>();

  public SWAG(){
    front = new Stk(){
      @Override
      V ag(V sum,V v){
        var ret = e();
        agg(ret,v,sum);
        return ret;
      }
    };
    back = new Stk(){
      @Override
      V ag(V sum,V v){
        var ret = e();
        agg(ret,sum,v);
        return ret;
      }
    };
  }

  protected abstract V e();
  protected abstract void agg(V v,V a,V b);

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

  public V sum(){
    var ret = e();
    agg(ret,front.peekSum(),back.peekSum());
    return ret;
  }

  private void f(Stk a,Stk b){
    while (b.size() -1 > tmp.size())
      tmp.add(b.pop());
    while (b.size() > 0)
      a.add(b.pop());
    while (tmp.size() > 0)
      b.add(tmp.pop());
  }

  public int size(){ return front.size() +back.size(); }

  abstract class Stk{
    private MyStack<V> val,sum;

    private Stk(){
      val = new MyStack<>();
      sum = new MyStack<>();
      sum.add(e());
    }

    private int size(){ return val.size(); }

    private void add(V v){
      val.add(v);
      sum.add(ag(sum.peek(),v));
    }

    abstract V ag(V sum,V v);

    private V pop(){
      sum.pop();
      return val.pop();
    }

    private V peekSum(){ return sum.peek(); }
  }
}