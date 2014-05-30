/*
 * DocumentBean.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.document;

public class DocumentBean {
//document_orl,category_orl,module_orl,member_orl,user_id,user_name,password,nickname,email,homepage,title,content,extra_vars,readed_count,vote_count,ipaddress,regdate,last_update,last_updater,parent_orl,reply_member_orl,reply_group,reply_depth,reply_num,grouporder,listorder,is_notice,title_bold,title_color

private String document_orl;
private String category_orl;
private String module_orl;
private String member_orl;
private String user_id;
private String user_name;
private String password;
private String nickname;
private String email;
private String homepage;
private String title;
private String content;
private String content_text;
private String extra_vars;
private String readed_count;
private String vote_count;
private String ipaddress;
private String regdate;
private String last_update;
private String last_updater;
private String parent_orl;
private String reply_member_orl;
private String reply_group;
private String reply_depth;
private String reply_num;
private String grouporder;
private String listorder;
private String is_notice;
private String title_bold;
private String title_color;

public String getDocument_orl() { return document_orl; }
public void setDocument_orl(String document_orl) { this.document_orl = document_orl; }
public String getCategory_orl() { return category_orl; }
public void setCategory_orl(String category_orl) { this.category_orl = category_orl; }
public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getMember_orl() { return member_orl; }
public void setMember_orl(String member_orl) { this.member_orl = member_orl; }
public String getUser_id() { return user_id; }
public void setUser_id(String user_id) { this.user_id = user_id; }
public String getUser_name() { return user_name; }
public void setUser_name(String user_name) { this.user_name = user_name; }
public String getPassword() { return password; }
public void setPassword(String password) { this.password = password; }
public String getNickname() { return nickname; }
public void setNickname(String nickname) { this.nickname = nickname; }
public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }
public String getHomepage() { return homepage; }
public void setHomepage(String homepage) { this.homepage = homepage; }
public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }
public String getContent_text() { return content_text; }
public void setContent_text(String content_text) { this.content_text = content_text; }
public String getExtra_vars() { return extra_vars; }
public void setExtra_vars(String extra_vars) { this.extra_vars = extra_vars; }
public String getReaded_count() { return readed_count; }
public void setReaded_count(String readed_count) { this.readed_count = readed_count; }
public String getVote_count() { return vote_count; }
public void setVote_count(String vote_count) { this.vote_count = vote_count; }
public String getIpaddress() { return ipaddress; }
public void setIpaddress(String ipaddress) { this.ipaddress = ipaddress; }
public String getRegdate() { return regdate; }
public void setRegdate(String regdate) { this.regdate = regdate; }
public String getLast_update() { return last_update; }
public void setLast_update(String last_update) { this.last_update = last_update; }
public String getLast_updater() { return last_updater; }
public void setLast_updater(String last_updater) { this.last_updater = last_updater; }
public String getParent_orl() { return parent_orl; }
public void setParent_orl(String parent_orl) { this.parent_orl = parent_orl; }
public String getReply_member_orl() { return reply_member_orl; }
public void setReply_member_orl(String reply_member_orl) { this.reply_member_orl = reply_member_orl; }
public String getReply_group() { return reply_group; }
public void setReply_group(String reply_group) { this.reply_group = reply_group; }
public String getReply_depth() { return reply_depth; }
public void setReply_depth(String reply_depth) { this.reply_depth = reply_depth; }
public String getReply_num() { return reply_num; }
public void setReply_num(String reply_num) { this.reply_num = reply_num; }
public String getGrouporder() { return grouporder; }
public void setGrouporder(String grouporder) { this.grouporder = grouporder; }
public String getListorder() { return listorder; }
public void setListorder(String listorder) { this.listorder = listorder; }
public String getIs_notice() { return is_notice; }
public void setIs_notice(String is_notice) { this.is_notice = is_notice; }
public String getTitle_bold() { return title_bold; }
public void setTitle_bold(String title_bold) { this.title_bold = title_bold; }
public String getTitle_color() { return title_color; }
public void setTitle_color(String title_color) { this.title_color = title_color; }

//category_title,comment_count,file_count
private String category_title;
private String comment_count;
private String file_count;
public String getCategory_title() { return category_title; }
public void setCategory_title(String category_title) { this.category_title = category_title; }
public String getComment_count() { return comment_count; }
public void setComment_count(String comment_count) { this.comment_count = comment_count; }
public String getFile_count() { return file_count; }
public void setFile_count(String file_count) { this.file_count = file_count; }

}