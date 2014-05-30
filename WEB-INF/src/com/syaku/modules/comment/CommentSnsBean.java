/*
 * CommentSnsBean.java 2011.11.08
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.comment;

public class CommentSnsBean {
//comment_orl,sns_name,module_orl,target_orl,msg_id

private String comment_orl;
private String sns_name;
private String module_orl;
private String target_orl;
private String msg_id;

public String getComment_orl() { return comment_orl; }
public void setComment_orl(String comment_orl) { this.comment_orl = comment_orl; }
public String getSns_name() { return sns_name; }
public void setSns_name(String sns_name) { this.sns_name = sns_name; }
public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getTarget_orl() { return target_orl; }
public void setTarget_orl(String target_orl) { this.target_orl = target_orl; }
public String getMsg_id() { return msg_id; }
public void setMsg_id(String msg_id) { this.msg_id = msg_id; }

}