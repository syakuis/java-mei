/*
 * SnsAuthHttp.java 2011.11.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.snsauth;

import java.util.*;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import oauth.signpost.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class SnsAuthHttp {
  private Logger log = Logger.getLogger( this.getClass() );

  public String getHttpGet(String http_url,OAuthConsumer consumerHttp) throws Exception {
    HttpGet http_get = new HttpGet(http_url);

    if (consumerHttp != null) {
      consumerHttp.sign(http_get);
    }

    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = httpclient.execute(http_get);

    HttpEntity entity = response.getEntity();
    return EntityUtils.toString(entity);
  }

  public String getHttpGet(String http_url) throws Exception {
    HttpGet http_get = new HttpGet(http_url);
    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = httpclient.execute(http_get);

    HttpEntity entity = response.getEntity();
    return EntityUtils.toString(entity);
  }

  public String getHttpPost(String http_url,UrlEncodedFormEntity formentity,OAuthConsumer consumerHttp) throws Exception {
    HttpPost http_post = new HttpPost(http_url);
    http_post.setEntity(formentity);

    if (consumerHttp != null) {
      consumerHttp.sign(http_post);
    }

    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = httpclient.execute(http_post);

    HttpEntity entity = response.getEntity();

    return EntityUtils.toString(entity);
  }

}