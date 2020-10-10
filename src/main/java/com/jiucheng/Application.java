package com.jiucheng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application
 *
 * @auther qiaoba
 * @date 2020/9/30 0030 13:51
 **/
@SpringBootApplication
//@MapperScan("com.jiucheng.dao")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
