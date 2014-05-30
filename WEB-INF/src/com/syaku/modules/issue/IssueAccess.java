/*
 * ScheduleAccess.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.issue;

import java.util.*;

import org.apache.log4j.Logger;
import com.ibatis.sqlmap.client.SqlMapClient;

import com.syaku.core.common.*;

public class IssueAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public Long sqlIssueCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.issue.select-issue-count", map); }
  public Long sqlIssueMax(Map map) throws Exception {return (Long) sqlMap.queryForObject("modules.issue.select-issue-max", map); }
  public List sqlIssueList(Map map) throws Exception { return sqlMap.queryForList("modules.issue.select-issue-list", map); }
  public String sqlIssueInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.issue.insert-issue",object); }
  public Object sqlIssueObject(Map map) throws Exception { return sqlMap.queryForObject("modules.issue.select-issue-one", map); }
  public void sqlIssueRootYUpdate(Object object) throws Exception { sqlMap.update("modules.issue.update-issue-issue-root-y",object); }
  public void sqlIssueRootNUpdate(Object object) throws Exception { sqlMap.update("modules.issue.update-issue-issue-root-n",object); }
  public void sqlIssueDelete(Map map) throws Exception { sqlMap.delete("modules.issue.delete-issue", map); }
  public void sqlIssueModuleDelete(Map map) throws Exception { sqlMap.delete("modules.issue.delete-issue-module", map); }
  public void sqlIssueCreate(Map map) throws Exception { sqlMap.insert("modules.issue.create-issue",map); }

  
}