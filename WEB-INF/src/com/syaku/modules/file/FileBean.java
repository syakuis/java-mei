/*
 * FileBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.file;

public class FileBean {
//file_orl,target_orl,sid,seq,module_orl,member_orl,filename,re_filename,folder,folder_date,size,checksum,extension,type,rdate,ip,num,listorder

private String file_orl;
private String target_orl;
private String sid;
private String seq;
private String module_orl;
private String member_orl;
private String filename;
private String re_filename;
private String folder;
private String folder_date;
private String size;
private String checksum;
private String extension;
private String type;
private String rdate;
private String ip;
private String num;
private String listorder;

public String getFile_orl() { return file_orl; }
public void setFile_orl(String file_orl) { this.file_orl = file_orl; }
public String getTarget_orl() { return target_orl; }
public void setTarget_orl(String target_orl) { this.target_orl = target_orl; }
public String getSid() { return sid; }
public void setSid(String sid) { this.sid = sid; }
public String getSeq() { return seq; }
public void setSeq(String seq) { this.seq = seq; }
public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getMember_orl() { return member_orl; }
public void setMember_orl(String member_orl) { this.member_orl = member_orl; }
public String getFilename() { return filename; }
public void setFilename(String filename) { this.filename = filename; }
public String getRe_filename() { return re_filename; }
public void setRe_filename(String re_filename) { this.re_filename = re_filename; }
public String getFolder() { return folder; }
public void setFolder(String folder) { this.folder = folder; }
public String getFolder_date() { return folder_date; }
public void setFolder_date(String folder_date) { this.folder_date = folder_date; }
public String getSize() { return size; }
public void setSize(String size) { this.size = size; }
public String getChecksum() { return checksum; }
public void setChecksum(String checksum) { this.checksum = checksum; }
public String getExtension() { return extension; }
public void setExtension(String extension) { this.extension = extension; }
public String getType() { return type; }
public void setType(String type) { this.type = type; }
public String getRdate() { return rdate; }
public void setRdate(String rdate) { this.rdate = rdate; }
public String getIp() { return ip; }
public void setIp(String ip) { this.ip = ip; }
public String getNum() { return num; }
public void setNum(String num) { this.num = num; }
public String getListorder() { return listorder; }
public void setListorder(String listorder) { this.listorder = listorder; }

private String mid;
public String getMid() { return mid; }
public void setMid(String mid) { this.mid = mid; }
private String browser_title;
public String getBrowser_title() { return browser_title; }
public void setBrowser_title(String browser_title) { this.browser_title = browser_title; }

private String user_id;
public String getUser_id() { return user_id; }
public void setUser_id(String user_id) { this.user_id = user_id; }
private String user_name;
public String getUser_name() { return user_name; }
public void setUser_name(String user_name) { this.user_name = user_name; }
private String nickname;
public String getNickname() { return nickname; }
public void setNickname(String nickname) { this.nickname = nickname; }


}