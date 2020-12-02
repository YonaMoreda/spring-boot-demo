package com.example.demo.Configuration;

import com.example.demo.util.StringToOrderStatusEnumConverter;
import com.example.demo.util.StringToOrderTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToOrderStatusEnumConverter());
        registry.addConverter(new StringToOrderTypeConverter());
    }
}
