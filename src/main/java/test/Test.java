package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import test.solver.ABC327_F;
import test.solver.BaseSolver;
import test.solver.RangeAddRangeSum;

public class Test{
  static final long LMT = 3000;

  public static void main(final String[] args){
    new RangeAddRangeSum(null,null,System.err).solve();
    //    test(ABC327_F.class);
  }

  private static void test(Class<? extends BaseSolver> cls){
    test(cls,"G:\\atcoder\\testcase\\" +cls.getSimpleName().replace("_","\\"));
  }

  private static void test(Class<? extends BaseSolver> cls,String path){
    String pathOut = path +"\\out";
    String pathIn = path +"\\in";
    String pathSubmit = path +"\\submit";
    File submit = new File(pathSubmit);
    if (!submit.exists())
      submit.mkdir();
    final File outFolder = new File(pathOut);
    boolean all = true;
    for (File testcase:outFolder.listFiles()) {
      final String testCase = testcase.getName();
      //実行タスク
      Thread th = new Thread(() -> {
        try {
          cls.getConstructor(InputStream.class,OutputStream.class,OutputStream.class)
              .newInstance(new FileInputStream(pathIn +"\\" +testCase),
                  new FileOutputStream(pathSubmit +"\\" +testCase),
                  System.err)
              .exe();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      //開始・経過時間
      long st = System.currentTimeMillis();
      long time;
      //タスク実行開始
      th.start();
      //制限時間or終了まで処理続行
      while ((time = System.currentTimeMillis() -st) <= LMT && th.isAlive())
        try {
          Thread.sleep(3);
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }

      //結果
      StringBuilder result = new StringBuilder(String.format("%-20s",testCase));

      //実行時間の判定
      if (time <= LMT) {
        if (strList(pathOut +"\\" +testCase)
            .equals(strList(pathSubmit +"\\" +testCase)))
          //正解
          result.append("AC ");
        else {
          //不正解
          result.append("WA ");
          all = false;
        }
      } else {
        //実行制限時間超過
        result.append("TLE");
        all = false;
        th.stop();
      }
      result.append(":" +String.format("%4s",time) +"ms");
      System.out.println(result);
    }
    if (all)
      System.out.println("all ok");
    else
      System.err.println(cls.getSimpleName());
  }

  private static List<String> strList(final String filePath){
    Scanner scanner = null;
    try {
      scanner = new Scanner(new FileInputStream(filePath));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    final List<String> list = new ArrayList<>();
    while (scanner.hasNext())
      list.add(scanner.next());
    return list;
  }
}
