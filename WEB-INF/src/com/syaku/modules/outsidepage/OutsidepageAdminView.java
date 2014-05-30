/*
 * OutsidepageAdminView.java 2011.07.24
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.outsidepage;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import java.lang.reflect.*;

import com.syaku.core.util.*;

import com.syaku.modules.member.*;

public class OutsidepageAdminView extends ActionOutsidepage {
  private Logger log = Logger.getLogger( this.getClass() );
  private MemberObject daoMember = new MemberObject();

  private boolean getOutsidepageGrant() throws Exception {
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


  public String dispOutsidepageAdminInsert() throws Exception {
    // 권한체크
    if (!getOutsidepageGrant()) { return "message"; }

    String module_orl = param.value("module_orl");

    Map objModule = new HashMap();

    O.put("listLayout",(List) daoLayout.sqlLayoutList(null));

    if (StringUtils.isNotEmpty(module_orl)) {
      Map mapSch = new HashMap();
      mapSch.put("module_orl",module_orl);
      mapSch.put("module", MOD_NAME); // 모듈 네임

      objModule = (HashMap) daoModule.objectOne(mapSch);
    }

    O.put("objModule", objModule);

    return SUCCESS;
  }

  // 권한관리
  public String dispOutsidepageAdminGrantInsert() throws Exception {
    if (!getOutsidepageGrant()) { return "message"; }
    String module_orl = param.value("module_orl");

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);

    O.put("objModuleGrants",(Map) daoModule.moduleGrants(mapSch));
    O.put("listGroup",(List) daoMember.sqlGroupList(null));
    return SUCCESS;
  }

}
