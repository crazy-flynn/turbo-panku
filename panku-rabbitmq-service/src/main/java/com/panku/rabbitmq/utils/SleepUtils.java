package com.panku.rabbitmq.utils;

/**
 * @description: 测试用睡眠
 * @author: crazy.coder
 */
public class SleepUtils {

    public static void sleep(int second){
        try {
            Thread.sleep(1000*second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
