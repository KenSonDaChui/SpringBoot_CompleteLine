package com.jiucheng.vo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.lang.reflect.Type;


/**
 * city
 *
 * @auther qiaoba
 * @date 2020/9/28 0028 16:10
 **/


@Document(indexName = "student",type = "docs", shards = 5, replicas = 1)
public class student {

    @Id
    public int id;

    @Field(type = FieldType.text, analyzer = "ik_max_word")
    public String name;

    @Field(type = FieldType.Integer)
    public int score;

    @Field(type = FieldType.keyword)
    public String sex;

    public student() {
    }

    public student(int id, String name, int score, String sex) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", sex='" + sex + '\'' +
                '}';
    }
}
