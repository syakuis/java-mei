/*
 * CounterAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.counter;

import java.util.*;

import org.apache.log4j.Logger;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class CounterAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public Long sqlCounterCount(Map map) throws Exception {return (Long) sqlMap.queryForObject("modules.counter.select-counter-count", map); }
  public List sqlCounterList(Map map) throws Exception { return sqlMap.queryForList("modules.counter.select-counter-list", map); }
  public void sqlCounterInsert(Object object) throws Exception { sqlMap.insert("modules.counter.insert-counter",object); }
  public List sqlCounterStatsDayList(Map map) throws Exception { return sqlMap.queryForList("modules.counter.select-counter-stats-day",map); }

  public void sqlCounterCreate(Map map) throws Exception { sqlMap.insert("modules.counter.create-counter",map); }

}