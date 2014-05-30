/*
 * MemberAuthBean.java 2011.08.04
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.member;

public class MemberAuthBean {
//member_orl,auth_key,success_date,regdate

private String member_orl;
private String auth_key;
private String success_date;
private String regdate;

public String getMember_orl() { return member_orl; }
public void setMember_orl(String member_orl) { this.member_orl = member_orl; }
public String getAuth_key() { return auth_key; }
public void setAuth_key(String auth_key) { this.auth_key = auth_key; }
public String getSuccess_date() { return success_date; }
public void setSuccess_date(String success_date) { this.success_date = success_date; }
public String getRegdate() { return regdate; }
public void setRegdate(String regdate) { this.regdate = regdate; }

}