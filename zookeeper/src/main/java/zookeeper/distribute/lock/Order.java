package zookeeper.distribute.lock;

public class Order {
    
    
    public void order(){
        System.out.println(Thread.currentThread().getName() + " -- 下单 ..");
    }
}
