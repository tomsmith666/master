package com.blog.filter.ThreadTest;

public class TicketRunnableDemoTest {
    //设置票数
    private  static int ticket = 5;

    public static void main(String[] args) {
        //新建两个线程
        RunnableDemoTest1 r
                =  new RunnableDemoTest1();
        new Thread(r).start();
        new  Thread(r).start();
    }

    static class  RunnableDemoTest1
            implements  Runnable{
        @Override
        public void run() {
            while (ticket > 0){
                saleTicket();
            }
        }

        public synchronized   void   saleTicket(){
            if(ticket>0){
                System.out.println("threadName:"
                        +Thread.currentThread()
                        + "正在出票，余票剩余:"
                        + +ticket-- +"张");

            }
        }
    }
}

