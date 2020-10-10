package com.jiucheng.dao;

import com.jiucheng.vo.student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


//@Mapper
public interface IstudentRepository extends  ElasticsearchRepository<student,Long>{

    List<student> findByScoreBetween( int p1 , int p2);
    /**
     * 此方法为ES内部集成方法,可以根据参数形式判断数据查询
     */
    List<student> findByScoreAndIdBetween(int score, Long id, Long id2);
    List<student> findByScoreBetweenAndIdBetween(int score1,int score2, Long id, Long id2);
}
