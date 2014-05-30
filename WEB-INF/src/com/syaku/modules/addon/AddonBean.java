/*
 * AddonBean.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.addon;

public class AddonBean {
//addon_orl,module_orl,addon,addon_type,title,addon_path,first_load,is_use,regdate,listorder

private String addon_orl;
private String module_orl;
private String addon;
private String addon_type;
private String title;
private String addon_path;
private String first_load;
private String is_use;
private String regdate;
private String listorder;

public String getAddon_orl() { return addon_orl; }
public void setAddon_orl(String addon_orl) { this.addon_orl = addon_orl; }
public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getAddon() { return addon; }
public void setAddon(String addon) { this.addon = addon; }
public String getAddon_type() { return addon_type; }
public void setAddon_type(String addon_type) { this.addon_type = addon_type; }
public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }
public String getAddon_path() { return addon_path; }
public void setAddon_path(String addon_path) { this.addon_path = addon_path; }
public String getFirst_load() { return first_load; }
public void setFirst_load(String first_load) { this.first_load = first_load; }
public String getIs_use() { return is_use; }
public void setIs_use(String is_use) { this.is_use = is_use; }
public String getRegdate() { return regdate; }
public void setRegdate(String regdate) { this.regdate = regdate; }
public String getListorder() { return listorder; }
public void setListorder(String listorder) { this.listorder = listorder; }

}