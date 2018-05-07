package cn.xzl.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xzl
 * @create 2018-05-02 14:09
 **/
public class AppClient {

    private String groupNode ="group";

    private ZooKeeper zk =null;

    Stat stat =new Stat();

    private volatile List<String> serverList;

    public void connetZk() throws Exception {
        //连接地址    超时时间     监听器
        zk= new ZooKeeper("hdp-node1:2181", Integer.MAX_VALUE, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getType()== Event.EventType.NodeChildrenChanged&&("/"+groupNode).equals(event.getPath())){
                    try {
                        updateServerList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        updateServerList();
    }

    public void updateServerList() throws Exception {
        List<String> newServerList =new ArrayList<>();
        //stat指定节点的状态信息
        List<String> subList = zk.getChildren("/" + groupNode, true);
        for (String s : subList) {
            byte[] data = zk.getData("/" + groupNode + "/" + s, false, stat);
            newServerList.add(new String(data,"utf-8"));
        }
        serverList =newServerList;
        System.out.println("serverlist  updata :"+serverList);
    }

    public void handle() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
        AppClient ac =new AppClient();
        ac.connetZk();
        ac.handle();
    }
}
