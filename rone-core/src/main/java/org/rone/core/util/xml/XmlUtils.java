package org.rone.core.util.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

import java.util.HashMap;
import java.util.Map;

/**
 * XML的工具，使用XStream实现java对象与xml格式相互转换
 * @author rone
 */
public class XmlUtils {

    private XmlUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * XStream对象的缓存
     */
    private static Map<Class<?>, XStream> xStreamMap = new HashMap<>();

    /**
     * 获取XStream对象
     * @param clazz java对象类型
     * @return
     */
    public static XStream getXStream(Class<?> clazz) {
        if (xStreamMap.containsKey(clazz)) {
            return xStreamMap.get(clazz);
        }

        XStream xStream = new XStream(new Xpp3Driver(new NoNameCoder()));
        // 去解析类上的XStream注解，主要是获取生成的xml中节点的名称
        xStream.processAnnotations(clazz);
        // 设置允许解析的java对象类型
        xStream.allowTypes(new Class[]{clazz});

        xStreamMap.put(clazz, xStream);
        return xStream;
    }

    /**
     * 将java对象转换成xml格式的字符串
     * @param clazz java对象类型
     * @param bean  java对象实例
     * @param <T>
     * @return      xml格式的字符串
     */
    public static <T> String bean2Xml(Class<T> clazz, T bean) {
        return getXStream(clazz).toXML(bean);
    }

    /**
     * 将xml格式的字符串转换成java对象
     * @param clazz     要换成的java对象类型
     * @param xmlStr    xml字符串
     * @param <T>
     * @return      java对象
     */
    public static <T> T xml2Bean(Class<T> clazz, String xmlStr) {
        return (T) getXStream(clazz).fromXML(xmlStr);
    }

}
