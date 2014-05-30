/*
 * ScheduleAccess.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.code;

import java.util.*;

import org.apache.log4j.Logger;
import com.ibatis.sqlmap.client.SqlMapClient;

import com.syaku.core.common.*;

public class CodeAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();


  public Long sqlCodeCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.code.select-code-count", map); }
  public Long sqlCodeCountOverlap(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.code.select-code-count-overlap", map); }
  public List sqlCodeList(Map map) throws Exception { return sqlMap.queryForList("modules.code.select-code-list", map); }
  public String sqlCodeInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.code.insert-code",object); }
  public Object sqlCodeObject(Map map) throws Exception { return sqlMap.queryForObject("modules.code.select-code-one", map); }
  public void sqlCodeUpdate(Object object) throws Exception { sqlMap.update("modules.code.update-code",object); }
  public void sqlCodeDelete(Map map) throws Exception { sqlMap.delete("modules.code.delete-code", map); }

  public List sqlCodeItemList(Map map) throws Exception { return sqlMap.queryForList("modules.code.select-code-item-list", map); }
  public void sqlCodeItemInsert(Object object) throws Exception { sqlMap.insert("modules.code.insert-code-item",object); }
  public void sqlCodeItemMoveUpdate(Map map) throws Exception { sqlMap.update("modules.code.update-code-item-move",map); }
  public void sqlCodeItemUpdate(Object object) throws Exception { sqlMap.update("modules.code.update-code-item",object); }
  public void sqlCodeItemNumSortUpdate(Map map) throws Exception { sqlMap.update("modules.code.update-code-item-num-sort",map); }
  public String sqlCodeItemNumMax(Map map) throws Exception { return (String) sqlMap.queryForObject("modules.code.select-code-item-num-max",map); }
  public Object sqlCodeItemObject(Map map) throws Exception { return sqlMap.queryForObject("modules.code.select-code-item-one", map); }
  public Long sqlCodeItemCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.code.select-code-item-count",map); }
  public Integer sqlCodeItemMax(Map map) throws Exception { return (Integer) sqlMap.queryForObject("modules.code.select-code-item-max",map); }
  public String sqlCodeItemNumMove(Map map) throws Exception { return (String) sqlMap.queryForObject("modules.code.select-code-item-num-move",map); }
  public void sqlCodeItemDeleteRemove(Map map) throws Exception { sqlMap.delete("modules.code.delete-code-item-remove",map); }
  public void sqlCodeItemDelete(Map map) throws Exception { sqlMap.delete("modules.code.delete-code-item",map); }

  public void sqlCodeCreate(Map map) throws Exception { sqlMap.insert("modules.code.create-code",map); }
  public void sqlCodeCreateItem(Map map) throws Exception { sqlMap.insert("modules.code.create-code-item",map); }
  public void sqlCodeCreateCounter(Map map) throws Exception { sqlMap.insert("modules.code.create-code-counter",map); }

}