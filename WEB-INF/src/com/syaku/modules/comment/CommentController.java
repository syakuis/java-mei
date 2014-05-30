/*
 * CommentController.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.comment;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

import com.syaku.modules.snsauth.*;

public class CommentController extends ActionComment {
  private Logger log = Logger.getLogger( this.getClass() );

  private SnsAuthObject daoSnsAuth = new SnsAuthObject();
  private SnsAuthStored daoSnsAuthStored = new SnsAuthStored();

  private CommentPrivilege privilege = new CommentPrivilege();

  public String procCommentInsert() throws Exception {
    Map mapSch = new HashMap();
    Map mapSns = new HashMap();

    String comment_orl = param.value("comment_orl");
    String module_orl = MODULE_ORL;
    String target_orl = param.value("target_orl");
    String parent_orl = param.value("parent_orl","0");
    String reply_group = param.value("reply_group");
    String reply_depth = param.valueIfEmpty("reply_depth","0");
    String reply_seq = param.valueIfEmpty("reply_seq","0");
    String member_orl = param.value("member_orl");
    String user_id = param.value("user_id");
    String password = param.value("password");
    String user_name = param.value("user_name");
    String nickname = param.value("nickname");
    String content = param.value("content");
    String ip = request.getRemoteAddr();

    // sns api
    String sns_description = param.value("sns_description");
    String sns_url = param.value("sns_url");
    String sns_not_send = param.valueIfEmpty("sns_not_send","N"); // 소셜 글전송여부


    try {
      // sns api
      String sns_name = null;
      String sns_photo = null;
      String sns_id = null; // sns uid
      String parent_sns_id = null; // 글 id

      member_orl = StringUtils.defaultIfEmpty((String) session.get("_MEI_MEMBER_ORL"),"0"); // 로그인 정보 그대로 유지

      if (MEI_GRANT_LOGIN) {
        user_id = (String) session.get("_MEI_USER_ID");
        user_name = (String) session.get("_MEI_USER_NAME");
        nickname = (String) session.get("_MEI_NICKNAME");
      }

      // sns api
      if (StringUtils.equals(MOD_IS_SNS,"Y")) {
        mapSns.clear();
        mapSns = (Map) daoSnsAuth.getSignIn(null);
        if (MapUtils.isNotEmpty(mapSns)) {
          sns_name = (String) mapSns.get("sns_main_name");

          if (StringUtils.isNotEmpty(sns_name)) {
            sns_id = (String) mapSns.get("sns_main_uid");
            user_id = (String) mapSns.get("sns_main_user_id");
            user_name = (String) mapSns.get("sns_main_nickname");
            nickname = (String) mapSns.get("sns_main_nickname");
            sns_photo = (String) mapSns.get("sns_main_profile_cover");
            MEI_GRANT_COMMENT = true;
          }
        }

      }

      if (!MEI_GRANT_MANAGER) {
        privilege.getCommentPrivilegeGrant(MEI_GRANT_COMMENT);
      }


      sqlMap.startTransaction();
      CommentBean bean = new CommentBean();

      bean.setModule_orl(module_orl);
      bean.setTarget_orl(target_orl);
      bean.setReply_depth(reply_depth);
      bean.setReply_seq(reply_seq);
      bean.setMember_orl(member_orl);
      bean.setParent_orl(parent_orl);
      bean.setUser_id(user_id);
      bean.setPassword(password);
      bean.setUser_name(user_name);
      bean.setNickname(nickname);
      bean.setContent(content);
      bean.setRdate(DateUtils.date("yyyyMMddHHmmss"));
      bean.setIp(ip);
      bean.setSns_name(sns_name);
      bean.setSns_photo(sns_photo);
      bean.setSns_id(sns_id);
      bean.setIs_del("N");

      if (StringUtils.isEmpty(comment_orl)) {
        // 답글 인 경우
        if (StringUtils.isNotEmpty(reply_group) && !reply_group.equals("0")) {
          mapSch.clear();
          mapSch.put("reply_group",reply_group);
          mapSch.put("reply_seq",reply_seq);
          daoComment.sqlCommentReplySeqUpdate(mapSch);

          bean.setReply_group(reply_group);
          reply_depth = ObjectUtils.toString(NumberUtils.stringToInt(reply_depth) + 1);
          bean.setReply_depth(reply_depth);
          reply_seq = ObjectUtils.toString(NumberUtils.stringToInt(reply_seq) + 1);
          bean.setReply_seq(reply_seq);
          
          // 답글인 경우 상위 SNS msg_id 읽어오기 : 다양한 SNS 사용으로 답글형식 전송안함.
          /*
          mapSch.clear();
          mapSch.put("comment_orl",parent_orl);
          CommentBean objComment= (CommentBean) daoComment.sqlCommentObject(mapSch);
          parent_sns_id = objComment.getSns_id();
          */
       }

        comment_orl = daoComment.sqlCommentInsert(bean);
        daoComment.getCommentCounterInsert(module_orl,target_orl);


        // sns api
        if (StringUtils.equals(MOD_IS_SNS,"Y") && StringUtils.isNotEmpty(sns_name) && StringUtils.equals(sns_not_send,"N")) {
        mapSns.clear();
        mapSns.put("message",content);
        mapSns.put("description",sns_description);
        if (parent_sns_id != null) {
          mapSns.put("parent_msg_id",parent_sns_id);
        }

        if (StringUtils.isNotEmpty(sns_url)) {
          mapSns.put("url",sns_url);
        }

        Map mapSnsPost = (Map) daoSnsAuth.getPost(null,mapSns);
        String msg_id = null;
        CommentSnsBean commentsnsbean = new CommentSnsBean();
        commentsnsbean.setComment_orl(comment_orl);
        commentsnsbean.setModule_orl(module_orl);
        commentsnsbean.setTarget_orl(target_orl);

        if (MapUtils.isNotEmpty(mapSnsPost)) {
          msg_id = (String) mapSnsPost.get("TWITTER");
          if (StringUtils.isNotEmpty(msg_id)) {
            commentsnsbean.setSns_name("TWITTER");
            commentsnsbean.setMsg_id(msg_id);
            daoComment.sqlCommentSnsInsert(commentsnsbean);
          }

          msg_id = (String) mapSnsPost.get("YOZM");
          if (StringUtils.isNotEmpty(msg_id)) {
            commentsnsbean.setSns_name("YOZM");
            commentsnsbean.setMsg_id(msg_id);
            daoComment.sqlCommentSnsInsert(commentsnsbean);
          }

          msg_id = (String) mapSnsPost.get("ME2DAY");
          if (StringUtils.isNotEmpty(msg_id)) {
            commentsnsbean.setSns_name("ME2DAY");
            commentsnsbean.setMsg_id(msg_id);
            daoComment.sqlCommentSnsInsert(commentsnsbean);
          }

          msg_id = (String) mapSnsPost.get("FACEBOOK");
          if (StringUtils.isNotEmpty(msg_id)) {
            commentsnsbean.setSns_name("FACEBOOK");
            commentsnsbean.setMsg_id(msg_id);
            daoComment.sqlCommentSnsInsert(commentsnsbean);
          }
        }
//          bean.setSns_id(daoSnsAuth.getPost(sns_name,mapSns)); // SNS 전송
        }
        // sns api


     } else {

        if (!MEI_GRANT_MANAGER) {
          if (StringUtils.isNotEmpty(sns_name)) {
              mapSns.clear();
              mapSns.put("comment_orl",comment_orl);
              mapSns.put("sns_id",sns_id); // sns uid
              privilege.getCommentPrivilegeGrant(privilege.getCommentIsMine(mapSns));
          } else {
            privilege.getCommentPrivilegeGrant(comment_orl,member_orl);
          }
        }

        bean.setComment_orl(comment_orl);
        daoComment.sqlCommentUpdate(bean);
      }

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("display","alert");
      MESSAGE.put("message",e.getMessage().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

  public String procCommentDelete() throws Exception {

    String comment_orl = param.value("comment_orl");
    String member_orl = (String) session.get("_MEI_MEMBER_ORL");
    String password = param.value("password");
    String user_name = param.value("user_name");
    String nickname = param.value("nickname");
    String sns_main_name = null;
    String sns_id = null; // sns uid

    if (MEI_GRANT_LOGIN) {
      member_orl = (String) session.get("_MEI_MEMBER_ORL"); // 로그인 정보 그대로 유지
    }

    try {
      sqlMap.startTransaction();

      HashMap mapSch = new HashMap();
      mapSch.put("comment_orl",comment_orl);
      CommentBean obj= (CommentBean) daoComment.sqlCommentObject(mapSch);
      String module_orl = obj.getModule_orl();
      String target_orl = obj.getTarget_orl();
      String reply_group = obj.getReply_group();
      String sns_name = obj.getSns_name();
      boolean is_sns = StringUtils.isNotEmpty(sns_name);

      if (!MEI_GRANT_MANAGER) {

        if (!MEI_GRANT_LOGIN && is_sns ) {
          daoSnsAuth.getSignIn(null);
          Map mapSns = (Map) daoSnsAuthStored.getUserInfo(sns_name);
          if (MapUtils.isNotEmpty(mapSns)) {
            sns_id = (String) mapSns.get("sns_id");
          }

          mapSch = new HashMap();
          mapSch.put("comment_orl",comment_orl);
          mapSch.put("sns_id",sns_id);

          privilege.getCommentPrivilegeGrant(privilege.getCommentIsMine(mapSch));
        } else {
          privilege.getCommentPrivilegeGrant(comment_orl,member_orl);
        }
        
      }

//      privilege.getCommentReplyException(comment_orl); // 답변존재 여부


        mapSch = new HashMap();
        mapSch.put("module_orl",module_orl);
        mapSch.put("target_orl",target_orl);
        mapSch.put("parent_orl",comment_orl);
        long child_count = daoComment.sqlCommentCount(mapSch);
        if (child_count == 0) {
          mapSch = new HashMap();
          mapSch.put("comment_orl",comment_orl);
          daoComment.sqlCommentDelete(mapSch);
        } else {
          mapSch.clear();
          mapSch.put("comment_orl",comment_orl);
          mapSch.put("is_del","Y");
          daoComment.sqlCommentIsDelete(mapSch);
        }

        // 댓글 및 답글 모두 삭제인 상태인 경우 모두 삭제
        mapSch = new HashMap();
        mapSch.put("module_orl",module_orl);
        mapSch.put("target_orl",target_orl);
        mapSch.put("reply_group",reply_group);
        mapSch.put("is_del","N");
        long count = daoComment.sqlCommentCount(mapSch);
        if (count == 0) {
          daoComment.sqlCommentReplyGroupDelete(mapSch);
        }

        daoComment.getCommentCounterInsert(module_orl,target_orl);

//      daoComment.sqlCommentDelete(mapSch);
//      daoComment.getCommentCounterInsert(module_orl,target_orl);

      // sns api
      if (is_sns) {
      mapSch = new HashMap();
      mapSch.put("module_orl",module_orl);
      mapSch.put("target_orl",target_orl);
      mapSch.put("comment_orl",comment_orl);
      List listSns = daoComment.sqlCommentSnsList(mapSch);
      int sns_total = listSns.size();
      Map mapData = new HashMap();
      for (int i =0; i < sns_total; i++) {
        CommentSnsBean objSns = (CommentSnsBean) listSns.get(i);
        mapData.put(objSns.getSns_name(),objSns.getMsg_id());
      }
      daoSnsAuth.getDelete(null,mapData);

      mapSch = new HashMap();
      mapSch.put("comment_orl",comment_orl);
      daoComment.sqlCommentSnsDelete(mapSch);
      }
      // sns api


      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("display","alert");
      MESSAGE.put("message",e.getMessage().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

}
