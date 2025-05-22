-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `book_catalog` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 切换到创建的数据库
USE `book_catalog`;

-- 创建图书表
CREATE TABLE IF NOT EXISTS t_book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL COMMENT '书名',
    author VARCHAR(255) NOT NULL COMMENT '作者',
    isbn VARCHAR(20) UNIQUE NOT NULL COMMENT 'ISBN号',
    type TINYINT NOT NULL COMMENT '图书类型(1-纸质书,2-电子书)',
    is_borrowed BOOLEAN DEFAULT FALSE COMMENT '是否已借出',
    borrow_date DATETIME COMMENT '借阅日期',
    return_date DATETIME COMMENT '应归还日期',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格',
    format VARCHAR(50) COMMENT '电子书格式',
    pages INT COMMENT '纸质书页数',
    additional_service VARCHAR(255) COMMENT '额外服务',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 创建预约表
CREATE TABLE IF NOT EXISTS t_reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL COMMENT '图书ID',
    user_id VARCHAR(50) NOT NULL COMMENT '用户ID',
    reservation_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '预约日期',
    is_notified BOOLEAN DEFAULT FALSE COMMENT '是否已通知',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (book_id) REFERENCES t_book(id)
);

-- 创建索引
CREATE INDEX idx_book_isbn ON t_book(isbn);
CREATE INDEX idx_book_type ON t_book(type);
CREATE INDEX idx_book_borrowed ON t_book(is_borrowed);
CREATE INDEX idx_reservation_book_id ON t_reservation(book_id);
CREATE INDEX idx_reservation_user_id ON t_reservation(user_id);    