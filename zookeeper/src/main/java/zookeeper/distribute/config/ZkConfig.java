package zookeeper.distribute.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 配置中心
 * 相关配置放在zk节点上
 */
public class ZkConfig {
    
    public Map<String,String> configCache = new HashMap<>();
    private CuratorFramework client;
    
    private static final String CONFIG_NODE_PREFIX = "/config";


    public ZkConfig() {
        String connectIp = "192.168.195.108:2181";
        client = CuratorFrameworkFactory.newClient(connectIp,new RetryNTimes(2,2));
        System.out.println("client o -> " + client);
        client.start();
        init();
    }
    
    private void init(){
        try {
            List<String> children = client.getChildren().forPath(CONFIG_NODE_PREFIX);
            for (String child : children) {
                byte[] bytes = client.getData().forPath(CONFIG_NODE_PREFIX + "/" + child);
                configCache.put(child,Arrays.toString(bytes));
            }
            
            //在此绑定监听器
            //设为true 可以直接拿到监听节点的内容
            PathChildrenCache watch = new PathChildrenCache(client,CONFIG_NODE_PREFIX,true);
            
            watch.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    System.out.println(client);
                    System.out.println("event -> " + event.toString());

                    String path = event.getData().getPath();
                    System.out.println("path -> " + path);
                    if (path.startsWith(CONFIG_NODE_PREFIX)) {
                        String cacheKey = path.replace(CONFIG_NODE_PREFIX + "/","");
                        System.out.println("配置 " + cacheKey + " 操作 -> " + event.toString());
                        if (PathChildrenCacheEvent.Type.CHILD_ADDED.equals(event.getType()) 
                          || PathChildrenCacheEvent.Type.CHILD_UPDATED.equals(event.getType())){
                            configCache.put(cacheKey,new String(event.getData().getData()));
                        } else if(PathChildrenCacheEvent.Type.CHILD_REMOVED.equals(event.getType())){
                            configCache.remove(cacheKey);
                        }
                    }
                }
            });
            watch.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getConfigValue(String key){
        return configCache.get(key);
    }
    
    public void setConfigCache(String key,String value){
        try {
            String path = client.create().creatingParentsIfNeeded().forPath(CONFIG_NODE_PREFIX + "/" + key, value.getBytes());
            configCache.put(key, value);
            System.out.println("增加配置成功 -> " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
