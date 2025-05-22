package com.bookcatalog.enums;

/**
 * 图书类型枚举
 */
public enum BookType {
    PAPER_BOOK(1, "纸质书"),
    E_BOOK(2, "电子书");

    private Integer code;
    private String name;

    BookType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static BookType getByCode(Integer code) {
        for (BookType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}    