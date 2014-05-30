/*
 * MenuAdminController.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.menu;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

import com.syaku.modules.module.*;

public class MenuAdminController extends ActionMenu {
  private Logger log = Logger.getLogger( this.getClass() );

  public String procMenuAdminInsert() throws Exception {
    String menu_orl = param.value("menu_orl");
    String title = param.value("title");

    try {
      sqlMap.startTransaction();

      MenuBean menubean = new MenuBean();
      menubean.setMenu_orl(menu_orl);
      menubean.setTitle(title);
      menubean.setRdate(DateUtils.date("yyyyMMddHHmmss"));

      if (StringUtils.isEmpty(menu_orl)) {
        daoMenu.sqlMenuInsert(menubean);
      } else {
        daoMenu.sqlMenuUpdate(menubean);
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

  public String procMenuAdminDelete() throws Exception {
    String menu_orl = param.value("menu_orl");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("menu_orl",menu_orl);

      daoMenu.sqlMenuDelete(mapSch);
      daoMenu.sqlMenuItemDeleteRemove(mapSch);

      // 아이템 삭제
      String name = modConfig.getString("mei.path.cache.menu") + "/menu_list_" + menu_orl + ".xml";
      String name_depth = modConfig.getString("mei.path.cache.menu") + "/menu_" + menu_orl + ".xml";
      File file = new File(name);
      if (file.exists()) { file.delete(); }
      file = new File(name_depth);
      if (file.exists()) { file.delete(); }

      // 모듈 메뉴 모두 리셋
      if ( !StringUtils.equals(menu_orl,"0") && StringUtils.isNotEmpty(menu_orl) ) {
        ModuleObject daoModule = new ModuleObject();
        Map map = new HashMap();
        map.put("menu_orl",menu_orl);
        map.put("new_menu_orl","0");
        daoModule.sqlModuleMenuUpdate(map);
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

  public String procMenuItemAdminInsert() throws Exception {
    String menu_orl = param.valueIfEmpty("menu_orl");

    try {
      sqlMap.startTransaction();
      daoMenu.getMenuItemInsert(param);

      Map mapSch = new HashMap();
      mapSch.put("menu_orl",menu_orl);
      daoMenu.getMenuCacheFile(mapSch); // 캐쉬파일 생성

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

  public String procMenuItemAdminMove() throws Exception {
    String menu_orl = param.valueIfEmpty("menu_orl");

    try {
      sqlMap.startTransaction();

      daoMenu.getMenuItemMove(param);

      Map mapSch = new HashMap();
      mapSch.put("menu_orl",menu_orl);
      daoMenu.getMenuCacheFile(mapSch); // 캐쉬파일 생성

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


  public String procMenuItemAdminDelete() throws Exception {
    String menu_orl = param.valueIfEmpty("menu_orl","");
    String menu_item_orl = param.valueIfEmpty("menu_item_orl","");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("menu_orl",menu_orl);
      mapSch.put("menu_item_orl",menu_item_orl);
      daoMenu.sqlMenuItemDelete(mapSch);

      mapSch = new HashMap();
      mapSch.put("menu_orl",menu_orl);
      daoMenu.getMenuCacheFile(mapSch); // 캐쉬파일 생성

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


  public String procMenuAdminCache() throws Exception {

    try {

      daoMenu.getMenuCacheFile(); // 캐쉬파일 생성

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
