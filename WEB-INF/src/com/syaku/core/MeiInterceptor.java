/*
 * ModuleInterceptor.java 2011.03.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.core;

import java.util.*;

import org.apache.log4j.Logger;

import org.apache.commons.lang.*;

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.interceptor.*;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.util.*;

public class MeiInterceptor extends AbstractInterceptor {
  private Logger log = Logger.getLogger( this.getClass() );

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {
    long startTime = System.currentTimeMillis();
    log.debug("[## MEI MeiInterceptor > before] Called");

    ActionContext context = invocation.getInvocationContext();
    Map parameters = context.getParameters();
    ValueStack valuestack = context.getValueStack();

    String permission = null;
    String type = null;

    if (parameters != null) {
      permission = (String) parameters.get("act_permission");
      type = (String) parameters.get("act_type");
    }

    Map MESSAGE = new HashMap(); // 메세지
    MESSAGE.put("message","");
    MESSAGE.put("display","");
    MESSAGE.put("error","false");
    MESSAGE.put("action","");
    MESSAGE.put("source","");

    String PARAM_MID = (String) valuestack.findValue("PARAM_MID");
    
    // 올바르지 않는 MID 값도 오류 처리
    if (StringUtils.isEmpty(PARAM_MID)) {
      MESSAGE.put("message","MID 값이 없습니다.");
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

    String result = invocation.invoke();

    long executionTime = System.currentTimeMillis() - startTime;
    log.debug("[## MEI MeiInterceptor > after] Called");
    log.debug("[## MEI MeiInterceptor] Process Time : " + executionTime + "ms");
    return result;
  }


}