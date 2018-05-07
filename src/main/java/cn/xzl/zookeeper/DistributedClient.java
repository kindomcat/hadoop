package cn.xzl.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author xzl
 * @create 2018-05-03 16:25
 * 分布式锁的实现
 **/
public class DistributedClient {

    //超时时间
    private static final int SESSION_TIMEOUT=5000;

    private String hosts ="hdp-node1";
    //父目录
    private String groupNode ="locks";
    //子目录
    private String subNode ="sub";
    //zk client
    private ZooKeeper zk;
    //当前client要创建的子节点
    private String thisPath;
    //当前client的节点
    private String waitPath;

    private CountDownLatch latch =new CountDownLatch(1);

    public void connetZk() throws Exception {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                //有连接建立时，打开latch
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }

                //发生了删除事件时
                if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(waitPath)) {
                    try {
                        dosomething();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //等待连接建立
        latch.await();
        //创建节点
        zk.create("/"+groupNode+"/"+subNode,null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        //让结果更清晰
        Thread.sleep(10);
        List<String> childrenNodes = zk.getChildren("/" + groupNode, false);

        if (childrenNodes.size()==1){
            dosomething();
        }else {
            String thisNode = thisPath.substring(("/" + groupNode + "/").length());
            Collections.sort(childrenNodes);
            int index = childrenNodes.indexOf(thisNode);
            if (index==0){
                dosomething();
            }else {
                this.waitPath ="/"+groupNode+"/"+childrenNodes.get(index-1);
                zk.getData(waitPath,true,new Stat());
            }
        }


    }

    //下一个获得锁
    private void dosomething() throws Exception{
        System.out.println("gain lock  :"+thisPath);
        try {
            Thread.sleep(2000);
            //dosomething
        } finally {
            System.out.println("finished :" +thisPath);
            //释放锁
            zk.delete(thisPath,-1);
        }

    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i <10 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DistributedClient ds  =new DistributedClient();
                    try {
                        ds.connetZk();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        Thread.sleep(Long.MAX_VALUE);
    }

}
