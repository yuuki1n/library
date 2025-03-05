package library.dataStructure.collection;

import static java.lang.Math.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

import library.util.*;

public class MyList<E> implements Iterable<E>{
  private E[] arr;
  private int hd,tl;

  public MyList(){ this(16); }

  public MyList(int n){ arr = Util.cast(new Object[Integer.highestOneBit(max(16,n) -1) <<1]); }

  public MyList(MyList<E> org){
    this(org.size() +1);
    System.arraycopy(org.arr,0,arr,0,tl = org.size());
  }

  public MyList(Collection<E> col){
    this(col.size() +1);
    col.forEach(this::add);
  }

  public void add(E t){ addLast(t); }

  public void addFirst(E e){
    hd = hd -1 &arr.length -1;
    arr[hd] = e;
    if (hd == tl)
      grow();
  }

  public void addLast(E e){
    arr[tl] = e;
    tl = tl +1 &arr.length -1;
    if (hd == tl)
      grow();
  }

  public E peek(){ return peekFirst(); }

  public E peekFirst(){ return arr[hd]; }

  public E peekLast(){ return arr[tl -1 &arr.length -1]; }

  public E poll(){ return pollFirst(); }

  public E pollFirst(){
    E ret = arr[hd];
    hd = hd +1 &arr.length -1;
    return ret;
  }

  public E pollLast(){
    tl = tl -1 &arr.length -1;
    return arr[tl];
  }

  public E get(int i){ return arr[hd +i &arr.length -1]; }

  public E remove(int i){
    i = hd +i &arr.length -1;
    E ret = arr[i];
    tl = tl -1 &arr.length -1;
    while (i != tl) {
      arr[i] = arr[i +1 &arr.length -1];
      i = i +1 &arr.length -1;
    }
    return ret;
  }

  public E removeFast(int i){
    swap(i,size() -1);
    return pollLast();
  }

  public void swap(int i,int j){
    i = hd +i &arr.length -1;
    j = hd +j &arr.length -1;
    Util.swap(arr,i,j);
  }

  public void set(int i,E t){ arr[hd +i &arr.length -1] = t; }

  public void clear(){ tl = hd; }

  public int size(){ return tl -hd &arr.length -1; }

  public boolean isEmpty(){ return tl == hd; }

  public void sort(){ sort(Util.cast(Comparator.naturalOrder())); }

  public void sort(Comparator<E> cmp){
    if (hd > tl)
      System.arraycopy(arr,hd,arr,tl,arr.length -hd);
    else
      System.arraycopy(arr,hd,arr,0,tl -hd);

    Arrays.sort(arr,hd = 0,tl = size(),cmp);
  }

  public <U> MyList<U> map(Function<E, U> func){
    MyList<U> ret = new MyList<>(size());
    forEach(t -> ret.add(func.apply(t)));
    return ret;
  }

  public MyList<E> rev(){
    MyList<E> ret = new MyList<>(size());
    for (int i = size();i-- > 0;)
      ret.add(get(i));
    return ret;
  }

  public int[] toIntArray(ToIntFunction<E> f){ return Util.arrI(size(),i -> f.applyAsInt(get(i))); }

  public E[] toArray(){
    if (hd == tl)
      return Util.cast(new Object[0]);
    E[] ret = Util.cast(Array.newInstance(arr[0].getClass(),size()));
    if (hd < tl)
      System.arraycopy(arr,hd,ret,0,tl -hd);
    else {
      System.arraycopy(arr,hd,ret,0,arr.length -hd);
      System.arraycopy(arr,0,ret,arr.length -hd,tl);
    }
    return ret;
  }

  private void grow(){
    E[] newarr = Util.cast(new Object[arr.length <<1]);
    System.arraycopy(arr,hd,newarr,0,arr.length -hd);
    System.arraycopy(arr,0,newarr,arr.length -hd,tl);
    hd = 0;
    tl = arr.length;
    arr = newarr;
  }

  @Override
  public Iterator<E> iterator(){
    return new Iterator<>(){
      int i = 0;

      @Override
      public boolean hasNext(){ return i < size(); }

      @Override
      public E next(){ return get(i++); }
    };
  }
}