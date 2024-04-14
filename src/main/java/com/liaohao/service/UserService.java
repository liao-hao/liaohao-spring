package com.liaohao.service;

import com.spring.BeanNameAware;
import com.spring.InitializingBean;
import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.LiaoHaoValue;
import com.spring.annotation.Scope;

@Component("userService")
@Scope("singleton")
public class UserService implements InitializingBean, IUserService, BeanNameAware {

    @Autowired
    private OrderService orderService;

    @LiaoHaoValue("xxx")
    private String test;

    private String beanName;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("userService afterPropertiesSet 初始化");
    }

    @Override
    public void test(){
        System.out.println("tests userService");
        orderService.test();
        System.out.println("test = " + test);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
