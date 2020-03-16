import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class randomAccess {
    public static void main(String[] args) throws IOException {
        double d = 1.5;
        float f = 14.56f;

        RandomAccessFile randomAccessFile = new RandomAccessFile("raf.txt", "rw");
        randomAccessFile.writeUTF("Hello world");
        randomAccessFile.seek(0);
        System.out.println(randomAccessFile.read());
        randomAccessFile.seek(0);
        byte[] b = {1,2,3};
        System.out.println(randomAccessFile.read(b));
        System.out.println(randomAccessFile.readBoolean());
        System.out.println(randomAccessFile.readByte());
        randomAccessFile.writeChar('c');
        randomAccessFile.seek(0);
        System.out.println(randomAccessFile.readChar());
        randomAccessFile.seek(0);
        randomAccessFile.writeDouble(d);
        randomAccessFile.seek(0);
        System.out.println(randomAccessFile.readDouble());
        randomAccessFile.seek(0);
        randomAccessFile.writeFloat(f);
        randomAccessFile.seek(0);
        System.out.println(randomAccessFile.readFloat());

    }
}
