package com.blog.filter.ThreadTest;
/**
 *描述
 *  某电影院正在上映某大片，共5张票，
 * 而他又有3个售票窗口售票，请设计一个程序模拟该电影院售票。
 * @author cy
 * @date 2021-06-09
 * @return
 **/
//使用继承Thread类实现卖票，导致每个窗口都卖了五张票.
// 而这个电影院总共才五张票，多线程出现了超卖现象。原因是继承Thread方式,是多线程多实例，无法实现资源的共享。
public class TicketTreadTest extends  Thread{
    //设置票数
    private int ticket = 5;
    public static void main(String[] args) {
        //创建两个线程
        TicketTreadTest thread1
                = new TicketTreadTest();
        TicketTreadTest thread2
                = new TicketTreadTest();
        TicketTreadTest thread3
                = new TicketTreadTest();
        thread1.start();
        thread2.start();
        thread3.start();
        System.out.println(thread1.getState());
    }
    @Override
    public void run() {
        while(true){
            //分别打印线程名称 和 ticket 数
            System.out.println("threadName:"
                            +Thread.currentThread()+"；ticket="+ticket--);
            if(ticket < 0){
                break;
            }
        }
    }
}
