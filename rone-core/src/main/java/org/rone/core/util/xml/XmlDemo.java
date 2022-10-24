package org.rone.core.util.xml;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

import java.util.ArrayList;
import java.util.List;

/**
 * XML工具使用示例
 * @author rone
 */
public class XmlDemo {

    public static void main(String[] args) {
        demo();
        hasParentClass();
        parseUnderLine();
    }

    private static void demo() {
        Data data = new Data();
        data.setAccount("RONE001");
        data.setName("中国");
        data.setPassWord("123456");
        List<Address> addressList = new ArrayList<>(3);
        addressList.add(new Address("杭州"));
        addressList.add(new Address("苏州"));
        addressList.add(new Address("上海"));
        data.setAddressList(addressList);
        List<String> labelList = new ArrayList<>();
        labelList.add("90后");
        labelList.add("IT");
        labelList.add("贫穷");
        data.setLabelList(labelList);
        String xmlStr = XmlUtils.bean2Xml(Data.class, data);
        System.out.println(xmlStr);
        System.out.println(XmlUtils.xml2Bean(Data.class, xmlStr));
    }

    /**
     * 当解析的类有父类的话，父类的@XStreamAlias("XXX")标注的节点名不能与子类的标注的节点名一致，XStream会优先解析父类如果同名的话会解析成父类。
     * 如果一定要同名的话，需要在子类的对应的XStream对象配置  XStream.addDefaultImplementation(子类.class, 父类.class);
     */
    private static void hasParentClass() {
        System.out.println();
        System.out.println("*********** 解析类有父类的情况 start *************");
        // 父类的 @XStreamAlias("Parent") 与子类的不同名 @XStreamAlias("Boy") 没问题
        Boy boy = new Boy();
        boy.setSurname("Hello");
        boy.setName("boy");
        String boyXmlStr = XmlUtils.bean2Xml(Boy.class, boy);
        System.out.println(boyXmlStr);
        System.out.println(JSON.toJSONString(XmlUtils.xml2Bean(Boy.class, boyXmlStr)));
        // 父类的 @XStreamAlias("Parent") 与子类的同名 @XStreamAlias("Parent") 会抛出异常
        Girl girl = new Girl();
        girl.setSurname("Hello");
        girl.setName("girl");
        String girlXmlStr = XmlUtils.bean2Xml(Girl.class, girl);
        System.out.println(girlXmlStr);
        try {
            System.out.println(JSON.toJSONString(XmlUtils.xml2Bean(Girl.class, girlXmlStr)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // 同名的情况下课通过如下的配置解决
        XStream girlXStream = XmlUtils.getXStream(Girl.class);
        girlXStream.addDefaultImplementation(Girl.class, Parent.class);
        System.out.println(JSON.toJSONString(XmlUtils.xml2Bean(Girl.class, girlXmlStr)));
        System.out.println("*********** 解析类有父类的情况 end ***************");
    }

    /**
     * 解析下划线，默认的 XStream 构造器会将 _ 解析成两个相连的 __
     * 此时 XStream 需要用 new XStream(new Xpp3Driver(new NoNameCoder())) 来实例化
     */
    private static void parseUnderLine() {
        System.out.println();
        System.out.println("*********** @XStreamAlias()中有下划线 start ***************");
        UnderLine underLine = new UnderLine("节点带下划线");
        XStream xStream = new XStream();
        xStream.processAnnotations(UnderLine.class);
        xStream.allowTypes(new Class[]{UnderLine.class});
        System.out.println(xStream.toXML(underLine));
        XStream xStreamUnderLine = new XStream(new Xpp3Driver(new NoNameCoder()));
        xStreamUnderLine.processAnnotations(UnderLine.class);
        xStreamUnderLine.allowTypes(new Class[]{UnderLine.class});
        System.out.println(xStreamUnderLine.toXML(underLine));
        System.out.println("*********** @XStreamAlias()中有下划线 end ***************");
    }

    /**
     * @XStreamAlias("DATA")    指定xml中节点的名称
     * 在类上没有该注解时，xml中节点名称为类的全量名
     */
    @XStreamAlias("DATA")
    public static class Data{
        @XStreamAlias("ACCOUNT")
        private String account;
        @XStreamAlias("NAME")
        private String name;
        /**
         * 没有 @XStreamAlias("NAME") 注解的xml中直接使用变量名作为xml节点名称
         */
        private String passWord;

        private List<Address> addressList;

        /**
         * 当属性为List且类型为String时，xml的格式为 <LABEL><string>90后</string><string>IT</string><string>贫穷</string></LABEL>
         */
        @XStreamAlias("LABEL")
        private List<String> labelList;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public List<Address> getAddressList() {
            return addressList;
        }

        public void setAddressList(List<Address> addressList) {
            this.addressList = addressList;
        }

        public List<String> getLabelList() {
            return labelList;
        }

        public void setLabelList(List<String> labelList) {
            this.labelList = labelList;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "account='" + account + '\'' +
                    ", name='" + name + '\'' +
                    ", passWord='" + passWord + '\'' +
                    ", addressList=" + addressList +
                    ", labelList=" + labelList +
                    '}';
        }
    }

    @XStreamAlias("ADDRESS")
    public static class Address{
        private String name;

        public Address() {
        }

        public Address(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    @XStreamAlias("Parent")
    public static class Parent {
        private String surname;

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }
    }

    @XStreamAlias("Boy")
    public static class Boy extends Parent {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @XStreamAlias("Parent")
    public static class Girl extends Parent {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @XStreamAlias("under_line")
    static class UnderLine {
        private String name;

        public UnderLine(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
