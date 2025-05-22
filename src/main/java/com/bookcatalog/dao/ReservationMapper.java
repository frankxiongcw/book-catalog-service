package com.bookcatalog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookcatalog.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 预约数据访问接口
 */
@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
    /**
     * 获取某本图书的所有有效预约
     * @param bookId 图书ID
     * @return 预约列表
     */
    List<Reservation> selectValidReservationsByBookId(Long bookId);
}    