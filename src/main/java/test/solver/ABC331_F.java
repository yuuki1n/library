package test.solver;

import java.io.*;

import library.dataStructure.rangeData.base.*;
import library.string.*;
import test.tester.*;

public class ABC331_F extends BaseTester{
  public ABC331_F(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  @Override
  public Object solve(){
    int N = in.it();
    int Q = in.it();
    char[] S = in.ch();
    RollingHash rh = new RollingHash(S,true);
    while (Q-- > 0)
      if (in.it() == 1) {
        int x = in.idx();
        char c = in.ch()[0];
        rh.upd(x,c);
      } else {
        int l = in.idx();
        int r = in.it();
        long has = rh.get(l,r);
        long rev = rh.get(r,l);
        log.printlns(has,rev);
        out.println(has == rev);
      }

    return null;
  }

  class Data extends BaseV{
    long has,rev;

    public Data(long has,long rev){
      this.has = has;
      this.rev = rev;
    }
  }
}