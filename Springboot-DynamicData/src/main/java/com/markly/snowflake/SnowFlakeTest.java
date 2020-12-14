package com.markly.snowflake;

/**
 * @Author: qingshan
 * @Description: 咕泡学院，只为更好的你
 */
public class SnowFlakeTest {
    public static void main(String[] args) {
        // 构造方法设置机器码：第2个机房的第6台机器
        SnowFlake  snowFlake = new SnowFlake(2, 6);
        for(int i =0; i < 100; i++){
            System.out.println(snowFlake.nextId());
        }
    }
}
