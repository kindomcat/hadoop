package cn.xzl.hadoopRPC;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtocolProxy;
import org.apache.hadoop.ipc.RPC;

/**
 * @author xzl
 * @create 2018-04-10 22:17
 **/
public class Server {

    public static void main(String[] args) throws Exception{
        RPC.Builder builder = new RPC.Builder(new Configuration());
        RPC.Server server = builder.setBindAddress("192.168.5.125").setPort(8888).setProtocol(RPCServer.class).setInstance(new RPCServerImpl()).build();
        server.start();
    }
}
