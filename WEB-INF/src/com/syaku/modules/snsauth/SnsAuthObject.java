/*
 * SnsAuthObject.java 2011.10.27
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
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.*;

import com.syaku.core.util.*;
import com.syaku.modules.module.*;

public class SnsAuthObject extends SnsAuthAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/snsauth/info.properties");

  // 대표 SNS 'N' 초기화
  public void getSnsAuthMainResetUpdate(String token) throws Exception { 
    Map map = new HashMap();
    map.put("token",token);
    map.put("main","N");
    sqlSnsAuthMainResetUpdate(map);
  }

  // 대표SNS 가져오기
  public String getSnsMainName(String token) throws Exception {
    String sns_name = null;

    Map mapSch = new HashMap();
    mapSch.put("token",token);
    mapSch.put("main","Y");

    SnsAuthBean objSns = (SnsAuthBean) sqlSnsAuthObject(mapSch);
    if (objSns != null) {
      sns_name = objSns.getName();
    }

    return sns_name;
  }
  
  // 통합 세션 체크
  public boolean getAuthSessValid() throws Exception {
    SnsAuthStored daoSnsAuthStored = new SnsAuthStored();
    if (daoSnsAuthStored.getAuthSessValid("TWITTER") == true || daoSnsAuthStored.getAuthSessValid("YOZM") == true || daoSnsAuthStored.getAuthSessValid("ME2DAY") == true || daoSnsAuthStored.getAuthSessValid("FACEBOOK") == true) {
      return true;
    } else {
      return false;
    }
  }

  // 통합 싱크 처리
  public Map getSignIn(Map M_INFO) throws Exception {
    ModuleObject daoModule = new ModuleObject();
    SnsAuthStored daoSnsAuthStored = new SnsAuthStored();

    Map mapData = new HashMap();

    if (MapUtils.isEmpty(M_INFO)) {
      Map mapSch = new HashMap();
      mapSch.put("module",modConfig.getString("mei.module.name"));
      M_INFO = (Map) daoModule.objectOne(mapSch);
    }

    TwitterAPI twitter = new TwitterAPI(M_INFO);
    YozmAPI yozm = new YozmAPI(M_INFO);
    Me2dayAPI me2day = new Me2dayAPI(M_INFO);
    FacebookAPI facebook = new FacebookAPI(M_INFO);

    String token = daoSnsAuthStored.getAuthCookie();

    if (StringUtils.isNotEmpty(token)) {

      Map mapSch = new HashMap();
      mapSch.put("token",token);
      List list = (List) sqlSnsAuthList(mapSch);
      int cnt = list.size();

      for (int i = 0; i < cnt; i++) {
        SnsAuthBean obj = (SnsAuthBean) list.get(i);
        String name = obj.getName();
        String post_send = obj.getPost_send();

        if (StringUtils.equals(name,"TWITTER")) {
          twitter.setPost_send(post_send);
        } else if (StringUtils.equals(name,"YOZM")) {
          yozm.setPost_send(post_send);
        } else if (StringUtils.equals(name,"ME2DAY")) {
          me2day.setPost_send(post_send);
        } else if (StringUtils.equals(name,"FACEBOOK")) {
          facebook.setPost_send(post_send);
        }

      }

    }

    String sns_main_name = getSnsMainName(token);
    mapData.put("sns_main_name",sns_main_name);

    twitter.getSignIn();
    mapData.put("twitter",twitter);
    mapData.put("twitter_uid",twitter.getUid());
    mapData.put("twitter_user_id",twitter.getUser_id());
    mapData.put("twitter_nickname",twitter.getNickname());
    mapData.put("twitter_profile_cover",twitter.getProfile_cover());
    if (StringUtils.equals(sns_main_name,"TWITTER")) {
      mapData.put("sns_main_uid",twitter.getUid());
      mapData.put("sns_main_user_id",twitter.getUser_id());
      mapData.put("sns_main_nickname",twitter.getNickname());
      mapData.put("sns_main_profile_cover",twitter.getProfile_cover());
    }

    yozm.getSignIn();
    mapData.put("yozm",yozm);
    mapData.put("yozm_uid",yozm.getUid());
    mapData.put("yozm_user_id",yozm.getUser_id());
    mapData.put("yozm_nickname",yozm.getNickname());
    mapData.put("yozm_profile_cover",yozm.getProfile_cover());
    if (StringUtils.equals(sns_main_name,"YOZM")) {
      mapData.put("sns_main_uid",yozm.getUid());
      mapData.put("sns_main_user_id",yozm.getUser_id());
      mapData.put("sns_main_nickname",yozm.getNickname());
      mapData.put("sns_main_profile_cover",yozm.getProfile_cover());
    }

    me2day.getSignIn();
    mapData.put("me2day",me2day);
    mapData.put("me2day_uid",me2day.getUid());
    mapData.put("me2day_user_id",me2day.getUser_id());
    mapData.put("me2day_nickname",me2day.getNickname());
    mapData.put("me2day_profile_cover",me2day.getProfile_cover());
    if (StringUtils.equals(sns_main_name,"ME2DAY")) {
      mapData.put("sns_main_uid",me2day.getUid());
      mapData.put("sns_main_user_id",me2day.getUser_id());
      mapData.put("sns_main_nickname",me2day.getNickname());
      mapData.put("sns_main_profile_cover",me2day.getProfile_cover());
    }

    facebook.getSignIn();
    mapData.put("facebook",facebook);
    mapData.put("facebook_uid",facebook.getUid());
    mapData.put("facebook_user_id",facebook.getUser_id());
    mapData.put("facebook_nickname",facebook.getNickname());
    mapData.put("facebook_profile_cover",facebook.getProfile_cover());
    if (StringUtils.equals(sns_main_name,"FACEBOOK")) {
      mapData.put("sns_main_uid",facebook.getUid());
      mapData.put("sns_main_user_id",facebook.getUser_id());
      mapData.put("sns_main_nickname",facebook.getNickname());
      mapData.put("sns_main_profile_cover",facebook.getProfile_cover());
    }

    return mapData;

  }

  // 통합 글쓰기
  public Map getPost(Map M_INFO,Map map) throws Exception {
    ModuleObject daoModule = new ModuleObject();
    SnsAuthStored daoSnsAuthStored = new SnsAuthStored();

    Map mapData = new HashMap();
    String msg_id = null;

    if (MapUtils.isEmpty(M_INFO)) {
      Map mapSch = new HashMap();
      mapSch.put("module",modConfig.getString("mei.module.name"));
      M_INFO = (Map) daoModule.objectOne(mapSch);
    }

    TwitterAPI twitter = new TwitterAPI(M_INFO);
    YozmAPI yozm = new YozmAPI(M_INFO);
    Me2dayAPI me2day = new Me2dayAPI(M_INFO);
    FacebookAPI facebook = new FacebookAPI(M_INFO);

    String token = daoSnsAuthStored.getAuthCookie();

    if (StringUtils.isNotEmpty(token) && MapUtils.isNotEmpty(map)) {
      Map mapSch = new HashMap();
      mapSch.put("post_send","Y");
      mapSch.put("token",token);
      List list = (List) sqlSnsAuthList(mapSch);
      int cnt = list.size();

      for (int i = 0; i < cnt; i++) {
        SnsAuthBean obj = (SnsAuthBean) list.get(i);
        String name = obj.getName();

        if (StringUtils.equals(name,"TWITTER")) {
          twitter.getSignIn();
          msg_id = twitter.getPost(map);
          if (StringUtils.isNotEmpty(msg_id)) {
            mapData.put("TWITTER",msg_id);
          }
        } else if (StringUtils.equals(name,"YOZM")) {
          yozm.getSignIn();
          msg_id = yozm.getPost(map);
          if (StringUtils.isNotEmpty(msg_id)) {
            mapData.put("YOZM",msg_id);
          }
        } else if (StringUtils.equals(name,"ME2DAY")) {
          me2day.getSignIn();
          msg_id = me2day.getPost(map);
          if (StringUtils.isNotEmpty(msg_id)) {
            mapData.put("ME2DAY",msg_id);
          }
        } else if (StringUtils.equals(name,"FACEBOOK")) {
          facebook.getSignIn();
          msg_id = facebook.getPost(map);
          if (StringUtils.isNotEmpty(msg_id)) {
            mapData.put("FACEBOOK",msg_id);
          }
        }

      }
    }

    return mapData;

  }

  // 통합 글 삭제
  public void getDelete(Map M_INFO,Map map) throws Exception {
    ModuleObject daoModule = new ModuleObject();
    SnsAuthStored daoSnsAuthStored = new SnsAuthStored();

    if (MapUtils.isEmpty(M_INFO)) {
      Map mapSch = new HashMap();
      mapSch.put("module",modConfig.getString("mei.module.name"));
      M_INFO = (Map) daoModule.objectOne(mapSch);
    }

    TwitterAPI twitter = new TwitterAPI(M_INFO);
    YozmAPI yozm = new YozmAPI(M_INFO);
    Me2dayAPI me2day = new Me2dayAPI(M_INFO);
    FacebookAPI facebook = new FacebookAPI(M_INFO);

    String token = daoSnsAuthStored.getAuthCookie();

    String msg_id = null;

    if (StringUtils.isNotEmpty(token) && MapUtils.isNotEmpty(map)) {
      msg_id = (String) map.get("TWITTER");
      if (StringUtils.isNotEmpty(msg_id)) {
        twitter.getSignIn();
        twitter.getDelete(msg_id);
      }

      msg_id = (String) map.get("YOZM");
      if (StringUtils.isNotEmpty(msg_id)) {
        yozm.getSignIn();
        yozm.getDelete(msg_id);
      }

      msg_id = (String) map.get("ME2DAY");
      if (StringUtils.isNotEmpty(msg_id)) {
        me2day.getSignIn();
        me2day.getDelete(msg_id);
      }

      msg_id = (String) map.get("FACEBOOK");
      if (StringUtils.isNotEmpty(msg_id)) {
        facebook.getSignIn();
        facebook.getDelete(msg_id);
      }


    }

  }

}