import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class SulfurAcidMainSolution {


  static Semaphore o = new Semaphore(4);
  static Semaphore h = new Semaphore(2);
  static Semaphore s = new Semaphore(1);

  static Semaphore ready = new Semaphore(0);
  static Semaphore done = new Semaphore(0);

  static Semaphore oHere = new Semaphore(0);
  static Semaphore hHere = new Semaphore(0);

  static int sCount = 0;

  static Semaphore lock = new Semaphore(1);

  public static void init() {


  }


  public static class Sulfur extends TemplateThread {

    public Sulfur(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      s.acquire();
      lock.acquire();
      sCount++;
      if (sCount == 2) { //sCount==1
        lock.release();
        oHere.acquire(4);
        hHere.acquire(2);
        ready.release(8); //ready.release(6);
        state.bond();
        done.acquire(8); //done.acquire(6);
        state.validate();
        lock.acquire();
        sCount=0;
        s.release(1);
        h.release(2);
        o.release(4);
        lock.release();
      } else {
        lock.release();
        ready.acquire();
        state.bond();
        done.release();
      }
    }

  }

  public static class Hydrogen extends TemplateThread {

    public Hydrogen(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      h.acquire();
      hHere.release();
      ready.acquire();
      state.bond();
      done.release();
    }

  }

  public static class Oxygen extends TemplateThread {

    public Oxygen(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      o.acquire();
      oHere.release();
      ready.acquire();
      state.bond();
      done.release();
    }

  }


  static SulfurAcidState state = new SulfurAcidState();

  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      run();
    }
  }

  public static void run() {
    try {
      int numRuns = 1;
      int numScenarios = 100;

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < numScenarios; i++) {
        for (int j = 0; j < 4; j++) {
          Oxygen o = new Oxygen(numRuns);
          threads.add(o);
        }
        for (int j = 0; j < 2; j++) {
          Hydrogen h = new Hydrogen(numRuns);
          threads.add(h);

        }
        Sulfur s = new Sulfur(numRuns);
        threads.add(s);

      }

      init();

      ProblemExecution.start(threads, state);
      System.out.println(new Date().getTime());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}