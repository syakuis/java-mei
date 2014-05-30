/*
 * ModuleOptionsBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.module;

public class ModuleOptionsBean {
//module_orl,name,value

private String module_orl;
private String name;
private String value;

public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getValue() { return value; }
public void setValue(String value) { this.value = value; }

public void empty() { this.empty(""); }
public void empty(String str) {
this.module_orl = str;
this.name = str;
this.value = str;

}

public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("&module_orl=").append(module_orl).append("&name=").append(name).append("&value=").append(value);
return sb.toString();
}


}
