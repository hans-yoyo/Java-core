package com.hyman.advance.jdk8;

import java.util.ArrayList;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/07/16 20:18
 * @version： 1.0.0
 */
public class MethodReferenceTest {

    public static void main(String[] args) {
        ArrayList<Car> cars = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Car car = Car.create(Car::new);
            cars.add(car);
        }
        cars.forEach(Car::showCar);
    }

    @java.lang.FunctionalInterface
    interface Factory<T> {
        T create();
    }

    static class Car {
        public void showCar() {
            System.out.println(this.toString());
        }

        public static Car create(Factory<Car> factory) {
            return factory.create();
        }
    }

}
