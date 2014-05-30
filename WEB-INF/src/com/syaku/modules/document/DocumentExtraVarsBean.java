/*
 * DocumentExtraVarsBean.java 2011.06.24
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.document;

public class DocumentExtraVarsBean {
//module_orl,document_orl,var_idx,value,eid

private String module_orl;
private String document_orl;
private String var_idx;
private String value;
private String eid;

public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getDocument_orl() { return document_orl; }
public void setDocument_orl(String document_orl) { this.document_orl = document_orl; }
public String getVar_idx() { return var_idx; }
public void setVar_idx(String var_idx) { this.var_idx = var_idx; }
public String getValue() { return value; }
public void setValue(String value) { this.value = value; }
public String getEid() { return eid; }
public void setEid(String eid) { this.eid = eid; }

}