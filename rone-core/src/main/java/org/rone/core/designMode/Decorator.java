package org.rone.core.designMode;

/**
 * 装饰器模式
 * ●.向一个现有的对象添加新的功能，同时又不改变其结构。遵循开闭原则(对扩展开放，对修改关闭)
 *     一般的，我们为了扩展一个类经常使用继承方式实现，由于继承为类引入静态特征，并且随着扩展功能的增多，子类会很膨胀。
 *     在不想增加很多子类的情况下扩展类。
 *     设计理念为优先使用对象组合而不是继承。
 * ●.使用场景： 1、扩展一个类的功能。 2、动态增加功能，动态撤销。
 * ●.优点：装饰类和被装饰类可以独立发展，不会相互耦合，装饰模式是继承的一个替代模式，装饰模式可以动态扩展一个实现类的功能。
 *     缺点：多层装饰比较复杂。
 * @author rone
 */
public class Decorator {

    public static void main(String[] args) {
        Shape circle = new Circle();

        Shape redCircle = new Circle();
        redCircle = new RedShapeDecorator(redCircle);

        Shape redRectangle = new Rectangle();
        redRectangle = new RedShapeDecorator(redRectangle);

        System.out.println("Circle with normal border");
        circle.draw();

        System.out.println("\nCircle of red border");
        redCircle.draw();

        System.out.println("\nRectangle of red border");
        redRectangle.draw();
    }

    /**
     * 实体装饰类
     */
    private static class RedShapeDecorator extends ShapeDecorator {

        public RedShapeDecorator(Shape decoratedShape) {
            super(decoratedShape);
        }

        @Override
        public void draw() {
            decoratedShape.draw();
            setRedBorder();
        }

        private void setRedBorder(){
            System.out.println("Border Color: Red");
        }
    }

    /**
     * 抽象装饰类
     */
    private static abstract class ShapeDecorator implements Shape {
        protected Shape decoratedShape;

        public ShapeDecorator(Shape decoratedShape){
            this.decoratedShape = decoratedShape;
        }

        @Override
        public void draw(){
            decoratedShape.draw();
        }
    }

    private static class Circle implements Shape {
        @Override
        public void draw() {
            System.out.println("Shape: Circle");
        }
    }

    private static class Rectangle implements Shape {
        @Override
        public void draw() {
            System.out.println("Shape: Rectangle");
        }
    }

    private interface Shape {
        void draw();
    }
}
