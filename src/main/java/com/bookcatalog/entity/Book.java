package com.bookcatalog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 图书实体类
 */
@Data
@TableName("t_book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer type; // 1-纸质书, 2-电子书
    private Boolean isBorrowed;
    private Date borrowDate;
    private Date returnDate;
    private BigDecimal price;
    private String format; // 电子书格式
    private Integer pages; // 纸质书页数
    private String additionalService;
}    