/*
 * InstallAdminController.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.install;

import java.util.*;
import java.lang.reflect.*;
import java.lang.reflect.Method.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class InstallAdminController extends ActionInstall {
  private Logger log = Logger.getLogger( this.getClass() );

  public String procInstallAdminModuleInstall() throws Exception {

    String classes = param.value("classes");
  
    try {

      if ( StringUtils.isNotEmpty(classes) ) {
        Class cls =Class.forName(classes);
        Object obj = cls.newInstance();
        Method method = obj.getClass().getDeclaredMethod("getInstallBefore");
        method.invoke(obj);
        Method method2 = obj.getClass().getDeclaredMethod("getInstallAfter");
        method2.invoke(obj);
      }

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
