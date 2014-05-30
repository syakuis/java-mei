/*
 * ScheduleManager.java 2011.03.15
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.schedule;

import java.util.*;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;
import com.syaku.core.util.*;

public class ScheduleManager {
  private Logger log = Logger.getLogger( this.getClass() );
  private Calendar calendar = Calendar.getInstance();
  private ScheduleObject daoSchedule = new ScheduleObject();
  private static final ScheduledExecutorService executor = ScheduleSingleton.getInstance();

  public void process() {

    try {
      
      Map mapSch = new HashMap();
      mapSch.put("isused","Y");
      List list = (List) daoSchedule.sqlScheduleList(mapSch);

      int count = list.size();

      for (int i =0; i < count; i++ ) { 
        ScheduleBean obj= (ScheduleBean) list.get(i);

        String schedule_orl = obj.getSchedule_orl();
        String repeat_time = obj.getRepeat_time();
        int repeat_time_int = NumberUtils.stringToInt(repeat_time,0);
        Schedule schedule = new Schedule(obj);
        
        log.info("[#MEI ScheduleManager] process : " + obj.getTitle() + "," + obj.getClasses() + "," + repeat_time);

        executor.scheduleAtFixedRate(schedule, repeat_time_int, repeat_time_int, TimeUnit.SECONDS);
      }

    } catch (Exception e) { log.error(e.toString()); }

  }

}


class Schedule implements Runnable {
  private Logger log = Logger.getLogger( this.getClass() );
  private SqlMapClient sqlMap = MyBatis.getInstance();
  private ScheduleObject daoSchedule = new ScheduleObject();
  private ScheduleBean obj;

  public Schedule(ScheduleBean obj) {
    this.obj = obj;
  }

  public void run() {

    try {
      execute();
    } catch(Exception e) {
      log.error(e.toString());
    }

  }

  private void execute() throws Exception {
    String title = obj.getTitle();
    String schedule_orl = obj.getSchedule_orl();
    String classes = obj.getClasses();

    String begin_date = DateUtils.date("yyyyMMddHHmmss");
    String error_msg = "";
    String isresult = "N";

    long startTime = System.currentTimeMillis();
    try {
      sqlMap.startTransaction();

      log.info("[#MEI Schedule] (" + schedule_orl + ")" + title + " / " + classes + "/" + begin_date);
      daoSchedule.scheduleClasses(classes);
      isresult = "Y";

      sqlMap.commitTransaction();

    } catch(Exception e) {
      log.debug(e.toString());
      error_msg = e.toString();
    } finally {
      sqlMap.endTransaction();
    }

    try {
      sqlMap.startTransaction();

      long executionTime = System.currentTimeMillis() - startTime;
      ScheduleHistoryBean data = new ScheduleHistoryBean();
      data.setSchedule_orl(schedule_orl);
      data.setBegin_date(begin_date);
      data.setProcess_date( ObjectUtils.toString(executionTime) );
      data.setError_msg(error_msg);
      data.setIsresult(isresult);
      
      // 스케줄 실행한 기록을 저장함
      daoSchedule.scheduleHistoryInsert(data);

      sqlMap.commitTransaction();
    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
    } finally {
      sqlMap.endTransaction();
    }


  }

}
