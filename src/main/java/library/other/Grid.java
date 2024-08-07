package library.other;

import library.dataStructure.collection.*;

public class Grid{
  private int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
  private int H,W;

  public Grid(int H,int W){
    this.H = H;
    this.W = W;
  }

  public int[] sur(int p){
    MyList<int[]> tmp = sur(toi(p),toj(p));
    int[] ret = new int[tmp.size()];
    for (int i = 0;i < tmp.size();i++)
      ret[i] = top(tmp.get(i)[0],tmp.get(i)[1]);
    return ret;
  }

  public MyList<int[]> sur(int i,int j){
    MyList<int[]> ret = new MyList<>();
    for (var d:dirs) {
      int ni = i +d[0];
      int nj = j +d[1];
      if (valid(ni,H) && valid(nj,W))
        ret.add(new int[]{ni, nj});
    }

    return ret;
  }

  private boolean valid(int i,int N){ return 0 <= i && i < N; }

  public int top(int i,int j){ return i *W +j; }

  public int toi(int p){ return p /W; }

  public int toj(int p){ return p %W; }
}