package com.beyondops.admin.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/13.
 */
public class TimeUtil {


  public static final DateTimeFormatter yyyyMMddHHmmssSSS = DateTimeFormatter
      .ofPattern("yyyyMMddHHmmssSSS");
  public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter
      .ofPattern("yyyyMMdd");
  public static final DateTimeFormatter yyMMdd = DateTimeFormatter
      .ofPattern("yyMMdd");
  public static final DateTimeFormatter yyyy_MM_ddHHmmss = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static final DateTimeFormatter yyyy_MM_dd_dot = DateTimeFormatter.ofPattern("yyyy.MM.dd");

  public static final DateTimeFormatter HH = DateTimeFormatter.ofPattern("HH");


  public static Timestamp getCurrentTimestamp() {
    return new Timestamp(new Date().getTime());
  }

  public static String getCur_yyyy_MM_dd() {
    return yyyy_MM_dd.format(LocalDateTime.now());
  }

  public static String getCur_yyyy_MM_ddHHmmss() {
    return yyyy_MM_ddHHmmss.format(LocalDateTime.now());
  }

  /**
   * Get the date string: "yyyy-MM-dd HH:mm:ss".
   *
   * @param date Date
   * @return date string
   */
  public static String get_yyyy_MM_ddHHmmss(LocalDateTime date) {
    if (null == date) {
      return "";
    }
    return yyyy_MM_ddHHmmss.format(date);
  }

  /**
   * Parse date from string: "yyyy-MM-dd HH:mm:ss".
   *
   * @param datestr date string
   * @return LocalDateTime
   */
  public static LocalDateTime parse_yyyy_MM_ddHHmmss(String datestr) {
    try {
      return LocalDateTime.parse(datestr, yyyy_MM_ddHHmmss);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static String getUnixTimeString() {
    return "" + System.currentTimeMillis();
  }

  public static long getUnixTime() {
    return System.currentTimeMillis();
  }

  public static String getCur_yyyyMMdd() {
    return yyyyMMdd.format(LocalDateTime.now());
  }

  public static String getCur_yyMMdd() {
    return yyMMdd.format(LocalDateTime.now());
  }

  public static int getCurHour() {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
  }

  public static int getCur_yyyyMMdd_Int() {
    return Integer.parseInt(yyyyMMdd.format(LocalDateTime.now()));
  }

  /**
   * Truncate date with format pattern.
   *
   * @param date Date
   * @param format Format pattern
   * @return Truncated Date
   */
  public static Date truncateDate(Date date, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date result = date;
    try {
      result = sdf.parse(sdf.format(date));
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * Get the time after n days.
   *
   * @param days How many days after
   * @return Timestamp
   */
  private Timestamp getTimeStampAfterDays(int days) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, days);
    return new Timestamp(cal.getTime().getTime());
  }

  private static int increaseToken = 0;
  private static final int maxToken = 1000;

  /**
   * Get current time's token.
   *
   * @return time mask
   */
  public static synchronized String genTimemark() {
    String timemark = yyyyMMddHHmmssSSS.format(LocalDateTime.now());
    increaseToken++;
    if (increaseToken >= maxToken) {
      increaseToken = 0;
    }
    String token = String.format("%03d", increaseToken);
    return timemark + token;
  }

  public Timestamp getTimestamp() {
    return new Timestamp(System.currentTimeMillis());
  }
}
