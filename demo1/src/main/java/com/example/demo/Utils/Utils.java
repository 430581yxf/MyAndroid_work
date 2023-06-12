package com.example.demo.Utils;

import com.example.demo.entity.Message;

import java.time.LocalDateTime;

public class Utils {
    public static void setTime(Message message){
        LocalDateTime localDateTime = null;
            localDateTime = LocalDateTime.now();
            message.setYear(localDateTime.getYear());
            message.setMonth(localDateTime.getMonth().ordinal());
            message.setDay(localDateTime.getDayOfMonth());
            message.setHour(localDateTime.getHour());
            message.setMinute(localDateTime.getMinute());
            message.setSec(localDateTime.getSecond());}

}
