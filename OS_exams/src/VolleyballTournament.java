import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class VolleyballTournament {

    public static void main(String[] args) throws InterruptedException{
        HashSet<Player> threads = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            Player p = new Player();
            threads.add(p);
        }
        // run all threads in background
        for(Thread t : threads)
            t.start();

        // after all of them are started, wait each of them to finish for maximum 2_000 ms
        for (Thread t : threads)
            t.join(2_000);

        // for each thread, terminate it if it is not finished
        for (Thread t : threads){
            if(t.isAlive()){
                t.interrupt();
                System.err.println("Possible deadlock!");
            }
        }
        System.out.println("Tournament finished.");

    }

    public static Semaphore allowEntrance = new Semaphore(12);
    public static Semaphore allowDressing = new Semaphore(4);
    public static Semaphore allowStart = new Semaphore(0);
    public static Semaphore allowFinish = new Semaphore(0);
    public static Semaphore lock = new Semaphore(1);
    public static int counter= 0;
    

    public static class Player extends Thread {

        public void run(){
            try{
                execute();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            // at most 12 players should print this in parallel
            allowEntrance.acquire();
            System.out.println("Player inside.");

            // at most 4 players may enter in the dressing room in parallel
            allowDressing.acquire();
            System.out.println("In dressing room.");
            Thread.sleep(10);// this represent the dressing time
            lock.acquire();
            counter++;
            if(counter==12) {
                allowStart.release(12);
                counter = 0;
            }
            allowDressing.release();
            lock.release();

            // after all players are ready, they should start with the game together
            allowStart.acquire();
            System.out.println("Game started.");

            lock.acquire();
            counter++;
            if(counter == 12) {
                allowFinish.release(12);
                counter = 0;
            }
            lock.release();
            Thread.sleep(100);// this represent the game duration

            allowFinish.acquire();
            System.out.println("Player done.");

            lock.acquire();
            counter++;
            if(counter == 12) {
                System.out.println("Game finished.");
                allowEntrance.release(12);
                counter = 0;
            }
            lock.release();
        }
    }
}