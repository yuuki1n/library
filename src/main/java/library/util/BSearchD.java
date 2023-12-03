package library.util;

import static java.lang.Math.*;

@FunctionalInterface
public interface BSearchD{
  default double bSearch(double o,double n){
    for (double c;1e-9 < abs(n -o);)
      if (judge(c = (o +n) /2))
        o = c;
      else
        n = c;
    return o;
  }

  boolean judge(double x);
}