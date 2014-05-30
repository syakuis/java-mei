/*
 * DocumentAdminView.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.document;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import java.lang.reflect.*;
import com.google.gson.*;
import com.google.gson.reflect.*;

import com.syaku.core.util.*;

import com.syaku.modules.member.*;

public class DocumentAdminView extends ActionDocument {
  private Logger log = Logger.getLogger( this.getClass() );

  private MemberObject daoMember = new MemberObject();

  private boolean getDocumentGrant() throws Exception {
    if (!MEI_GRANT_MANAGER) {
      MESSAGE.put("message","권한이 없습니다.");
      MESSAGE.put("display","");
      MESSAGE.put("error","true");
      MESSAGE.put("action","");
      MESSAGE.put("source","");
      valuestack.push(MESSAGE);
    }

    return MEI_GRANT_MANAGER;
  }

  public String dispDocumentAdminExtraKeysView() throws Exception {
    if (!getDocumentGrant()) { return "message"; }
    String module_orl = MODULE_ORL;

    Map mapSch = new HashMap();
    mapSch.put("module_orl", module_orl);
    O.put("listExtra", (List) daoDocument.sqlDocumentExtraKeysList(mapSch));
    return SUCCESS;
  }

  public String dispDocumentAdminExtraKeysInsert() throws Exception {
    if (!getDocumentGrant()) { return "message"; }
    String module_orl = MODULE_ORL;
    String var_idx = param.value("var_idx");

    DocumentExtraKeysBean obj = new DocumentExtraKeysBean();

    if (StringUtils.isNotEmpty(var_idx)) {
      Map mapSch = new HashMap();
      mapSch.put("module_orl",module_orl);
      mapSch.put("var_idx",var_idx);
      obj = (DocumentExtraKeysBean) daoDocument.sqlDocumentExtraKeysObject(mapSch);
    }

    O.put("objExtra", obj);
    return SUCCESS;
  }

  // 권한관리
  public String dispDocumentAdminGrantInsert() throws Exception {
    if (!getDocumentGrant()) { return "message"; }
    String module_orl = MODULE_ORL;
    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);

    O.put("objModuleGrants",(Map) daoModule.moduleGrants(mapSch));
    O.put("listGroup",(List) daoMember.sqlGroupList(null));
    return SUCCESS;
  }

}
