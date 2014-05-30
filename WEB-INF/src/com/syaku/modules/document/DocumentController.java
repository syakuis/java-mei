/*
 * DocumentController.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.document;

import java.util.*;

import org.apache.log4j.Logger;

import org.apache.commons.lang.*;
import org.apache.commons.io.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;


public class DocumentController extends ActionDocument {
  private Logger log = Logger.getLogger( this.getClass() );

  private DocumentPrivilege privilege = new DocumentPrivilege();

  private void getDocumentGrant(String document_orl,String member_orl,String password) throws Exception {
    if (!MEI_GRANT_MANAGER) {

      if (!MEI_GRANT_LOGIN) { // 로그인 하지 않은 경우 비밀번호 비교
        MEI_GRANT_WRITE = privilege.getDocumentPassword(document_orl,password);
      } else { // 본인 글인 경우에만 수정가능
        MEI_GRANT_WRITE = privilege.getDocumentIsMine(document_orl,member_orl);
      }

      privilege.getDocumentPrivilegeException(MEI_GRANT_WRITE); 
    }
  }

  public String procDocumentInsert() throws Exception {
    String document_orl = param.value("document_orl");
    String category_orl = param.valueIfEmpty("category_orl","0");

    String member_orl = "0";
    String user_id = null;
    String user_name = param.value("user_name");
    String password = param.value("password");
    String nickname = user_name;
    String email = param.value("email");
    String homepage = param.value("homepage");
    String title = param.value("title");
    String content = param.value("content");
    String content_text = "";
    try { content_text = content.replaceAll("(<{1}\\/{0,1})[^<>]*(\\/{0,1}>{1})",""); }
    catch (Exception e) { log.error(e.toString()); }

    String extra_vars = param.value("extra_vars");
    String readed_count = param.value("readed_count");
    String vote_count = param.value("vote_count");
    String ipaddress = request.getRemoteAddr();
    String regdate = param.value("regdate");

    String parent_orl = param.valueIfEmpty("parent_orl","0");
    String reply_member_orl = param.value("reply_member_orl");
    String reply_group = param.value("reply_group");
    String reply_depth = param.valueIfEmpty("reply_depth","0");
    String reply_num = param.valueIfEmpty("reply_num","0");

    String is_notice = param.valueIfEmpty("is_notice","N");
    String title_bold = param.valueIfEmpty("title_bold","N");
    String title_color = param.value("title_color");

    // 공지,타이틀은 관리자가 아니고 수정인 경우에는 변경할 수 없음.
    if (!MEI_GRANT_MANAGER && StringUtils.isNotEmpty(document_orl)) {
      is_notice = null;
      title_bold = null;
      title_color = null;
    }

    if (StringUtils.isNotEmpty((String) session.get("_MEI_USER_ID"))) {
      member_orl = (String) session.get("_MEI_MEMBER_ORL");
      user_id = (String) session.get("_MEI_USER_ID");
      user_name = (String) session.get("_MEI_USER_NAME");
      nickname = (String) session.get("_MEI_NICKNAME");
      email = StringUtils.defaultIfEmpty(email,(String) session.get("_MEI_EMAIL"));
      homepage = StringUtils.defaultIfEmpty(email,(String) session.get("_MEI_HOMEPAGE"));
    }

    try {
      sqlMap.startTransaction();

      DocumentBean bean = new DocumentBean();
      bean.setDocument_orl(document_orl);
      bean.setCategory_orl(category_orl);
      bean.setModule_orl(MODULE_ORL);
      bean.setMember_orl(member_orl);
      bean.setUser_id(user_id);
      bean.setUser_name(user_name);
      bean.setPassword(password);
      bean.setNickname(nickname);
      bean.setEmail(email);
      bean.setHomepage(homepage);
      bean.setTitle(title);
      bean.setContent(content);
      bean.setContent_text(content_text);
      bean.setExtra_vars(extra_vars);
      bean.setReaded_count("0");
      bean.setVote_count("0");
      bean.setIpaddress(ipaddress);
      bean.setRegdate(regdate);

      bean.setParent_orl(parent_orl);
      bean.setReply_member_orl(reply_member_orl);
      bean.setReply_group(reply_group);
      bean.setReply_depth(reply_depth);
      bean.setReply_num(reply_num);

      bean.setIs_notice(is_notice);
      bean.setTitle_bold(title_bold);
      bean.setTitle_color(title_color);

      Map mapSch = new HashMap();

      if (StringUtils.isEmpty(document_orl)) {

        if (!MEI_GRANT_MANAGER) {
          privilege.getDocumentPrivilegeException(MEI_GRANT_WRITE);
        }
        
        if (StringUtils.isEmpty(regdate)) {
          bean.setRegdate(DateUtils.date("yyyyMMddHHmmss"));
        }

        // 답글 인 경우
        if (StringUtils.isNotEmpty(reply_group) && !reply_group.equals("0")) {
          mapSch.clear();
          mapSch.put("reply_group",reply_group);
          mapSch.put("reply_num",reply_num);
          daoDocument.sqlDocumentReplySeqUpdate(mapSch);

          bean.setReply_group(reply_group);
          reply_depth = ObjectUtils.toString(NumberUtils.stringToInt(reply_depth) + 1);
          bean.setReply_depth(reply_depth);
          reply_num = ObjectUtils.toString(NumberUtils.stringToInt(reply_num) + 1);
          bean.setReply_num(reply_num);
        }

        document_orl = (String) daoDocument.sqlDocumentInsert(bean);

        // 파일첨부 처리
        mapSch = new HashMap();
        mapSch.put("sid", param.value("sid") );
        mapSch.put("seq","1");
        mapSch.put("module_orl", MODULE_ORL );
        mapSch.put("target_orl",document_orl);
        daoFile.getFileUpdateTarget(mapSch);

      } else {
        getDocumentGrant(document_orl,member_orl,password);

        bean.setLast_update(regdate);
        bean.setLast_updater(nickname);

        daoDocument.sqlDocumentUpdate(bean);
      }
      
      // 확장변수
      if (StringUtils.equals((String) MM.get("options_is_extravars"),"Y")) {
        Map<String,String> mapExtra = new HashMap<String,String>();
        mapExtra = ExtraVarsUtils.getRequest(request,false,"extra_vars");
        daoDocument.getDocumentExtraVarsInsert(MODULE_ORL,document_orl,mapExtra);
      }

      if (StringUtils.isEmpty(document_orl)) {
        // 알림
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

  public String procDocumentDelete() throws Exception {
    String member_orl = (String) session.get("_MEI_MEMBER_ORL");
    String document_orl = param.value("document_orl");
    String password = param.value("password");

    try {
      getDocumentGrant(document_orl,member_orl,password);

      sqlMap.startTransaction();
      Map mapSch = new HashMap();
      mapSch.put("document_orl",document_orl);
      daoDocument.sqlDocumentDelete(mapSch);

      // 관련 코맨트 모두 삭제

      // 확장변수 삭제
      mapSch = new HashMap();
      mapSch.put("document_orl",document_orl);
      daoDocument.sqlDocumentExtraVarsDocumentDelete(mapSch);

      // 파일첨부
      mapSch = new HashMap();
      mapSch.put("module_orl",MODULE_ORL);
      mapSch.put("target_orl",document_orl);
      mapSch.put("seq","1");
      daoFile.getFileDeleteTarget(mapSch);

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