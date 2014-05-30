 /*
 * CalendarUtils.java 2010.01.21
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import java.util.*;
import java.text.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class CalendarUtils {

  private Logger log = Logger.getLogger( this.getClass() );
  private DecimalFormat df = new DecimalFormat("00");
  private Calendar calendar = Calendar.getInstance();

  private int year,month,day;

  public CalendarUtils() {
  }

  public CalendarUtils(String date) throws Exception {
    setDate(date);
    calendar.set(year,month,day);
  }

  public void getInstance() {
    calendar.set(year,month,day);
  }

  public void setDate(String date) throws Exception {
    Date now = DateUtils.setDate(date);
    year = now.getYear() + 1900;
    month = now.getMonth();
    day = now.getDate();
  }

    // 이번달 시작일
  public int getStartDay() {
    return calendar.getMinimum(calendar.DATE);
  }

    // 이번달 마지막일
  public int getEndDay() {
    return calendar.getActualMaximum(calendar.DAY_OF_MONTH);
  }

  // 요일 1 : 일 , 2 : 월 , 3 : 화 , 4 : 수 , 5 : 목 , 6 : 금 , 7 : 토
  public int getWeek() {
    return calendar.get(calendar.DAY_OF_WEEK);
  }

  public void year(int conut) {
    calendar.add(calendar.YEAR,conut);
  }

  public void month(int conut) {
    calendar.add(calendar.MONTH,conut);
  }

  public void day(int conut) {
    calendar.add(calendar.DATE,conut);
  }

  public Calendar getCalendar() {
    return calendar;
  }

  public String date() throws Exception {
    return date("yyyy-MM-dd");
  }
  public String date(String format) throws Exception {
    String date = StringUtils.join(new String[] {
                              Integer.toString(calendar.get(Calendar.YEAR))
                            , df.format(calendar.get(Calendar.MONTH) + 1)
                            , df.format(calendar.get(Calendar.DATE))
                          });

    return DateUtils.date(format,date);
  }

  public void setYear(int year) { this.year = year; }
  public void setMonth(int month) { this.month = month; }
  public void setDay(int day) { this.day = day; }

}