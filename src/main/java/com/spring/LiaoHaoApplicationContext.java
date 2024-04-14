package com.spring;

import com.liaohao.service.BeanDefinition;
import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.annotation.Scope;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiaoHaoApplicationContext {


    private Class<?> configClass;
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> singletonObjects = new HashMap<>();
    private Map<Class<?>, BeanDefinition> classBeanDefinitionMap = new HashMap<>();
    private List<BeanPostProcessor> beanPostProcessList = new ArrayList<>();


    public LiaoHaoApplicationContext(Class<?> configClass) {
        this.configClass = configClass;

        // 扫描
        scan(configClass);

        // 遍历beanDefinitionMap
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (Scope.Scopes.SINGLETON.equals(beanDefinition.getScope())) {
                Object bean = create(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }


    }

    private Object create(String beanName, BeanDefinition beanDefinition) {
        System.out.println("create bean:" + beanName);
        Class<?> type = beanDefinition.getType();
        Object instance = null;
        try {


            instance = type.getConstructor().newInstance();

            // BeanPostProcess
            for (BeanPostProcessor beanPostProcess : beanPostProcessList) {
                beanPostProcess.postProcessBeforeInitialization(instance, beanName);
            }
            Field[] declaredFields = type.getDeclaredFields();
            // 依赖注入
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getName()));
                }
            }

            // 处理Aware 接口
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            // 判断是否实现了InitializingBean
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }
            // BeanPostProcess
            for (BeanPostProcessor beanPostProcess : beanPostProcessList) {
                instance = beanPostProcess.postProcessAfterInitialization(instance, beanName);
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 1. 得到扫描路径
     * 2. 根据扫描路径遍历所有的class文件
     * 3. 根据class文件得到class对象
     * 4. 判断是否是一个bean
     * 5. 解析scope注解
     * 6. 生成beanDefinition对象
     * 7. 存入map
     *
     * @param configClass
     */
    private void scan(Class<?> configClass) {
        // 扫描
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value().replace(".", "/");
            ClassLoader classLoader = this.getClass().getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());

            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    String absolutePath = f.getAbsolutePath();
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                    System.out.println("扫描class文件" + absolutePath);

                    try {
                        Class<?> aClass = classLoader.loadClass(absolutePath.replace("\\", "."));

                        if (aClass.isAnnotationPresent(Component.class)) {  // 表示是一个bean

                            if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                                BeanPostProcessor beanPostProcess = (BeanPostProcessor) aClass.getConstructor().newInstance();
                                beanPostProcessList.add(beanPostProcess);
                            }

                            Component component = aClass.getAnnotation(Component.class);
                            String beanName = component.value();
                            if (beanName.equals("")) {
                                beanName = Introspector.decapitalize(aClass.getSimpleName()); // 小写
                            }
                            BeanDefinition beanDefinition = getBeanDefinition(aClass);
                            beanDefinitionMap.put(beanName, beanDefinition);
                            classBeanDefinitionMap.put(aClass, beanDefinition);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
    }

    private BeanDefinition getBeanDefinition(Class<?> aClass) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setType(aClass);
        String className = aClass.toString();

        // 判断是否单例
        if (aClass.isAnnotationPresent(Scope.class)) {
            Scope scopeAnnotation = aClass.getAnnotation(Scope.class);
            String value = scopeAnnotation.value();
            beanDefinition.setScope(value);
        } else {
            beanDefinition.setScope(Scope.Scopes.SINGLETON);
        }
        return beanDefinition;
    }


    /**
     * 获取对象
     *
     * @param beanName
     * @return 对象
     */
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        if (beanDefinition == null) { // 为空表示没有这个bean
            throw new NullPointerException();
        }

        String scope = beanDefinition.getScope();

        if (scope.equals(Scope.Scopes.SINGLETON)) {
            Object o = singletonObjects.get(beanName);
            if (o == null) {
                o = create(beanName, beanDefinition);
            }
            return o;
        } else if (scope.equals(Scope.Scopes.PROTOTYPE)) { // 原型
            return create(beanName, beanDefinition);
        }
        return null;
    }

}
