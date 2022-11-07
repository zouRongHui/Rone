package org.rone.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 阿里巴巴FastJson
 * ●.一个json格式的数据已经转换成字符串形式，后续又对其进行了转换操作，导致后续反转换一次后无法解析成json格式数据。
 *      JSON.toJSONString()对String数据操作后得到的数据会在收尾增加字符串符号 " ，需要留意。
 * ●.AutoType引发的反序列化漏洞。
 *     AutoType是fastJson用来在序列化的时候记录转换的类的类型的，解决了反序列化时无法区分子类和父类、接口和实现类的情况。
 *         AutoType会序列化后的字符串中增加一个 "@type" 字段来记录对象的类型方便在反序列化的时候定位到具体的类型。
 *     该特性会导致反序列攻击，例如 {"@type":"com.sun.rowset.JdbcRowSetImpl","dataSourceName":"rmi://localhost:1099/Exploit","autoCommit":true}
 *         会利用该特性生成一个JdbcRowSetImpl触发远程命令执行。
 *     官方的解决方案为：
 *         1.通过黑名单和白名单来约束。
 *         2.版本1.2.68提供了safeMode ，配置safeMode后，无论白名单和黑名单，都不支持autoType，可一定程度上缓解反序列化Gadgets类变种攻击。
 *             ParserConfig.getGlobalInstance().setSafeMode(true);
 * @author rone
 */
public class FastJsonDemo {

    public static void main(String[] args) {
        newObject();
        object2Json();
        json2Object();
    }

    /**
     * Json转Object
     */
    public static void json2Object() {
        JSONObject userJson = new JSONObject();
        userJson.put("name", "rone");
        userJson.put("sex", "man");
        userJson.put("age", "21");
        userJson.put("job", "programmer");
        User user = JSON.parseObject(userJson.toJSONString(), User.class);
        //User{name='rone', sex='man', age=21, job='programmer', address='null'}
        System.out.println(user);
    }

    /**
     * Object转Json
     */
    public static void object2Json() {
        User user = new User("rone", "man", 21, "programmer", "浙江杭州");
        //这里貌似没有Object转JsonObject的方法，可能原因是设计者认为Object比起JsonObject更为方便所以没必要提供这个方法
        String userJsonString = JSON.toJSONString(user);
        //{"address":"浙江杭州","age":21,"job":"programmer","name":"rone","sex":"man"}
        System.out.println(userJsonString);

    }

    /**
     * 创建一个Json对象
     */
    public static void newObject() {
        //新建一个json对象
        JSONObject json = new JSONObject();
        //添加属性，类似Map
        json.put("name", "rone");
        json.put("sex", "man");
        //转换成String格式， {"sex":"man","name":"rone"}
        System.out.println(json.toJSONString());

        Map<String, Object> map = new HashMap<>(3);
        map.put("first", "hello");
        map.put("second", "the");
        map.put("third", "world");
        //支持已某个Map为引创建
        JSONObject mapJson = new JSONObject(map);
        //{"third":"world","first":"hello","second":"the"}
        System.out.println(mapJson.toJSONString());
    }

    static class User {
        private String name;
        private String sex;
        private Integer age;
        private String job;
        private String address;

        public User() {}

        public User(String name, String sex, Integer age, String job, String address) {
            this.name = name;
            this.sex = sex;
            this.age = age;
            this.job = job;
            this.address = address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            User user = (User) o;
            return Objects.equals(name, user.name) &&
                    Objects.equals(sex, user.sex) &&
                    Objects.equals(age, user.age) &&
                    Objects.equals(job, user.job) &&
                    Objects.equals(address, user.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, sex, age, job, address);
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", age=" + age +
                    ", job='" + job + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
