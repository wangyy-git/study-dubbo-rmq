package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;

import java.io.IOException;

public class UncontectedRetry  {
    public static void main(String[] args) throws IOException {
        String IP = "192.168.195.108:2181";
        Integer time_out = 20000;
        CuratorFramework client = CuratorFrameworkFactory.newClient(IP, time_out, time_out, new RetryNTimes(2, 1000));

        client.start();
        client.getConnectionStateListenable()
                .addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                System.out.println("=========");
                //关掉zk server ：SUSPENDED
                
                System.out.println(connectionState.toString());
                if (connectionState == ConnectionState.LOST) {
                    try {
                        System.out.println("断后重连 .......");
                        if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                            doTask(client);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        
        doTask(client);
        System.in.read();
    }
    
    private static void doTask(CuratorFramework client){
        try {
            byte[] bytes = client.getData().forPath("/zk");
            System.out.println(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
