/*
 * SnsAuthView.java 2011.10.27
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

import com.syaku.core.util.*;

public class SnsAuthView extends ActionSnsAuth {
  private Logger log = Logger.getLogger( this.getClass() );

  private TwitterAPI twitter = new TwitterAPI(S);
  private YozmAPI yozm = new YozmAPI(S);
  private Me2dayAPI me2day = new Me2dayAPI(S);
  private FacebookAPI facebook = new FacebookAPI(S);

  public String dispSnsAuthSampleList() throws Exception {

    Map map = daoSnsAuth.getSignIn(S);
    O.put("sns_main_name",map.get("sns_main_name"));
    O.put("twitter",map.get("twitter"));
    O.put("yozm",map.get("yozm"));
    O.put("me2day",map.get("me2day"));
    O.put("facebook",map.get("facebook"));
    return SUCCESS;
  }

  public String dispSnsAuthSignIn() throws Exception {
    String sns_name = param.value("sns_name");
    String oauth_token = param.value("oauth_token");

    if (StringUtils.isEmpty(oauth_token)) {
      oauth_token = param.value("token");
    }

    String auth_url = null;

    if (StringUtils.isEmpty(sns_name)) {
      sns_name = StringUtils.defaultString((String) session.get("_MEI_" + oauth_token + "_NAME"),"");
    }


    if (StringUtils.isEmpty(oauth_token)) {
      oauth_token = param.value("code");
      if (StringUtils.isNotEmpty(oauth_token)) {
        sns_name = "FACEBOOK";
      }
    }

    if (StringUtils.equals(sns_name,"TWITTER")) {
      auth_url = twitter.getAccess(param);
    } else if (StringUtils.equals(sns_name,"YOZM")) {
      auth_url = yozm.getAccess(param);
    } else if (StringUtils.equals(sns_name,"ME2DAY")) {
      auth_url = me2day.getAccess(param);
    } else if (StringUtils.equals(sns_name,"FACEBOOK")) {
      auth_url = facebook.getAccess(param);
    }

    O.put("auth_url",auth_url);
    O.put("url",(String) S.get("options_" + sns_name.toLowerCase() + "_callback") + "?sns_name=" + sns_name);
    MOD_FTL_MODULE_LAYOUT_HIDE = true;
    return SUCCESS;
  }

}

