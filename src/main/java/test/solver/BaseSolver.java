package test.solver;

import java.io.InputStream;
import java.io.OutputStream;

import library.util.Util;

public abstract class BaseSolver extends Util{
  public BaseSolver(InputStream in,OutputStream out,OutputStream log){ super(in,out,log); }

  abstract Object solve();

  public void exe(){
    out.println(solve());
    out.flush();
  }
}
