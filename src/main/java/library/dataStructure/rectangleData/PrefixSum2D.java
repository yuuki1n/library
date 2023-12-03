package library.dataStructure.rectangleData;

public abstract class PrefixSum2D{
  private long[] sum;
  private int w;

  public PrefixSum2D(int h,int w){
    this.w = w;
    sum = new long[(h +1) *(w +1)];
    for (int i = 0;i < h;i++)
      for (int j = 0;j < w;j++)
        sum[top(i +1,j +1)] = a(i,j) +sum[top(i +1,j)] +sum[top(i,j +1)] -sum[top(i,j)];
  }

  abstract long a(int i,int j);

  private int top(int i,int j){ return i *(w +1) +j; }

  public long get(int il,int ir,int jl,int jr){
    return sum[top(ir,jr)] -sum[top(il,jr)] -sum[top(ir,jl)] +sum[top(il,jl)];
  }
}