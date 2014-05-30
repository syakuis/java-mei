/*
 * OutsidepageAdminController.java 2011.07.24
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.outsidepage;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class OutsidepageAdminController extends ActionOutsidepage {
  private Logger log = Logger.getLogger( this.getClass() );

  private OutsidepagePrivilege privilege = new OutsidepagePrivilege();

  public String procOutsidepageAdminDelete() throws Exception {
    String module_orl = param.value("module_orl");

    try {
      sqlMap.startTransaction();
      privilege.getOutsidepagePrivilegeException(MEI_GRANT_MANAGER);

      // 모듈삭제
      daoModule.moduleDelete(module_orl);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

  public String procOutsidepageAdminGrantInsert() throws Exception {
    try {
      sqlMap.startTransaction();
      privilege.getOutsidepagePrivilegeException(MEI_GRANT_MANAGER);

      daoModule.moduleGrantsInsert(param);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("display","alert");
      MESSAGE.put("message",e.getMessage().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

}
