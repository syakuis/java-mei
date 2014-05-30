/*
 * ScheduleAdminController.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.schedule;

import java.util.*;
import java.io.*;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class ScheduleAdminController extends ActionSchedule {
  private Logger log = Logger.getLogger( this.getClass() );
  private static final ScheduledExecutorService executor = ScheduleSingleton.getInstance();

  public String procScheduleAdminState() throws Exception {
    String state = param.value("state");

    try {

      if ( StringUtils.equals(state,"start") ) {
        ScheduleSingleton.begin();
      }

      if ( StringUtils.equals(state,"stop") ) {
        executor.shutdown();
      }

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

  public String procScheduleItemAdminInsert() throws Exception {

    String schedule_orl = param.value("schedule_orl");
    String title = param.value("title"); 
    String classes= param.value("classes"); 
    String repeat_time = param.value("repeat_time"); 
    String last_date =DateUtils.date("yyyyMMddHHmmss");
    String isused = param.value("isused"); 
    String listorder = param.value("listorder"); 

    try {
      sqlMap.startTransaction();
 
      ScheduleBean Bean=new ScheduleBean();
 
      Bean.setSchedule_orl(schedule_orl);
      Bean.setTitle(title);
      Bean.setClasses(classes);
      Bean.setRepeat_time(repeat_time);
      Bean.setLast_date(last_date);
      Bean.setIsused(isused);
      Bean.setListorder(listorder);

      if (StringUtils.isNotEmpty(schedule_orl)){
        daoSchedule.sqlScheduleUpdate(Bean);
      }else{
        daoSchedule.sqlScheduleInsert(Bean);
      }

      sqlMap.commitTransaction();
      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

  public String procScheduleAdminExecute() throws Exception {
    String schedule_orl = param.value("schedule_orl");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("schedule_orl",schedule_orl);

      ScheduleBean obj= (ScheduleBean) daoSchedule.sqlScheduleObject(mapSch);
      String title = obj.getTitle();
      String classes = obj.getClasses();

      String begin_date = DateUtils.date("yyyyMMddHHmmss");
      String error_msg = "";
      String isresult = "N";
      
      // 클래스 호출
      long startTime = System.currentTimeMillis();

      try {
        log.debug("[MEI Schedule Loader Classes] (" + schedule_orl + ")" + title + " / " + classes);
        daoSchedule.scheduleClasses(classes);
        isresult = "Y";
      } catch(Exception e) {
        log.error(e.toString());
        error_msg = e.toString();
      }

      long executionTime = System.currentTimeMillis() - startTime;
      String process_date = ObjectUtils.toString(executionTime);

      ScheduleHistoryBean data = new ScheduleHistoryBean();
      data.setSchedule_orl(schedule_orl);
      data.setBegin_date(begin_date);
      data.setProcess_date(process_date);
      data.setError_msg(error_msg);
      data.setIsresult(isresult);

      // 스케줄 실행한 기록을 저장함
      daoSchedule.scheduleHistoryInsert(data);

      sqlMap.commitTransaction();
      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

    public String procScheduleItemAdminDelete() throws Exception {

    try {
      sqlMap.startTransaction();

      String schedule_orl = param.value("schedule_orl");
      Map map = new HashMap();
      map.put("schedule_orl",schedule_orl);
      daoSchedule.sqlScheduleDelete(map);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }


}
