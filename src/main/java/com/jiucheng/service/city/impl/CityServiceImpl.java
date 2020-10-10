package com.jiucheng.service.city.impl;

import com.jiucheng.dao.Icity;
import com.jiucheng.service.city.cityService;

import com.jiucheng.vo.city;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * studentServiceImpl
 *
 * @auther qiaoba
 * @date 2020/10/9 0009 15:10
 **/
@Service
public class CityServiceImpl implements cityService {

    @Autowired
    public Icity icity;

    @Override
    public List<city> findAll() {
        return icity.findAll();
    }
}
