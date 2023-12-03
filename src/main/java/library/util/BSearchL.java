package library.util;

import static java.lang.Math.*;

@FunctionalInterface
public interface BSearchL{
  default long bSearch(long o,long n){
    for (long c = 0;1 < abs(n -o);)
      if (judge(c = o +n >>1))
        o = c;
      else
        n = c;
    return o;
  }

  boolean judge(long x);
}