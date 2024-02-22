package library.dataStructure;

import static java.util.Arrays.*;

import java.util.*;

import library.util.*;

public class PersistentArray{
  private TreeMap<Integer, Integer>[] arr;

  public PersistentArray(int n){
    arr = Util.cast(new TreeMap[n]);
    setAll(arr,i -> new TreeMap<>());
  }

  public int get(int i,int t){ return arr[i].floorEntry(t).getValue(); }

  public int set(int i,int v,int t){
    arr[i].put(t,v);
    return v;
  }
}