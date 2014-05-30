/*
 * MailSend.java 2011.06.17
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import java.util.*;

import org.apache.log4j.Logger;

import org.apache.commons.configuration.*;
import org.apache.commons.lang.*;
import org.apache.commons.mail.*;

public class MailSend {
  private Logger log = Logger.getLogger( this.getClass() );
  private Configuration meiConfig = ConfigUtils.getProperties("mei.properties"); // mei 정보
  private String host,user,password,charset;
  private int port;
  private HtmlEmail htmlEmail;

  private String from;
  private String from_name;
  private String to;
  private String to_name;
  private String subject;
  private String content;

  // 보내는
  public String getFrom() { return from; }
  public void setFrom(String from) { this.from = from; }
  public String getFrom_name() { return from_name; }
  public void setFrom_name(String from_name) { this.from_name = from_name; }

  // 받는
  public String getTo() { return to; }
  public void setTo(String to) { this.to = to; }
  public String getTo_name() { return to_name; }
  public void setTo_name(String to_name) { this.to_name = to_name; }

  public String getSubject() { return subject; }
  public void setSubject(String subject) { this.subject = subject; }
  public String getContent() { return content; }
  public void setContent(String content) { this.content = content; }

  public MailSend() {
    host = meiConfig.getString("mei.mail.smtp.host");
    user = meiConfig.getString("mei.mail.smtp.user");
    password = meiConfig.getString("mei.mail.smtp.password");
    port = meiConfig.getInt("mei.mail.smtp.port");
    charset = meiConfig.getString("mei.charset");

    htmlEmail = new HtmlEmail();
    htmlEmail.setHostName(host);
    htmlEmail.setSmtpPort(port);
    htmlEmail.setAuthenticator( new DefaultAuthenticator(user,password) );
    htmlEmail.setSSLOnConnect(false);
    htmlEmail.setCharset(charset);
  }

  // 프리마커 템플릿 사용할 경우
  public void setTemplate(String dir , String ftl , Object data) throws Exception {
    this.content = TemplateUtils.getString(dir,ftl,data);
  }

  public void send() throws Exception {

    log.info(StringUtils.join(new String[] {
      "\r\n## MailSend.send\r\n" , 
      "==========================================================================\r\n" , 
      "> smtp host : " , host , "\r\n" , 
      "> smtp user : " , user , "\r\n" , 
      "> smtp port : " , ObjectUtils.toString(port) , "\r\n" , 
      "> smtp charset : " , charset , "\r\n" , 
      "> smtp to : " , to , "\r\n" , 
      "> smtp to_name : " , to_name , "\r\n" , 
      "> smtp to : " , from , "\r\n" , 
      "> smtp to_name : " , from_name , "\r\n" , 
      "> smtp subject : " , subject , "\r\n" , 
      "=========================================================================="
    }));

    try {
      htmlEmail.addTo(to,to_name);
      htmlEmail.setFrom(from,from_name);
      htmlEmail.setSubject(subject);
      htmlEmail.setHtmlMsg(content);
      htmlEmail.send();
    } catch (Exception e) {
      throw new Exception(e.toString());
    }

  }

}