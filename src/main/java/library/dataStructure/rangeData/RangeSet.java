package library.dataStructure.rangeData;

import static java.lang.Math.*;

import java.util.*;

import test.tester.*;

public class RangeSet{
  private long sz;
  private TreeSet<long[]> set;

  public RangeSet(){
    set = new TreeSet<>(Comparator.comparing(t -> t[0]));
    long inf = Solver.infL;
    set.add(new long[]{-inf, -inf});
    set.add(new long[]{inf, inf});
  }

  public long size(){ return sz; }

  public long add(long x){ return add(x,x +1); }

  public long add(long l,long r){
    long ret = r -l;
    long[] t = {l, r};
    for (var s = set.floor(t);t[0] <= s[1];s = set.floor(t))
      ret -= add(t,s);
    for (var s = set.ceiling(t);s[0] <= t[1];s = set.ceiling(t))
      ret -= add(t,s);
    sz += ret;
    set.add(t);
    return ret;
  }

  public long remove(long x){ return remove(x,x +1); }

  public long remove(long l,long r){
    long ret = 0;
    long[] t = {l, r};
    for (var s = set.floor(t);l < s[1];s = set.floor(t))
      ret += remove(t,s);
    for (var s = set.ceiling(t);s[0] < r;s = set.ceiling(t))
      ret += remove(t,s);
    sz -= ret;
    return ret;
  }

  private long add(long[] t,long[] s){
    long ret = min(t[1],s[1]) -max(t[0],s[0]);
    set.remove(s);
    t[0] = min(t[0],s[0]);
    t[1] = max(t[1],s[1]);
    return ret;
  }

  private long remove(long[] t,long[] s){
    set.remove(s);
    long ret = min(t[1],s[1]) -max(t[0],s[0]);
    if (s[0] < t[0])
      set.add(new long[]{s[0], t[0]});
    if (t[1] < s[1])
      set.add(new long[]{t[1], s[1]});
    return ret;
  }

  public long[] get(long x){
    long[] ret = set.floor(new long[]{x});
    return x < ret[1] ? ret : null;
  }

  public long mex(){
    var t = get(0);
    return t == null ? 0 : t[1];
  }

  @Override
  public String toString(){
    List<String> list = new ArrayList<>();
    for (var s:set)
      list.add(Arrays.toString(s));
    return list.toString();
  }
}