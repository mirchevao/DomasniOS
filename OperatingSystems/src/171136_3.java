import java.io.*;
import java.io.File;
import java.util.HashSet;


class FileScanner extends Thread {

    private String fileToScan;
    //TODO: Initialize the start value of the counter
    private static Long counter = new Long(0);

    public FileScanner (String fileToScan) {
        this.fileToScan=fileToScan;
        //TODO: Increment the counter on every creation of FileScanner object
        synchronized (this){
            counter++;
        }
    }

    public static long getSize(File file){
       long size = 0;
        if(!file.isDirectory())
            return 0;
        for(File f : file.listFiles()){
            if(f.isDirectory()){
                size+=getSize(f);
            }
            else{
                size+=f.length();
            }
        }
        return size;
    }
    public static void printInfo(File file)  {

        /*
         * TODO: Print the info for the @argument File file, according to the requirement of the task
         * */
        if(file.isDirectory()){
            long size = getSize(file);
            System.out.println("dir: "+file.getAbsolutePath()+" "+size);
        }
        else{
            System.out.println("file: "+file.getAbsolutePath()+" "+file.length());
        }

    }

    public static Long getCounter () {
        return counter;
    }


    public void run() {

        //TODO Create object File with the absolute path fileToScan.
        File file =new File(fileToScan);

        //TODO Create a list of all the files that are in the directory file.
        File [] files =file.listFiles();
        HashSet<Thread> threads = new HashSet<>();

        for (File f : files) {

            /*
             * TODO If the File f is not a directory, print its info using the function printInfo(f)
             * */
             if(!f.isDirectory())
             {
                 printInfo(f);
             }
            /*
             * TODO If the File f is a directory, create a thread from type FileScanner and start it.
             * */
            else{
                Thread thread = new FileScanner(f.getAbsolutePath());
                threads.add(thread);
                thread.start();
             }


            //TODO: wait for all the FileScanner-s to finish
            for(Thread t: threads){
                try{
                t.join();
            }catch (InterruptedException e){
                    e.getStackTrace();
                }
            }

        }

    }

    public static void main (String [] args) throws  InterruptedException{
        String FILE_TO_SCAN = "C:\\Users\\Olgica\\Desktop";

        //TODO Construct a FileScanner object with the fileToScan = FILE_TO_SCAN
        FileScanner fileScanner = new FileScanner(FILE_TO_SCAN);

        //TODO Start the thread from type FileScanner
        fileScanner.start();
        //TODO wait for the fileScanner to finish
        fileScanner.join();
        //TODO print a message that displays the number of thread that were created
        System.out.println(getCounter());

    }
}

