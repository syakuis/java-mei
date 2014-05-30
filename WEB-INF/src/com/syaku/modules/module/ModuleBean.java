/*
 * ModuleBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.module;

public class ModuleBean {
//module_orl,module,mid,browser_title,skin,layout_orl,menu_orl,content,rdate,header_content,footer_content

private String module_orl;
private String module;
private String mid;
private String browser_title;
private String skin;
private String layout_orl;
private String menu_orl;
private String content;
private String rdate;
private String header_content;
private String footer_content;

public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getModule() { return module; }
public void setModule(String module) { this.module = module; }
public String getMid() { return mid; }
public void setMid(String mid) { this.mid = mid; }
public String getBrowser_title() { return browser_title; }
public void setBrowser_title(String browser_title) { this.browser_title = browser_title; }
public String getSkin() { return skin; }
public void setSkin(String skin) { this.skin = skin; }
public String getLayout_orl() { return layout_orl; }
public void setLayout_orl(String layout_orl) { this.layout_orl = layout_orl; }
public String getMenu_orl() { return menu_orl; }
public void setMenu_orl(String menu_orl) { this.menu_orl = menu_orl; }
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }
public String getRdate() { return rdate; }
public void setRdate(String rdate) { this.rdate = rdate; }
public String getHeader_content() { return header_content; }
public void setHeader_content(String header_content) { this.header_content = header_content; }
public String getFooter_content() { return footer_content; }
public void setFooter_content(String footer_content) { this.footer_content = footer_content; }

public void empty() { this.empty(""); }
public void empty(String str) {
this.module_orl = str;
this.module = str;
this.mid = str;
this.browser_title = str;
this.skin = str;
this.layout_orl = str;
this.menu_orl = str;
this.content = str;
this.rdate = str;
this.header_content = str;
this.footer_content = str;

}

public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("&module_orl=").append(module_orl).append("&module=").append(module).append("&mid=").append(mid).append("&browser_title=").append(browser_title).append("&skin=").append(skin).append("&layout_orl=").append(layout_orl).append("&menu_orl=").append(menu_orl).append("&content=").append(content).append("&rdate=").append(rdate).append("&header_content=").append(header_content).append("&footer_content=").append(footer_content);
return sb.toString();
}

}
