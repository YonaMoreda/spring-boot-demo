package com.example.demo.util;

import org.springframework.core.convert.converter.Converter;

public class StringToOrderTypeConverter implements Converter<String, OrderType>{
    @Override
    public OrderType convert(String s) {
        return OrderType.valueOf(s.toUpperCase());
    }
}
