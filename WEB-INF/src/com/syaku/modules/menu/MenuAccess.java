/*
 * MenuAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.menu;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class MenuAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public Long sqlMenuCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.menu.select-menu-count",map); }
  public List sqlMenuList(Map map) throws Exception { return sqlMap.queryForList("modules.menu.select-menu-list",map); }
  public List sqlMenuXml(Map map) throws Exception { return sqlMap.queryForList("modules.menu.select-menu-xml", map); }
  public Object sqlMenuObject(Map map) throws Exception { return sqlMap.queryForObject("modules.menu.select-menu-one", map); }
  public String sqlMenuInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.menu.insert-menu",object); }
  public void sqlMenuUpdate(Object object) throws Exception { sqlMap.update("modules.menu.update-menu",object); }
  public void sqlMenuDelete(Map map) throws Exception { sqlMap.delete("modules.menu.delete-menu",map); }

  public Object sqlMenuItemObject(Map map) throws Exception { return sqlMap.queryForObject("modules.menu.select-menu-item-one", map); }

  public List sqlMenuItemList(Map map) throws Exception { return sqlMap.queryForList("modules.menu.select-menu-item-list",map); }
  public Long sqlMenuItemCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.menu.select-menu-item-count",map); }

  public Integer sqlMenuItemMax(Map map) throws Exception { return (Integer) sqlMap.queryForObject("modules.menu.select-menu-item-max",map); }
  public String sqlMenuItemInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.menu.insert-menu-item",object); }
  public void sqlMenuItemUpdate(Object object) throws Exception { sqlMap.update("modules.menu.update-menu-item",object); }
  public void sqlMenuItemUpdateNumSort(Map map) throws Exception { sqlMap.update("modules.menu.update-menu-item-num-sort",map); }
  public void sqlMenuItemUpdateMove(Map map) throws Exception { sqlMap.update("modules.menu.update-menu-item-move",map); }
  public void sqlMenuItemDelete(Map map) throws Exception { sqlMap.delete("modules.menu.delete-menu-item",map); }

  public String sqlMenuItemMoveNum(Map map) throws Exception { return (String) sqlMap.queryForObject("modules.menu.select-menu-item-move-num",map); }
  public String sqlMenuItemMaxNum(Map map) throws Exception { return (String) sqlMap.queryForObject("modules.menu.select-menu-item-max-num",map); }
  public void sqlMenuItemDeleteRemove(Map map) throws Exception { sqlMap.delete("modules.menu.delete-menu-item-remove",map); }

  public void sqlMenuCreate(Map map) throws Exception { sqlMap.insert("modules.menu.create-menu",map); }
  public void sqlMenuItemCreate(Map map) throws Exception { sqlMap.insert("modules.menu.create-menu-item",map); }

  // 메뉴 명과 url 경로가 일치하는 데이터 조회
  public Object sqlMenuSearchObject(Map map) throws Exception { return sqlMap.queryForObject("modules.menu.select-menu-one-search", map); }
  public Object sqlMenuItemSearchObject(Map map) throws Exception { return sqlMap.queryForObject("modules.menu.select-menu-item-one-search", map); }
}