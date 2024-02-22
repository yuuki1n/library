package library.dataStructure.collection;

import java.util.*;

import library.util.*;

public class MyDeque<E> {
  private E[] arr = Util.cast(new Object[16]);
  private int hd,tl;

  public void addFirst(E e){
    hd = mod(hd -1);
    arr[hd] = e;

    if (hd == tl)
      grow();
  }

  public void addLast(E e){
    arr[tl] = e;
    tl = mod(tl +1);
    if (hd == tl)
      grow();
  }

  public E peek(){ return peekFirst(); }

  public E peekFirst(){ return arr[hd]; }

  public E peekLast(){ return arr[tl]; }

  public E poll(){ return pollFirst(); }

  public E pollFirst(){
    E ret = arr[hd];
    hd = mod(hd +1);
    return ret;
  }

  public E pollLast(){
    tl = mod(tl -1);
    return arr[tl];
  }

  public E get(int i){ return arr[mod(hd +i)]; }

  public int size(){ return mod(tl -hd); }

  public boolean isEmpty(){ return tl == hd; }

  public void sort(){ sort(Util.cast(Comparator.naturalOrder())); }

  public void sort(Comparator<E> cmp){
    if (hd > tl)
      System.arraycopy(arr,hd,arr,tl,arr.length -hd);
    else
      System.arraycopy(arr,hd,arr,0,tl -hd);

    Arrays.sort(arr,hd = 0,tl = size(),cmp);
  }

  private int mod(int i){ return (i +arr.length) %arr.length; }

  private void grow(){
    E[] newarr = Util.cast(new Object[arr.length <<1]);
    System.arraycopy(arr,hd,newarr,0,arr.length -hd);
    System.arraycopy(arr,0,newarr,arr.length -hd,tl);
    hd = 0;
    tl = arr.length;
    arr = newarr;
  }
}