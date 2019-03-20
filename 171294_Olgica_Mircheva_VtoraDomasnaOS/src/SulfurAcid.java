
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;


public class SulfurAcid {


  public static void init() {


  }


  public static class Sulfur extends TemplateThread {

    public Sulfur(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.bond();
      state.validate();
    }

  }

  public static class Hydrogen extends TemplateThread {

    public Hydrogen(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.bond();
      state.validate();
    }

  }

  public static class Oxygen extends TemplateThread {

    public Oxygen(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.bond();
      state.validate();
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