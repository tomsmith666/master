package com.blog.filter.ThreadTest;

import org.junit.jupiter.api.Test;

class ThreadYield extends Thread{


    @Override
    public void run() {
        for (int i = 1; i <= 50; i++) {
            System.out.println("" + this.getName() + "-----" + i);
            // 当i为30时，该线程就会把CPU时间让掉，让其他或者自己的线程执行（也就是谁先抢到谁执行）
            if (i ==30) {
                this.yield();
            }
        }

    }

    @Test
    public void test(){
        {

            ThreadYield yt1 = new ThreadYield();
            ThreadYield yt2 = new ThreadYield();
            yt1.start();
            yt2.start();
        }
    }
}
