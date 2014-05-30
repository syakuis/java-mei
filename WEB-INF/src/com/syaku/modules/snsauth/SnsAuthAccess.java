/*
 * SnsAuthAccess.java 2011.10.27
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.snsauth;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class SnsAuthAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public List sqlSnsAuthList(Map map) throws Exception { return sqlMap.queryForList("modules.snsauth.select-snsauth-list",map); }
  public Object sqlSnsAuthObject(Map map) throws Exception { return sqlMap.queryForObject("modules.snsauth.select-snsauth-one",map); }
  public Long sqlSnsAuthCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.snsauth.select-snsauth-count",map); }
  public String sqlSnsAuthInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.snsauth.insert-snsauth",object); }
  public void sqlSnsAuthDelete(Map map) throws Exception { sqlMap.delete("modules.snsauth.delete-snsauth",map); }

  public void sqlSnsAuthPostSendUpdate(Map map) throws Exception { sqlMap.update("modules.snsauth.update-snsauth-postsend",map); }
  public void sqlSnsAuthMainUpdate(Map map) throws Exception { sqlMap.update("modules.snsauth.update-snsauth-main",map); }
  public void sqlSnsAuthMainOneUpdate(Map map) throws Exception { sqlMap.update("modules.snsauth.update-snsauth-main-one",map); }
  public void sqlSnsAuthMainResetUpdate(Map map) throws Exception { sqlMap.update("modules.snsauth.update-snsauth-main-reset",map); }

  public void sqlSnsAuthCreate(Map map) throws Exception { sqlMap.insert("modules.snsauth.create-snsauth",map); }
}