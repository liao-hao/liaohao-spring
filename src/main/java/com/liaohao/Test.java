package com.liaohao;

import com.liaohao.service.IUserService;
import com.liaohao.service.OrderService;
import com.spring.LiaoHaoApplicationContext;

public class Test {

    public static void main(String[] args) {
        // 1. 创建容器
        // 扫描->创建Bean
        LiaoHaoApplicationContext applicationContext = new LiaoHaoApplicationContext(AppConfig.class);

        // 从容器中获取对象
        IUserService iuserService = (IUserService) applicationContext.getBean("userService");

        OrderService orderService = (OrderService) applicationContext.getBean("orderService");

        // 使用对象
        iuserService.test();


    }

}
