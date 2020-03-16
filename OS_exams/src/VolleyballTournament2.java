import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class VolleyballTournament2 extends Thread {

    public static void main(String[] args) throws InterruptedException {
        HashSet<Player> threads = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            Player p = new Player();
            threads.add(p);
        }
        // run all threads in background
        for(Thread t: threads)
            t.start();

        // after all of them are started, wait each of them to finish for maximum 2_000 ms
        for(Thread t : threads)
            t.join(2000);

        // for each thread, terminate it if it is not finished
        boolean deadlock = false;
        for(Thread t : threads)
        {
            if(t.isAlive()){
                t.interrupt();
                deadlock = true;
                System.out.println("Possible deadlock!");
            }
        }

        if(!deadlock)
            System.out.println("Tournament finished.");


    }
    public static Semaphore playerInside = new Semaphore(12);
    public static Semaphore playerDressing = new Semaphore(4);
    public static Semaphore gameStart = new Semaphore(0);
    public static Semaphore gameFinished = new Semaphore(0);
    public static Semaphore playerDone = new Semaphore(0);
    public static class Player extends Thread {

        public void run(){
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            // at most 12 players should print this in parallel
            playerInside.acquire();
            System.out.println("Player inside.");
            // at most 4 players may enter in the dressing room in parallel
            playerDressing.acquire();
            System.out.println("In dressing room.");
            Thread.sleep(10);// this represent the dressing time
            playerDressing.release();
            gameStart.release(1);
            // after all players are ready, they should start with the game together
            System.out.println("Player done.");
            playerDone.acquire();
            gameFinished.release();
            // only one player should print the next line, representing that the game has finished
            gameFinished.acquire(12);
            System.out.println("Game finished.");
            playerInside.release(12);
        }
}
}