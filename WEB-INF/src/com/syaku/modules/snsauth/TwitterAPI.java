/*
 * TwitterAPI.java 2011.10.20
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.snsauth;

import java.util.*;

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

public class TwitterAPI extends SnsOAuthAPI {
  private Logger log = Logger.getLogger( this.getClass() );
  private static final SqlMapClient sqlMap = MyBatis.getInstance();

  public HttpServletRequest request = ServletActionContext.getRequest();
  public HttpServletResponse response = ServletActionContext.getResponse();
  public HttpSession session = request.getSession();

  private SnsAuthObject mSnsAuthObject = new SnsAuthObject();
  private SnsAuthStored mSnsAuthStored = new SnsAuthStored();
  private SnsAuthHttp mSnsAuthHttp = new SnsAuthHttp();

  private String uid,user_id,nickname,profile_cover,post_send;
  public String getUid() { return this.uid; }
  public String getUser_id() { return this.user_id; }
  public String getNickname() { return this.nickname; }
  public String getProfile_cover() { return this.profile_cover; }
  public void setPost_send(String post_send) { this.post_send = post_send; }
  public String getPost_send() { return this.post_send; }

  final String API_URL = "http://api.twitter.com/1";
  final String SNS_NAME = "TWITTER";
  final int TEXT_LENGTH = 140;

  public TwitterAPI(Map M_INFO) {
    this.setRequest_token_url( (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_requesttokenurl") );
    this.setAuthorize_url( (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_authorizeurl") );
    this.setAccess_token_url( (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_accesstokenurl") );
    this.setConsumer_key( (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_consumerkey") );
    this.setConsumer_secret( (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_consumersecret") );
    this.setCallback( (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_callback") );
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
          String access_token_secret = objSns.getAccess_token_secret();

          this.getInstance();
          getAuthSync(access_token,access_token_secret);
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

  public String getAccess(ParameterUtils param) throws Exception{
    String auth_url = null;
    String oauth_token = param.value("oauth_token");
    String oauth_verifier = param.value("oauth_verifier");

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
          access_token_secret = objSns.getAccess_token_secret();
        }

        mSnsAuthStored.setAuthCookie(token); // 갱신
      }

      this.getInstance();
      if (access_token != null && access_token_secret != null) {
        
        getAuthSync(access_token,access_token_secret);
        auth_url = "SUCCESS";

      } else {
        mSnsAuthStored.getSessRemove(this.SNS_NAME);
        auth_url = getRequestToken();
        mSnsAuthStored.setSessCreate(this.SNS_NAME,this.consumer,this.provider);

      }

    } else {


      try {
        sqlMap.startTransaction();
        
        if (StringUtils.isEmpty(token)) {
          // 쿠키 생성 및 토큰 디비저장.
          token = DigestUtils.sha256Hex(DigestUtils.sha256("syaku" + DateUtils.date("yyyyMMddHHmmss") + "me"));
        }

        getSuccess(oauth_token,oauth_verifier);
        access_token = consumer.getToken();
        access_token_secret = consumer.getTokenSecret();

        getAuthSync(access_token,access_token_secret);
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
        mSnsAuthStored.setSessClean(oauth_token);

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

  public void getSuccess(String oauth_token,String oauth_verifier) throws Exception{
    // 세션에 저장했던 토큰
    this.consumer = (OAuthConsumer) session.getAttribute("_MEI_" + oauth_token + "_CR");
    this.provider = (OAuthProvider) session.getAttribute("_MEI_" + oauth_token + "_PR");

    String request_token = (String) session.getAttribute("_MEI_" + oauth_token + "_REQT");
    String request_tokensecret = (String) session.getAttribute("_MEI_" + oauth_token + "_REQTS");

    if (StringUtils.isNotEmpty(request_token) && StringUtils.isNotEmpty(request_token)) {
      getAccessToken(oauth_verifier);
    }
  }

  public void getAuthSync(String access_token,String access_tokensecret) throws Exception{

    if (access_token != null && access_tokensecret != null) {
      getAcceptToken(access_token,access_tokensecret);
      mSnsAuthStored.setAuthSess(this.SNS_NAME,this.consumer);
    }

  }

  public Map<String,String> getUserInfo() throws Exception {
    this.uid = null;
    this.user_id = null;
    this.nickname = null;
    this.profile_cover = null;

    OAuthConsumer sess_consumer = (OAuthConsumer) mSnsAuthStored.getAuthSess(this.SNS_NAME);

    Map<String,String> mapSS = (Map<String,String>) mSnsAuthStored.getUserInfo(this.SNS_NAME);
    Map<String,String> mapRet = new HashMap();

    if (MapUtils.isNotEmpty(mapSS)) {
      this.uid = mapSS.get("uid");
      this.user_id = mapSS.get("user_id");
      this.nickname = mapSS.get("nickname");
      this.profile_cover = mapSS.get("profile_cover");
    } else {

      String result = mSnsAuthHttp.getHttpGet(API_URL + "/account/verify_credentials.json",sess_consumer);

      try {
        JSONObject objJson = JSONObject.fromObject(result);

        String uid = objJson.getString("id"); // uid
        String user_id = objJson.getString("screen_name");
        String nickname = objJson.getString("name");
        String profile_cover = objJson.getString("profile_image_url");

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
      mapRet.put("uid",this.uid);
      mapRet.put("user_id",this.user_id);
      mapRet.put("nickname",this.nickname);
      mapRet.put("profile_cover",this.profile_cover);
    }

    return mapRet;

  }

  public String getPost(Map map) throws Exception {
    OAuthConsumer sess_consumer = (OAuthConsumer) mSnsAuthStored.getAuthSess(this.SNS_NAME);

    String msg_id = null;
    String message = (String) map.get("message");
    String parent_msg_id = (String) map.get("parent_msg_id");
    String url = (String) map.get("url");

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    if (StringUtils.isNotEmpty(url)) {
      // url 줄이기
      message = "#봉안닷컴 " + url + " " + message;
    } else {
      message = "#봉안닷컴 " + message;
    }

    message = FnUtils.getCutString(message,TEXT_LENGTH,"");

    formparams.add(new BasicNameValuePair("status", message));

    // 리트윗
    if (StringUtils.isNotEmpty(parent_msg_id)) {
      formparams.add(new BasicNameValuePair("in_reply_to_status_id", parent_msg_id));
    }

    UrlEncodedFormEntity formentity = new UrlEncodedFormEntity(formparams, "UTF-8");

    String result = mSnsAuthHttp.getHttpPost(API_URL + "/statuses/update.json",formentity,sess_consumer);
    log.debug("[MEI TWITTER POST result]" + result);

    JSONObject objJson = JSONObject.fromObject(result);
    try {
      msg_id = objJson.getString("id");
    } catch (Exception e) {
      getException(result);
    }

    log.debug("[MEI TWITTER POST ID]" + msg_id);
    return msg_id;
  }


  public void getDelete(String msg_id) throws Exception {
    OAuthConsumer sess_consumer = (OAuthConsumer) mSnsAuthStored.getAuthSess(this.SNS_NAME);

   String result = mSnsAuthHttp.getHttpPost(API_URL + "/statuses/destroy/" + msg_id + ".json",null,sess_consumer);
    JSONObject objJson = JSONObject.fromObject(result);
    getException(result);
  }

  public void getException(String json) throws Exception {
    String msg_text = null;
    JSONObject objJson = JSONObject.fromObject(json);
    try {
      msg_text = objJson.getString("error");
    } catch (Exception e) {
    }

    if (StringUtils.isNotEmpty(msg_text)) {
      log.debug("TWITTER : " + msg_text);
      //throw new Exception("TWITTER : " + msg_text);
    }

  }

}