package dubbospi.interf.warper;

import com.alibaba.dubbo.common.URL;
import dubbospi.interf.CarColor;

public class CarWarper implements CarColor {
    
    private CarColor carColor;

    public CarWarper(CarColor carColor) {
        this.carColor = carColor;
    }

    @Override
    public void printColor(URL url) {
        System.out.println("before .....");
        carColor.printColor(url);
        System.out.println("after .....");
    }
}
