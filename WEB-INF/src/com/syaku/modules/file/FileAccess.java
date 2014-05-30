/*
 * FileAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.file;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class FileAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public List sqlFileAdminModuleList(Map map) throws Exception { return sqlMap.queryForList("modules.file.select-file-admin-module-list", map); }
  public List sqlFileAdminList(Map map) throws Exception { return sqlMap.queryForList("modules.file.select-file-admin-list", map); }
  public Long sqlFileAdminCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.file.select-file-admin-count", map); }

  //getFileObject
  public Object sqlFileObject(Map map) throws Exception { return sqlMap.queryForObject("modules.file.select-file-one", map); }
  public List sqlFileList(Map map) throws Exception { return sqlMap.queryForList("modules.file.select-file-list", map); }
  public List sqlFileItem(Map map) throws Exception { return sqlMap.queryForList("modules.file.select-file-item", map); }
  public Object sqlFileItemObject(Map map) throws Exception { return sqlMap.queryForObject("modules.file.select-file-item-once", map); }
  public Long sqlFileCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.file.select-file-count", map); }
  public Map sqlFileSizeSumCount(Map map) throws Exception { return (Map) sqlMap.queryForObject("modules.file.select-file-size-sum-count", map); }

  // getFileInsert
  public String sqlFileInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.file.insert-file",object); }

  //getFileUpdateTarget , sqlFileUpdateTarget
  public void sqlFileUpdateTarget(Map map) throws Exception { sqlMap.update("modules.file.update-file-target",map); }

  public void sqlFileDelete(Map map) throws Exception { sqlMap.delete("modules.file.delete-file",map); }
  public void sqlFileModuleDelete(Map map) throws Exception { sqlMap.delete("modules.file.delete-file-module",map); }

  public void sqlFileDeleteTarget(Map map) throws Exception { sqlMap.delete("modules.file.delete-file-target",map); }
  public void sqlFileDeleteArray(Map map) throws Exception { sqlMap.delete("modules.file.delete-file-array",map); }
  public void sqlFileSidDelete(Map map) throws Exception { sqlMap.delete("modules.file.delete-file-sid",map); }

  public Long sqlFileCounterCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.file.select-file-counter-count",map); }
  public Long sqlFileCounterCountObject(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.file.select-file-counter-count-one",map); }
  public void sqlFileCounterInsert(Object object) throws Exception { sqlMap.insert("modules.file.insert-file-counter",object); }
  public void sqlFileCounterUpdate(Object object) throws Exception { sqlMap.update("modules.file.update-file-counter",object); }
  public void sqlFileCounterDelete(Map map) throws Exception { sqlMap.delete("modules.file.delete-file-counter",map); }
  public void sqlFileCounterModuleDelete(Map map) throws Exception { sqlMap.delete("modules.file.delete-file-counter-module",map); }

  public void sqlFileCreate(Map map) throws Exception { sqlMap.insert("modules.file.create-file",map); }
  public void sqlFileCounterCreate(Map map) throws Exception { sqlMap.insert("modules.file.create-file-counter",map); }


}