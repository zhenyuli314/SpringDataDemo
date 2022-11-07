package com.lzy.test;

import com.lzy.SpringDataEsDemoApplication;
import com.lzy.entity.Product;
import com.lzy.mapper.ProductMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringDataEsDemoApplication.class)
/**
 * 文档操作
 */
public class SpringDataESProductDaoTest {
    @Autowired
    private ProductMapper productMapper;

    /**
     * 新增
     */
    @Test
    public void save() {
        Product product = new Product();
        product.setId(2L).setTitle("华为手机").setCategory("手机").setPrice(2999.0).setImages("http://www.atguigu/hw/jpg");
        productMapper.save(product);
    }

    /**
     * 批量新增
     */
    @Test
    public void saveAll() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setId((long) i).setTitle("[" + i + "]小米手机").setCategory("手机").setPrice(1999.0 + i).setImages("http://www.atguigu/xm.jpg");
            productList.add(product);
        }
        productMapper.saveAll(productList);
    }

    /**
     * 修改
     */
    @Test
    public void update() {
        Product product = new Product();
        //id相同则是修改功能
        product.setId(2L).setTitle("小米手机").setCategory("手机").setPrice(999.0).setImages("http://www.atguigu/xm.jpg");
        productMapper.save(product);
    }

    /**
     * 删除
     */
    @Test
    public void delete() {
        Product product = new Product();
        product.setId(1L);
        productMapper.delete(product);
    }

    /**
     * 查询一个
     */
    @Test
    public void findById() {
        Product product = productMapper.findById(2L).get();
        System.out.println(product);
    }

    /**
     * 查询所有
     */
    @Test
    public void findAll() {
        Iterable<Product> products = productMapper.findAll();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    /**
     * 分页查询
     */
    @Test
    public void findByPageable() {
        //设置排序(排序方式，正序还是倒序，排序的 id)
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        int currentPage = 0;//当前页，第一页从 0 开始，1 表示第二页
        int pageSize = 5;//每页显示多少条
        //设置查询分页
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);
        //分页查询
        Page<Product> productPage = productMapper.findAll(pageRequest);
        for (Product Product : productPage.getContent()) {
            System.out.println(Product);
        }
    }

    /**
     * 根据title查询
     */
    @Test
    public void findByTitleTest(){
        List<Product> products = productMapper.findByTitle("手机");
        System.out.println(products);
        List<Product> products1 = productMapper.findByTitleOrCategory("手机", "手机");
        System.out.println(products1);
    }

}
