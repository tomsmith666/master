package com.blog.filter.ThreadTest;

import org.junit.jupiter.api.Test;
//多线程

//方法1
//public class TreadTest extends Thread{
//    @Test
//    public  void Test1() {
//        //创建两个线程
//        TreadTest thread1 = new TreadTest();
//        TreadTest thread2 = new TreadTest();
//        thread1.start();
//        thread2.start();
//    }
//    public void run(){
//        for (int i = 0; i <100;i++){
//            System.out.println(Thread.currentThread().getName()+" and i="+i);
//        }
//    }
//}

//方法2
public class TreadTest1 {
    @Test
    public void Test1(){
        TreadTest2 treadTest2 = new TreadTest2();
        new Thread(treadTest2).start();
        new Thread(treadTest2).start();
    }

    static class TreadTest2 implements Runnable{
        @Override
        public void run() {
            for (int i = 0; i < 100; i++){
                System.out.println(Thread.currentThread().getName()+"i="+i);
            }
        }
    }
}
