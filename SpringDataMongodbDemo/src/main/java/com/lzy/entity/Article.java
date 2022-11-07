package com.lzy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)

@Document(collection = "article")//关联集合名称（表名）
public class Article {
    @Id //用来标识主键
    private Integer id;
    //@Field建立实体类中属性和collection中字段的映射关系，省略表示二者的名称一致
    //@Field
    private String name;
    private String content;
    private Integer hits;
}
