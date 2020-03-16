import java.util.Scanner;

public class producerConsumer {
    public static void main(String[] args) throws InterruptedException {

        final PC pc = new PC();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pc.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pc.consume();
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
    public static class PC {
        public void produce() throws InterruptedException {
            synchronized (this){
                System.out.println("producer thread running");
                //released the lock on same resource
                wait();
                //waits for someone to call notify
                System.out.println("resumed");
            }
        }
        public void consume() throws InterruptedException {
            Thread.sleep(1000);
            Scanner scanner = new Scanner(System.in);
            synchronized (this) {
                System.out.println("waiting to return key");
                scanner.nextLine();
                System.out.println("return key pressed");
                notify(); //can wakeup
                Thread.sleep(2000);
            }
        }
    }
}

