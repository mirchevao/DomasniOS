import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class VolleyTournament extends Thread {

    public static void main(String[] args) throws InterruptedException {
        HashSet<Player> threads = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            Player p = new Player();
            threads.add(p);
        }
        // run all threads in background
        for(Thread t : threads)
            t.start();

        // after all of them are started, wait each of them to finish for maximum 2_000 ms
        for(Thread t : threads)
            t.join(2000);
        // for each thread, terminate it if it is not finished
        boolean flag = true;
        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                flag=false;
                System.out.println("Possible deadlock!");
            }
        }
        if(flag)
            System.out.println("Tournament finished.");

    }

    public static Semaphore lock = new Semaphore(1);
    public static Semaphore allowPlayerInside = new Semaphore(12);
    public static Semaphore allowPlayerInDressingRoom = new Semaphore(4);
    public static int counter = 0;
    public static Semaphore signalizeGameStarted = new Semaphore(0);
    public static Semaphore signalizeGameFinished = new Semaphore(0);

    static class Player extends Thread {
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public void execute() throws InterruptedException {
            // at most 12 players should print this in parallel
            allowPlayerInside.acquire(); //lets 12 players inside
            System.out.println("Player inside.");
            allowPlayerInDressingRoom.acquire(); //allows 4 players in dressing room
            // at most 4 players may enter in the dressing room in parallel
            System.out.println("In dressing room.");
            Thread.sleep(10);// this represent the dressing time

            lock.acquire();
            counter++;
            if(counter == 12)  {
                signalizeGameStarted.release(12);
                counter=0;
            }
            //if not 12 it will release one permit for dressing room
            //because at least one will be dressed
            allowPlayerInDressingRoom.release();

            lock.release();
            // after all players are ready, they should start with the game together
            signalizeGameStarted.acquire();
            System.out.println("Game started.");
            Thread.sleep(100);// this represent the game duration
            lock.acquire();
            counter++;
            if(counter == 12) {
                signalizeGameFinished.release(12);
                counter=0;
            }
            lock.release();
            signalizeGameFinished.acquire();
            System.out.println("Player done.");
            // only one player should print the next line, representing that the game has finished
            lock.acquire();
            counter++;
            if(counter==12) {
                System.out.println("Game finished.");
                allowPlayerInside.release(12);
                counter=0;
            }
            lock.release();
        }
    }
}

