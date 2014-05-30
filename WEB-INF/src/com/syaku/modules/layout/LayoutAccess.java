/*
 * ScheduleAccess.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.layout;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class LayoutAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public List sqlLayoutList(Map map) throws Exception { return sqlMap.queryForList("modules.layout.select-layout-list",map); }
  public Long sqlLayoutCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.layout.select-layout-count",map); }
  public Object sqlLayoutObject(Map map) throws Exception { return sqlMap.queryForObject("modules.layout.select-layout-one", map); }

  public String sqlLayoutInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.layout.insert-layout",object); }
  public void sqlLayoutUpdate(Object object) throws Exception { sqlMap.update("modules.layout.update-layout",object); }
  public void sqlLayoutDelete(Map map) throws Exception { sqlMap.delete("modules.layout.delete-layout",map); }

  public void sqlLayoutCreate(Map map) throws Exception { sqlMap.insert("modules.layout.create-layout",map); }

}