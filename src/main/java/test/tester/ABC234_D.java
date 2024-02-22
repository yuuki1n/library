package test.tester;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;

import library.dataStructure.AVLTree;
import library.dataStructure.rangeData.base.BaseV;

public class ABC234_D extends BaseTester{
  public ABC234_D(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int K = in.it();
    long[] P = in.lg(N);
    var avl = new AVLTree<Long>(Comparator.comparing(d -> -d)){
      @Override
      protected Long e(){ return 0L; }

      @Override
      protected Long agg(Long a,Long b){ return a +b; }
    };
    for (int i = 0;i < K;i++)
      avl.add(P[i]);
    out.println(avl.get(K -1));
    for (int i = K;i < N;i++) {
      avl.add(P[i]);
      out.println(avl.get(K -1));
    }

    return null;
  }

  private void log(AVLTree<Long> avl){
    long[] arr = new long[(int) avl.size()];
    for (int i = 0;i < avl.size();i++)
      arr[i] = avl.get(i);
    log.println(arr);
  }

  class Data extends BaseV{
    long v;

    public Data(long v){ this.v = v; }
  }
}