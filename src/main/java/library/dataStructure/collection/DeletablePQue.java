package library.dataStructure.collection;

import java.util.*;

import library.util.*;

public class DeletablePQue<T> {
  private Queue<T> que,rem;
  private Comparator<T> cmp;

  public DeletablePQue(){ this(Util.cast(Comparator.naturalOrder())); }

  public DeletablePQue(Comparator<T> cmp){
    que = new PriorityQueue<>(cmp);
    rem = new PriorityQueue<>(cmp);
    this.cmp = cmp;
  }

  public boolean add(T t){ return que.add(t); }

  public boolean remove(T t){ return rem.add(t); }

  public T poll(){ return adj().poll(); }

  public T peek(){ return adj().peek(); }

  private Queue<T> adj(){
    while (!que.isEmpty() && !rem.isEmpty()
        && cmp.compare(que.peek(),rem.peek()) == 0) {
      que.poll();
      rem.poll();
    }
    return que;
  }

  public boolean isEmpty(){ return adj().isEmpty(); }
}