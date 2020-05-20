package dubbospi.main;

import dubbospi.interf.CarColor;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) {

        //加载接口的实现类
        ServiceLoader<CarColor> serviceLoaders = ServiceLoader.load(CarColor.class);

        Iterator<CarColor> iterator = serviceLoaders.iterator();
        
        while (iterator.hasNext()) {
            CarColor carColor = iterator.next();
            carColor.printColor();
            System.out.println(carColor.getClass().getName());
        }
    }
}
