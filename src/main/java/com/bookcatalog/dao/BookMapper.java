package com.bookcatalog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookcatalog.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图书数据访问接口
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}    