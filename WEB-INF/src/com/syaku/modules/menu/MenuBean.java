/*
 * MenuBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.menu;

public class MenuBean {
  //menu_orl,title,listorder,rdate

  private String menu_orl;
  private String title;
  private String listorder;
  private String rdate;

  public String getMenu_orl() { return menu_orl; }
  public void setMenu_orl(String menu_orl) { this.menu_orl = menu_orl; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getListorder() { return listorder; }
  public void setListorder(String listorder) { this.listorder = listorder; }
  public String getRdate() { return rdate; }
  public void setRdate(String rdate) { this.rdate = rdate; }

}
