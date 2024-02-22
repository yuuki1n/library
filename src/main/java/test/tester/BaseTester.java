package test.tester;

import static java.lang.Math.*;

import java.io.*;
import java.util.*;

import library.io.*;
import library.util.*;

public abstract class BaseTester extends BaseSolver{
  public BaseTester(InputStream in,OutputStream out,OutputStream log){
    super(new MyReader(in),new MyWriter(out,false),new MyWriter(log,true));
  }

  public abstract Object solve();

  public boolean verify(MyReader in,Scanner subIn,Scanner extIn){
    while (extIn.hasNext())
      if (!subIn.hasNext() || !extIn.next().equals(subIn.next()))
        return false;
    return subIn.hasNext() == extIn.hasNext();
  }

  protected boolean gosaCheck(double sub,double ext,double dlt){
    return abs(sub -ext) <= dlt || ext != 0 && abs((sub -ext) /ext) <= dlt;
  }

  public long exe(){
    reset();
    int T = multi ? in.it() : 1;
    while (T-- > 0)
      Optional.ofNullable(solve()).ifPresent(out::println);
    out.flush();
    return elapsed();
  }
}
