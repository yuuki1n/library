package library.dataStructure;

import static java.lang.Math.*;
import static java.util.Arrays.*;

public class FastSet{
  private int size;
  private long[][] data = new long[1][1];

  public FastSet(){ this(1 <<18); }

  public FastSet(int n){ grow(n); }

  private void grow(int n){
    int K = (37 -Integer.numberOfLeadingZeros((n = max(data[0].length <<7,n)) -1)) /6;
    long[][] newData = new long[K][];
    for (int i = 0;i < data.length;i++)
      newData[i] = copyOf(data[i],n = n +63 >>6);
    for (int i = data.length;i < K;i++) {
      newData[i] = new long[n = n +63 >>6];
      for (int j = 0;j < newData[i -1].length;j++)
        if (newData[i -1][j] != 0)
          newData[i][j >>6] |= 1L <<j;
    }
    data = newData;
  }

  public boolean get(int x){ return x >>6 < data[0].length && (data[0][x >>6] >>>(x &63) &1) == 1; }

  public void set(int x){
    if (data[0].length <= x >>6)
      grow(x +1);
    if (get(x))
      return;
    size++;
    for (int i = 0;i < data.length;x >>= 6)
      data[i++][x >>6] |= 1L <<x;
  }

  public void clear(int x){
    if (!get(x))
      return;
    size--;
    long lastValue = 0;
    for (int i = 0;i < data.length && lastValue == 0;x >>= 6)
      lastValue = data[i++][x >>6] &= ~(1L <<x);
  }

  public int floor(int x){
    if (x < 0)
      return -1;
    if (get(x))
      return x;
    for (int i = 0,y = x;i < data.length;i++) {
      int offset = y &63;
      y >>= 6;
      long headMask = offset == 0 ? 0 : -1L >>>64 -offset;
      if (y < data[i].length && (data[i][y] &headMask) != 0)
        return last(i -1,y <<6 |highestOneBitOffset(data[i][y] &headMask));
    }
    return -1;
  }

  public int ceiling(int x){
    if (data[0].length <= x >>6)
      return -1;
    if (get(x))
      return x;
    for (int i = 0,y = x;i < data.length;i++) {
      int offset = y &63;
      y >>= 6;
      long tailMask = offset == 63 ? 0 : -1L <<1 +offset;
      if ((data[i][y] &tailMask) != 0)
        return first(i -1,y <<6 |lowestOneBitOffset(data[i][y] &tailMask));
    }
    return -1;
  }

  public int size(){ return size; }

  private int last(int i,int x){
    while (0 <= i)
      x = x <<6 |highestOneBitOffset(data[i--][x]);
    return x;
  }

  private int first(int i,int x){
    while (0 <= i)
      x = x <<6 |lowestOneBitOffset(data[i--][x]);
    return x;
  }

  private int highestOneBitOffset(long x){ return 63 -Long.numberOfLeadingZeros(x); }

  private int lowestOneBitOffset(long x){ return Long.numberOfTrailingZeros(x &-x); }

  public int[] toArray(){
    if (size == 0)
      return new int[0];
    int[] ret = new int[size];
    ret[0] = first(data.length -1,0);
    for (int i = 1;i < size;i++)
      ret[i] = ceiling(ret[i -1] +1);
    return ret;
  }
}