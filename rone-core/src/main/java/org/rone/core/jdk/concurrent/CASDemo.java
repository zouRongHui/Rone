package org.rone.core.jdk.concurrent;

import java.util.Random;

/**
 * CAS(compare and swap 比较并替换)机制
 * CAS中的原理:
 * 	使用三个操作数：内存地址V、原始值A、预期值B。在更新数据之前，只有当原始值A和实际内存值V相同时，才会将B覆盖到V上，
 * 	如果不同，会将V的值覆盖到A上，并重新计算并判断是否更新数据。
 * 实例：
 * 	初始变量值为10。
 * 	线程1需要将变量+1.此时V:10、A:10、B:11;
 * 	在线程1提交更新数据之前，线程2抢先一步将变量值更新为了11;
 * 	此时，线程1中V:11、A:10、B:11，显然不满足V==A的条件，提交更新失败;
 * 	线程1会重新获取内存中的实际数据并重新计算，此时V:11、A:11、B:12;此过程称为【自旋】;
 * 	线程1提交更新，满足V==A的情况，此时进行swap操作，将V的值更新为B;
 * 伪代码：
 * 	线程1：获取需要计算的数据
 * 	线程1：计算完毕
 * 	线程2：更新了实际内存数据
 * 	线程1：提交更新：
 * 	int v = 实际内存数据;
 * 	int a = 原始数据;
 * 	int b = 预期数据;
 * 	Method cas() {
 * 		if (v==a) {
 * 			v = b;
 *      } else {
 * 			// 重新获取数据并计算
 * 			cas();
 *      }
 *  }
 *
 * Synchronized属于悲观锁，悲观的认为程序中的并发情况很严重，所以严防死守;
 * 而CAS属于乐观锁，认为并发并不严重，所以让程序去不断的尝试更新。
 *
 * 实例：Atomic系列类、Lock系列类
 *
 * 优点：
 * 	相比Synchronized，在部分情况下性能有所提升。
 * 缺点：
 * 	1.CPU开销很大
 * 		在高并发情况下，线程可能会一直处于更新循环状态;
 * 	2.不能保证代码块的原子性
 * 		CAS只适用于单个变量，同时保证多个变量的同步必须使用Synchronized
 * 	3.ABA问题
 * 		线程1、线程2都需要将值从A改为B，线程3需要将B改为A；
 * 			预设流程为线程1执行，线程2执行失败(即不会执行修改操作)，线程3执行，最终得到A值。
 * 			实际情况可能会发生：首先线程1执行，然后线程3执行，最后线程2执行，得到B值。
 * 		例如银行取款操作
 * 			余额100，取款50，存款50；
 * 			取款50元由于某种情况导致产生了两个线程，都执行100 -> 50操作，
 * 			此时有转账进来50，线程3执行50 -> 100操作，产生ABA问题时，会得到50的终值，与实际不符。
 * 		解决方案：引入版本号机制
 * @author rone
 */
public class CASDemo {

    public static void main(String[] args) {
        final int[] i = {0};
        Random random = new Random();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 10; j++) {
                    int count = 0;
                    cas(j, count);
                }
            }

            private void cas(int j, int count) {
                int i1 = i[0];
                int i2 = i1 + 1;
                try {
                    Thread.sleep(1000 * (random.nextInt(3) + 1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i1 == i[0]) {
                    i[0] = i2;
                    System.out.println(String.format("thread1 第%s次执行的第%s次更新，更新成功，此时数据为：%s，更新成功后数据为：%s", j, ++count, i1, i2));
                } else {
                    System.out.println(String.format("thread1 第%s次执行的第%s次更新，需要重试，此时数据为：%s，本地数据为: %s", j, ++count, i[0], i1));
                    cas(j, count);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 10; j++) {
                    int count = 0;
                    cas(j, count);
                }
            }

            private void cas(int j, int count) {
                int i1 = i[0];
                int i2 = i1 + 1;
                try {
                    Thread.sleep(1000 * (random.nextInt(3) + 1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i1 == i[0]) {
                    i[0] = i2;
                    System.out.println(String.format("thread2 第%s次执行的第%s次更新，更新成功，此时数据为：%s，更新成功后数据为：%s", j, ++count, i1, i2));
                } else {
                    System.out.println(String.format("thread2 第%s次执行的第%s次更新，需要重试，此时数据为：%s，本地数据为: %s", j, ++count, i[0], i1));
                    cas(j, count);
                }
            }
        });
        thread1.start();
        thread2.start();

        while (i[0] < 20) {
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("最终数据为" + i[0]);

    }
}
