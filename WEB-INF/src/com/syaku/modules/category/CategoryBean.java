/*
 * CategoryBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.category;

public class CategoryBean {
//category_orl,parent_orl,category_seq,module_orl,title,rdate

private String category_orl;
private String parent_orl;
private String category_seq;
private String module_orl;
private String title;
private String rdate;

public String getCategory_orl() { return category_orl; }
public void setCategory_orl(String category_orl) { this.category_orl = category_orl; }
public String getParent_orl() { return parent_orl; }
public void setParent_orl(String parent_orl) { this.parent_orl = parent_orl; }
public String getCategory_seq() { return category_seq; }
public void setCategory_seq(String category_seq) { this.category_seq = category_seq; }
public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }
public String getRdate() { return rdate; }
public void setRdate(String rdate) { this.rdate = rdate; }

private String count;
public String getCount() { return count; }
public void setCount(String count) { this.count = count; }


}