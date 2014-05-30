 /*
 * IssueController.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.issue;

import java.util.*;

import org.apache.log4j.Logger;

import org.apache.commons.lang.*;
import org.apache.commons.io.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;


public class IssueController extends ActionIssue {
  private Logger log = Logger.getLogger( this.getClass() );

  public String procIssueInsert() throws Exception {

      String issue_orl = param.value("issue_orl");
      String group_orl = param.value("group_orl");
      String member_orl = "0";
      String category_orl = param.value("category_orl");
      String user_id = null;
      String user_name = null;
      String nickname = null;
      String title = param.value("title");
      String content = param.value("content");
      String state_code_orl = param.value("state_code_orl");
      String ipaddress = request.getRemoteAddr();


    if (StringUtils.isNotEmpty((String) session.get("_MEI_USER_ID"))) {
      member_orl = (String) session.get("_MEI_MEMBER_ORL");
      user_id = (String) session.get("_MEI_USER_ID");
      user_name = (String) session.get("_MEI_USER_NAME");
      nickname = (String) session.get("_MEI_NICKNAME");
     }

    try {
      sqlMap.startTransaction();

      IssueBean bean = new IssueBean();

      bean.setIssue_orl(issue_orl);
      bean.setGroup_orl(group_orl);
      bean.setModule_orl(MODULE_ORL);
      bean.setMember_orl(member_orl);
      bean.setCategory_orl(category_orl);
      bean.setUser_id(user_id);
      bean.setUser_name(user_name);
      bean.setNickname(nickname);
      bean.setTitle(title);
      bean.setContent(content);
      bean.setState_code_orl(state_code_orl);
      bean.setIssue_root("Y");
      bean.setIpaddress(ipaddress);
      bean.setReg_date(DateUtils.date("yyyyMMddHHmmss"));

      // 관련글 추가인 경우
      Map mapSch = new HashMap();
      if ( StringUtils.isNotEmpty(group_orl) && !StringUtils.equals(group_orl,"0") ) {
        mapSch = new HashMap();
        mapSch.put("group_orl",group_orl);
        daoIssue.sqlIssueRootNUpdate(mapSch);
      }

      issue_orl = daoIssue.sqlIssueInsert(bean);

      // 파일첨부 처리
      mapSch = new HashMap();
      mapSch.put("sid", param.value("sid") );
      mapSch.put("seq","1");
      mapSch.put("module_orl", MODULE_ORL );
      mapSch.put("target_orl",issue_orl);
      daoFile.getFileUpdateTarget(mapSch);

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

  public String procIssueDelete() throws Exception {

    String group_orl = param.value("group_orl");
    String issue_orl = param.value("issue_orl");

    try {

      sqlMap.startTransaction();

      Map mapSch = new HashMap();

      mapSch = new HashMap();
      mapSch.put("issue_orl",issue_orl);
      daoIssue.sqlIssueDelete(mapSch);

      // 파일첨부
      mapSch = new HashMap();
      mapSch.put("module_orl",MODULE_ORL);
      mapSch.put("target_orl",issue_orl);
      mapSch.put("seq","1");
      daoFile.getFileDeleteTarget(mapSch);

      mapSch = new HashMap();
      mapSch.put("group_orl",group_orl);
      long issue_orl_long = daoIssue.sqlIssueMax(mapSch);

      if (issue_orl_long > 0) {
      mapSch = new HashMap();
      mapSch.put("issue_orl",issue_orl_long);
      daoIssue.sqlIssueRootYUpdate(mapSch);
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
}