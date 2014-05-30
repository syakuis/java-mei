/*
 * LayoutAdminController.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.layout;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

import com.syaku.modules.module.*;

public class LayoutAdminController extends ActionLayout {
  private Logger log = Logger.getLogger( this.getClass() );

  public String procLayoutAdminInsert() throws Exception {

    String layout_orl = param.value("layout_orl");
    String menu_orl = param.valueIfEmpty("menu_orl","0");
    String layout = param.value("layout");
    String title = param.value("title");
    String head_script = param.value("head_script");
    String extra_vars = ExtraVarsUtils.getObjectJson(request,"options_");
    String mobile = param.value("mobile");

    try {
      sqlMap.startTransaction();
      LayoutBean bean = new LayoutBean();

      bean.setLayout_orl(layout_orl);
      bean.setMenu_orl(menu_orl);
      bean.setLayout(layout);
      bean.setTitle(title);
      bean.setHead_script(head_script);
      bean.setExtra_vars(extra_vars);
      bean.setMobile(mobile);
      bean.setRdate(DateUtils.date("yyyyMMddHHmmss"));

      if (StringUtils.isEmpty(layout_orl)) {
        daoLayout.sqlLayoutInsert(bean);
      } else {
        daoLayout.sqlLayoutUpdate(bean);
      }

      // 모듈 메뉴 모두 업데이트
      if ( !StringUtils.equals(layout_orl,"0") && StringUtils.isNotEmpty(layout_orl) ) {
        ModuleObject daoModule = new ModuleObject();
        Map map = new HashMap();
        map.put("layout_orl",layout_orl);
        map.put("menu_orl",menu_orl);
        daoModule.sqlModuleLayoutMenuUpdate(map);
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

  public String procLayoutAdminDelete() throws Exception {
    String layout_orl = param.value("layout_orl");

    try {
      sqlMap.startTransaction();

      HashMap mapSch = new HashMap();
      mapSch.put("layout_orl",layout_orl);

      daoLayout.sqlLayoutDelete(mapSch);

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
