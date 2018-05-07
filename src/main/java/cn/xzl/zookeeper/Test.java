package cn.xzl.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author xzl
 * @create 2018-05-03 18:02
 **/
public class Test {

    public void connetZk() throws Exception {

    }

    public static void main(String[] args) throws Exception {
      ZooKeeper  zk= new ZooKeeper("hdp-node1:2181", Integer.MAX_VALUE, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(123);
            }
        });
      while (true){

      }
    }


}
