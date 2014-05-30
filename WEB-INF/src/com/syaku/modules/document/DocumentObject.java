/*
 * DocumentObject.java 2011.05.23
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
import org.apache.commons.configuration.*;
import org.apache.commons.lang.time.DateUtils;

import com.syaku.core.util.*;

public class DocumentObject extends DocumentAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  private org.apache.commons.lang.time.DateUtils DateUtils2 = new org.apache.commons.lang.time.DateUtils();
  private com.syaku.core.util.DateUtils DateUtils = new com.syaku.core.util.DateUtils();
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/document/info.properties");

  public void getDocumentModuleDelete(Map map) throws Exception {
    sqlDocumentModuleDelete(map);
    sqlDocumentExtraKeysModuleDelete(map);
    sqlDocumentExtraVarsModuleDelete(map);
  }

  public List getDocumentPageList(Map map,Map modConfig) throws Exception {

    boolean is_reply = ( StringUtils.equals( (String) modConfig.get("options_is_reply"),"Y") );
    boolean is_comment = ( StringUtils.equals( (String) modConfig.get("options_is_comment"),"Y") );
    boolean is_category = ( StringUtils.equals( (String) modConfig.get("options_is_category") , "Y") );
    String options_icons = StringUtils.defaultIfEmpty( (String) modConfig.get("options_icons"),"");

    boolean is_new = (StringUtils.indexOf(options_icons,"new") > -1);
    boolean is_update = (StringUtils.indexOf(options_icons,"update") > -1);
    boolean is_file = (StringUtils.indexOf(options_icons,"file") > -1);

    boolean is_extravars = (StringUtils.equals((String) modConfig.get("options_is_extravars") , "Y"));

    String now_date = DateUtils.date("yyyyMMddHHmmss");

    List listRet = new ArrayList();
    List list = new ArrayList();

    if (is_reply) {
      list = sqlDocumentReplyPageList(map);
    } else {
      list = sqlDocumentPageList(map);
    }

    int count = list.size();
    for (int i = 0; i < count; i++ ) {
      DocumentBean documentbean= (DocumentBean) list.get(i);


      String document_orl = documentbean.getDocument_orl();
      String category_orl = documentbean.getCategory_orl();
      String module_orl = documentbean.getModule_orl();
      String member_orl = documentbean.getMember_orl();
      String user_id = documentbean.getUser_id();
      String user_name = documentbean.getUser_name();
      String password = documentbean.getPassword();
      String nickname = documentbean.getNickname();
      String email = documentbean.getEmail();
      String homepage = documentbean.getHomepage();
      String title = documentbean.getTitle();
      String content = documentbean.getContent();
      String extra_vars = documentbean.getExtra_vars();
      String readed_count = documentbean.getReaded_count();
      String vote_count = documentbean.getVote_count();
      String ipaddress = documentbean.getIpaddress();
      String regdate = documentbean.getRegdate();
      String last_update = documentbean.getLast_update();
      String last_updater = documentbean.getLast_updater();
      String parent_orl = documentbean.getParent_orl();
      String reply_group = documentbean.getReply_group();
      String reply_depth = documentbean.getReply_depth();
      String reply_num = documentbean.getReply_num();
      String grouporder = documentbean.getGrouporder();
      String listorder = documentbean.getListorder();
      String is_notice = documentbean.getIs_notice();
      String title_bold = documentbean.getTitle_bold();
      String title_color = documentbean.getTitle_color();

      Date date = DateUtils.setDate(regdate);
      date = DateUtils2.addDays(date,1);
      String new_regdate = DateUtils.date("yyyyMMddHHmmss",date);

      Boolean document_new = false;
      if (is_new && (NumberUtils.createLong(new_regdate) > NumberUtils.createLong(now_date))) {
        document_new = true;
      }

      String category_title = (is_category) ? documentbean.getCategory_title() : "";
      String comment_count = (is_comment) ? documentbean.getComment_count() : "0";
      String file_count = (is_file) ? documentbean.getFile_count() : "0";

      Map mapRet = new HashMap();
      mapRet.put("document_orl",document_orl);
      mapRet.put("category_orl",category_orl);
      mapRet.put("module_orl",module_orl);
      mapRet.put("member_orl",member_orl);
      mapRet.put("user_id",user_id);
      mapRet.put("user_name",user_name);
      mapRet.put("password",password);
      mapRet.put("nickname",nickname);
      mapRet.put("email",email);
      mapRet.put("homepage",homepage);
      mapRet.put("title",title);
      mapRet.put("content",content);
      mapRet.put("extra_vars",extra_vars);
      mapRet.put("readed_count",readed_count);
      mapRet.put("vote_count",vote_count);
      mapRet.put("ipaddress",ipaddress);
      mapRet.put("regdate",regdate);
      mapRet.put("last_update",last_update);
      mapRet.put("last_updater",last_updater);
      mapRet.put("parent_orl",parent_orl);
      mapRet.put("reply_group",reply_group);
      mapRet.put("reply_depth",reply_depth);
      mapRet.put("reply_num",reply_num);
      mapRet.put("grouporder",grouporder);
      mapRet.put("listorder",listorder);

      mapRet.put("document_new",document_new);
      mapRet.put("category_title",category_title);
      mapRet.put("is_new",is_new);
      mapRet.put("comment_count",comment_count);
      mapRet.put("file_count",file_count);

      mapRet.put("is_notice",is_notice);
      mapRet.put("title_bold",title_bold);
      mapRet.put("title_color",title_color);

      listRet.add(i,mapRet);
    }

    return listRet;

  }
/*
  public List getDocumentExtraKeysList(Map map) throws Exception { return (List) sqlDocumentExtraKeysList(map); }
  public Object getDocumentExtraKeysObject(Map map) throws Exception { return (Object) sqlDocumentExtraKeysObject(map); }
  public List getDocumentExtraList(Map map) throws Exception { return (List) sqlDocumentExtraList(map); }
  public List getDocumentExtraView(Map map) throws Exception { return (List) sqlDocumentExtraView(map); }
*/

  public Map getDocumentExtraMap(Map map) throws Exception {
    Map mapData = new HashMap();
    List list = sqlDocumentExtraView(map);
    int count = list.size();
    for (int i = 0; i < count; i++) {
      Map obj = (HashMap) list.get(i);
      Number var_idx = (Number) obj.get("var_idx");
      mapData.put("extra_vars" + var_idx,obj);
    }

    return mapData;
  }

  public Map getDocumentExtraVarsMap(Map map) throws Exception {
    Map mapData = new HashMap();

    List list = sqlDocumentExtraVarsList(map);
    int count = list.size();
    for (int i = 0; i < count; i++) {
      DocumentExtraVarsBean obj = (DocumentExtraVarsBean) list.get(i);
      String module_orl = obj.getModule_orl();
      String document_orl = obj.getDocument_orl();
      String var_idx = obj.getVar_idx();
      String value = obj.getValue();
      String eid = obj.getEid();
      mapData.put("extra_vars" + var_idx,value);
    }

    return mapData;

  }

  public void getDocumentExtraVarsInsert(String module_orl,String document_orl,Map map) throws Exception {
    List listExtra = new ArrayList();
    Map mapRet = new HashMap();

    Map mapData = new HashMap();

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    List list = sqlDocumentExtraKeysList(mapSch);
    int count = list.size();
    for (int i = 0; i < count; i++) {
      DocumentExtraKeysBean obj = (DocumentExtraKeysBean) list.get(i);
      String var_idx = obj.getVar_idx();
      String var_name = obj.getVar_name();
      String var_type = obj.getVar_type();
      String var_is_required = obj.getVar_is_required();
      String var_search = obj.getVar_search();
      String var_default = obj.getVar_default();
      String var_desc = obj.getVar_desc();
      String eid = obj.getEid();

      mapData.put("extra_vars" + var_idx,eid);
    }

    mapSch.clear();
    mapSch.put("document_orl",document_orl);
    sqlDocumentExtraVarsDocumentDelete(mapSch);

    int total = 0;
    Iterator iterator = map.keySet().iterator();
    while(iterator.hasNext()){
      String name = (String) iterator.next();
      String value = (String) map.get(name);

      if (StringUtils.isNotEmpty(name)) {
        String var_idx = name.replaceAll("extra_vars","");
        String eid = (String) mapData.get(name);

        Map mapD = new HashMap();
        mapD.put("var_idx",var_idx);
        mapD.put("value",value);
        mapD.put("eid",eid);

        listExtra.add(total,mapD);
        total++;

      }

    }

    if (total > 0) {
      mapRet.put("module_orl",module_orl);
      mapRet.put("document_orl",document_orl);
      mapRet.put("listExtra",listExtra);
      sqlDocumentExtraVarsIterateInsert(mapRet);
    }
  }

}