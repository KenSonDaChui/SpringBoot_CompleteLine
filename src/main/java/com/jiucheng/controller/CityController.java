package com.jiucheng.controller;

import com.jiucheng.service.city.impl.CityServiceImpl;
import com.jiucheng.vo.city;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * CityController
 *
 * @auther qiaoba
 * @date 2020/9/30 0030 14:18
 **/
@Controller
@RequestMapping("/city")
public class CityController {

    @Autowired
    public CityServiceImpl cityService;

    @RequestMapping(value = "/findAllCity.do" , method = RequestMethod.GET)
    public String findAllCity() {
        System.out.println("--------------------");
        List<city> list = cityService.findAll();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        return "hello";
    }

}
