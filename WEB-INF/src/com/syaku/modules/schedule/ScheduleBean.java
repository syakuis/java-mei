/*
 * ScheduleBean.java 2012-03-23
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.schedule;

public class ScheduleBean {
//schedule_orl,title,classes,repeat_time,last_date,isused,listorder

private String schedule_orl;
private String title;
private String classes;
private String repeat_time;
private String last_date;
private String isused;
private String listorder;

public String getSchedule_orl() { return schedule_orl; }
public void setSchedule_orl(String schedule_orl) { this.schedule_orl = schedule_orl; }
public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }
public String getClasses() { return classes; }
public void setClasses(String classes) { this.classes = classes; }
public String getRepeat_time() { return repeat_time; }
public void setRepeat_time(String repeat_time) { this.repeat_time = repeat_time; }
public String getLast_date() { return last_date; }
public void setLast_date(String last_date) { this.last_date = last_date; }
public String getIsused() { return isused; }
public void setIsused(String isused) { this.isused = isused; }
public String getListorder() { return listorder; }
public void setListorder(String listorder) { this.listorder = listorder; }


}
