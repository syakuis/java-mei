/*
 * CommentAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.admin;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class AdminAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public List sqlAdminList(Map map) throws Exception { return sqlMap.queryForList("modules.admin.select-admin-list",map); }
  public Long sqlAdminCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.admin.select-admin-count",map); }
  public Object sqlAdminObject(Map map) throws Exception { return sqlMap.queryForObject("modules.admin.select-admin-one", map); }
  public void sqlAdminInsert(Object object) throws Exception { sqlMap.insert("modules.admin.insert-admin",object); }
  public void sqlAdminUpdate(Object object) throws Exception { sqlMap.update("modules.admin.update-admin",object); }

  public void sqlAdminCreate(Map map) throws Exception { sqlMap.insert("modules.admin.create-admin",map); }
}