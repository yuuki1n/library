package test.solver;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

import library.util.Util;

public abstract class BaseSolver extends Util{
  public BaseSolver(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  public abstract Object solve();

  public void exe(){
    Optional.ofNullable(solve()).ifPresent(out::println);
    out.flush();
  }
}
