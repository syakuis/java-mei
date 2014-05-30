/*
 * SnsOAuthAPI.java 2011.10.20
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.snsauth;

import java.util.*;

import oauth.signpost.*;
import oauth.signpost.basic.*;
import oauth.signpost.exception.*;
import oauth.signpost.commonshttp.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class SnsOAuthAPI {
  private Logger log = Logger.getLogger( this.getClass() );

  private String request_token_url;
  public void setRequest_token_url(String request_token_url) {this.request_token_url = request_token_url; }
  private String authorize_url;
  public void setAuthorize_url(String authorize_url) {this.authorize_url = authorize_url; }
  private String access_token_url;
  public void setAccess_token_url(String access_token_url) {this.access_token_url = access_token_url; }

  private String consumer_key;
  public void setConsumer_key(String consumer_key) {this.consumer_key = consumer_key; }
  private String consumer_secret;
  public void setConsumer_secret(String consumer_secret) {this.consumer_secret = consumer_secret; }
  private String callback;
  public void setCallback(String callback) {this.callback = callback; }

  public OAuthProvider provider;
  public OAuthConsumer consumer;

  public void getInstance() throws Exception{
    log.debug("[MEI SnsOAuthAPI getInstance]");
    this.provider = new CommonsHttpOAuthProvider(this.request_token_url, this.access_token_url, this.authorize_url);
    this.consumer = new CommonsHttpOAuthConsumer(this.consumer_key, this.consumer_secret);

    log.debug("[MEI SnsOAuthAPI] consumer_key : " + consumer_key);
    log.debug("[MEI SnsOAuthAPI] consumer_secret : " + consumer_secret);

  }

  public String getRequestToken() throws Exception{
    log.debug("[MEI SnsOAuthAPI] callback : " + callback);

    String oauth_url = provider.retrieveRequestToken(consumer, callback);

    log.debug("[MEI SnsOAuthAPI] token : " + consumer.getToken());
    log.debug("[MEI SnsOAuthAPI] token_secret : " + consumer.getTokenSecret());
    log.debug("[MEI SnsOAuthAPI] oauth_url : " + oauth_url);

    return oauth_url;
  }

  public void getAccessToken(String oauth_verifier) throws Exception {
    log.debug("[MEI SnsOAuthAPI getAccessToken]");
    provider.retrieveAccessToken(consumer, oauth_verifier);
    log.debug("[MEI SnsOAuthAPI] access_token : " + consumer.getToken());
    log.debug("[MEI SnsOAuthAPI] access_token_secret : " + consumer.getTokenSecret());
  }

  // 재인증 처리
  public void getAcceptToken(String access_token,String access_tokensecret) throws Exception {
    log.debug("[MEI SnsOAuthAPI getAcceptToken]");
    consumer.setTokenWithSecret(access_token, access_tokensecret);
    log.debug("[MEI SnsOAuthAPI] access_token : " + consumer.getToken());
    log.debug("[MEI SnsOAuthAPI] access_token_secret : " + consumer.getTokenSecret());
  }

}
