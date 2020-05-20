package dubbospi.interf.warper;

import com.alibaba.dubbo.common.URL;
import dubbospi.interf.CarColor;

public class CarWarper2 implements CarColor {
    
    private CarColor carColor;

    public CarWarper2(CarColor carColor) {
        this.carColor = carColor;
    }

    @Override
    public void printColor(URL url) {
        System.out.println("before2 .....");
        carColor.printColor(url);
        System.out.println("after2 .....");
    }
}
