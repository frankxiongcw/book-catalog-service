package com.bookcatalog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bookcatalog.dao.ReservationMapper;
import com.bookcatalog.entity.Reservation;
import com.bookcatalog.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预约服务实现类
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public List<Reservation> getValidReservationsByBookId(Long bookId) {
        return reservationMapper.selectValidReservationsByBookId(bookId);
    }

    @Override
    public void notifyReservers(Long bookId) {
        List<Reservation> reservations = getValidReservationsByBookId(bookId);
        
        for (Reservation reservation : reservations) {
            // 模拟通知预约用户
            System.out.println("通知用户 " + reservation.getUserId() + "：图书ID " + bookId + " 现在可用。");
            // 更新通知状态
            reservation.setIsNotified(true);
            this.updateById(reservation);
        }
    }
}    