import java.io.File;
import java.io.FilenameFilter;

public class HW01_1 {
    /*
    prosecna golemina na datoteki so ekstenzija .txt
    zadadeni kako argument na komandna linija
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No argument error");
            return;
        }

        File imenik = new File(args[0]); //se zadava od cmd
        if(!imenik.exists()) {
            System.out.println("File does not exist");
            return;
        }
        if(!imenik.isDirectory()) {
            System.out.println("The file is not in the system");
            return;
        }
        File files[] = imenik.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.indexOf(".txt")!=-1;
            }
        });

        long total = 0;
        for(File f : files) {
            total += f.length();
        }
        double avg = (double) total/files.length;
        System.out.println("Total size of the .txt files is: " + total + " and the average is: " + avg);
    }
}
class dirfilter implements FilenameFilter {
    String s;
    dirfilter(String s){
        this.s = s;
    }
    public boolean accept(File dir, String name) {
        String f = new File(name).getName();
        return f.indexOf(s)!=-1;
    }
    public static void listFile(String absolutePath, String prefix) {
        File file = new File(absolutePath);
        if( file.exists()) {
            File[] subfiles = file.listFiles();
            for(File f : subfiles) {
                System.out.println(prefix + getPermissions(f) + " " + f.getName());
                if(f.isDirectory()) {
                    listFile(f.getAbsolutePath(), prefix + " ");
                }
            }
        }
    }
    public static String getPermissions(File f) {
        return String.format("%s%s%s", f.canRead() ? "r" : "-", f.canWrite() ? "w" : "-", f.canExecute() ? "x" : "-");
    }
}
