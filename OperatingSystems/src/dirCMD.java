import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class dirCMD {
    public static void main(String[] args) {
        if(args.length ==0) {
            System.out.println("no arg");
            return;
        }
        File imenik = new File(args[0]);
        if(!imenik.exists()) {
            System.out.println("file no exist");
            return;
        }
        if(!imenik.isDirectory()) {
            System.out.println("not dir");
            return;
        }
        File[] files = imenik.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.getName().contains(".txt"))
                    return true;
                return false;
            }
        });
        long total = 0;
        for(File f : files)
            total+=f.length();
        double avg = total/files.length;
        System.out.println(avg);
    }

    public static void listFiles(String absolutePath, String prefix) {
        File file = new File(absolutePath);
        if(file.exists()) {
            File[] subfiles = file.listFiles();
            for(File f : subfiles) {
                System.out.println(prefix + getPermissions(f) + " " + f.getName());
                if(f.isDirectory())
                    listFiles(f.getAbsolutePath(), prefix + " ");
            }
        }
    }
    public static String getPermissions(File file) {
        return String.format("%s%s%s", file.canRead() ? "r" : "-", file.canWrite() ? "w" : "-", file.canExecute() ? "x" :"-");
    }
}
