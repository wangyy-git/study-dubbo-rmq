package zookeeper.distribute.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserThread implements Runnable{

//    private static Lock lock = new ReentrantLock();
    
    private static ZkLock lock = new ZkLock();
    @Override
    public void run() {
        new Order().order();

//        lock.init();
//        lock.tryLock();
        lock.lock();
        boolean isReduce = new Stock().reduceStock();
        lock.unlock();

        if (isReduce) {
            new Pay().pay();
        } else {
            System.out.println(Thread.currentThread().getName() + "购买下单失败");
        }

    }
}
