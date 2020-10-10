package com.jiucheng.dao;


import com.jiucheng.vo.city;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface Icity {
    List<city> findAll();
}
