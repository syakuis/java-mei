/*
 * LayoutBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.layout;

public class LayoutBean {
  //layout_orl,menu_orl,layout,title,head_script,extra_vars,mobile,rdate

  private String layout_orl;
  private String menu_orl;
  private String layout;
  private String title;
  private String head_script;
  private String extra_vars;
  private String mobile;
  private String rdate;

  public String getLayout_orl() { return layout_orl; }
  public void setLayout_orl(String layout_orl) { this.layout_orl = layout_orl; }
  public String getMenu_orl() { return menu_orl; }
  public void setMenu_orl(String menu_orl) { this.menu_orl = menu_orl; }
  public String getLayout() { return layout; }
  public void setLayout(String layout) { this.layout = layout; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getHead_script() { return head_script; }
  public void setHead_script(String head_script) { this.head_script = head_script; }
  public String getExtra_vars() { return extra_vars; }
  public void setExtra_vars(String extra_vars) { this.extra_vars = extra_vars; }
  public String getMobile() { return mobile; }
  public void setMobile(String mobile) { this.mobile = mobile; }
  public String getRdate() { return rdate; }
  public void setRdate(String rdate) { this.rdate = rdate; }

}
