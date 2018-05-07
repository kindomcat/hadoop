package cn.xzl;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;

/**
 * @author xzl
 * @create 2018-04-03 13:44
 **/
public class HdfsClient {
    FileSystem fs =null;

    @Before
    public void init() throws Exception{
        Configuration conf = new Configuration();
        //设置副本数
        conf.set("dfs.replication", "1");
         fs  =FileSystem.get(new URI("hdfs://hdp-node1:9000"), conf, "hadoop");
    }
    /*
    * 创建文件夹
    * */
    @Test
    public void mkdir()throws Exception{
        fs.mkdirs(new Path("/java/test"));
    }

    @Test
    public void delete() throws Exception{
        fs.delete(new Path("/user"),true);

    }

    @Test
    public void upload() throws Exception {
        Path path =new Path("e://aaa.txt");
        fs.copyFromLocalFile(path,new Path("java/test"));
        boolean b = fs.isDirectory(new Path("/java/test"));
        System.out.println(b);
    }

    @Test
    public void download() throws Exception {
        fs.copyToLocalFile(new Path("/test/somefile.1"),new Path("e:/"));
    }

    @Test
    public void ls() throws Exception{
        RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(new Path("/"), true);
        while (iterator.hasNext()){
            LocatedFileStatus next = iterator.next();
            System.out.println(next.getBlockSize());
            System.out.println(next.getPath());
        }
    }

    @Test
    public void testUpload() throws Exception{
        FSDataOutputStream outputStream = fs.create(new Path("/test/bbb.txt"), true);
        FileInputStream inputStream = new FileInputStream("e:/aaa.txt");
        IOUtils.copy(inputStream,outputStream);
    }

    @Test
    public void testDownload() throws Exception{
        FSDataInputStream inputStream = fs.open(new Path("/test/somefile.2"));
        FileOutputStream outputStream = new FileOutputStream("e:/bbb.txt");
        IOUtils.copy(inputStream,outputStream);
    }
}
