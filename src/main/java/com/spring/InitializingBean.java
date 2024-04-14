package com.spring;

/**
 * 初始化
 */
public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
