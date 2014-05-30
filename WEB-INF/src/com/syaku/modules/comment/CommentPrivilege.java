/*
 * KinModel.java 2011.01.01
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
import org.apache.commons.lang.*;

public class CommentPrivilege {
  private Logger log = Logger.getLogger( this.getClass() );
  private CommentObject daoComment = new CommentObject();

  // 해당 자료에 대한 소유자 체크
  public boolean getCommentIsMine(String comment_orl,String member_orl) throws Exception {
    if (StringUtils.isEmpty(comment_orl) || StringUtils.isEmpty(member_orl)) {
      return false;
    }

    Map map = new HashMap();
    map.put("comment_orl",comment_orl);
    map.put("member_orl",member_orl);
    return getCommentIsMine(map);
  }
  public boolean getCommentIsMine(Map map) throws Exception {
    long cnt = (long) daoComment.sqlCommentCount(map);
    if (cnt > 0) { return true; } else { return false; }
  }

  // 쓰기 권한
  public void getCommentPrivilegeGrant(String comment_orl,String member_orl) throws Exception {
    getCommentPrivilegeGrant(getCommentIsMine(comment_orl,member_orl));
  }
  public void getCommentPrivilegeGrant(boolean grant) throws Exception {
    if (!grant) { throw new Exception("권한이 없습니다."); }
  }

  // 답변존재여부
  public boolean getCommentIsReply(String comment_orl) throws Exception {
    Map map = new HashMap();
    map.put("parent_orl",comment_orl);
    long cnt = (long) daoComment.sqlCommentCount(map);
    if (cnt > 0) { return true; } else { return false; }
  }

  public void getCommentReplyException(String comment_orl) throws Exception {
    if (getCommentIsReply(comment_orl)) {
      throw new Exception("답변이 존재합니다.");
    }
  }

}