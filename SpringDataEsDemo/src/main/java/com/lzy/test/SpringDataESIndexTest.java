package com.lzy.test;

import com.lzy.SpringDataEsDemoApplication;
import com.lzy.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringDataEsDemoApplication.class)
/**
 * 索引操作
 */
public class SpringDataESIndexTest {

    @Autowired
    private ElasticsearchRestTemplate template;

    @Test
    public void createIndex() {
        //创建索引，系统初始化会自动创建
        System.out.println("创建索引");
    }

    @Test
    public void deleteIndex() {

        //删除索引
//        boolean b = template.deleteIndex(Product.class);
//        System.out.println(b);
    }
}
