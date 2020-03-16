import java.util.Scanner;

public class TwoThreads {
   /* public static class Thread1 extends Thread {
        public void run() {
            System.out.println("A");
            System.out.println("B");
        }
    }

    public static class Thread2 extends Thread {
        public void run() {
            System.out.println("1");
            System.out.println("2");
        }
    }

    public static void main(String[] args) {
        new Thread1().start();
        new Thread2().start();
    }*/
   public static void main(String[] args) throws InterruptedException {
       final ThreadAB threadAB = new ThreadAB();

       Thread thread1 = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   threadAB.ThreadA();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });

       Thread thread2 =  new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   threadAB.Thread2();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });

       thread1.start();
       thread2.start();
       thread1.join();
       thread2.join();
   }
   public static class ThreadAB extends Thread {
       public void ThreadA() throws InterruptedException {
           synchronized (this) {
               System.out.println("A");
               System.out.println("B");
               wait();
               //System.out.println("Continue with thread2");
           }
       }

       public void Thread2() throws InterruptedException {
           //Scanner scanner = new Scanner(System.in);
           synchronized (this) {
               //System.out.println("waiting for signal");
                //scanner.nextLine();
               System.out.println("1");
               System.out.println("2");
               notify();
               Thread.sleep(1000);
           }
       }

   }

}