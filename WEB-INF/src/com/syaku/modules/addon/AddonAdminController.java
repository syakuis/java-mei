/*
 * AddonAdminController.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.addon;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;
import com.syaku.modules.module.*;

public class AddonAdminController extends ActionAddon {
  private Logger log = Logger.getLogger( this.getClass() );


  public String procAddonInstall() throws Exception {
    String module_orl = param.value("module_orl");
    String addon = param.value("addon");

    try {
      sqlMap.startTransaction();
      
      if ( StringUtils.isEmpty( addon ) ) {
        throw new Exception("에드온이 없습니다.");
      }

      String source = MEI_PATH_ABSOLUTE_RELATIVE + MEI_PATH_ADDONS + "/" + addon + "/addon.xml";
      Map mapData = daoAddon.prop2map(source);

      String addon_type = (String) mapData.get("type");
      String title = (String) mapData.get("title");
      String addon_path = (String) mapData.get("path");

      AddonBean addonbean = new AddonBean();

      addonbean.setModule_orl(module_orl);
      addonbean.setAddon(addon);
      addonbean.setAddon_type(addon_type);
      addonbean.setTitle(title);
      addonbean.setAddon_path(addon_path);
      addonbean.setIs_use("Y");
      addonbean.setRegdate( DateUtils.date("yyyyMMddHHmmss") );

      daoAddon.sqlAddonInsert(addonbean);

      daoAddon.propReload();

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

  // 애드온 사용여부 설정
  public String procAddonIsUseUpdate() throws Exception {
    String addon_orl = param.value("addon_orl");
    String is_use = param.value("is_use");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("addon_orl",addon_orl);
      mapSch.put("is_use",is_use);
      daoAddon.sqlAddonIsUseUpdate(mapSch);
      
      daoAddon.propReload();

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

  // 기본애드온 설정
  public String procAddonFirstLoadUpdate() throws Exception {
    String addon_type = param.value("addon_type");
    String addon_orl = param.value("addon_orl");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("addon_type",addon_type);
      mapSch.put("first_load","N");
      daoAddon.sqlAddonFistLoadUpdate(mapSch);

      mapSch = new HashMap();
      mapSch.put("addon_type",addon_type);
      mapSch.put("addon_orl",addon_orl);
      mapSch.put("first_load","Y");
      daoAddon.sqlAddonFistLoadUpdate(mapSch);

      daoAddon.propReload();

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

  

  public String procAddonDelete() throws Exception {
    String addon_orl = param.value("addon_orl");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("addon_orl",addon_orl);
      daoAddon.sqlAddonDelete(mapSch);

      daoAddon.propReload();

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