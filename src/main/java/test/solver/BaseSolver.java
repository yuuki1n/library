package test.solver;

import static java.lang.Math.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Scanner;

import library.io.MyReader;
import library.io.MyWriter;
import library.util.Util;

public abstract class BaseSolver extends Util{
  public BaseSolver(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

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
    Optional.ofNullable(solve()).ifPresent(out::println);
    out.flush();
    return elapsed();
  }
}
