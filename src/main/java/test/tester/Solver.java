package test.tester;

import java.util.*;

/**
 * 仮で動かすところ
 * @author yuuki_n
 *
 */
public class Solver extends BaseSolver{
  static boolean multi = false;

  public Solver(MyReader in,MyWriter out,MyWriter log){ super(in,out,log); }

  public static void main(final String[] args) throws Exception{
    var in = new MyReader(System.in);
    var out = new MyWriter(System.out,false);
    var log = new MyWriter(System.err,true);
    int T = Solver.multi ? in.it() : 1;
    while (T-- > 0)
      Optional.ofNullable(new Solver(in,out,log)
          .solve()).ifPresent(out::println);
    out.flush();
  }

  Object solve(){

    return 1;
  }
}
