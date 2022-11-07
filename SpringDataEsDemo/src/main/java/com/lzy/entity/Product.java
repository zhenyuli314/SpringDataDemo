package com.lzy.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@EqualsAndHashCode

@Document(indexName = "product") //关联索引

public class Product {
    @Id //主键
    @Field(index = false,type = FieldType.Integer)
    private Long id;//商品唯一标识

    /**
     * index:是否能被索引 ，默认是true
     * analyzer:存储时使用的分词器
     * searchAnalyzer:搜索时使用的分词器
     * store：是否存储，默认是false
     * type：数据类型，默认是FieldType.auto
     */
    @Field(analyzer = "ik_smart",searchAnalyzer = "ik_smart",store = true,type = FieldType.Text)//能被分词
    private String title;//商品名称

    @Field(type = FieldType.Keyword)//不能被分词
    private String category;//分类名称

    @Field(type = FieldType.Double)//数据类型为double
    private Double price;//商品价格

    @Field(type = FieldType.Keyword,index = false)//不能被查询
    private String images;//图片地址
}
