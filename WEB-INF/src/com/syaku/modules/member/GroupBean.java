/*
 * GroupBean.java 2011.06.08
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.member;

public class GroupBean {
//gorup_orl,title,regdate,is_default,is_admin,description

private String group_orl;
private String title;
private String regdate;
private String is_default;
private String is_admin;
private String description;

public String getGroup_orl() { return group_orl; }
public void setGroup_orl(String group_orl) { this.group_orl = group_orl; }
public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }
public String getRegdate() { return regdate; }
public void setRegdate(String regdate) { this.regdate = regdate; }
public String getIs_default() { return is_default; }
public void setIs_default(String is_default) { this.is_default = is_default; }
public String getIs_admin() { return is_admin; }
public void setIs_admin(String is_admin) { this.is_admin = is_admin; }
public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }

}