/*
 * SnsAuthStored.java 2011.10.20
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.snsauth;

import java.util.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import com.opensymphony.xwork2.*;
import org.apache.struts2.ServletActionContext;

import oauth.signpost.*;
import oauth.signpost.basic.*;
import oauth.signpost.exception.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class SnsAuthStored {
  private Logger log = Logger.getLogger( this.getClass() );
  public final int COOKIE_TIME = 2592000; // 60*60*24*30 = 30일

  public HttpServletRequest request = ServletActionContext.getRequest();
  public HttpServletResponse response = ServletActionContext.getResponse();
  public HttpSession session = request.getSession();

  public void setSessCreate(String sns_name,OAuthConsumer consumer,OAuthProvider provider) throws Exception{ // OAuth
    String token = consumer.getToken();
    session.setAttribute("_MEI_" + token + "_NAME",sns_name);
    session.setAttribute("_MEI_" + token + "_CR",consumer);
    session.setAttribute("_MEI_" + token + "_PR",provider);
    session.setAttribute("_MEI_" + token + "_REQT",consumer.getToken());
    session.setAttribute("_MEI_" + token + "_REQTS",consumer.getTokenSecret());
  }

  public void setSessCreate(String sns_name,String token) throws Exception{ // 미투데이, 페이스북
    session.setAttribute("_MEI_" + token + "_NAME",sns_name);
    session.setAttribute("_MEI_" + token + "_REQT",token);
  }

  public void setSessClean(String token) throws Exception{
     session.removeAttribute("_MEI_" + token + "_NAME");
     session.removeAttribute("_MEI_" + token + "_CR");
     session.removeAttribute("_MEI_" + token + "_PR");
     session.removeAttribute("_MEI_" + token + "_REQT");
     session.removeAttribute("_MEI_" + token + "_REQTS");
  }


  public void setAuthSess(String sns_name,OAuthConsumer consumer) throws Exception { // OAuth
    session.setAttribute("_MEI_" + sns_name + "_CR",consumer);
  }
  public void setAuthSess(String sns_name,String token) throws Exception { // 미투데이 , 페이스북
    session.setAttribute("_MEI_" + sns_name + "_CR",token);
  }
  public Object getAuthSess(String sns_name) throws Exception{
    return (Object) session.getAttribute("_MEI_" + sns_name + "_CR");
  }
  public boolean getAuthSessValid(String sns_name) throws Exception {
    return (getAuthSess(sns_name) != null);
  }

  public void setAuthCookie(String token) throws Exception {
    Cookie cookie;
    cookie = new Cookie("_MEI_SNS_TOKEN",token);
    cookie.setMaxAge(COOKIE_TIME);
    cookie.setPath("/");
    response.addCookie(cookie);
  }
  public String getAuthCookie() throws Exception {
    HashMap<String,String> mapCookie = (HashMap) setCookies(request.getCookies());
    String token = mapCookie.get("_MEI_SNS_TOKEN");
    return token;
  }

  public void getSessRemove(String sns_name) throws Exception{
   session.removeAttribute("_MEI_" + sns_name + "_CR");
   session.removeAttribute("_MEI_" + sns_name + "_SUSERINFO");
  }

  public void getDestroy() throws Exception{
    Cookie cookie;
    cookie = new Cookie("_MEI_SNS_TOKEN",null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
  }

  // user_id,nickname,profile_cover
  public void setUserInfo(String sns_name,Map map) throws Exception{
    session.setAttribute("_MEI_" + sns_name + "_SUSERINFO",map);
  }

  public Map<String,String> getUserInfo(String sns_name) throws Exception{
    return (Map<String,String>) session.getAttribute("_MEI_" + sns_name + "_SUSERINFO");
  }

  public HashMap setCookies(Cookie[] cookies) {
    HashMap mapRet = new HashMap();

    if(cookies != null){
      for (int i = 0; i < cookies.length; i++) {
        Cookie obj = cookies[i];
        mapRet.put(obj.getName(),obj.getValue());
      }
    }

    return mapRet;
  }

}
