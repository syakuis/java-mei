/*
 * MemberAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.member;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class MemberAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public Integer sqlMemberAuthLast(Map map) throws Exception { return (Integer) sqlMap.queryForObject("modules.member.select-memberauth-last", map); }
  public String sqlMemberAuthInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.member.insert-memberauth",object); }
  public Object sqlMemberAuthObject(Map map) throws Exception { return sqlMap.queryForObject("modules.member.select-memberauth-one", map); }
  public void sqlMemberAuthUpdateSuccessDate(Map map) throws Exception { sqlMap.update("modules.member.update-memberauth-successDate",map); }
  public void sqlMemberAuthDelete(Map map) throws Exception { sqlMap.delete("modules.member.delete-memberauth",map); }

  public Long sqlMemberCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.member.select-member-count", map); }
  public Long sqlMemberOverlap(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.member.select-member-overlap", map); }

  public List sqlMemberList(Map map) throws Exception { return sqlMap.queryForList("modules.member.select-member-list", map); }
  public Object sqlMemberObject(Map map) throws Exception { return sqlMap.queryForObject("modules.member.select-member-one", map); }
  public String sqlMemberInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.member.insert-member",object); }
  public void sqlMemberUpdate(Object object) throws Exception { sqlMap.update("modules.member.update-member",object); }
  public void sqlMemberUpdatePassword(Map map) throws Exception { sqlMap.update("modules.member.update-member-password",map); }
  public void sqlMemberUpdateTempPassword(Map map) throws Exception { sqlMap.update("modules.member.update-member-temppassword",map); }
  public void sqlMemberUpdateTempPasswordReset(Map map) throws Exception { sqlMap.update("modules.member.update-member-temppassword-reset",map); }
  public void sqlMemberUpdateLastLogin(Map map) throws Exception { sqlMap.update("modules.member.update-member-last_login",map); }
  public void sqlMemberUpdateState(Map map) throws Exception { sqlMap.update("modules.member.update-member-state",map); }
  public void sqlMemberDelete(Map map) throws Exception { sqlMap.delete("modules.member.delete-member",map); }

  public void sqlMemberDynamicUpdate(Map map) throws Exception { sqlMap.update("modules.member.update-member-dynamic",map); }
  public void sqlMemberUpdateModuleOrl(Map map) throws Exception { sqlMap.update("modules.member.update-member-moduleorl",map); }

  public List sqlGroupList(Map map) throws Exception { return sqlMap.queryForList("modules.member.select-group-list", map); }
  public Long sqlGroupCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.member.select-group-count", map); }
  public Object sqlGroupObject(Map map) throws Exception { return sqlMap.queryForObject("modules.member.select-group-one", map); }
  public Object sqlGroupDefaultObject() throws Exception { return sqlMap.queryForObject("modules.member.select-group-default-one", null); }
  public String sqlGroupInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.member.insert-group",object); }
  public void sqlGroupUpdate(Object object) throws Exception { sqlMap.update("modules.member.update-group",object); }
  public void sqlGroupUpdateDefaultReset() throws Exception { sqlMap.update("modules.member.update-group-default-reset",null); }
  public void sqlGroupDelete(Map map) throws Exception { sqlMap.delete("modules.member.delete-group",map); }

  public List sqlGroupMemberList(Map map) throws Exception { return sqlMap.queryForList("modules.member.select-group-member-list", map); }
  public Long sqlGroupMemberCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.member.select-group-member-count", map); }
  public void sqlGroupMemberInsert(Object object) throws Exception { sqlMap.insert("modules.member.insert-group-member",object); }
  public void sqlGroupMemberArrayInsert(Map map) throws Exception { sqlMap.insert("modules.member.insert-group-member-array",map); }
  public void sqlGroupMemberDelete(Map map) throws Exception { sqlMap.delete("modules.member.delete-group-member",map); }
  public void sqlGroupMemberGroupDelete(Map map) throws Exception { sqlMap.delete("modules.member.delete-group-member-group",map); }

  public void sqlMemberCreate(Map map) throws Exception { sqlMap.insert("modules.member.create-member",map); }
  public void sqlMemberAuthCreate(Map map) throws Exception { sqlMap.insert("modules.member.create-member-auth",map); }
  public void sqlMemberGroupCreate(Map map) throws Exception { sqlMap.insert("modules.member.create-group",map); }
  public void sqlMemberGroupMemberCreate(Map map) throws Exception { sqlMap.insert("modules.member.create-group-member",map); }

}