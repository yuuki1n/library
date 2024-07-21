package library.dataStructure.collection;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.*;
import java.util.ArrayList;

public class BitSet{
  private long[] a;

  public BitSet(){ this(1 <<16); }

  public BitSet(int n){ a = new long[n +63 >>6]; }

  public BitSet or(BitSet b){
    BitSet ret = new BitSet(max(last(),b.last()) +1);
    for (int i = 0;i < ret.a.length;i++)
      ret.a[i] = (i < a.length ? a[i] : 0) |(i < b.a.length ? b.a[i] : 0);
    return ret;
  }

  public BitSet and(BitSet b){
    BitSet ret = new BitSet(min(last(),b.last()) +1);
    for (int i = 0;i < ret.a.length;i++)
      ret.a[i] = a[i] &b.a[i];
    return ret;
  }

  public BitSet xor(BitSet b){
    BitSet ret = new BitSet(max(last(),b.last()) +1);
    for (int i = 0;i < ret.a.length;i++)
      ret.a[i] = a[i] ^b.a[i];
    return ret;
  }

  public BitSet shift(int n){
    BitSet ret = new BitSet(last() +n +65);
    int m = abs(n) >>6,r = abs(n) &63;
    if (n < 0) {
      n *= -1;
      for (int i = 0;i < ret.a.length;i++)
        ret.a[i] = a[i +m] >>r |a[i +m +1] <<64 -r >>>64 -r;
    } else
      for (int i = last() >>6;i >= 0;i--) {
        ret.a[i +m] |= a[i] <<r;
        if (r > 0)
          ret.a[i +m +1] |= a[i] >>>64 -r;
      }
    return ret;
  }

  public int last(){
    for (int i = a.length;0 < i;)
      if (a[--i] != 0)
        return i <<6 |63 -Long.numberOfLeadingZeros(a[i]);
    return -1;
  }

  public boolean get(int i){ return i >>6 < a.length && (a[i >>6] >>i &1) == 1; }

  public void set(int i){
    if (a.length <= i >>6)
      a = copyOf(a,max(a.length <<1,i >>6 +1));
    a[i >>6] |= 1L <<i;
  }

  public void clear(int i){
    if (i >>6 < a.length)
      a[i >>6] &= ~(1L <<i);
  }

  public void flip(int i){
    if (i >>6 < a.length)
      a[i >>6] ^= 1L <<i;
  }

  public int cardinality(){
    int ret = 0;
    for (long x:a)
      ret += Long.bitCount(x);
    return ret;
  }

  public int[] toArray(){
    List<Integer> list = new ArrayList<>();
    for (int i = 0;i < a.length;i++)
      if (a[i] != 0)
        for (int j = 0;j < 64;j++)
          if ((a[i] >>j &1) == 1)
            list.add(i *64 +j);
    return list.stream().mapToInt(a -> a).toArray();
  }
}