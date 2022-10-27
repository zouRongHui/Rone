package org.rone.core.util.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.rone.core.util.hibernate.entity.Employee;
import org.rone.core.util.hibernate.entity.News;
import org.rone.core.util.hibernate.entity.People;

import java.sql.Date;
import java.util.List;

/**
 * @author rone
 */
public class HibernateDemo {

    public static void main(String[] args) {
        SessionFactory sessionFactory;
        Session session;
        Transaction transaction;

        //1.创建SessionFactory
        //不指定文件名默认是找hibernate.cfg.xml文件
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate/hibernate.cfg.xml").build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        //2.创建Session
        session = sessionFactory.openSession();
        //3.开启事务
        transaction = session.beginTransaction();
        //4.执行操作
        testSave(session);
        // testGet(session);
        // testUpdate(session);
        // testSaveOrUpdate(session);
        // testMerge(session);
        // testDelete(session);
        // testEvict(session);
        // testHQL(session);
        // testPageQuery(session);
        // testFieldQuery(session);
        // testAnnotation(session);
        // testC3P0(session);
        // testTwoTables(session);
        // testParentAndChild(session);
        //5.提交事务
        //首先会调用flush方法，根据session缓存中的数据更新数据表中的记录(可能是insert、delete或update)
        transaction.commit();
        //6.关闭Session
        session.close();
        //7.关闭SessionFactory
        sessionFactory.close();
    }

    public static void testSave(Session session) {
        News news = new News("R", "Rone", new Date(new java.util.Date().getTime()));
        System.out.println(session.save(news));
    }

    public static void testGet(Session session) {
        News n = session.get(News.class, 1);
        System.out.println(n);
    }

    public static void testUpdate(Session session) {
        News n = session.get(News.class, 1);
        n.setAuthor("TR");
        session.update(n);
    }

    public static void testSaveOrUpdate(Session session) {
        News n = new News("R", "Jason", new Date(new java.util.Date().getTime()));
        session.saveOrUpdate(n);
    }

    public static void testMerge(Session session) {
        News n = new News("R", "Jason", new Date(new java.util.Date().getTime()));
        n.setId(50);
        // News n = session.get(News.class, 5);
        n.setTitle("C++");
        News ne = (News) session.merge(n);
        System.out.println(ne);
    }

    public static void testDelete(Session session) {
        News n = new News();
        n.setId(1);
        // News n = (News) session.get(News.class, 8);
        session.delete(n);
        System.out.println(n);
    }

    /**
     * 将一个持久化对象从session缓存中删除，变成游离态
     * @param session
     */
    public static void testEvict(Session session) {
        News n1 = session.get(News.class, 1);
        News n2 = session.get(News.class, 2);
        n1.setAuthor("Rone");
        n2.setAuthor("Rone");
        session.evict(n1);
    }

    public static void testHQL(Session session) {
        //1. 创建Query实例
        // String hql = "from News n where n.id > ?";
        String hql = "from News n where n.id > :id";
        Query<News> query = session.createQuery(hql);
        //2. 绑定参数
        // query.setParameter(0, 0);
        query.setParameter("id", 0);

        //3. 执行查询
        List<News> listNews = query.list();
        for(News n : listNews) {
            System.out.println(n);
        }
    }

    public static void testPageQuery(Session session) {
        //分页检索
        String hql = "from News ";
        Query<News> query = session.createQuery(hql);
        int pageSize = 4;
        int pages = 1;
        //设置该页的首项
        query.setFirstResult((pages - 1) * pageSize);
        //设置每页的数据量
        query.setMaxResults(pageSize);
        List<News> listNews = query.list();
        listNews.forEach(news -> System.out.println(news));
    }

    public static void testFieldQuery(Session session) {
        //投影检索，查询结果仅包含部分属性
        String hql = "select n.id, n.title from News n";
        Query query = session.createQuery(hql);
        List<Object[]> list = query.list();
        for (Object[] obj : list) {
            for (Object o : obj) {
                System.out.print(String.format("数据：%s ", o));
            }
            System.out.println();
        }
        //需要在POJO中创建一个部分属性的构造器
        String hql2 = "select new News(n.id, n.title) from News n";
        Query<News> query2 = session.createQuery(hql2);
        List<News> list2 = query2.list();
        for (News n : list2) {
            System.out.println(n);
        }
    }

    /**
     * 使用@注解的实体类
     * @param session
     */
    public static void testAnnotation(Session session) {
        Employee emp = new Employee("邹荣辉", "rone@rone.com");
        session.save(emp);
    }

    public static void testC3P0(Session session) {
        session.doWork(arg0 -> System.out.println(arg0));
    }

    /**
     * 多表的级联查询
     */
    public static void testTwoTables(Session session) {
        //left join有些情况下会报错 Path expected for join! ，原因不清楚，在hibernate5.0版本中有出错过
        // String hql = "select n from News n left join Employee e on n.id = e.id where e.name='邹荣辉'";
        String hql = "select n from News n, Employee e where n.id = e.id and e.name='邹荣辉'";
        javax.persistence.Query query = session.createQuery(hql);
        List<News> objs = query.getResultList();
        objs.forEach(obj -> System.out.println(obj));
    }

    /**
     * 自关联
     * @param session
     */
    public static void testParentAndChild(Session session) {
        // People parent = new People("parent");
        // session.save(parent);
        // People child = new People("child");
        // child.setFather(parent);
        // session.save(child);

        People parent = session.get(People.class, 1);
        // People{id=1, name='parent', father=null, childs=[People{id=2, name='child', father=parent, childs=[]}]}
        System.out.println(parent);
        People child = session.get(People.class, 2);
        // People{id=2, name='child', father=parent, childs=[]}
        System.out.println(child);
    }
}
