/*
 * DefaultAction.java 2011.03.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.util.*;
import org.apache.struts2.ServletActionContext;

import org.apache.commons.configuration.*;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

import com.syaku.core.util.*;

import com.syaku.modules.admin.*;
import com.syaku.modules.module.*;

public class DefaultAction extends ActionSupport {
  private Logger log = Logger.getLogger( this.getClass() );

  public HttpServletRequest request = ServletActionContext.getRequest();
  public ParameterUtils param = new ParameterUtils(request);
  public ValueStack valuestack;

  private ModuleObject daoModule = new ModuleObject();
  private AdminObject daoAdmin = new AdminObject();

  public String url;

  public String execute() throws Exception {
    ActionContext context = ActionContext.getContext();
    valuestack = context.getValueStack();
    Map MESSAGE = new HashMap(); // 메세지
    String mid = param.value("mid");

    Map mapModule = new HashMap();
    Map mapSch = new HashMap();

    if (StringUtils.isNotEmpty(mid)) {
      mapSch.put("mid",mid);
      mapModule = (Map) daoModule.objectOne(mapSch);
      if ( MapUtils.isEmpty(mapModule) ) { mid = ""; }
    }

    if (StringUtils.isEmpty(mid)) {
      AdminBean adminbean = (AdminBean) daoAdmin.sqlAdminObject(null);
      if (adminbean != null) {
        String module_orl = adminbean.getModule_orl();
        mapSch.put("module_orl",module_orl);
        mapModule = (Map) daoModule.objectOne(mapSch);
        mid = (String) mapModule.get("mid");
      }
    }

    if ( MapUtils.isNotEmpty(mapModule) ) {
      String module = (String) mapModule.get("module");
      Configuration config = new PropertiesConfiguration("/com/syaku/modules/" + module + "/info.properties");
      url = config.getString("mei.struts.default.action");
    }

    if ( StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(mid) ) {
      url = url + ".me?mid=" + mid;
      return "redirect";
    } else {
      MESSAGE.put("message","페이지를 찾을 수 없습니다.");
      MESSAGE.put("display","");
      MESSAGE.put("error","true");
      MESSAGE.put("action","");
      MESSAGE.put("source","");
      valuestack.push(MESSAGE);

      return "message";
    }

  }

}