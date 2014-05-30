/*
 * GroupMemberBean.java 2011.06.08
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.member;

public class GroupMemberBean {
//group_orl,member_orl,regdate

private String group_orl;
private String member_orl;
private String regdate;

public String getGroup_orl() { return group_orl; }
public void setGroup_orl(String group_orl) { this.group_orl = group_orl; }
public String getMember_orl() { return member_orl; }
public void setMember_orl(String member_orl) { this.member_orl = member_orl; }
public String getRegdate() { return regdate; }
public void setRegdate(String regdate) { this.regdate = regdate; }

}