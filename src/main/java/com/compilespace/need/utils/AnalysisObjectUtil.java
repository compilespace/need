package com.compilespace.need.utils;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈解析复杂对象工具类,比如List<Object>〉
 *
 * @author Admin
 * @since 1.0.0
 */
public class AnalysisObjectUtil {
    /**
     * @param source 需要处理的复杂对象
     * @param strArr 复杂对象中的get属性,也就是它对应的变量
     * @return Map<String   ,       Object> 将复杂对象从新封装成Map返回
     */
    public static Map<String, Object> manageObjectProperty(Object source, String[] strArr) {
        Map<String, Object> map = new HashMap<>();
        if (source == null)
            return null;
        if (strArr == null || strArr.length < 1) {
            return null;
        }
        for (String str : strArr) {
            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(
                    source.getClass(), str);
            if (sourcePd != null && sourcePd.getReadMethod() != null) {
                try {
                    Method readMethod = sourcePd.getReadMethod();
                    if (!Modifier.isPublic(readMethod.getDeclaringClass()
                            .getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source, new Object[0]);
                    //如果是时间,就格式化一下:
                    if (value instanceof Date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        value = sdf.format(value);
                    }
                    //map装值
                    map.put(str, value);
                } catch (Exception ex) {
                    throw new RuntimeException(
                            "Could not copy properties from source to target",
                            ex);
                }
            }
        }
        return map;
    }
}