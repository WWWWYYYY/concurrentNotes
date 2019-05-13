package com.concurrent.jmm;

public class FinalTest {

    private final Object obj;

    public FinalTest() {
        this.obj = new Object();
    }
    private void m(){}

    public static void main(String[] args) {
        FinalTest instance =new FinalTest();
        instance.m();//在使用instance实例调用方法之前必须使得instance的属性obj被完全实例化

    }
}
