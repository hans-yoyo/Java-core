package com.hyman.javabasis.generics;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/01 15:59
 * @version： 1.0.0
 */
public class Info<T> {

    private T value;

    public Info() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Info{" + "value=" + value + '}';
    }
}
