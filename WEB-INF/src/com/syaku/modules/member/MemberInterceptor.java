/*
 * ModuleInterceptor.java 2011.03.15
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

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.interceptor.*;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.util.*;

public class MemberInterceptor extends AbstractInterceptor {
  private Logger log = Logger.getLogger( this.getClass() );
  public MemberObject daoMember = new MemberObject();

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {
    log.debug("[## MEI MemberInterceptor intercept] Called");
    ActionContext context = invocation.getInvocationContext();
    Map parameters = context.getParameters();
    ValueStack valuestack = context.getValueStack();
    Map session = context.getSession();

    Map MESSAGE = new HashMap(); // 메세지
    MESSAGE.put("message","");
    MESSAGE.put("display","");
    MESSAGE.put("error","false");
    MESSAGE.put("action","");
    MESSAGE.put("source","");

    String PARAM_MID = (String) valuestack.findValue("PARAM_MID");
    String MID_MODULE_ORL = (String) valuestack.findValue("MID_MODULE_ORL");

    log.debug("[## MEI MemberInterceptor PARAM_MID] " + PARAM_MID);
    log.debug("[## MEI MemberInterceptor MID_MODULE_ORL] " + MID_MODULE_ORL);

    String permission = null;
    String type = null;

    if (parameters != null) {
      permission = (String) parameters.get("act_permission");
      type = (String) parameters.get("act_type");
    }

    log.debug("[## MEI MemberInterceptor permission] " + permission);
    log.debug("[## MEI MemberInterceptor type] " + type);

    if (!StringUtils.equals(permission,"everyone")) {

    String MEI_USER_ID = (String) session.get("_MEI_USER_ID");
    String MEI_USER_NAME = (String) session.get("_MEI_USER_NAME");
    String MEI_NICKNAME = (String) session.get("_MEI_NICKNAME");
    String MEI_MEMBER_ORL = (String) session.get("_MEI_MEMBER_ORL");

    // 권한 가져오기 호출 (코어에서 생성된 정보)
    boolean MEI_GRANT_LOGIN = (Boolean) valuestack.findValue("MEI_GRANT_LOGIN");
    boolean MEI_GRANT_ADMIN = (Boolean) valuestack.findValue("MEI_GRANT_ADMIN");
    boolean MEI_GRANT_MINE = (Boolean) valuestack.findValue("MEI_GRANT_MINE");
    boolean MEI_GRANT_ACCESS = (Boolean) valuestack.findValue("MEI_GRANT_ACCESS");
    boolean MEI_GRANT_LIST = (Boolean) valuestack.findValue("MEI_GRANT_LIST");
    boolean MEI_GRANT_VIEW = (Boolean) valuestack.findValue("MEI_GRANT_VIEW");
    boolean MEI_GRANT_WRITE = (Boolean) valuestack.findValue("MEI_GRANT_WRITE");
    boolean MEI_GRANT_COMMENT = (Boolean) valuestack.findValue("MEI_GRANT_COMMENT");
    boolean MEI_GRANT_MANAGER = (Boolean) valuestack.findValue("MEI_GRANT_MANAGER");

    // 로그인 체크
    if (StringUtils.equals(permission,"member")) {
      if (!daoMember.getIsLogin(session)) {

        if (StringUtils.equals(type,"view")) {
          daoMember.getLoginRemove(session);

          return "login";
        } else {

          MESSAGE.put("message","로그인 하셔야 합니다.");
          MESSAGE.put("display","alert");
          MESSAGE.put("error","true");
          MESSAGE.put("action","");
          MESSAGE.put("source","");
          valuestack.push(MESSAGE);
          return "message";

        }

      }

    }


    // 접근 권한 체크
    if (StringUtils.isNotEmpty(PARAM_MID)) {

      if (!MEI_GRANT_ACCESS) { // 접속권한

        if (!daoMember.getIsLogin(session)) {
          daoMember.getLoginRemove(session);

          return "login";
        } else {
          MESSAGE.put("message","권한이 없습니다.");
          MESSAGE.put("error","true");
          MESSAGE.put("action","");
          MESSAGE.put("source","");

          if (StringUtils.equals(type,"view")) {
            MESSAGE.put("display","");
          } else {
            MESSAGE.put("display","alert");
          }

          valuestack.push(MESSAGE);

          return "message";
        }

      }

    }

    }

    String result = invocation.invoke();

    return result;
  }


}