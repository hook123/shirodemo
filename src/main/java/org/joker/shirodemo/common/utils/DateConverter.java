package org.joker.shirodemo.common.utils;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author joker
 * @Date 3/22/19 3:22 PM
 * @Description
 */
public class DateConverter implements Converter<String , Date> {
    @Override
    public Date convert(String source) {
        Date date=null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(source);
        } catch (ParseException e) {
            System.out.println("日期转换失败");
            e.printStackTrace();
        }
        return date;
    }
}
