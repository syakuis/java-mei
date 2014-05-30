/*
 * CounterHit.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.counter;

import java.util.*;
import javax.servlet.http.*;
import org.apache.struts2.ServletActionContext;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
public class CounterHit extends CounterObject {
  private Logger log = Logger.getLogger( this.getClass() );

  private HttpServletRequest request = ServletActionContext.getRequest();
  private HttpServletResponse response = ServletActionContext.getResponse();

  public CounterHit() throws Exception {
    try {
      sqlMap.startTransaction();

      HashMap<String,String> mapCookie = (HashMap) setCookies(request.getCookies());
      String is = StringUtils.defaultString(mapCookie.get("_MEI_IS_COUNTER"),"");
      String ip = request.getRemoteAddr();

      if (!is.equals(ip)) {
        String rdate = DateUtils.date("yyyyMMddHHmmss");
        String user_agent = request.getHeader("User-Agent");

        CounterBean data = new CounterBean();
        data.setIp(ip);
        data.setRdate(rdate);
        data.setUser_agent(StringEscapeUtils.escapeSql(user_agent));
        sqlCounterInsert(data);

        Cookie cookie = new Cookie("_MEI_IS_COUNTER",ip);
        cookie.setMaxAge(216000);    // 하루
        response.addCookie(cookie);
      }

      sqlMap.commitTransaction();
    } catch (Exception e) {
      log.debug(e.toString()); // 로그 출력
    } finally {
      sqlMap.endTransaction();
    }
  
  }

  private HashMap setCookies(Cookie[] cookies) {
    HashMap mapRet = new HashMap();

    if(cookies != null){
      for (int i = 0; i < cookies.length; i++) {
        Cookie obj = cookies[i];
        mapRet.put(obj.getName(),obj.getValue());
      }
    }

    return mapRet;
  }

  private void reset() {
    Cookie cookie = new Cookie("_MEI_IS_COUNTER","");
    cookie.setMaxAge(0);
    response.addCookie(cookie); 
  }




}
