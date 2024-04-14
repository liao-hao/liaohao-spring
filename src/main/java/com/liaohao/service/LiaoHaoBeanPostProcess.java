package com.liaohao.service;

import com.spring.BeanPostProcessor;
import com.spring.annotation.Component;

import java.lang.reflect.Proxy;

@Component
public class LiaoHaoBeanPostProcess implements BeanPostProcessor {


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {

        // 实现AOP
        if (beanName.equals("userService")) {
            Object proxyInstance = Proxy.newProxyInstance(LiaoHaoBeanPostProcess.class.getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        System.out.println("切面逻辑");
                        return method.invoke(bean, args);
                    });
            return proxyInstance;
        }

        System.out.println("doPostProcessAfterInitialization: " + beanName);
        return bean;
    }
}
