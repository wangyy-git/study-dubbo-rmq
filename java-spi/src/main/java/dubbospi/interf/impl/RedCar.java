package dubbospi.interf.impl;

import dubbospi.interf.CarColor;

public class RedCar implements CarColor {
    @Override
    public void printColor() {
        System.out.println("this is a red car");
    }
}
