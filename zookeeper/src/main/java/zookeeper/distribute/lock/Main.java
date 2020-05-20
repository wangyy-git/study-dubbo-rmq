package zookeeper.distribute.lock;


public class Main {
    public static void main(String[] args) {
        
        Thread thread1 = new Thread(new UserThread(),"A");
        Thread thread2 = new Thread(new UserThread(),"B");
        Thread thread3 = new Thread(new UserThread(),"C");
        
        thread1.start();
        thread2.start();
        thread3.start();
        
    }
    
    
}
