package library.util;

import static java.lang.Math.*;

@FunctionalInterface
public interface BSearchI{
  default int bSearch(int o,int n){
    for (int c = 0;1 < abs(n -o);)
      if (judge(c = o +n >>1))
        o = c;
      else
        n = c;
    return o;
  }

  boolean judge(int x);
}