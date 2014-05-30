/*
 * MemberView.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.member;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

import com.syaku.core.util.*;

public class MemberView extends ActionMember {
  private Logger log = Logger.getLogger( this.getClass() );

  private boolean getMemberGrant(boolean grant) throws Exception {
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

  public String dispMemberUserIdSearch() throws Exception { return SUCCESS; }
  public String dispMemberPasswordSearch() throws Exception { return SUCCESS; }
  public String dispMemberAuthMailSend() throws Exception { return SUCCESS; }
  public String dispMemberAuthView() throws Exception { 
    String auth = param.value("auth");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("type","auth_key");
      mapSch.put("auth_key",auth);
      mapSch.put("success_date",DateUtils.date("yyyyMMddHHmmss"));
      daoMember.getMemberAuthSuccess(mapSch);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {

      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.getMessage().replaceAll("\n",""));
      MESSAGE.put("error","true");
      valuestack.push(MESSAGE);
      return "message";

    } finally {
      sqlMap.endTransaction();
    }

    return SUCCESS; 
  }

  public String dispMemberInsert() throws Exception {
      String member_orl = (String) session.get("_MEI_MEMBER_ORL");

    MemberBean memberbean = new MemberBean();

    if (StringUtils.isNotEmpty(member_orl)) {
      Map mapSch = new HashMap();
      mapSch.put("member_orl", member_orl);
      memberbean = (MemberBean) daoMember.sqlMemberObject(mapSch);
     }

     O.put("objMember" , memberbean );

    return SUCCESS;
  }

  public String dispMemberView() throws Exception {
      String member_orl = (String) session.get("_MEI_MEMBER_ORL");

      if ( !getMemberGrant( StringUtils.isNotEmpty(member_orl) ) ) {
        return "message";
      }

    Map mapSch = new HashMap();
    mapSch.put("member_orl", member_orl);
    O.put("listGroup",(List) daoMember.sqlGroupList(null));
    O.put("objMember" , (MemberBean) daoMember.sqlMemberObject(mapSch) );
    O.put("listGroupMember" , (List) daoMember.sqlGroupMemberList(mapSch) );

    return SUCCESS;
  }


  public String dispMemberLoginForm() throws Exception {
    // LAYOUT 정보가 있을 경우 해당 로그인화면이 존재한다면 호출함.
    if (MapUtils.isNotEmpty(LAYOUT)) {
      String layout = (String) layoutConfig.get("login_layout_hide");
      String path = meiConfig.getString("mei.path.absolute") + meiConfig.getString("mei.path.relative");
      String ftl = (String) layoutConfig.get("login_ftl");

      if (StringUtils.isNotEmpty(ftl)) {
        // 로그인폼 템플릿 변경
        File file = new File(path + ftl);
        if (file.exists()) { setMod_ftl_module(ftl); } // 템플릿 존재 여부 후 변경
      }

      if (StringUtils.equals(layout,"false") ) {
        MOD_FTL_MODULE_LAYOUT = ""; // 레이아웃 삭제
      }
    }

    return SUCCESS;
  }

}
