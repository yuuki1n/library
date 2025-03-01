package library.dataStructure.cuboidData;

public abstract class Sum3D{
  private long[] sum;
  private int w,d;

  public Sum3D(int h,int w,int d){
    this.w = w;
    this.d = d;
    sum = new long[(h +1) *(w +1) *(d +1)];
    for (int i = 0;i < h;i++)
      for (int j = 0;j < w;j++)
        for (int k = 0;k < d;k++)
          sum[top(i +1,j +1,k +1)] = a(i,j,k)
              +sum[top(i,j +1,k +1)] +sum[top(i +1,j,k +1)] +sum[top(i +1,j +1,k)]
              -sum[top(i +1,j,k)] -sum[top(i,j +1,k)] -sum[top(i,j,k +1)]
              +sum[top(i,j,k)];
  }

  abstract long a(int i,int j,int k);

  private int top(int i,int j,int k){ return i *(w +1) *(d +1) +j *(d +1) +k; }

  public long get(int il,int ir,int jl,int jr,int kl,int kr){
    return sum[top(ir,jr,kr)]
        -sum[top(il,jr,kr)] -sum[top(ir,jl,kr)] -sum[top(ir,jr,kl)]
        +sum[top(ir,jl,kl)] +sum[top(il,jr,kl)] +sum[top(il,jl,kr)]
        -sum[top(il,jl,kl)];
  }
}