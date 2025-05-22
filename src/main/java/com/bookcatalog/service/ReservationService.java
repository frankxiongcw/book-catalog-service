package com.bookcatalog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bookcatalog.entity.Reservation;

import java.util.List;

/**
 * 预约服务接口
 */
public interface ReservationService extends IService<Reservation> {
    /**
     * 获取某本图书的所有有效预约
     * @param bookId 图书ID
     * @return 预约列表
     */
    List<Reservation> getValidReservationsByBookId(Long bookId);

    /**
     * 通知预约用户图书可用
     * @param bookId 图书ID
     */
    void notifyReservers(Long bookId);
}    