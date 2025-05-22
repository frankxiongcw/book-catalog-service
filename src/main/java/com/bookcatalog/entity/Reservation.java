package com.bookcatalog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 图书预约实体类
 */
@Data
@TableName("t_reservation")
public class Reservation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long bookId;
    private String userId;
    private Date reservationDate;
    private Boolean isNotified;
}    