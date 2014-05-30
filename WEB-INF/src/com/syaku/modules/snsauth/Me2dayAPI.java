/*
 * Me2dayAPI.java 2011.11.16
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

public class Me2dayAPI {
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

  final String API_URL = "http://me2day.net/api";
  final String SNS_NAME = "ME2DAY";
  private String API_NONCE;
  private String API_AKEY;
  private String API_CALLBACK;
  final int TEXT_LENGTH = 150;

  public Me2dayAPI(Map M_INFO) {
    this.API_NONCE = (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_nonce");
    this.API_AKEY = (String) M_INFO.get("options_" + this.SNS_NAME.toLowerCase() + "_akey");
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
            String access_token_secret = objSns.getAccess_token_secret();
            getAuthSync(access_token_secret,objSns.getUid());
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

    String oauth_token = param.value("token","");
    String result = param.value("result","");
    String result_user_id = param.value("user_id","");
    String oauth_verifier = param.value("user_key","");

    String token = mSnsAuthStored.getAuthCookie();

    String access_token = null;
    String access_token_secret = null;

    Map mapSch = new HashMap();

    if (StringUtils.isEmpty(result)) {

      if (StringUtils.isNotEmpty(token)) {
        mapSch.clear();
        mapSch.put("name",this.SNS_NAME);
        mapSch.put("token",token);
        SnsAuthBean objSns = (SnsAuthBean) mSnsAuthObject.sqlSnsAuthObject(mapSch);

        if (objSns != null) {
          access_token_secret = objSns.getAccess_token_secret(); // ukey(md5)
        }

        mSnsAuthStored.setAuthCookie(token); // 갱신
      }

      if (access_token_secret != null) {
        getAuthSync(access_token_secret,result_user_id);
        auth_url = "SUCCESS";
      } else {
        mSnsAuthStored.getSessRemove(this.SNS_NAME);

        String json = mSnsAuthHttp.getHttpGet(API_URL + "/get_auth_url.json?akey=" + URLEncoder.encode(API_AKEY,"utf-8"));
        JSONObject objJson = JSONObject.fromObject(json);
        auth_url = objJson.getString("url");
        oauth_token = objJson.getString("token");
        mSnsAuthStored.setSessCreate(this.SNS_NAME,oauth_token);

      }

    } else {

      try {
        sqlMap.startTransaction();
        
        if (StringUtils.isEmpty(token)) {
          // 쿠키 생성 및 토큰 디비저장.
          token = DigestUtils.sha256Hex(DigestUtils.sha256("syaku" + DateUtils.date("yyyyMMddHHmmss") + "me"));
        }

        String ukey = API_NONCE + DigestUtils.md5Hex(API_NONCE + oauth_verifier);

        access_token = oauth_verifier; // user_key
        access_token_secret = ukey;

        getAuthSync(access_token_secret,result_user_id); // 세션
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

  public void getAuthSync(String access_tokensecret,String me2uid) throws Exception{
    if (access_tokensecret != null) {
      mSnsAuthStored.setAuthSess(this.SNS_NAME,access_tokensecret);
    }

    if (me2uid != null) {
      session.setAttribute("_MEI_" + this.SNS_NAME + "_UID",me2uid);
    }

  }

  public Map<String,String> getUserInfo() throws Exception {
    String access_tokensecret = (String) mSnsAuthStored.getAuthSess(this.SNS_NAME);
    String me2uid = (String) session.getAttribute("_MEI_" + this.SNS_NAME + "_UID");

    Map<String,String> mapSS = (Map<String,String>) mSnsAuthStored.getUserInfo(this.SNS_NAME);
    Map<String,String> mapRet = new HashMap();

    if (MapUtils.isNotEmpty(mapSS)) {
      this.uid = mapSS.get("uid");
      this.user_id = mapSS.get("user_id");
      this.nickname = mapSS.get("nickname");
      this.profile_cover = mapSS.get("profile_cover");

    } else {

      String result = mSnsAuthHttp.getHttpGet(API_URL + "/get_person/" + me2uid + ".json?ukey=" + URLEncoder.encode(access_tokensecret,"utf-8") + "&akey=" + URLEncoder.encode(this.API_AKEY,"utf-8"),null);

      try {
        JSONObject objJson = JSONObject.fromObject(result);
        String user_id = objJson.getString("id");
        String nickname = objJson.getString("nickname");
        String profile_cover = objJson.getString("face");

        String uid = user_id;

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
    String access_tokensecret = (String) mSnsAuthStored.getAuthSess(this.SNS_NAME);
    String me2uid = (String) session.getAttribute("_MEI_" + this.SNS_NAME + "_UID");

    String msg_id = null;
    String message = (String) map.get("message");
    String parent_msg_id = (String) map.get("parent_msg_id");
    String url = (String) map.get("url");
    String tag = "봉안닷컴";

    message = FnUtils.getCutString(message,TEXT_LENGTH,"");

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    if (StringUtils.isNotEmpty(url)) {
      message = "\"" + message +"\":" + url;
    }

    formparams.add(new BasicNameValuePair("uid", me2uid));
    formparams.add(new BasicNameValuePair("ukey", access_tokensecret));
    formparams.add(new BasicNameValuePair("akey", this.API_AKEY));
    formparams.add(new BasicNameValuePair("post[body]", message));
    formparams.add(new BasicNameValuePair("post[tags]", tag));

    UrlEncodedFormEntity formentity = new UrlEncodedFormEntity(formparams, "UTF-8");

    String result = mSnsAuthHttp.getHttpPost(API_URL + "/create_post/" + me2uid + ".json",formentity,null);
    log.debug("[MEI ME2DAY POST result]" + result);

    JSONObject objJson = JSONObject.fromObject(result);
    try {
      msg_id = objJson.getString("post_id");
    } catch (Exception e) {
      getException(result);
    }

    log.debug("[MEI ME2DAY POST ID]" + msg_id);
    return msg_id;
  }

  public void getDelete(String msg_id) throws Exception {
    String access_tokensecret = (String) mSnsAuthStored.getAuthSess(this.SNS_NAME);
    String me2uid = (String) session.getAttribute("_MEI_" + this.SNS_NAME + "_UID");

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair("uid", me2uid));
    formparams.add(new BasicNameValuePair("ukey", access_tokensecret));
    formparams.add(new BasicNameValuePair("akey", this.API_AKEY));
    formparams.add(new BasicNameValuePair("post_id", msg_id));
    UrlEncodedFormEntity formentity = new UrlEncodedFormEntity(formparams, "UTF-8");

    String result = mSnsAuthHttp.getHttpPost(API_URL + "/delete_post.json",formentity,null);
    JSONObject objJson = JSONObject.fromObject(result);
    getException(result);
  }

  public void getException(String json) throws Exception {
    String msg_text = null;
    JSONObject objJson = JSONObject.fromObject(json);
    try {
      msg_text = objJson.getString("description");
    } catch (Exception e) {
    }

    if (StringUtils.isNotEmpty(msg_text)) {
      log.debug("ME2DAY : " + msg_text);
      //throw new Exception("ME2DAY : " + msg_text);
    }

  }

}