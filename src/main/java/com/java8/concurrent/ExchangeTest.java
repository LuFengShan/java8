package com.java8.concurrent;

import java.util.concurrent.Exchanger;

/**
 * {@link Exchanger#exchange(Object)}:用于两个线程间的交换对象，Exchange只是等待两个线程，直到两个线程都调用exchange()方法的时候，
 * 两个线程配对并且交换对象
 */
public class ExchangeTest {
    public static void main(String[] args) {
        Exchanger exchanger = new Exchanger();
        new Thread(new HeiNan(exchanger)).start();
        new Thread(new HeiBei(exchanger)).start();
    }
}

/**
 * 河南
 */
class HeiNan implements Runnable {

    Exchanger ex;

    HeiNan(Exchanger ex) {
        this.ex = ex;

    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            Country country = null;
            if (i == 0)
                country = new Country("河南*城A");
            else
                country = new Country("河南*城B");

            try {
                // exchanging with an dummy Country object
                Country exchange = (Country) ex.exchange(country);
                System.out.println("从HeNan线程中交换country对象: " + exchange.getCountryName());
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

/**
 * 河北
 */
class HeiBei implements Runnable {

    Exchanger ex;

    HeiBei(Exchanger ex) {
        this.ex = ex;
    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            Country country = null;
            if (i == 0)
                country = new Country("河北*城A");
            else
                country = new Country("河北*城B");
            try {
                Country exchange = (Country) ex.exchange(country);
                System.out.println("从HeBei线程中交换country对象: " + exchange.getCountryName());
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

/**
 * 城市
 */
class Country {

    /**
     * 城市名字
     */
    private String countryName;

    public Country(String countryName) {
        super();
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }
}
