/*
 * FacebookAPI.java 2011.11.17
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

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

import oauth.signpost.*;
import oauth.signpost.basic.*;
import oauth.signpost.exception.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import net.sf.json.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import com.opensymphony.xwork2.*;
import org.apache.struts2.ServletActionContext;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;
import com.syaku.core.util.*;

public class FacebookAPI {
  private Logger log = Logger.getLogger( this.getClass() );
  private static final SqlMapClient sqlMap = MyBatis.getInstance();

  public HttpServletRequest request = ServletActionContext.getRequest();
  public HttpServletResponse response = ServletActionContext.getResponse();
  public HttpSession session = request.getSession();

  private SnsAuthObject mSnsAuthObject = new SnsAuthObject();
  private SnsAuthStored mSnsAuthStored = new SnsAuthStored();
  private SnsAuthHttp mSnsAuthHttp = new SnsAuthHttp();

   // user_id 변경이 가능한 계정이며, uid 고유한 숫자값이다.
  private String uid,user_id,nickname,profile_cover,post_send;
  public String getUid() { return this.uid; }
  public String getUser_id() { return this.user_id; }
  public String getNickname() { return this.nickname; }
  public String getProfile_cover() { return this.profile_cover; }
  public void setPost_send(String post_send) { this.post_send = post_send; }
  public String getPost_send() { return this.post_send; }

  final String API_URL = "https://graph.facebook.com";

  final String SNS_NAME = "FACEBOOK";
  private String API_APPID;
  private String API_APPSECRET;
  private String API_SCOPE;
  private String API_CALLBACK;

  public FacebookAPI(Map M_INFO) {
    this.API_APPID = (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_appid");
    this.API_APPSECRET = (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_appsecret");
    this.API_SCOPE = (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_scope");
    this.API_CALLBACK = (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_callback");
  }

  // 싱크 처리
  public void getSignIn() throws Exception {

    try {

      String token = mSnsAuthStored.getAuthCookie();

      if (StringUtils.isNotEmpty(token)) {

        if (mSnsAuthStored.getAuthSessValid(this.SNS_NAME) == false) {
          Map mapSch = new HashMap();
          mapSch.put("name",this.SNS_NAME);
          mapSch.put("token",token);
          SnsAuthBean objSns = (SnsAuthBean) mSnsAuthObject.sqlSnsAuthObject(mapSch);

          if (objSns != null) {
            String access_token = objSns.getAccess_token();
            getAuthSync(access_token);
          }

        }

        if (mSnsAuthStored.getAuthSessValid(this.SNS_NAME) == true) {
        Map<String,String> mapUser = this.getUserInfo();
        mSnsAuthStored.setUserInfo(this.SNS_NAME,mapUser);
        }

        mSnsAuthStored.setAuthCookie(token); // 갱신
      } else {
        mSnsAuthStored.getSessRemove(this.SNS_NAME);
      }

    } catch (Exception e) {
      log.error(e.toString());
      mSnsAuthStored.getSessRemove(this.SNS_NAME);
    }

  }

  public String getAccess(ParameterUtils param) throws Exception {
    String auth_url = null;

    String oauth_token = param.value("code","");
    String oauth_verifier = param.value("access_token","");

    String token = mSnsAuthStored.getAuthCookie();

    String access_token = null;
    String access_token_secret = null;

    Map mapSch = new HashMap();

    if (StringUtils.isEmpty(oauth_token)) {

      if (StringUtils.isNotEmpty(token)) {
        mapSch.clear();
        mapSch.put("name",this.SNS_NAME);
        mapSch.put("token",token);
        SnsAuthBean objSns = (SnsAuthBean) mSnsAuthObject.sqlSnsAuthObject(mapSch);

        if (objSns != null) {
          access_token = objSns.getAccess_token();
        }

        mSnsAuthStored.setAuthCookie(token); // 갱신
      }

      if (access_token != null) {
        getAuthSync(access_token);
        auth_url = "SUCCESS";
      } else {
        mSnsAuthStored.getSessRemove(this.SNS_NAME);
        auth_url = "https://www.facebook.com/dialog/oauth?client_id=" + this.API_APPID + "&redirect_uri=" + URLEncoder.encode(this.API_CALLBACK,"utf-8") + "&scope=" + URLEncoder.encode(this.API_SCOPE,"utf-8");
      }

    } else {

      try {
        sqlMap.startTransaction();
        
        if (StringUtils.isEmpty(token)) {
          // 쿠키 생성 및 토큰 디비저장.
          token = DigestUtils.sha256Hex(DigestUtils.sha256("syaku" + DateUtils.date("yyyyMMddHHmmss") + "me"));
        }
        access_token = getSuccess(oauth_token);
        access_token_secret = oauth_token; // code

        getAuthSync(access_token); // 세션
        Map<String,String> mapUser = this.getUserInfo();
        String uid = this.uid;
        String ip = request.getRemoteAddr();
        String user_agent = request.getHeader("User-Agent");

        mSnsAuthObject.getSnsAuthMainResetUpdate(token);

        SnsAuthBean snsauthbean = new SnsAuthBean();
        snsauthbean.setToken(token);
        snsauthbean.setName(this.SNS_NAME);
        snsauthbean.setAccess_token(access_token);
        snsauthbean.setAccess_token_secret(access_token_secret);
        snsauthbean.setMain("Y");
        snsauthbean.setPost_send("Y");
        snsauthbean.setUid(uid);
        snsauthbean.setReg_date(DateUtils.date("yyyyMMddHHmmss"));
        snsauthbean.setIp(ip);
        snsauthbean.setUser_agent(user_agent);
        mSnsAuthObject.sqlSnsAuthInsert(snsauthbean);

        mSnsAuthStored.setAuthCookie(token); // 갱신
        mSnsAuthStored.setUserInfo(this.SNS_NAME,mapUser);

        auth_url = "SUCCESS";

        sqlMap.commitTransaction();

      } catch (Exception e) {
        mSnsAuthStored.getSessRemove(this.SNS_NAME);
        log.error(e.toString()); // 로그 출력

      } finally {
        sqlMap.endTransaction();
      }

    }

    return auth_url;
  }

  public String getSuccess(String oauth_token) throws Exception {
    String access_token = null;
    String result = mSnsAuthHttp.getHttpGet( this.API_URL + "/oauth/access_token?client_id=" + this.API_APPID + "&redirect_uri=" + URLEncoder.encode(this.API_CALLBACK,"utf-8") + "&client_secret=" + URLEncoder.encode(this.API_APPSECRET,"utf-8") + "&code=" + URLEncoder.encode(oauth_token,"utf-8"));

    try {
      String[] at = result.split("=");
      access_token = at[1];
    } catch (Exception e) {
      getException(result);
    }

    return access_token;
  }

  public void getAuthSync(String access_token) throws Exception{

    if (StringUtils.isNotEmpty(access_token)) {
      mSnsAuthStored.setAuthSess(this.SNS_NAME,access_token);
    }

  }

  // 회원정보가져오기
  public Map<String,String> getUserInfo() throws Exception {
    this.uid = null;
    this.user_id = null;
    this.nickname = null;
    this.profile_cover = null;

    String access_token = (String) mSnsAuthStored.getAuthSess(this.SNS_NAME);

    Map<String,String> mapSS = (Map<String,String>) mSnsAuthStored.getUserInfo(this.SNS_NAME);
    Map<String,String> mapRet = new HashMap();

      if (MapUtils.isNotEmpty(mapSS)) {
        this.uid = mapSS.get("uid");
        this.user_id = mapSS.get("user_id");
        this.nickname = mapSS.get("nickname");
        this.profile_cover = mapSS.get("profile_cover");
      } else {

      String result = mSnsAuthHttp.getHttpGet(this.API_URL + "/me?access_token=" + URLEncoder.encode(access_token,"utf-8"),null);

      try {

        JSONObject objJson = JSONObject.fromObject(result);
        String uid = objJson.getString("id"); // id
        String user_id = (String) objJson.get("username");

        String nickname = objJson.getString("name");
        String profile_cover = "https://graph.facebook.com/" + uid + "/picture";

        this.uid = uid;
        this.user_id = user_id;
        this.nickname = nickname;
        this.profile_cover = profile_cover;

      } catch (Exception e) {
        mSnsAuthStored.getSessRemove(this.SNS_NAME);
        getException(result);
      }
    }

    if (this.user_id != null || this.nickname != null || this.profile_cover != null) {
      mapRet.put("user_id",this.user_id);
      mapRet.put("nickname",this.nickname);
      mapRet.put("profile_cover",this.profile_cover);
      mapRet.put("uid",this.uid);
    }

    return mapRet;

  }


  public String getPost(Map map) throws Exception {
    String access_token = (String) mSnsAuthStored.getAuthSess(this.SNS_NAME);

    String msg_id = null;
    String message = (String) map.get("message");
    String parent_msg_id = (String) map.get("parent_msg_id");
    String url = (String) map.get("url");
    String description = (String) map.get("description");
    String tag = "봉안닷컴";

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair("access_token", access_token));
    formparams.add(new BasicNameValuePair("message", message));
    formparams.add(new BasicNameValuePair("name", tag));
    formparams.add(new BasicNameValuePair("description", description));
    formparams.add(new BasicNameValuePair("picture", ""));
    formparams.add(new BasicNameValuePair("link", url));
    UrlEncodedFormEntity formentity = new UrlEncodedFormEntity(formparams, "UTF-8");

    String result = mSnsAuthHttp.getHttpPost(this.API_URL + "/me/feed",formentity,null);
    log.debug("[MEI FACEBOOK POST result]" + result);

    JSONObject objJson = JSONObject.fromObject(result);
    try {
      msg_id = objJson.getString("id");
    } catch (Exception e) {
      getException(result);
    }

    log.debug("[MEI FACEBOOK POST ID]" + msg_id);
    return msg_id;
  }

  public void getDelete(String msg_id) throws Exception {
    String access_token = (String) mSnsAuthStored.getAuthSess(this.SNS_NAME);

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair("access_token", access_token));
    formparams.add(new BasicNameValuePair("method", "DELETE"));
    UrlEncodedFormEntity formentity = new UrlEncodedFormEntity(formparams, "UTF-8");

    String result = mSnsAuthHttp.getHttpPost(this.API_URL + "/" + msg_id,formentity,null);
    log.debug("FACEBOOK getDelete : " + result);
  }

  public void getException(String json) throws Exception {
    String msg_text = null;
    JSONObject objJson = JSONObject.fromObject(json);
    JSONObject objErr = objJson.getJSONObject("error");
    try {
      msg_text = objErr.getString("message");
    } catch (Exception e) {
    }

    if (StringUtils.isNotEmpty(msg_text)) {
      log.debug("FACEBOOK : " + msg_text);
      //throw new Exception("FACEBOOK : " + msg_text);
    }

  }

}
