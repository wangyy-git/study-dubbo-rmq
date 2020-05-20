package zookeeper.distribute.config;

import java.util.concurrent.TimeUnit;

public class ConfigMain {

    public static void main(String[] args) throws InterruptedException {
        ZkConfig config = new ZkConfig();

        while (true) {
            System.out.println();
            System.out.println("-----------------");
            config.configCache.forEach((key, value) -> System.out.println(key + "----" + value));
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
