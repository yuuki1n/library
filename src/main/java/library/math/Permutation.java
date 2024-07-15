package library.math;

import static java.util.Arrays.*;

import library.util.*;

public class Permutation{
  private int n;
  int[] arr;

  public Permutation(int n){ arr = Util.arrI(this.n = n,i -> i); }

  public Permutation(int[] arr){ this.arr = copyOf(arr,n = arr.length); }

  public boolean increment(){ return crement(1); }

  public boolean decrement(){ return crement(-1); }

  private boolean crement(int d){
    int l = n -2;
    while (0 <= l && arr[l] *d >= arr[l +1] *d)
      l--;

    if (l < 0)
      return false;

    int r = n -1;
    while (arr[l] *d >= arr[r] *d)
      r--;
    swap(l,r);

    l++;
    r = n -1;
    while (l < r)
      swap(l++,r--);

    return true;
  }

  private void swap(int l,int r){
    arr[l] ^= arr[r];
    arr[r] ^= arr[l];
    arr[l] ^= arr[r];
  }
}