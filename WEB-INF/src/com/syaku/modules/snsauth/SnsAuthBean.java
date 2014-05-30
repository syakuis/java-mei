/*
 * SnsAuthBean.java 2011.10.27
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.snsauth;

public class SnsAuthBean {

//oauth_orl,token,name,access_token,access_token_secret,main,post_send,uid,reg_date,listorder,ip,user_agent
private String oauth_orl;
private String token;
private String name;
private String access_token;
private String access_token_secret;
private String main;
private String post_send;
private String uid;
private String reg_date;
private String listorder;
private String ip;
private String user_agent;

public String getOauth_orl() { return oauth_orl; }
public void setOauth_orl(String oauth_orl) { this.oauth_orl = oauth_orl; }
public String getToken() { return token; }
public void setToken(String token) { this.token = token; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getAccess_token() { return access_token; }
public void setAccess_token(String access_token) { this.access_token = access_token; }
public String getAccess_token_secret() { return access_token_secret; }
public void setAccess_token_secret(String access_token_secret) { this.access_token_secret = access_token_secret; }
public String getMain() { return main; }
public void setMain(String main) { this.main = main; }
public String getPost_send() { return post_send; }
public void setPost_send(String post_send) { this.post_send = post_send; }
public String getUid() { return uid; }
public void setUid(String uid) { this.uid = uid; }
public String getReg_date() { return reg_date; }
public void setReg_date(String reg_date) { this.reg_date = reg_date; }
public String getListorder() { return listorder; }
public void setListorder(String listorder) { this.listorder = listorder; }
public String getIp() { return ip; }
public void setIp(String ip) { this.ip = ip; }
public String getUser_agent() { return user_agent; }
public void setUser_agent(String user_agent) { this.user_agent = user_agent; }
}