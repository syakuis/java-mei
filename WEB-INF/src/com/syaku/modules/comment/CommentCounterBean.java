/*
 * CommentCounterBean.java 2011.06.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.comment;

public class CommentCounterBean {
//module_orl,target_orl,count,reg_date,last_update

private String module_orl;
private String target_orl;
private String count;
private String reg_date;
private String last_update;

public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getTarget_orl() { return target_orl; }
public void setTarget_orl(String target_orl) { this.target_orl = target_orl; }
public String getCount() { return count; }
public void setCount(String count) { this.count = count; }
public String getReg_date() { return reg_date; }
public void setReg_date(String reg_date) { this.reg_date = reg_date; }
public String getLast_update() { return last_update; }
public void setLast_update(String last_update) { this.last_update = last_update; }

}