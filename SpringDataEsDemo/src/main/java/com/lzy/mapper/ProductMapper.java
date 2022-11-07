package com.lzy.mapper;
import com.lzy.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductMapper extends ElasticsearchRepository<Product,Long>{

    /**
     * 命名规则查询
     */
    //根据标题查询
    List<Product> findByTitle(String title);

    //根据标题或内容查询
    List<Product> findByTitleOrCategory(String title,String category);

}
