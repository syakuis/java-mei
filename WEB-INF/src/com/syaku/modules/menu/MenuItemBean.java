/*
 * MenuItemBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.menu;

public class MenuItemBean {
//menu_item_orl,parent_orl,num,menu_orl,url,name,open_window,expand,is_mobile,rdate,group_orls,btn_normal,btn_hover,btn_active,select_query

private String menu_item_orl;
private String parent_orl;
private String num;
private String menu_orl;
private String url;
private String name;
private String open_window;
private String expand;
private String is_mobile;
private String rdate;
private String group_orls;
private String btn_normal;
private String btn_hover;
private String btn_active;
private String select_query;

public String getMenu_item_orl() { return menu_item_orl; }
public void setMenu_item_orl(String menu_item_orl) { this.menu_item_orl = menu_item_orl; }
public String getParent_orl() { return parent_orl; }
public void setParent_orl(String parent_orl) { this.parent_orl = parent_orl; }
public String getNum() { return num; }
public void setNum(String num) { this.num = num; }
public String getMenu_orl() { return menu_orl; }
public void setMenu_orl(String menu_orl) { this.menu_orl = menu_orl; }
public String getUrl() { return url; }
public void setUrl(String url) { this.url = url; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getOpen_window() { return open_window; }
public void setOpen_window(String open_window) { this.open_window = open_window; }
public String getExpand() { return expand; }
public void setExpand(String expand) { this.expand = expand; }
public String getIs_mobile() { return is_mobile; }
public void setIs_mobile(String is_mobile) { this.is_mobile = is_mobile; }
public String getRdate() { return rdate; }
public void setRdate(String rdate) { this.rdate = rdate; }
public String getGroup_orls() { return group_orls; }
public void setGroup_orls(String group_orls) { this.group_orls = group_orls; }
public String getBtn_normal() { return btn_normal; }
public void setBtn_normal(String btn_normal) { this.btn_normal = btn_normal; }
public String getBtn_hover() { return btn_hover; }
public void setBtn_hover(String btn_hover) { this.btn_hover = btn_hover; }
public String getBtn_active() { return btn_active; }
public void setBtn_active(String btn_active) { this.btn_active = btn_active; }
public String getSelect_query() { return select_query; }
public void setSelect_query(String select_query) { this.select_query = select_query; }

}