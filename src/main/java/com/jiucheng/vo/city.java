package com.jiucheng.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * city
 *
 * @auther qiaoba
 * @date 2020/9/28 0028 16:10
 **/

@Data
public class city implements Serializable {

    public int CityID;
    public String CityName;
    public int RootID;
    public int Child;
    public int Layer;
    public int Sort;
    public int Isopen;
    public List<city> list;


}
