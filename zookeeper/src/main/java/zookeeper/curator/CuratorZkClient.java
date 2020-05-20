package zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

public class CuratorZkClient {
    public static void main(String[] args) throws Exception {
        String IP = "192.168.195.108:2181";
        Integer time_out = 20000;
        CuratorFramework client = CuratorFrameworkFactory.newClient(IP, time_out, time_out, new RetryNTimes(2, 1000));
        
        client.start();
        client.create().withMode(CreateMode.EPHEMERAL)
                .forPath("/curator","curator".getBytes());

        
        //实现watch机制
        //为节点添加watcher Curator之nodeCache一次注册，N次监听
        //监听数据节点的变更，会触发事件
        NodeCache nodeCache = new NodeCache(client,"/curator");
        //传true 
        System.out.println("================");
        //如果设置为true,那么NodeCache在第一次启动打的时候就会立刻在Zookeeper
        // 上读取对应节点的数据内容，并保存在Cache中
        // 如果为true则首次不会缓存节点内容到cache中，
        // 默认为false,设置为true首次不会触发监听事件
        nodeCache.start(false);
        nodeCache.getListenable()
                .addListener(() -> {
                    
                    System.out.println("node change .......");
                });

        System.out.println(nodeCache.getCurrentData().toString());
        System.out.println(client.create().orSetData().forPath("/curator", "2".getBytes()));
    }
}
