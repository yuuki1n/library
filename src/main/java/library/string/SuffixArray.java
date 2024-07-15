package library.string;

import static java.util.Arrays.*;

import java.util.function.*;

import library.util.*;

public class SuffixArray{
  public static int[] sa(char[] s){ return sais(Util.arrI(s.length,i -> s[i] -'a'),26); }

  private static int[] sais(int[] s,int k){
    int n = s.length;
    int m = 0;
    boolean[] ls = new boolean[n];
    for (int i = n -2;i >= 0;i--) {
      ls[i] = s[i] < s[i +1] || s[i] == s[i +1] && ls[i +1];
      if (!ls[i] && ls[i +1])
        m++;
    }
    int[] lms = new int[m];
    int[] lmsMap = new int[n];
    int[] sumL = new int[k +1];
    int[] sumS = new int[k +1];
    for (int i = 0,j = 0;i < n;i++) {
      if (i > 0 && !ls[i -1] && ls[i]) {
        lmsMap[i] = j;
        lms[j++] = i;
      } else
        lmsMap[i] = -1;
      if (ls[i])
        sumL[s[i] +1]++;
      else
        sumS[s[i]]++;
    }
    for (int i = 0;i <= k;i++) {
      sumS[i] += sumL[i];
      if (i < k)
        sumL[i +1] += sumS[i];
    }
    Function<int[], int[]> inducedSort = a -> {
      int[] sa = Util.arrI(n,i -> -1);
      int[] buf = new int[k +1];
      System.arraycopy(sumS,0,buf,0,k +1);
      for (int d:a)
        sa[buf[s[d]]++] = d;
      System.arraycopy(sumL,0,buf,0,k +1);
      sa[buf[s[n -1]]++] = n -1;
      for (int i = 0;i < n;i++) {
        int v = sa[i] -1;
        if (v >= 0 && !ls[v])
          sa[buf[s[v]]++] = v;
      }
      System.arraycopy(sumL,0,buf,0,k +1);
      for (int i = n -1;i >= 0;i--) {
        int v = sa[i] -1;
        if (v >= 0 && ls[v])
          sa[--buf[s[v] +1]] = v;
      }
      return sa;
    };
    int[] sa = inducedSort.apply(lms);
    if (m == 0)
      return sa;
    int[] sortedLms = new int[m];
    int p = 0;
    for (int i:sa)
      if (lmsMap[i] != -1)
        sortedLms[p++] = i;
    int[] recS = new int[m];
    int recK = 0;
    recS[lmsMap[sortedLms[0]]] = 0;
    for (int i = 1;i < m;i++) {
      int l = sortedLms[i -1];
      int r = sortedLms[i];
      int endL = lmsMap[l] +1 < m ? lms[lmsMap[l] +1] : n;
      int endR = lmsMap[r] +1 < m ? lms[lmsMap[r] +1] : n;
      boolean same = endL -l == endR -r;
      if (same) {
        while (l < endL && s[l] == s[r]) {
          l++;
          r++;
        }
        same = l < n && s[l] == s[r];
      }
      if (!same)
        recK++;
      recS[lmsMap[sortedLms[i]]] = recK;
    }
    int[] recSA = sais(recS,recK);
    setAll(sortedLms,i -> lms[recSA[i]]);
    return inducedSort.apply(sortedLms);
  }
}