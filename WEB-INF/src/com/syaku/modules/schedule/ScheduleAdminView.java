/*
 * ScheduleAdminView.java 2012-03-23
 *
 * Copyright (c) 2011, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.schedule;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class ScheduleAdminView extends ActionSchedule {
  private Logger log = Logger.getLogger( this.getClass() );
  private static final ScheduledExecutorService executor = ScheduleSingleton.getInstance();

  private boolean getScheduleGrant() throws Exception {
    if (!MEI_GRANT_MANAGER) {
      MESSAGE.put("message","권한이 없습니다.");
      MESSAGE.put("display","");
      MESSAGE.put("error","true");
      MESSAGE.put("action","");
      MESSAGE.put("source","");
      valuestack.push(MESSAGE);
    }

    return MEI_GRANT_MANAGER;
  }

  public String dispScheduleItemAdminList() throws Exception {
    if (!getScheduleGrant()) { return "message"; }
    
    Map objSchedule= new HashMap();

    objSchedule.put( "is_shutdown" , executor.isShutdown() );
    objSchedule.put( "is_terminated" , executor.isTerminated() );

    O.put("objSchedule", objSchedule );

    int page_row = meiConfig.getInt("mei.paging.row");
    int page_link = meiConfig.getInt("mei.paging.link");
    int page = meiConfig.getInt("mei.paging.now");
    paging = new PageNavigator(request,page,page_row,page_link);

    long total = daoSchedule.sqlScheduleCount(null);
    paging.setTotal_count(total);
    paging.setPageIndex();

    Map mapSch = new HashMap();
    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());

    O.put("listSchedule", (List) daoSchedule.sqlScheduleList(mapSch) );

    return SUCCESS;
  }

 public String dispScheduleItemAdminInsert() throws Exception  {
    if (!getScheduleGrant()) { return "message"; } 

    String schedule_orl = param.value("schedule_orl");

    ScheduleBean Bean = new ScheduleBean();

  if ( StringUtils.isNotEmpty(schedule_orl) ) {

    Map map = new HashMap();
    map.put("schedule_orl",schedule_orl);
 
    Bean =(ScheduleBean)daoSchedule.sqlScheduleObject(map);
  }
    O.put("objSchedule",Bean);

    return SUCCESS;
  }

}
