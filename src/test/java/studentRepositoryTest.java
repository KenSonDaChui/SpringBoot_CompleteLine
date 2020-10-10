import com.jiucheng.Application;

import com.jiucheng.dao.IstudentRepository;
import com.jiucheng.vo.student;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class studentRepositoryTest {

    @Autowired
    private IstudentRepository istudentRepository;


    /**
     * 新增方法
     */
    @Test
    public void testSave(){
        student s = new student(1, "邓云辉", 22,"男");
        istudentRepository.save(s);
    }

    /**
     * 批量新增方法
     */
    @Test
    public void testSaveList(){
        List<student> list = new ArrayList<>();
        student s1 = new student(2, "黄如意", 77,"男");
        student s2 = new student(3, "胡云成", 45,"女");
        student s3 = new student(4, "任吉祥", 18,"女");
        student s4 = new student(5, "孙翔", 99,"女");
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
        istudentRepository.saveAll(list);
    }

    /**
     * 新增方法
     */
    @Test
    public void testDelete(){
        istudentRepository.deleteAll();
    }


    @Test
    public void testQuery(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("name", "胡"));
        Page<student> page = istudentRepository.search(queryBuilder.build());
        long totalElements = page.getTotalElements();
        System.out.println("获取的总条数："+totalElements);
        for (student item : page) {
            System.out.println(item);
            System.out.println(page);
        }
    }

    @Test
    public void testQuerymuch(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(
                QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name","胡"))
                        .must(QueryBuilders.matchQuery("sex","女")));
        Page<student> page = istudentRepository.search(queryBuilder.build());
        long totalElements = page.getTotalElements();
        System.out.println("获取的总条数："+totalElements);
        for (student item : page) {
            System.out.println(item);
            System.out.println(page);
        }
    }


    @Test
    public void findByPriceBetween(){
        Iterable<student> list = istudentRepository.findByScoreBetween(23,88);
        for (student item : list) {
            System.out.println(item);
        }
    }

}
