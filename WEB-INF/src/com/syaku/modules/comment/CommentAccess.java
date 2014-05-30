/*
 * CommentAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.comment;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class CommentAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public List sqlCommentList(Map map) throws Exception { return sqlMap.queryForList("modules.comment.select-comment-list",map); }
  public List sqlCommentMapList(Map map) throws Exception { return sqlMap.queryForList("modules.comment.select-map-comment",map); }
  public Object sqlCommentObject(Map map) throws Exception { return sqlMap.queryForObject("modules.comment.select-comment-one",map); }
  
  public Long sqlCommentCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.comment.select-comment-count",map); }
  public String sqlCommentInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.comment.insert-comment",object); }
  public void sqlCommentUpdate(Object object) throws Exception { sqlMap.update("modules.comment.update-comment",object); }
  public void sqlCommentDelete(Map map) throws Exception { sqlMap.delete("modules.comment.delete-comment",map); }

  public void sqlCommentIsDelete(Map map) throws Exception { sqlMap.update("modules.comment.update-comment-del",map); }
  public void sqlCommentReplyGroupDelete(Map map) throws Exception { sqlMap.delete("modules.comment.delete-comment-replygroup",map); }

  public void sqlCommentModuleDelete(Map map) throws Exception { sqlMap.delete("modules.comment.delete-comment-module",map); }
  public void sqlCommentTargetDelete(Map map) throws Exception { sqlMap.delete("modules.comment.delete-comment-target",map); }
  public void sqlCommentReplySeqUpdate(Object object) throws Exception { sqlMap.update("modules.comment.update-comment-reply-seq",object); }
  public void sqlCommentReplyDelete(Map map) throws Exception { sqlMap.delete("modules.comment.delete-comment-reply",map); }

  public Long sqlCommentCounterCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.comment.select-comment-counter-count",map); }
  public Long sqlCommentCounterCountObject(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.comment.select-comment-counter-count-one",map); }
  public void sqlCommentCounterInsert(Object object) throws Exception { sqlMap.insert("modules.comment.insert-comment-counter",object); }
  public void sqlCommentCounterUpdate(Object object) throws Exception { sqlMap.update("modules.comment.update-comment-counter",object); }
  public void sqlCommentCounterDelete(Map map) throws Exception { sqlMap.delete("modules.comment.delete-comment-counter",map); }
  public void sqlCommentCounterModuleDelete(Map map) throws Exception { sqlMap.delete("modules.comment.delete-comment-counter-module",map); }


  public List sqlCommentSnsList(Map map) throws Exception { return sqlMap.queryForList("modules.comment.select-commentsns-list",map); }
  public void sqlCommentSnsInsert(Object object) throws Exception { sqlMap.insert("modules.comment.insert-commentsns",object); }
  public void sqlCommentSnsDelete(Map map) throws Exception { sqlMap.delete("modules.comment.delete-commentsns",map); }

  public List sqlCommentAdminList(Map map) throws Exception { return sqlMap.queryForList("modules.comment.select-comment-admin-list",map); }
  public Long sqlCommentAdminCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.comment.select-comment-admin-count",map); }


  public void sqlCommentCreate(Map map) throws Exception { sqlMap.insert("modules.comment.create-comment",map); }
  public void sqlCommentCreateCounter(Map map) throws Exception { sqlMap.insert("modules.comment.create-comment-counter",map); }
  public void sqlCommentCreateSns(Map map) throws Exception { sqlMap.insert("modules.comment.create-comment-sns",map); }

}