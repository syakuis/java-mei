/*
 * DocumentExtraKeysBean.java 2011.06.24
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.document;

public class DocumentExtraKeysBean {
//module_orl,var_idx,var_name,var_type,var_is_required,var_search,var_default,var_desc,eid

private String module_orl;
private String var_idx;
private String var_name;
private String var_type;
private String var_is_required;
private String var_search;
private String var_default;
private String var_desc;
private String eid;

public String getModule_orl() { return module_orl; }
public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
public String getVar_idx() { return var_idx; }
public void setVar_idx(String var_idx) { this.var_idx = var_idx; }
public String getVar_name() { return var_name; }
public void setVar_name(String var_name) { this.var_name = var_name; }
public String getVar_type() { return var_type; }
public void setVar_type(String var_type) { this.var_type = var_type; }
public String getVar_is_required() { return var_is_required; }
public void setVar_is_required(String var_is_required) { this.var_is_required = var_is_required; }
public String getVar_search() { return var_search; }
public void setVar_search(String var_search) { this.var_search = var_search; }
public String getVar_default() { return var_default; }
public void setVar_default(String var_default) { this.var_default = var_default; }
public String getVar_desc() { return var_desc; }
public void setVar_desc(String var_desc) { this.var_desc = var_desc; }
public String getEid() { return eid; }
public void setEid(String eid) { this.eid = eid; }

}