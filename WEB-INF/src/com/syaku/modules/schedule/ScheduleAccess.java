/*
 * ScheduleAccess.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.schedule;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class ScheduleAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public Long sqlScheduleCount(Map map) throws Exception {return (Long) sqlMap.queryForObject("modules.schedule.select-schedule-count", map); }
  public List sqlScheduleList(Map map) throws Exception { return sqlMap.queryForList("modules.schedule.select-schedule-list", map); }
  public Object sqlScheduleObject(Map map) throws Exception { return sqlMap.queryForObject("modules.schedule.select-schedule-one", map); }

  public String sqlScheduleInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.schedule.insert-schedule",object); }
  public void sqlScheduleUpdate(Object object) throws Exception { sqlMap.update("modules.schedule.update-schedule",object); }
  public void sqlScheduleLastDateUpdate(Map map) throws Exception { sqlMap.update("modules.schedule.update-schedule-lastdate",map); }
  public void sqlScheduleDelete(Map map) throws Exception { sqlMap.delete("modules.schedule.delete-schedule",map); }

  public Long sqlScheduleHistoryCount(Map map) throws Exception {return (Long) sqlMap.queryForObject("modules.schedule.select-schedule-history-count", map); }
  public List sqlScheduleHistoryList(Map map) throws Exception { return sqlMap.queryForList("modules.schedule.select-schedule-history-list", map); }
  public String sqlScheduleHistoryInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.schedule.insert-schedule-history",object); }
  public void sqlScheduleHistoryDelete(Map map) throws Exception { sqlMap.delete("modules.schedule.delete-schedule-history",map); }

  public void sqlScheduleCreate(Map map) throws Exception { sqlMap.insert("modules.schedule.create-schedule",map); }
  public void sqlScheduleHistoryCreate(Map map) throws Exception { sqlMap.insert("modules.schedule.create-schedule-history",map); }

}