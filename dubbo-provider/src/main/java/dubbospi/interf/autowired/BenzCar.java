package dubbospi.interf.autowired;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.factory.SpiExtensionFactory;
import dubbospi.interf.CarColor;

public class BenzCar implements CarColor {
    
    private CarColor carColor;

    
    /**
     * 自动注入点
     * SPI注入过程：
     * 通过SpiExtensionFactory获取CarColor的Adaptive类，所以注入进来的
     * 对象其实是一个Adaptive类对象，代理对象
     */
    public void setCarColor(CarColor carColor) {
        this.carColor = carColor;
    }
    
    public CarColor getCarColor() {
        return carColor;
    }

    @Override
    public void printColor(URL url) {
        System.out.println("this is a benz");
        carColor.printColor(url);
    }
}
