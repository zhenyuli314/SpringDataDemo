package com.lzy.springdataredisdemo.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data//@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
public class Article implements Serializable {
    private String author;
    private Date createTime;
    private String title;
}
