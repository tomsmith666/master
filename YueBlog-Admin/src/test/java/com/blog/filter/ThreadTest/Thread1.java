package com.blog.filter.ThreadTest;

import org.junit.jupiter.api.Test;

class Thread1 implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "运行  :  " + i);
        }
    }
    @Test
    public void test(){
        new Thread(new Thread1()).start();
        new Thread(new Thread1()).start();
    }
}
