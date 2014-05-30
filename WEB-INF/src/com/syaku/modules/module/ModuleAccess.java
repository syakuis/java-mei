/*
 * ModuleAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.module;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class ModuleAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public List sqlModuleList(Map map) throws Exception { return sqlMap.queryForList("modules.module.select-module-list",map); }
  public Long sqlModuleCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.module.select-module-count",map); }
  public Object sqlModuleObject(Map map) throws Exception { return sqlMap.queryForObject("modules.module.select-module-one",map); }
  public String sqlModuleInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.module.insert-module",object); }
  public void sqlModuleUpdate(Object object) throws Exception { sqlMap.update("modules.module.update-module",object); }
  public void sqlModuleLayoutUpdate(Map map) throws Exception { sqlMap.update("modules.module.update-module-layout",map); }
  public void sqlModuleMenuUpdate(Map map) throws Exception { sqlMap.update("modules.module.update-module-menu",map); }
  public void sqlModuleLayoutMenuUpdate(Map map) throws Exception { sqlMap.update("modules.module.update-module-layout-menu",map); }
  public void sqlModuleDelete(Map map) throws Exception { sqlMap.delete("modules.module.delete-module",map); }

  public List sqlModuleOptionsList(Map map) throws Exception { return sqlMap.queryForList("modules.module.select-module-options-list",map); }
  public Long sqlModuleOptionsCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.module.select-module-options-count",map); }
  public Object sqlModuleOptionsObject(Map map) throws Exception { return sqlMap.queryForObject("modules.module.select-module-options-one",map); }
  public void sqlModuleOptionsInsert(Object object) throws Exception { sqlMap.insert("modules.module.insert-module-options",object); }
  public void sqlModuleOptionsUpdate(Object object) throws Exception { sqlMap.update("modules.module.update-module-options",object); }
  public void sqlModuleOptionsDelete(Map map) throws Exception { sqlMap.delete("modules.module.delete-module-options",map); }

  public List sqlModuleGrantsList(Map map) throws Exception { return sqlMap.queryForList("modules.module.select-module-grants-list",map); }
  public void sqlModuleGrantsInsert(Object object) throws Exception { sqlMap.insert("modules.module.insert-module-grants",object); }
  public void sqlModuleGrantsArrayInsert(Map map) throws Exception { sqlMap.insert("modules.module.insert-module-grants-array",map); }
  public void sqlModuleGrantsDelete(Map map) throws Exception { sqlMap.delete("modules.module.delete-module-grants",map); }
  public void sqlModuleGrantsGroupDelete(Map map) throws Exception { sqlMap.delete("modules.module.delete-module-grants-group",map); }

  public void sqlModuleCreate(Map map) throws Exception { sqlMap.insert("modules.module.create-module",map); }
  public void sqlModuleOptionsCreate(Map map) throws Exception { sqlMap.insert("modules.module.create-module-options",map); }
  public void sqlModuleGrantsCreate(Map map) throws Exception { sqlMap.insert("modules.module.create-module-grants",map); }

}