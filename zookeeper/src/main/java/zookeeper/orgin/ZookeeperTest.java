package zookeeper.orgin;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperTest implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String IP = "192.168.195.108:2181";
    private static Integer TIME_OUT = 20000;
    private static ZooKeeper zk;
    public static void main(String[] args) {
        try {
            zk = new ZooKeeper(IP,TIME_OUT,new ZookeeperTest());
            System.out.println("zk连接成功！");

            Thread.sleep(6000);
            List<String> children = zk.getChildren("/", false);
            children.forEach(System.out::println);

//            BaseZookeeper baseZookeeper = new BaseZookeeper();
//            System.out.println(baseZookeeper.createNode("/java", "java"));
            createZnode();

            Stat stat = new Stat();
//            System.out.println(new String(zk.getData("/java",true, stat)));
            zk.getData("/java", false, (i, s, o, bytes, stat1) -> {
                //在执行完getData 之后实行回调
                System.out.println("DataCallback .....");
                System.out.println(new String(bytes));
            },null);
            System.out.println("stat" + stat);

            System.in.read();
        } catch (IOException e) {
            System.out.println("连接失败");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            System.out.println("获取子节点失败");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("watcher 通知的内容 --> " + watchedEvent);
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
            System.out.println("watched received event ...");
        } else if (watchedEvent.getType().equals(Event.EventType.NodeDataChanged)) {
            System.out.println("原生watch监听器被触发 ... node data changed");
        } else if (watchedEvent.getType().equals(Event.EventType.NodeCreated)) {
            System.out.println("原生watch监听器被触发 ... node data created");
        }
        
    }

    private static void createZnode(){
        String path = "/zk";
        byte[] data = "java".getBytes();
        try {
            String create = zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create znode -> " + create);

        } catch (Exception e) {
            System.out.println("创建节点失败 ... ");
            e.printStackTrace();
        }
    }

    public String createNode(String path,String data) throws Exception{
        return this.zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public void closeConnection() throws InterruptedException{

        if (zk != null) {
            zk.close();
        }
    }
}
