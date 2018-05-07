package cn.xzl.zookeeper;

import io.netty.bootstrap.Bootstrap;
import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author xzl
 * @create 2018-04-12 11:04
 **/
public class SimpleDemo {

    private static final int SESSION_TIMEOUT=3000;

    ZooKeeper zk;

    Watcher wh =new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            System.out.println(event.toString());
            try {
                zk.getChildren("/zk", new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        System.out.println(123);
                        if (watchedEvent.getType().equals(Event.EventType.NodeCreated)){
                            System.out.println("create children");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /*
     *Author: xzl
     *@Description:初始化
     *@Date:11:10 2018/4/12
     */
    public void createZKInstance() throws Exception{
        zk =new ZooKeeper("hdp-node1:2181",Integer.MAX_VALUE,wh);
    }

    private void ZKOperations() throws IOException, InterruptedException, KeeperException
    {
        System.out.println("/n1. 创建 ZooKeeper 节点 (znode ： zoo2, 数据： myData2 ，权限： OPEN_ACL_UNSAFE ，节点类型： Persistent");
        zk.create("/zoo2", "myData2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("/n2. 查看是否创建成功： ");
        /*System.out.println(new String(zk.getData("/zoo2", false, null)));
        System.out.println("/n3. 修改节点数据 ");
        zk.setData("/zoo2", "shenlan211314".getBytes(), -1);
        System.out.println("/n4. 查看是否修改成功： ");
        System.out.println(new String(zk.getData("/zoo2", false, null)));
        System.out.println("/n5. 删除节点 ");
        zk.delete("/zoo2", -1);
        System.out.println("/n6. 查看节点是否被删除： ");
        System.out.println(" 节点状态： [" + zk.exists("/zoo2", false) + "]");*/
    }
    public static void main(String[] args) throws Exception {
        SimpleDemo dm = new SimpleDemo();
        dm.createZKInstance();
    }





}
