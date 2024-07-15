package library.dataStructure.rectangleData;

import static java.lang.Math.*;

public abstract class SparseTable2D{
  private long[][][][] tbl;

  public SparseTable2D(int h,int w){
    int hl = max(1,32 -Integer.numberOfLeadingZeros(h -1));
    int wl = max(1,32 -Integer.numberOfLeadingZeros(w -1));
    tbl = new long[hl][wl][][];
    for (int hi = 0;hi < hl;hi++)
      for (int wi = 0;wi < wl;wi++) {
        int hhl = h -(1 <<hi) +1;
        int wwl = w -(1 <<wi) +1;
        tbl[hi][wi] = new long[hhl][wwl];
        for (int i = 0;i < hhl;i++)
          for (int j = 0;j < wwl;j++)
            if ((hi |wi) == 0)
              tbl[0][0][i][j] = init(i,j);
            else if (0 < hi)
              tbl[hi][wi][i][j] = agg(tbl[hi -1][wi][i][j],tbl[hi -1][wi][i +(1 <<hi -1)][j]);
            else
              tbl[hi][wi][i][j] = agg(tbl[hi][wi -1][i][j],tbl[hi][wi -1][i][j +(1 <<wi -1)]);
      }
  }

  protected abstract long init(int i,int j);
  protected abstract long agg(long a,long b);

  public long get(int i0,int j0,int i1,int j1){
    int il = max(0,31 -Integer.numberOfLeadingZeros(i1 -i0 -1));
    int jl = max(0,31 -Integer.numberOfLeadingZeros(j1 -j0 -1));
    i1 = max(0,i1 -(1 <<il));
    j1 = max(0,j1 -(1 <<jl));
    long[][] tmp = tbl[il][jl];
    return agg(agg(tmp[i0][j0],tmp[i0][j1]),agg(tmp[i1][j0],tmp[i1][j1]));
  }
}