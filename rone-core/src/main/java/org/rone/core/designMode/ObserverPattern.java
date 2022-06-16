package org.rone.core.designMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式
 * Java已经提供了对观察者模式的支持
 * ●.定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。
 * ●.使用场景：一个对象的变动触发其他一系列对象的变动。
 * 例如：游戏中角色的移动触发怪物的攻击，陷阱的发动
 *
 * @author rone
 */
public class ObserverPattern {

    /**
     * 示例为游戏中人物移动触发事情
     */
    public static void main(String[] args) {
        //初始化对象
        Hero hero = new Hero();
        Monster monster = new Monster();
        Trap trap = new Trap();
        Treasure treasure = new Treasure();
        //注册观察者
        hero.attachObserver(monster);
        hero.attachObserver(trap);
        hero.attachObserver(treasure);
        //移动事件
        hero.move();
        //删除观察者
        hero.detachObserver(monster);
        hero.detachObserver(trap);
        hero.detachObserver(treasure);
    }

    /**
     * 观察者
     * Java已经提供了对观察者模式的支持 java.util.Observer
     */
    interface Observer {
        void update();
    }

    /**
     * 怪物观察者
     */
    static class Monster implements Observer {

        @Override
        public void update() {
            if (inRange()) {
                System.out.println("怪物 对主角攻击！");
            }
        }

        private boolean inRange() {
            //判断主角是否在自己的影响范围内，这里忽略细节，直接返回true
            return true;
        }
    }

    /**
     * 陷阱观察者
     */
    static class Trap implements Observer {

        @Override
        public void update() {
            if (inRange()) {
                System.out.println("陷阱 困住主角！");
            }
        }

        private boolean inRange() {
            //判断主角是否在自己的影响范围内，这里忽略细节，直接返回true
            return true;
        }
    }

    /**
     * 宝物观察者
     */
    static class Treasure implements Observer {

        @Override
        public void update() {
            if (inRange()) {
                System.out.println("宝物 为主角加血！");
            }
        }

        private boolean inRange() {
            //判断主角是否在自己的影响范围内，这里忽略细节，直接返回true
            return true;
        }
    }

    /**
     * 被观察者
     * Java已经提供了对观察者模式的支持 java.util.Observable
     */
    static abstract class Subject {

        private final List<Observer> observerList = new ArrayList<>();

        public void attachObserver(Observer observer) {
            observerList.add(observer);
        }

        public void detachObserver(Observer observer) {
            observerList.remove(observer);
        }

        public void notifyObservers() {
            for (Observer observer : observerList) {
                observer.update();
            }
        }
    }

    /**
     * 游戏主角被观察者
     */
    static class Hero extends Subject {
        void move() {
            System.out.println("主角向前移动");
            notifyObservers();
        }
    }
}
