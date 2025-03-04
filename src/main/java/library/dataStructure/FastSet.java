package library.dataStructure;

public class FastSet{
  private int K = 5,size;
  private long[][] data;

  public FastSet(){
    data = new long[K][];
    for (int i = 0;i < K;i++)
      data[i] = new long[1 <<(K -i -1) *6];
  }

  public boolean get(int x){ return (data[0][x >>6] >>>(x &63) &1) == 1; }

  public void set(int x){
    if (get(x))
      return;
    size++;
    for (int i = 0;i < data.length;i++) {
      int offset = x &63;
      x >>= 6;
      data[i][x] |= 1L <<offset;
    }
  }

  public void clear(int x){
    if (!get(x))
      return;
    size--;
    long lastValue = 0;
    for (int i = 0;i < data.length && lastValue == 0;i++) {
      int offset = x &63;
      x >>= 6;
      lastValue = data[i][x] &= ~(1L <<offset);
    }
  }

  public int floor(int x){
    if (x < 0)
      return -1;
    if (get(x))
      return x;
    for (int i = 0,y = x;i < data.length;i++) {
      int offset = y &63;
      y = y >>>6;
      long headMask = offset == 0 ? 0 : -1L >>>64 -offset;
      if ((data[i][y] &headMask) != 0)
        return last(i -1,y <<6 |highestOneBitOffset(data[i][y] &headMask));
    }
    return -1;
  }

  public int ceiling(int x){
    if (1 <<K *6 <= x)
      return -1;
    if (get(x))
      return x;
    for (int i = 0,y = x;i < data.length;i++) {
      int offset = y &63;
      y = y >>>6;
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
}