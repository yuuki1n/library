package library.dataStructure;

import java.util.*;

import library.util.*;

public class PriorityDeque<T> {
  private T[] arr;
  private Comparator<T> cmp;
  private int size;

  public PriorityDeque(){ this(Util.cast(Comparator.naturalOrder())); }

  public PriorityDeque(Comparator<T> cmp){
    this.cmp = cmp;
    arr = Util.cast(new Object[16]);
  }

  public int size(){ return size; }

  public boolean isEmpty(){ return size == 0; }

  public void add(T x){
    arr[size++] = x;
    if (size == arr.length)
      arr = Arrays.copyOf(arr,size <<1);
    up(size -1,1);
  }

  public T peek(){ return peekFirst(); }

  public T poll(){ return pollFirst(); }

  public T peekFirst(){ return arr[0]; }

  public T pollFirst(){
    T ret = peek();
    down(swap(0,--size),1);
    return ret;
  }

  public T peekLast(){ return size < 2 ? arr[0] : arr[1]; }

  public T pollLast(){
    T ret = peekLast();
    down(swap(1,--size),1);
    return ret;
  }

  private void down(int k,int root){
    int x = 2 -(k &1);
    while (2 *k +x < size) {
      int c = 2 *k +x +2;
      if (size <= c || (x == 1 ? isDsc(c -2,c) : isDsc(c,c -2)))
        c -= 2;
      if (c < size && (x == 1 ? isDsc(c,k) : isDsc(k,c)))
        k = swap(c,k);
      else
        break;
    }
    up(k,root);
  }

  private void up(int k,int root){
    if ((k |1) < size && isDsc(k &~1,k |1)) {
      swap(k &~1,k |1);
      k ^= 1;
    }

    int p;
    while (root < k && isDsc(p = (k >>1) -1 &~1,k))
      k = swap(p,k);
    while (root < k && isDsc(k,p = (k >>1) -1 |1))
      k = swap(p,k);
  }

  private boolean isDsc(int i,int j){ return cmp.compare(arr[i],arr[j]) > 0; }

  private int swap(int i,int j){
    T t = arr[i];
    arr[i] = arr[j];
    arr[j] = t;
    return i;
  }
}