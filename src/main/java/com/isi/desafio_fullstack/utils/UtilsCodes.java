package com.isi.desafio_fullstack.utils;

import java.time.LocalDateTime;
import java.util.TimeZone;


public class UtilsCodes {

    public static String formatName(String name){
        return name.trim().toLowerCase();
    }


    public static LocalDateTime dateToday(){
        return LocalDateTime.now(TimeZone.getTimeZone("America/Sao_Paulo").toZoneId());
    }

}