package cn.xzl.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xzl
 * @create 2018-04-11 17:18
 **/
public class Test {

    public static void main(String[] args) throws Exception{
        RandomAccessFile accessFile = new RandomAccessFile("e:/aaa.txt", "rw");
        FileChannel channel = accessFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        int read = channel.read(buffer);
        while (read!=-1){
            buffer.flip();
            while (buffer.hasRemaining()){
                System.out.print((char)buffer.get());
            }
            buffer.clear();
            read =channel.read(buffer);
        }
        accessFile.close();
    }
}
