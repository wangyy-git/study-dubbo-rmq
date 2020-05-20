package zookeeper.distribute.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 最小的节点获取锁  执行结束完成后需要将此节点删除
 * 后一个节点获取删除节点watcher后继续操作
 */
public class ZkLock {
    
    private ThreadLocal<CuratorFramework> client = new ThreadLocal<>();
    private static String ip = "192.168.195.108:2181";
    private static String LOCK_FIRST_NAME = "/lock";
    
    //最初应用的是String 导致了线程不安全
    private ThreadLocal<String> cuurrentNodeName = new ThreadLocal<>();
    
    public void init(){
        //因为是并发调用所以防止重复连接
        if (client.get() == null){

            client.set(CuratorFrameworkFactory.newClient(ip, new RetryNTimes(2, 2)));
//            client = CuratorFrameworkFactory.builder()
//                    .connectString(ip)
//                    .sessionTimeoutMs(20000)
//                    .retryPolicy(new RetryNTimes(2,2))
//                    .build();

            CuratorFramework zk = client.get();
            zk.start();
            
        }
        
    }

    /**
     * 如果是最小节点，则返回true，可执行后续操作
     * 若不是，则需要监测前一节点
     */
    public boolean tryLock(){
        String childNodeName = LOCK_FIRST_NAME + "/subb_";
        try {
            CuratorFramework zk = client.get();
            String path = zk.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(childNodeName, new byte[0]);
            cuurrentNodeName.set(path);
            System.out.println(Thread.currentThread().getName() 
             + " cuurrentNodeName -> " + path);
            
            List<String> children = zk.getChildren().forPath(LOCK_FIRST_NAME);
            Collections.sort(children);

//            System.out.println(Arrays.toString(children.toArray()));
            String minChild = children.get(0);
            System.out.println(Thread.currentThread().getName() + " minChild --> " + minChild);
            String currentSubName = cuurrentNodeName.get().substring(cuurrentNodeName.get().lastIndexOf("/") + 1);
            System.out.println(Thread.currentThread().getName() + " currentSubName -> " + currentSubName);
            CountDownLatch latch = new CountDownLatch(1);
            
            if (LOCK_FIRST_NAME.concat("/").concat(minChild).equals(cuurrentNodeName.get())) {
                System.out.println(Thread.currentThread().getName() + "获取了锁");
                return true;
            } else {
                //等待前一个节点的删除
                System.out.println(Thread.currentThread().getName() + "阻塞了 ....");
                int index = children.indexOf(currentSubName);
                String preNodeName = children.get(index - 1);
                System.out.println(Thread.currentThread().getName() + " preNodeName -> " + LOCK_FIRST_NAME + "/" + preNodeName);
                
                //这里的目的应该是先暂时将当前线程挂起
                //等到前一节点删除  再进行后续操作
//                zk.checkExists().usingWatcher(new Watcher() {
                zk.getData().usingWatcher(new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        System.out.println(Thread.currentThread().getName() + "  " + event.toString());
                        if (Event.EventType.NodeDeleted.equals(event.getType())){
                            System.out.println("NodeDeleted " + currentSubName);
                            latch.countDown();
                        }
                    }
                }).forPath(LOCK_FIRST_NAME + "/" + preNodeName);
            }
            
            latch.await();
            return true;
            
        } catch (Exception e) {
            System.out.println("try lock failed --> " + cuurrentNodeName);
            e.printStackTrace();
        }

        return false;
    }
    
    public void lock(){
        init();
        if (tryLock()) {
            System.out.println(Thread.currentThread().getName() + "获取锁成功.....");
        } else {
            System.out.println(Thread.currentThread().getName() + "获取锁失败.....");
        }
    }
    
    public void unlock(){
        try {
            System.out.println(Thread.currentThread().getName() + " 删除节点 -> " + cuurrentNodeName.get());
            //应该是电脑性能原因  导致前一个节点已经被删除 而前一个线程还为进行到watcher
            //导致监控报错 找不到对应节点  所以设置了休眠100ms
            Thread.sleep(10);
            client.get().delete().forPath(cuurrentNodeName.get());
            System.out.println(Thread.currentThread().getName() + " 删除成功 " + cuurrentNodeName.get());
            cuurrentNodeName.set(null);
            client.get().close();
        } catch (Exception e) {
            System.out.println("删除节点失败 -> " + cuurrentNodeName);
            e.printStackTrace();
        }
    }
    
}
