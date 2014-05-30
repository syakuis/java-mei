/*
 * CommentBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.comment;

public class CommentBean {
//comment_orl,module_orl,target_orl,parent_orl,reply_group,reply_depth,reply_seq,member_orl,user_id,password,user_name,nickname,content,rdate,ip,listorder,sns_name,sns_photo,sns_id,is_del
// mid

private String comment_orl;
private String module_orl;
private String target_orl;
private String parent_orl;
private String reply_group;
private String reply_depth;
private String reply_seq;
private String member_orl;
private String user_id;
private String password;
private String user_name;
private String nickname;
private String content;
private String rdate;
private String ip;
private String listorder;
private String sns_name;
private String sns_photo;
private String sns_id;
private String is_del;

public String getComment_orl() { return comment_orl; }
public void setComment_orl(String comment_orl) { this.comment_orl = comment_orl; }
public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getTarget_orl() { return target_orl; }
public void setTarget_orl(String target_orl) { this.target_orl = target_orl; }
public String getParent_orl() { return parent_orl; }
public void setParent_orl(String parent_orl) { this.parent_orl = parent_orl; }
public String getReply_group() { return reply_group; }
public void setReply_group(String reply_group) { this.reply_group = reply_group; }
public String getReply_depth() { return reply_depth; }
public void setReply_depth(String reply_depth) { this.reply_depth = reply_depth; }
public String getReply_seq() { return reply_seq; }
public void setReply_seq(String reply_seq) { this.reply_seq = reply_seq; }
public String getMember_orl() { return member_orl; }
public void setMember_orl(String member_orl) { this.member_orl = member_orl; }
public String getUser_id() { return user_id; }
public void setUser_id(String user_id) { this.user_id = user_id; }
public String getPassword() { return password; }
public void setPassword(String password) { this.password = password; }
public String getUser_name() { return user_name; }
public void setUser_name(String user_name) { this.user_name = user_name; }
public String getNickname() { return nickname; }
public void setNickname(String nickname) { this.nickname = nickname; }
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }
public String getRdate() { return rdate; }
public void setRdate(String rdate) { this.rdate = rdate; }
public String getIp() { return ip; }
public void setIp(String ip) { this.ip = ip; }
public String getListorder() { return listorder; }
public void setListorder(String listorder) { this.listorder = listorder; }

public String getSns_name() { return sns_name; }
public void setSns_name(String sns_name) { this.sns_name = sns_name; }
public String getSns_photo() { return sns_photo; }
public void setSns_photo(String sns_photo) { this.sns_photo = sns_photo; }
public String getSns_id() { return sns_id; }
public void setSns_id(String sns_id) { this.sns_id = sns_id; }

public String getIs_del() { return is_del; }
public void setIs_del(String is_del) { this.is_del = is_del; }


private String mid;
public String getMid() { return mid; }
public void setMid(String mid) { this.mid = mid; }

}