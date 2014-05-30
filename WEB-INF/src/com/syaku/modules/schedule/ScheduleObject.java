/*
 * ScheduleObject.java 2012-03-23
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.schedule;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;

public class ScheduleObject extends ScheduleAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/schedule/info.properties");

  // 클래스 실행
  public Object scheduleClasses(String classes) throws Exception {
    Class cls =Class.forName(classes);
    Object object = (Object) cls.newInstance();
    return object;
  }

  // 스케줄 실행한 기록을 저장함
  public void scheduleHistoryInsert(ScheduleHistoryBean data) throws Exception {
    sqlScheduleHistoryInsert(data);
    Map mapSch = new HashMap();
    mapSch.put("schedule_orl" , data.getSchedule_orl() );
    mapSch.put("last_date" , data.getBegin_date() );
    sqlScheduleLastDateUpdate(mapSch);
  }

}