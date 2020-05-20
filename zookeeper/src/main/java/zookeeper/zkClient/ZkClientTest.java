package zookeeper.zkClient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;


public class ZkClientTest {
    public static void main(String[] args) throws Exception {
        String IP = "192.168.195.108:2181";
        int timeOut = 50000;
        ZkClient zk = new ZkClient(IP,timeOut);
        zk.setZkSerializer(new MyZkSerializer());
        System.out.println((String) zk.readData("/zk"));
        zk.subscribeDataChanges("/super", new IZkDataListener() {
            //回调函数
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("变更节点为：" + s + "，变更数据为：" + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("删除的节点为：" + s);
            }
        });

        zk.createPersistent("/super", "123");
        Thread.sleep(1000);
        zk.writeData("/super", "456", -1);
        
        Thread.sleep(1000);
        
        System.in.read();
    }
}
