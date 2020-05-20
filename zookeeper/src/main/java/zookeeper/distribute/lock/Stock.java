package zookeeper.distribute.lock;

/**
 * 库存
 */
public class Stock {
    
    //简化库存记录
    private static Integer STOCK = 2;
    
    public boolean reduceStock(){
        if (STOCK > 0) {
            System.out.println(Thread.currentThread().getName() + " 库存减成功...");
            STOCK--;
            return true;
        }
        System.out.println(Thread.currentThread().getName() + " 库存减失败...");
        return false;
        
    }
}
