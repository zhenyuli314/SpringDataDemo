package com.lzy.test;

import com.lzy.entity.Product;
import com.lzy.mapper.ProductMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataESSearchTest {
    @Autowired
    private ProductMapper productMapper;

    /**
     * term 查询
     * search(termQueryBuilder) 调用搜索方法，参数查询构建器对象
     */
    @Test
    public void termQuery() {
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("category", "手机");
//        Iterable<Product> products = productMapper.search(termsQueryBuilder);
//        for (Product product : products) {
//            System.out.println(product);
//        }
    }

    /**
     * term 查询加分页
     */
    @Test
    public void termQueryByPage() {
        int currentPage = 0;
        int pageSize = 5;
        //设置查询分页
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手机");
//        Iterable<Product> products = productMapper.search(termQueryBuilder, pageRequest);
//        for (Product product : products) {
//            System.out.println(product);
//        }
    }

}
