/*
 * CommentController.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.admin;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.configuration.*;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class AdminController extends ActionAdmin {
  private Logger log = Logger.getLogger( this.getClass() );

  public String procAdminSettingInsert() throws Exception {
    String admin_orl = param.value("admin_orl");
    String module_orl = param.value("module_orl");

    try {

      sqlMap.startTransaction();

      AdminBean adminbean = new AdminBean();
      adminbean.setAdmin_orl(admin_orl);
      adminbean.setModule_orl(module_orl);
      
      if (StringUtils.isEmpty(admin_orl)) {
        daoAdmin.sqlAdminInsert(adminbean);
      } else {
        daoAdmin.sqlAdminUpdate(adminbean);
      }

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


}
