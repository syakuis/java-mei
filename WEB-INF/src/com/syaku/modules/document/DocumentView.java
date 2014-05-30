/*
 * DocumentView.java 2011.05.23
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

import com.syaku.core.*;
import com.syaku.core.util.*;

public class DocumentView extends ActionDocument {
  private Logger log = Logger.getLogger( this.getClass() );

  private boolean getDocumentGrant(boolean grant) throws Exception {
    if (!grant) {
      MESSAGE.put("message","권한이 없습니다.");
      MESSAGE.put("display","");
      MESSAGE.put("error","true");
      MESSAGE.put("action","");
      MESSAGE.put("source","");
      valuestack.push(MESSAGE);
    }
    return grant;
  }

  public String dispDocumentList() throws Exception {
    if (!getDocumentGrant(MEI_GRANT_LIST)) { return "message"; }

    // 목록 수
    int page_row = NumberUtils.stringToInt( (String) MM.get("options_list_count") , meiConfig.getInt("mei.paging.row") );
    // 페이지 수
    int page_link = NumberUtils.stringToInt( (String) MM.get("options_page_count") , meiConfig.getInt("mei.paging.link") );

    int page = meiConfig.getInt("mei.paging.now");
    paging = new PageNavigator(request,page,page_row,page_link);

    String list_style = param.valueIfEmpty("list_style","list");
    String category_orl = param.value("category_orl");
    String sch_type = param.value("sch_type");
    if ( !StringUtils.equals( sch_type , "subject" ) && !StringUtils.equals( sch_type , "content" ) && !StringUtils.equals( sch_type , "nickname" ) ) { sch_type = ""; }
    String sch_value = param.value("sch_value");

    Map mapSch = new HashMap();
    mapSch.put("module_orl", MODULE_ORL );

    // 분류
    if (StringUtils.equals((String) MM.get("options_is_category") , "Y")) {
      O.put("listCategory",(List) daoCategory.getCategoryList(mapSch)); // 분류
    }

    // 상담글 사용여부
    if (StringUtils.equals( (String) MM.get("options_is_consultation") , "Y") && !MEI_GRANT_MANAGER) {
      mapSch.put("reply_member_orl",StringUtils.defaultIfEmpty((String) session.get("_MEI_MEMBER_ORL"),"-1"));
    }

    mapSch.put("category_orl", category_orl);
    mapSch.put("sch_type", sch_type);
    mapSch.put("sch_value", sch_value);

    long total = daoDocument.sqlDocumentCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());

    // 정렬
    String order_type = StringUtils.defaultIfEmpty( (String) MM.get("options_order_type"),"ASC" );
    String order_target = StringUtils.defaultIfEmpty( (String) MM.get("options_order_target"),"listorder" );
    String listorder = "is_notice DESC, " + order_target + " " + order_type; // 공지사항부터 노출

    mapSch.put("listorder",listorder);
    O.put("listDocument", (List) daoDocument.getDocumentPageList(mapSch,MM));

    if (list_style.equals("gallery")) {
      modSkinChange("document.gallery.list.html");
    }
    return SUCCESS;
  }

  public String dispDocumentView() throws Exception {

    try {
      sqlMap.startTransaction();

      String member_orl = StringUtils.defaultIfEmpty((String) session.get("_MEI_MEMBER_ORL"),"0");
      String document_orl = param.value("document_orl","");
      String category_orl = param.value("category_orl","");

      Map mapSch = new HashMap();

      // 조회수
      mapSch = new HashMap();
      mapSch.put("document_orl", document_orl);
      mapSch.put("readed_count", "readed_count + 1");
      daoDocument.sqlDocumentUpdateReadedCount(mapSch);

      mapSch = new HashMap();
      mapSch.put("document_orl", document_orl);
      DocumentBean object = (DocumentBean) daoDocument.sqlDocumentObject(mapSch);
      String title= object.getTitle();
      String content= object.getContent();
      O.put("objDocument", object);

      if (!MEI_GRANT_MANAGER) {
        // 상담기능 사용여부 : 사용인 경우 본인글만 열람가능.
        if (StringUtils.equals((String) MM.get("options_is_consultation") , "Y")) { 
          MEI_GRANT_VIEW = StringUtils.equals( member_orl , object.getReply_member_orl() );
        }

        // 본인 글만 수정가능
        MEI_GRANT_MINE = StringUtils.equals( member_orl , object.getMember_orl() );
        MEI_GRANT_WRITE = MEI_GRANT_MINE;
      }

      if (!getDocumentGrant(MEI_GRANT_VIEW)) { return "message"; }

      
      // 코멘트

      // 확장변수
      if (StringUtils.equals((String) MM.get("options_is_extravars"),"Y")) {
        mapSch = new HashMap();
        mapSch.put("module_orl", MODULE_ORL);
        mapSch.put("document_orl", document_orl);
        O.put("listExtra", (List) daoDocument.sqlDocumentExtraView(mapSch));
        O.put("objExtra", (Map) daoDocument.getDocumentExtraMap(mapSch));
      }

      mapSch = new HashMap();
      mapSch.put("document_orl", document_orl);
      mapSch.put("module_orl", MODULE_ORL);
      mapSch.put("category_orl", category_orl);
      O.put("objDocumentPervnext", (Object) daoDocument.sqlDocumentPervnextObject(mapSch));

      sqlMap.commitTransaction();
    } catch (Exception e) { log.error(e.toString()); } finally { sqlMap.endTransaction(); }

    return SUCCESS;
  }

  public String dispDocumentInsert() throws Exception {
    MEI_ADDONS.add("/addons/datepick/addon.html"); // 애드온 추가

    String document_orl = param.value("document_orl");

    String parent_orl = param.value("parent_orl");
    String reply_member_orl = param.value("reply_member_orl");
    String reply_group = param.value("reply_group");
    String reply_depth = param.value("reply_depth");
    String reply_num = param.value("reply_num");

    String member_orl = StringUtils.defaultIfEmpty((String) session.get("_MEI_MEMBER_ORL"),"0");
    String nickname = (String) session.get("_MEI_NICKNAME");
    String user_id = (String) session.get("_MEI_USER_ID");
    String regdate = DateUtils.date("yyyyMMddHHmmss");

    DocumentBean documentbean = new DocumentBean();
    documentbean.setMember_orl(member_orl);
    documentbean.setNickname(nickname);
    documentbean.setUser_id(user_id);
    documentbean.setRegdate(regdate);

    DocumentBean objDocumentReply = new DocumentBean();

    Map mapSch = new HashMap();

    if (StringUtils.isNotEmpty(document_orl)) {
      mapSch = new HashMap();
      mapSch.put("document_orl", document_orl);
      documentbean = (DocumentBean) daoDocument.sqlDocumentObject(mapSch);

      if (!MEI_GRANT_MANAGER) {
        // 본인 글만 수정가능.
        MEI_GRANT_WRITE = StringUtils.equals(member_orl,documentbean.getMember_orl());
      }

    } else {
      
      if (NumberUtils.stringToInt(parent_orl) > 0 && StringUtils.equals((String) MM.get("options_is_reply"),"Y")) {
        mapSch = new HashMap();
        mapSch.put("document_orl", parent_orl);
        objDocumentReply = (DocumentBean) daoDocument.sqlDocumentObject(mapSch);
      }

    }

    if (!getDocumentGrant(MEI_GRANT_WRITE)) { return "message"; }
    
    O.put("objDocument", documentbean);
    O.put("objDocumentReply", objDocumentReply);

    // 분류
    if (StringUtils.equals((String) MM.get("options_is_category") , "Y")) {
      mapSch = new HashMap();
      mapSch.put("module_orl", MODULE_ORL);
      O.put("listCategory",(List) daoCategory.sqlCategoryList(mapSch)); // 분류
    }

    // 확장변수
    if (StringUtils.equals((String) MM.get("options_is_extravars"),"Y")) {
      mapSch = new HashMap();
      mapSch.put("module_orl", MODULE_ORL);
      mapSch.put("document_orl", document_orl);
      O.put("listExtra", (List) daoDocument.sqlDocumentExtraView(mapSch));
      O.put("objExtra", (Map) daoDocument.getDocumentExtraMap(mapSch));
    }

    return SUCCESS;
  }

  public String dispDocumentDeletePassword() throws Exception {
    String document_orl = param.value("document_orl");
    O.put("document_orl",document_orl);

    return SUCCESS;
  }

}