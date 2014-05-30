/*
 * CommentObject.java 2011.06.15
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
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;

public class CommentObject extends CommentAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration meiConfig = ConfigUtils.getProperties("mei.properties"); // mei 정보
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/comment/info.properties");

  private final String DELETE_CONTENT = "삭제된 게시물입니다.";

  public List getCommentList(Map map,String sess_member_orl) throws Exception {
    return getCommentList(map,sess_member_orl,false);
  }
  public List getCommentList(Map map,String sess_member_orl,Boolean is_manager) throws Exception {
    boolean is_member = StringUtils.isNotEmpty(sess_member_orl); // 권한 체크

    List listComment = new ArrayList();
    List list = sqlCommentMapList(map);
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      Map obj = (HashMap) list.get(i);

      String member_orl = ObjectUtils.toString((Number) obj.get("member_orl"));
      String content = (String) obj.get("content");
      String content_orig = content;
      String is_del = (String) obj.get("is_del");
      if (StringUtils.equals(is_del,"Y")) {
        content = DELETE_CONTENT;
      }
      Boolean is_mine = false;

      // 권한체크
      if (is_member) {
        if (StringUtils.equals(sess_member_orl,member_orl) || is_manager) { is_mine = true; }
      }

      obj.put("content",content);
      obj.put("content_orig",content_orig);
      obj.put("is_mine",is_mine);
      listComment.add(i,obj);
    }

    return listComment;
  }

  // SNS 전용 목록
  public List getCommentSnsList(Map map,String sess_member_orl,Boolean is_manager,Map mapSns) throws Exception {
    boolean is_member = StringUtils.isNotEmpty(sess_member_orl); // 권한 체크

    // SNS 경로
    Map<String,String> mapDomain = new HashMap<String,String>();
    mapDomain.put("TWITTER","http://twitter.com/?id=");
    mapDomain.put("FACEBOOK","http://www.facebook.com/");
    mapDomain.put("ME2DAY","http://me2day.net/");
    mapDomain.put("YOZM","http://yozm.daum.net/");

    List listComment = new ArrayList();
    List list = sqlCommentMapList(map);
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      Map obj = (HashMap) list.get(i);
      
      Boolean is_mine = false;

      String member_orl = ObjectUtils.toString((Number) obj.get("member_orl"));

      String content = (String) obj.get("content");
      String content_orig = content;
      String is_del = (String) obj.get("is_del");
      if (StringUtils.equals(is_del,"Y")) {
        content = DELETE_CONTENT;
      }

      String user_id = (String) obj.get("user_id");
      String sns_name = (String) obj.get("sns_name");
      String sns_id = (String) obj.get("sns_id"); // sns uid

      String sns_url = "";
      if (StringUtils.isNotEmpty(sns_name)) {
        sns_url = mapDomain.get(sns_name) + sns_id;
      }
      obj.put("sns_url",sns_url);

      // 권한체크
      if (is_member) {
        if (StringUtils.equals(sess_member_orl,member_orl) || is_manager) { is_mine = true; }
      }

      if (StringUtils.isNotEmpty(sns_name) && !is_mine) {
        if (MapUtils.isNotEmpty(mapSns)) {
          String uid = (String) mapSns.get(sns_name.toLowerCase() + "_uid"); // sns uid
          if (StringUtils.equals(uid,sns_id)) { is_mine = true; }
        }
      }
      obj.put("content",content);
      obj.put("content_orig",content_orig);
      obj.put("is_mine",is_mine);
      listComment.add(i,obj);
    }

    return listComment;
  }

  public Long getCommentCounterCountObject(String module_orl,String target_orl) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    mapSch.put("target_orl",target_orl);

    return sqlCommentCounterCountObject(mapSch);
  }

  public void getCommentCounterInsert(String module_orl,String target_orl) throws Exception {
    String reg_date = DateUtils.date("yyyyMMddHHmmss");

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    mapSch.put("target_orl",target_orl);
    long total = sqlCommentCounterCount(mapSch);
    long count = sqlCommentCount(mapSch);

    CommentCounterBean md = new CommentCounterBean();
    md.setModule_orl(module_orl);
    md.setTarget_orl(target_orl);
    md.setCount("" + count);
    md.setReg_date(reg_date);
    if (total == 0) {
      sqlCommentCounterInsert(md);
    } else {
      md.setLast_update(reg_date);
      sqlCommentCounterUpdate(md);
    }

  }

  public void getCommentTargetDelete(String module_orl,String target_orl) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    mapSch.put("target_orl",target_orl);

    sqlCommentTargetDelete(mapSch);
    getCommentCounterInsert(module_orl,target_orl);
  }

  // getCommentSkinList
  public List skinList() {
    String MEI_PATH_ABSOLUTE_RELATIVE = meiConfig.getString("mei.path.absolute_relative");
    String module = MEI_PATH_ABSOLUTE_RELATIVE + modConfig.getString("mei.module.path.skins");
    List listSkins = new ArrayList();

    try {
      if (StringUtils.isEmpty(module)) { return listSkins; }

      File dir = new File(module);
      if (!dir.exists()) { return listSkins; }

      File dir_arr[] = dir.listFiles();
      int count = dir_arr.length;
      int o = 0;

      for (int i = 0; i < count; i++) {
        File info = dir_arr[i];
        if (dir_arr[i].isDirectory()) {
          String name = info.getName();
          if (name.equals(".svn") || StringUtils.isEmpty(name)) { // SVN 폴더 제외
            continue;
          }

          listSkins.add(o,name);
          o++;
        }
      }

    } catch (Exception e) {
      log.error("[#MEI CommentObject.skinList]" + e.toString() );
    }

    return listSkins;
  }


  public void getCommentModuleDelete(String module_orl) throws Exception {
    HashMap mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    sqlCommentModuleDelete(mapSch);
    sqlCommentCounterModuleDelete(mapSch);
  }

}