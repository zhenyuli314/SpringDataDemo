package com.lzy.mapper;

import com.lzy.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleMapper extends MongoRepository<Article,Long> {
    //根据名称查询
    List<Article> findByNameLike(String name);
    //根据点击量查询
    List<Article> findByHitsGreaterThan(Integer hits);
}
