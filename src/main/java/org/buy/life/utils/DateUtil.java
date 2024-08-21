package org.buy.life.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: zhaoss
 * @Date: 2023/3/17 - 03 - 17 - 14:16
 * @Description: com.fire.data.utils
 * @version: 1.0
 */
@Slf4j
public class DateUtil {
    /**
     *@Author ht
     *@Date 2020/12/2 15:31
     * 当前时间前多少天
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    public static long convertToTimestamp(int past) {
        try {
            String pastDate = getPastDate(past);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(pastDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.getTimeInMillis();
        } catch (ParseException e) {
            log.error("DateUtil.convertToTimestamp error",e);
        }
        return System.currentTimeMillis();
    }

    public static LocalDate toLocalData(Long milliseconds) {
       try {
          return  Instant.ofEpochMilli(milliseconds).atZone(ZoneOffset.ofHours(8)).toLocalDate();
       }catch (Exception e){
           return LocalDate.now();
       }
    }


    public static LocalDateTime toLocalDataTime(Long milliseconds) {
        try {
            return  Instant.ofEpochMilli(milliseconds).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        }catch (Exception e){
            return LocalDateTime.now();
        }
    }

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth()); // 获取当前月的第一天
        System.out.println(firstDay.until(date, ChronoUnit.DAYS));
    }
}
