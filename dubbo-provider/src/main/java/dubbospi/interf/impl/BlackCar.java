package dubbospi.interf.impl;

import com.alibaba.dubbo.common.URL;
import dubbospi.interf.CarColor;

public class BlackCar implements CarColor {
    @Override
    public void printColor(URL url) {
        System.out.println(url.toString());
        System.out.println("this is a black car");
    }
}
