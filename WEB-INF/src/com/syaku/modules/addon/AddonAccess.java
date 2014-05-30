/*
 * AddonAccess.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.addon;

import java.util.*;

import org.apache.log4j.Logger;
import com.ibatis.sqlmap.client.SqlMapClient;

import com.syaku.core.common.*;

public class AddonAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public void sqlAddonCreate(Map map) throws Exception { sqlMap.insert("modules.addon.create-addon",map); }

  public Long sqlAddonCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.addon.select-addon-count", map); }
  public List sqlAddonList(Map map) throws Exception { return sqlMap.queryForList("modules.addon.select-addon-list", map); }
  public String sqlAddonInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.addon.insert-addon",object); }
  public Object sqlAddonObject(Map map) throws Exception { return sqlMap.queryForObject("modules.addon.select-addon-one", map); }
  public void sqlAddonIsUseUpdate(Map map) throws Exception { sqlMap.update("modules.addon.update-addon-isuse", map); }
  public void sqlAddonFistLoadUpdate(Map map) throws Exception { sqlMap.update("modules.addon.update-addon-firstload", map); }
  public void sqlAddonDelete(Map map) throws Exception { sqlMap.delete("modules.addon.delete-addon", map); }

}