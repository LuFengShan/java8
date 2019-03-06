package com.java8.concurrent;

import java.util.concurrent.Exchanger;

public class ExchangeTest {
    public static void main(String[] args) {
        Exchanger exchanger = new Exchanger();
        new Thread(new Producer(exchanger)).start();
        new Thread(new ConsumerExchangeTest(exchanger)).start();
    }
}

/**
 * 河南
 */
class Producer implements Runnable {

    Exchanger ex;

    Producer(Exchanger ex) {
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
                System.out.println("从Consumer线程获得country对象: " + exchange.getCountryName());
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

/**
 * 河北
 */
class ConsumerExchangeTest implements Runnable {

    Exchanger ex;

    ConsumerExchangeTest(Exchanger ex) {
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
                System.out.println("从Producer线程获得country对象: " + exchange.getCountryName());
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
