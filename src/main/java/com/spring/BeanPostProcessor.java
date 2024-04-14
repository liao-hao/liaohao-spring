package com.spring;

import com.spring.annotation.LiaoHaoValue;

import java.lang.reflect.Field;

/**
 * 初始化前, 初始化后
 */
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {

        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(LiaoHaoValue.class)) {
                field.setAccessible(true);
                field.set(bean, field.getAnnotation(LiaoHaoValue.class).value());
            }
        }


        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }


}
