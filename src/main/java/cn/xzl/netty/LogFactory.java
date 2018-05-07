package cn.xzl.netty;

/**
 * @author xzl
 * @create 2018-04-17 10:19
 **/
public class LogFactory {

    public static StackTraceElement[] getLog(){
        return new Exception().getStackTrace();
    }
}
