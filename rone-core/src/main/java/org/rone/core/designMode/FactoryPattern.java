package org.rone.core.designMode;

import java.util.Objects;

/**
 * 工厂模式
 * ●.工厂模式是一个统称，具体可细分为 简单工厂模式、工厂方法模式、抽象工厂模式。
 * ●.简单工厂模式
 *     简单工厂模式有唯一的工厂类，工厂类的创建方法根据传入的参数做if-else条件判断，决定最终创建什么样的产品对象。
 * ●.工厂方法模式
 *     工厂方法模式由多个工厂类实现工厂接口，利用多态来创建不同的产品对象，从而避免了冗长的if-else条件判断。
 * ●.抽象工厂模式
 *     抽象工厂模式把产品子类进行分组，同组中的不同产品由同一个工厂子类的不同方法负责创建，从而减少了工厂子类的数量。
 * @author rone
 */
public class FactoryPattern {

    public static void main(String[] args) {
        System.out.println("简单工厂模式:");
        Objects.requireNonNull(SimpleFactory.createClothes("ShortSleeve")).wearClothes();
        Objects.requireNonNull(SimpleFactory.createClothes("DownJacket")).wearClothes();

        System.out.println("工厂方法模式:");
        FactoryMethod shortSleeveFactory = new ShortSleeveFactory();
        shortSleeveFactory.createClothes().wearClothes();
        FactoryMethod downJacketFactory = new DownJacketFactory();
        downJacketFactory.createClothes().wearClothes();

        System.out.println("抽象工厂模式:");
        AbstractFactory summerFactory = new SummerFactory();
        AbstractFactory winterFactory = new WinterFactory();
        summerFactory.gainClothes().wearClothes();
        summerFactory.gainTrousers().wearTrousers();
        winterFactory.gainClothes().wearClothes();
        winterFactory.gainTrousers().wearTrousers();
    }

    /**
     * 简单工厂模式
     *  通过参数的不同来实例化不同的对象
     * 缺点：一旦新增一个类别需要改动原来的代码，不符合开闭原则
     */
    private static class SimpleFactory {
        public static Clothes createClothes(String type) {
            if ("ShortSleeve".equals(type)) {
                return new ShortSleeve();
            } else if ("DownJacket".equals(type)) {
                return new DownJacket();
            } else {
                return null;
            }
        }
    }


    /**
     * 工厂方法模式
     *  一个产品对应一个工厂。
     * 缺点：工厂太多
     */
    private interface FactoryMethod {
        Clothes createClothes();
    }

    /**
     * 短袖生产工厂
     */
    private static class ShortSleeveFactory implements FactoryMethod {
        @Override
        public Clothes createClothes() {
            return new ShortSleeve();
        }
    }

    /**
     * 羽绒服生产工厂
     */
    private static class DownJacketFactory implements FactoryMethod {
        @Override
        public Clothes createClothes() {
            return new DownJacket();
        }
    }


    /**
     * 抽象工厂模式
     *  将产品进行分组，同组产品一个工厂。
     */
    private interface AbstractFactory {
        Clothes gainClothes();
        Trousers gainTrousers();
    }

    /**
     * 夏装工厂，只生产短袖和短裤
     */
    private static class SummerFactory implements AbstractFactory {
        @Override
        public Clothes gainClothes() {
            return new ShortSleeve();
        }
        @Override
        public Trousers gainTrousers() {
            return new Shorts();
        }
    }

    /**
     * 冬装工厂，只生产羽绒服和保暖裤
     */
    private static class WinterFactory implements AbstractFactory {
        @Override
        public Clothes gainClothes() {
            return new DownJacket();
        }
        @Override
        public Trousers gainTrousers() {
            return new WarmPants();
        }
    }


    /**
     * 上衣
     */
    private interface Clothes {
        void wearClothes();
    }

    /**
     * 裤子
     */
    private interface Trousers {
        void wearTrousers();
    }

    /**
     * 短袖
     */
    private static class ShortSleeve implements Clothes {
        @Override
        public void wearClothes() {
            System.out.println("this is short sleeve.");
        }
    }

    /**
     * 羽绒服
     */
    private static class DownJacket implements Clothes {
        @Override
        public void wearClothes() {
            System.out.println("this is down jacket.");
        }
    }

    /**
     * 短裤
     */
    private static class Shorts implements Trousers {
        @Override
        public void wearTrousers() {
            System.out.println("this is shorts.");
        }
    }

    /**
     * 保暖裤
     */
    private static class WarmPants implements Trousers {
        @Override
        public void wearTrousers() {
            System.out.println("this is warm pants.");
        }
    }
}
