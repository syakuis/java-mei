/*
 * ScheduleHistoryBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.schedule;

public class ScheduleHistoryBean {
//history_orl,schedule_orl,begin_date,process_date,error_msg,isresult

private String history_orl;
private String schedule_orl;
private String begin_date;
private String process_date;
private String error_msg;
private String isresult;

public String getHistory_orl() { return history_orl; }
public void setHistory_orl(String history_orl) { this.history_orl = history_orl; }
public String getSchedule_orl() { return schedule_orl; }
public void setSchedule_orl(String schedule_orl) { this.schedule_orl = schedule_orl; }
public String getBegin_date() { return begin_date; }
public void setBegin_date(String begin_date) { this.begin_date = begin_date; }
public String getProcess_date() { return process_date; }
public void setProcess_date(String process_date) { this.process_date = process_date; }
public String getError_msg() { return error_msg; }
public void setError_msg(String error_msg) { this.error_msg = error_msg; }
public String getIsresult() { return isresult; }
public void setIsresult(String isresult) { this.isresult = isresult; }

}
