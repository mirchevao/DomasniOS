import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

class CountTree {
    public static int sync = 0; //count of unsync elements
    public static int NUM_RUNS = 10;
    /**
     * Promenlivata koja treba da go sodrzi brojot na pojavuvanja na elementot 3
     */
    int count = 0;
    /**
     * TODO: definirajte gi potrebnite elementi za sinhronizacija
     */
    //sync element semaphore-mutex
    Semaphore mt;


    public void init() {
        mt = new Semaphore(1);
    }


    class Counter extends Thread {

        public void count(int[] data) throws InterruptedException {
            for(int i = 0; i<data.length; i++) {
                if(data[i]==3) {
                    sync++;
                    mt.acquire();
                    count++;
                    mt.release();
                }
             }
        }
        private int[] data;

        public Counter(int[] data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                count(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            CountTree environment = new CountTree();
            environment.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void start() throws Exception {

        init();

        HashSet<Thread> threads = new HashSet<Thread>();
        Scanner s = new Scanner(System.in);
        int total=s.nextInt();

        for (int i = 0; i < NUM_RUNS; i++) {
            int[] data = new int[total];
            for (int j = 0; j < total; j++) {
                data[j] = s.nextInt();
            }
            Counter c = new Counter(data);
            threads.add(c);
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
        System.out.println(count);
        System.out.println(sync);


    }
}

