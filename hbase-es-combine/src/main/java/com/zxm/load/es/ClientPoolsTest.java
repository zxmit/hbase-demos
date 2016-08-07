package com.zxm.load.es;

import org.elasticsearch.client.transport.TransportClient;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by zxm on 7/28/16.
 */
public class ClientPoolsTest implements Runnable{

    public static void main(String[] args) {
//        Thread t1 = new Thread(new ClientPoolsTest());
//        Thread t2 = new Thread(new ClientPoolsTest());
        for(int i=0; i<100; i++) {
            Thread t1 = new Thread(new ClientPoolsTest());
            t1.start();
        }
//        Thread t3 = new Thread(new ClientPoolsTest());
//        t1.start();
//        t2.start();
//        t3.start();
    }

    private static ESClientPools pools;
    private static Random random = new Random();

    static {
        pools = new ESClientPools(10);
    }

    public void run() {
        TransportClient client = pools.borrow();
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pools.giveBack(client);
    }
}
