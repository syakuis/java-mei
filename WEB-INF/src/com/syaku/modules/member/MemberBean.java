/*
 * MemberBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.member;

public class MemberBean {
//member_orl,module_orl,user_id,nickname,user_name,password,temp_password,email,email_id,email_host,homepage,state,is_admin,rdate,last_login,listorder

private String member_orl;
private String module_orl;
private String user_id;
private String nickname;
private String user_name;
private String password;
private String temp_password;
private String email;
private String email_id;
private String email_host;
private String homepage;
private String state;
private String is_admin;
private String rdate;
private String last_login;
private String listorder;

public String getMember_orl() { return member_orl; }
public void setMember_orl(String member_orl) { this.member_orl = member_orl; }
public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getUser_id() { return user_id; }
public void setUser_id(String user_id) { this.user_id = user_id; }
public String getNickname() { return nickname; }
public void setNickname(String nickname) { this.nickname = nickname; }
public String getUser_name() { return user_name; }
public void setUser_name(String user_name) { this.user_name = user_name; }
public String getPassword() { return password; }
public void setPassword(String password) { this.password = password; }
public String getTemp_password() { return temp_password; }
public void setTemp_password(String temp_password) { this.temp_password = temp_password; }
public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }
public String getEmail_id() { return email_id; }
public void setEmail_id(String email_id) { this.email_id = email_id; }
public String getEmail_host() { return email_host; }
public void setEmail_host(String email_host) { this.email_host = email_host; }
public String getHomepage() { return homepage; }
public void setHomepage(String homepage) { this.homepage = homepage; }
public String getState() { return state; }
public void setState(String state) { this.state = state; }
public String getIs_admin() { return is_admin; }
public void setIs_admin(String is_admin) { this.is_admin = is_admin; }
public String getRdate() { return rdate; }
public void setRdate(String rdate) { this.rdate = rdate; }
public String getLast_login() { return last_login; }
public void setLast_login(String last_login) { this.last_login = last_login; }
public String getListorder() { return listorder; }
public void setListorder(String listorder) { this.listorder = listorder; }

}