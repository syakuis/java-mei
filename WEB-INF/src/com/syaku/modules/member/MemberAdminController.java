/*
 * MemberAdminController.java 2011.06.08
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
import org.apache.commons.lang.*;

import com.google.gson.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class MemberAdminController extends ActionMember {
  private Logger log = Logger.getLogger( this.getClass() );

  private MemberException exception = new MemberException();

  public String procMemberAdminInsert() throws Exception {

    try {
      sqlMap.startTransaction();
      exception.getGrant(MEI_GRANT_MANAGER);

      MemberInsert memberInsert = new MemberInsert( meiConfig , modConfig , MM , MOD_FTL_MODULE_SKIN );
      memberInsert.prosses(param,true);

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

  public String procMemberAdminDelete() throws Exception {
    String member_orl = param.value("member_orl");

    try {
      sqlMap.startTransaction();

      HashMap mapSch = new HashMap();
      mapSch.put("member_orl",member_orl);

      daoMember.getMemberDelete(param);

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


  public String procGroupAdminInsert() throws Exception {

    String group_orl = param.value("group_orl");
    String title = param.value("title");
    String is_default = param.valueIfEmpty("is_default","N");
    String description = param.value("description");
    String regdate = DateUtils.date("yyyyMMddHHmmss");

    try {
      sqlMap.startTransaction();

      GroupBean groupbean = new GroupBean();

      groupbean.setGroup_orl(group_orl);
      groupbean.setTitle(title);
      groupbean.setRegdate(regdate);
      groupbean.setIs_default(is_default);
      groupbean.setIs_admin("N");
      groupbean.setDescription(description);

      // 기본 그룹 초기화
      if (StringUtils.equals(is_default,"Y")) {
        daoMember.sqlGroupUpdateDefaultReset();
      }

      if (StringUtils.isEmpty(group_orl)) {
        daoMember.sqlGroupInsert(groupbean);
      } else {
        daoMember.sqlGroupUpdate(groupbean);
      }

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

  public String procGroupAdminDelete() throws Exception {
    String group_orl = param.value("group_orl");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("group_orl",group_orl);
      daoMember.sqlGroupMemberGroupDelete(mapSch);
      daoModule.sqlModuleGrantsGroupDelete(mapSch);
      daoMember.sqlGroupDelete(mapSch);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }


}
