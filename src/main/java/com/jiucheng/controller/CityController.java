package com.jiucheng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * CityController
 *
 * @auther qiaoba
 * @date 2020/9/30 0030 14:18
 **/
@Controller
@RequestMapping("/city")
public class CityController {

    @RequestMapping(value = "/findAllCity.do" , method = RequestMethod.GET)
    public String findAllCity() {
        System.out.println("--------------------");
        return "hello";
    }

}
