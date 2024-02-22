package test;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import library.io.MyReader;
import test.tester.*;

public class Test{
  final int thread = 8;
  static final long LMT = 2000;
  List<Class<?>> errList = new ArrayList<>();

  private void exe() throws Exception{
    //    Thread.sleep(1000);
    //    test(ABC331_F.class);
    test(ABC133_F.class,"a01");
    test(ABC133_F.class);
    //    test(ABC331_F.class,"00_sample_00.txt");
    //    new ABC309_F(System.in,System.out,System.err).exe();
    //    test(ABC256_Ex.class,"01_n_small_00.txt");
    //    test(ABC309_F.class);
    //    test(ABC322_F.class);
    //    test(ABC332_F.class);
    //    test(RangeReverseRangeSum.class);
  }

  private void test(Class<? extends BaseTester> cls,String fileName) throws Exception{
    String path = "G:\\atcoder\\testcase\\" +cls.getSimpleName().replace("_","\\");
    var file = new File(path +"\\in\\" +fileName);
    BaseTester solver = cls.getConstructor(InputStream.class,OutputStream.class,OutputStream.class)
        .newInstance(new FileInputStream(file),System.out,System.err);
    solver.exe();
  }

  private void test(Class<? extends BaseTester> cls) throws Exception{
    String path = "G:\\atcoder\\testcase\\" +cls.getSimpleName().replace("_","\\");
    ExecutorService executor = Executors.newFixedThreadPool(thread);
    List<MyTask> futures = new ArrayList<>();
    for (var file:new File(path +"\\in").listFiles()) {
      var out = new ByteArrayOutputStream();
      var log = new ByteArrayOutputStream();
      BaseTester solver = cls.getConstructor(InputStream.class,OutputStream.class,OutputStream.class)
          .newInstance(new FileInputStream(file),out,log);
      MyTask future = new MyTask(() -> solver.exe(),file,solver,out);
      executor.submit(future);
      futures.add(future);
    }

    List<Result> results = new ArrayList<>();
    for (var task:futures) {
      var file = task.file;
      var solver = task.solver;
      var out = task.out;
      String result;
      long time = 0;
      try {
        time = task.get(LMT,TimeUnit.MILLISECONDS);
        if (solver.verify(
            new MyReader(new FileInputStream(file)),
            new Scanner(new ByteArrayInputStream(out.toByteArray())),
            new Scanner(new FileInputStream(path +"\\out\\" +file.getName()))))
          //正解
          result = "AC";
        else
          //不正解
          result = "WA";
      } catch (TimeoutException e) {
        result = "TLE";
        time = LMT;
      }
      results.add(new Result(file.getName(),result,time));
    }

    var all = true;
    for (var result:results) {
      System.out.println(result);
      if (!"AC".equals(result.result))
        all = false;
    }

    if (!all)
      errList.add(cls);
    executor.shutdown();
  }

  public static void main(final String[] args) throws Exception{
    long st = System.currentTimeMillis();
    Test test = new Test();
    test.exe();
    for (var err:test.errList)
      System.err.println(err.getSimpleName());
    long time = System.currentTimeMillis() -st;
    Thread.sleep(100);
    System.err.println(time);
  }

  class Result{
    private String testcase;
    private String result;
    private long time;

    public Result(String testcase,String result,long time){
      this.testcase = testcase;
      this.result = result;
      this.time = time;
    }

    @Override
    public String toString(){ return String.format("%-25s",testcase) +result +" " +String.format("%4s",time) +"ms"; }
  }

  class MyTask extends FutureTask<Long>{
    private File file;
    private BaseTester solver;
    private ByteArrayOutputStream out;

    public MyTask(Callable<Long> callable,File file,BaseTester solver,ByteArrayOutputStream out){
      super(callable);
      this.file = file;
      this.solver = solver;
      this.out = out;
    }
  }
}