package com.example.demo.util;


import org.springframework.core.convert.converter.Converter;

public class StringToOrderStatusEnumConverter implements Converter<String, OrderStatus> {
    @Override
    public OrderStatus convert(String s) {
        try {
            return OrderStatus.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null; //TODO:: HANDLE EXCEPTION!
        }
    }
}
