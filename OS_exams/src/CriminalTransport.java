import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Semaphore;

public class CriminalTransport {


    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 60; i++) {
            Policeman red = new Policeman();
            threads.add(red);
            Criminal green = new Criminal();
            threads.add(green);
        }
        // run all threads in background
        for(Thread t : threads)
            t.start();
        // after all of them are started, wait each of them to finish for maximum 1_000 ms
        for(Thread t : threads)
            t.join(1_000);
        // for each thread, terminate it if it is not finished
        boolean flag = true;
        for(Thread t : threads){
            if(t.isAlive()){
                t.interrupt();
                System.err.println("Terminated transport");
                flag = false;
            }
        }
        if(flag)
            System.out.println("Finished transport");

    }

    public static Semaphore lock = new Semaphore(1);
    public static Semaphore allowPolicemen = new Semaphore(2);
    public static Semaphore allowCriminals = new Semaphore(2);
    public static Semaphore CriminalsHere = new Semaphore(0);
    public static int criminals = 0;
    public static int policemen = 0;
    public static Semaphore allowExit = new Semaphore(0);

    public static class Policeman extends Thread{

        public void run(){
            try{
                execute();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            // waits until it is valid to enter the car
            allowPolicemen.acquire();
            System.out.println("Policeman enters in the car");
            CriminalsHere.acquire();
            lock.acquire();
            policemen++;
            if(policemen == 2){
                System.out.println("Start driving.");
                Thread.sleep(100);
                // one policeman prints the this command to notice that everyone can exit
                System.out.println("Arrived.");
                policemen=0;
                allowExit.release(4);
            }
            lock.release();
            // when the four passengers are inside, one policeman prints the starting command
            // the exit from the car is allowed after the "Arrived." message is printed
            allowExit.acquire();
            System.out.println("Policeman exits from the car");
            lock.acquire();
            policemen++;
            lock.release();
            CriminalsHere.acquire();
            if(policemen==2){
                policemen=0;
                criminals=0;
                allowCriminals.release(2);
                allowPolicemen.release(2);
            }

        }

    }

    public static class Criminal extends Thread{

        public void run(){
            try{
                execute();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            // waits until it is valid to enter the car
            allowCriminals.acquire();
            System.out.println("Criminal enters in the car");
            lock.acquire();
            criminals++;
            if(criminals == 2){
                CriminalsHere.release(2);
                criminals=0;
            }
            lock.release();

            Thread.sleep(100);
            // the exit from the car is allowed after the "Arrived." message is printed
            allowExit.acquire();
            System.out.println("Criminal exits from the car");
            lock.acquire();
            criminals++;
            if(criminals==2){
                CriminalsHere.release(2);
                criminals=0;
            }
            lock.release();
        }
    }
}