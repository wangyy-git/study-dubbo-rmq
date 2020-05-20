package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.RetryNTimes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * client的领导者选举  不同于zk server的leader选举
 * 会在latchPath下面生产对应数量的临时有序节点，对应的有序生成的后缀的最小值作为leader
 * 
 * curator recipes的LeaderLatch给我们提供了leader选举的便利方法，
 * 并提供了LeaderLatchListener供自定义处理
 * 
 * LeaderLatch使用了zk的EPHEMERAL_SEQUENTIAL，节点名会自动带上编号，
 * 默认LOCK_NAME为latch-，另外对于protected的，
 * 会自动添加上PROTECTED_PREFIX(_c_)以及protectedId(UUID)，
 * 因而最后的节点名的格式为PROTECTED_PREFIX+UUID+LOCK_NAME+编号，
 * 类似_c_a749fd26-b739-4510-9e1b-d2974f6dd1d1-latch-0000000045
 * 
 * LeaderLatch使用了ConnectionStateListener对自身节点变化进行相应处理，
 * 取index为0的节点位leader，对于非leader的还对前一个节点添加watcher
 * 针对前一节点删除进行处理，触发checkLeadership操作，
 * 重新检查自身的index是否是在children排在第一位，如果是则更新为leader，
 * 触发相应操作，如果不是则重新watch前面一个节点。
 * 如此一环扣一环的实现显得十分精妙。
 * 
 * 用途：在集群中可能需要知道集群中到底有几台机器
 */
public class LeaderLatchDemo {
    public static void main(String[] args) throws Exception {
        //
        List<CuratorFramework> clients = new ArrayList<>();
        List<LeaderLatch> leaderLatches = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String IP = "192.168.195.108:2181";
            Integer time_out = 20000;
            CuratorFramework client = CuratorFrameworkFactory.newClient(IP, time_out, time_out, new RetryNTimes(2, 1000));
            clients.add(client);
            client.start();

            LeaderLatch leaderLatch = new LeaderLatch(client, "/demo", i + "");
            leaderLatches.add(leaderLatch);
            
            leaderLatch.start();
        }

        System.out.println("11111111111111111111");
        TimeUnit.SECONDS.sleep(15);
        System.out.println("选举结束");
        leaderLatches.forEach(l ->{
            if (l.hasLeadership()) {
                System.out.println(l.getId());
                try {
                    System.out.println(l.getLeader().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            
        });
        
        System.in.read();
        for (CuratorFramework f:clients) {
            f.close();
        }

        
    }
}
