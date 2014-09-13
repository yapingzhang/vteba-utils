package com.vteba.test;

import java.util.HashMap;
import java.util.Map;

/**
 * 如果不迭代，是不会发生并发修改异常的。
 * Description: <br>
 * @author  Administrator 
 * @see
 * @since
 */
public class TestMapput {
    private final static Map<Integer, Integer> map = new HashMap<Integer, Integer>();

    public static void main(String[] args) {
        Thread t1 = new Thread(new Thread1());
        t1.start();
        
        Thread t2 = new Thread(new Thread2());
        t2.start();
    }

    public static class Thread1 implements Runnable {

        @Override
        public void run() {
            int i = 0;
            for (;;) {
                map.put(i++, i);
                System.out.println("添加了：" + i);
                if (i > 100) {
                    System.out.println("大于100中断");
                    break;
                }
            }
        }
        
    }
    
    public static class Thread2 implements Runnable {

        @Override
        public void run() {
            for (;;) {
                if (map.size() > 0) {
//                    for (Entry<Integer, Integer> entry : map.entrySet()) {
//                        entry.getValue();
//                    }
                    for (int i = 1; i < 120; i++) {
                        Integer i2 = map.get(i);
                        if (i2 == null) {
                            System.out.println("没有取到值：" + i + "，添加。");
                            map.put(i, i);
                        } else {
                            System.out.println("取到值：" + i);
                        }
                    }
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
    }
}
