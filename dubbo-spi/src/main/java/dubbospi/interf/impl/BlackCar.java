package dubbospi.interf.impl;

import dubbospi.interf.CarColor;

public class BlackCar implements CarColor {
    @Override
    public void printColor() {
        System.out.println("this is a black car");
    }
}
