package cn.xzl.zookeeper;

import org.apache.zookeeper.*;

/**
 * @author xzl
 * @create 2018-05-02 11:35
 **/
public class AppServer {

    private String groupNode ="group";
    private String subNode ="sub";

    public void connetZk(String address) throws Exception{
        //连接地址    超时时间     监听器
        ZooKeeper zk = new ZooKeeper("hdp-node1:2181", Integer.MAX_VALUE, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
        String path = zk.create("/" + groupNode + "/" + subNode, address.getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("create  :"+path);
    }

    public void handle() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
        AppServer as =new AppServer();
        as.connetZk(args[0]);
        as.handle();
    }
}
