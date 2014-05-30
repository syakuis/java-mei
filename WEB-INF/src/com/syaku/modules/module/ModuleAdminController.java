/*
 * ModuleAdminController.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.module;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

import com.syaku.modules.layout.*;

public class ModuleAdminController extends ActionModule {
  private Logger log = Logger.getLogger( this.getClass() );

  public String procModuleAdminInsert() throws Exception {

    String module_orl = MODULE_ORL;
    String mid = param.value("module_id");
    String browser_title = param.value("browser_title");
    String skin = param.value("skin");
    String layout_orl = param.valueIfEmpty("layout_orl","0");
    String content = param.valueIfEmpty("content","");
    String header_content = param.valueIfEmpty("header_content","");
    String footer_content = param.valueIfEmpty("footer_content","");

    try {
      sqlMap.startTransaction();

      ModuleBean modulebean = new ModuleBean();
      modulebean.setModule_orl(module_orl);
      modulebean.setModule(MOD_NAME);
      modulebean.setMid(mid);
      modulebean.setBrowser_title(browser_title);
      modulebean.setSkin(skin);
      modulebean.setLayout_orl(layout_orl);
      modulebean.setMenu_orl("0"); // 레이어 정보 호출
      modulebean.setContent(content);
      modulebean.setHeader_content(header_content);
      modulebean.setFooter_content(footer_content);

      module_orl = daoModule.moduleInsert(modulebean);

      // 모듈 옵션
      Map mapOptions = param.target(false,"options_");
      Map mapPropOptions = ConfigUtils.prop2map(modConfig,"mei.options");
      mapOptions = ExtraVarsUtils.extendMap(true,mapOptions,mapPropOptions);
      daoModule.moduleOptionsInsert(module_orl,mapOptions);

      ModuleProperties prop = new ModuleProperties();
      prop.config2prop(module_orl);

      sqlMap.commitTransaction();
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

  public String procModuleAdminDelete() throws Exception {
    String module_orl = param.value("module_orl");

    try {
      sqlMap.startTransaction();

      HashMap mapSch = new HashMap();
      mapSch.put("module_orl",module_orl);

      daoModule.sqlModuleDelete(mapSch);

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

  public String procModuleAdminPropInsert() throws Exception {
    String module_orl = param.value("m_orl");

    try {

      ModuleProperties prop = new ModuleProperties();
      prop.config2prop(module_orl);

      MESSAGE.put("error","false");
    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }


}
