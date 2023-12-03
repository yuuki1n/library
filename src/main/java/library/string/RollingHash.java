package library.string;

import static java.lang.Math.*;

import library.util.Mod61;

/**
 * ローリングハッシュ
 * 基本はRHashクラスの内部で使う
 * @author yuuki_n
 *
 */
class RollingHash{

  int n;
  private long[] hash,pow,S;
  private long m;
  private boolean updatale;

  public RollingHash(long[] S,long m,boolean updatale){
    this.S = new long[S.length];
    this.m = m;
    this.updatale = updatale;
    n = S.length;
    hash = new long[n +1];
    pow = new long[n +1];
    pow[0] = 1;
    for (int i = 0;i < n;i++)
      pow[i +1] = Mod61.mul(pow[i],m);
    for (int i = 0;i < n;i++)
      set(i,S[i]);
  }

  public long get(int l,int r){ return Mod61.mod(hash(r) -Mod61.mul(hash(l),pow[r -l])); }

  public long get(int[] s){
    long ret = 0;
    for (int i = 0;i < s.length;i += 2)
      ret = Mod61.mod(Mod61.mul(ret,pow[abs(s[i +1] -s[i])]) +get(s[i],s[i +1]));
    return ret;
  }

  public void set(int i,long v){
    upd(i,v -S[i]);
    S[i] = v;
  }

  private void upd(int i,long v){
    i++;
    if (updatale)
      for (int x = i;x <= n;x += x &-x)
        hash[x] = Mod61.mod(hash[x] +Mod61.mul(v,pow[x -i]));
    else
      hash[i] = Mod61.mod(Mod61.mul(hash[i -1],m) +v);
  }

  private long hash(int i){
    long ret = 0;
    if (updatale)
      for (int x = i;x > 0;x -= x &-x)
        ret = Mod61.mod(ret +Mod61.mul(hash[x],pow[i -x]));
    else
      ret = hash[i];
    return ret;
  }

}