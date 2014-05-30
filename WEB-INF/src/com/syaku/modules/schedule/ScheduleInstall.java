/*
 * ScheduleInstall.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.schedule;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;
import com.syaku.modules.module.*;
import com.syaku.modules.menu.*;
import com.syaku.modules.install.*;

public class ScheduleInstall extends ScheduleObject {
  private Logger log = Logger.getLogger( this.getClass() );
  private ModuleObject daoModule = new ModuleObject();
  private MenuObject daoMenu = new MenuObject();
  private InstallObject daoInstall = new InstallObject();
  private PropertiesConfiguration CONFIG;

  private String module = modConfig.getString("mei.module.name");
  private String title = modConfig.getString("mei.module.title");
  private String rdate;
  private String config_name = "mei.install." + module;
  private String module_orl; // 싱글인 경우

  public void getInstallBefore() throws Exception {
    try {
      sqlMap.startTransaction();

      prepare();
      getTableCreate();

      sqlMap.commitTransaction();
    } catch (Exception e) {
      log.error(e.toString());
    } finally {
      sqlMap.endTransaction();
    }
  }

  public void getInstallAfter() throws Exception {
    try {
      sqlMap.startTransaction();

      prepare();
      getModuleInsert();
      getDataMenuInsert();
      getPropModule();

      sqlMap.commitTransaction();
    } catch (Exception e) {
      log.error(e.toString());
    } finally {
      sqlMap.endTransaction();
    }
  }


  //install
  public void prepare() throws Exception {
    CONFIG = (PropertiesConfiguration) daoInstall.setModuleProperties();
    CONFIG.setAutoSave(true);
  }

  public void getTableCreate() throws Exception {
    sqlScheduleCreate(null);
    sqlScheduleHistoryCreate(null);

    rdate = DateUtils.date("yyyyMMddHHmmss");
    CONFIG.setProperty(config_name + ".rdate" , rdate);
    CONFIG.setProperty(config_name , true);
  }

  public void getPropModule() throws Exception {
    //생성된 모듈 데이터 프로퍼티 생성
    String get_module_orl = daoModule.getModule_orl(module);
    ModuleProperties prop = new ModuleProperties();
    prop.config2prop(get_module_orl);
  }

  public void getModuleInsert() throws Exception {
    String get_module_orl = daoModule.getModule_orl(module);
    this.module_orl = CONFIG.getString(config_name + ".module_orl" , get_module_orl );

    if ( StringUtils.isEmpty(module_orl) ) {
      ModuleBean modulebean = new ModuleBean();
      modulebean.setModule(module);
      modulebean.setMid(module);
      modulebean.setBrowser_title(title);
      modulebean.setRdate(rdate);
      module_orl = daoModule.moduleInsert(modulebean);
    }

    CONFIG.setProperty(config_name + ".module_orl",module_orl);
  }

  public void getDataMenuInsert() throws Exception {
    String menu_orl = "1";
    String parent_orl = "2";
    String menu_item_orl = CONFIG.getString(config_name + ".menu_item_orl");
    String url = "dispScheduleAdminInsert.me?mid=admin";

    if ( StringUtils.isEmpty(menu_item_orl) ) {
      menu_item_orl = daoMenu.getMenu_item_orl(title,url);

      if ( StringUtils.isEmpty(menu_item_orl) ) {
      MenuItemBean data = new MenuItemBean();
      data.setMenu_item_orl(null);
      data.setParent_orl(parent_orl);
      data.setNum( daoMenu.getMenuItemNumSort(menu_orl,parent_orl) );
      data.setMenu_orl(menu_orl);
      data.setUrl(url);
      data.setName(title);
      data.setOpen_window("0");
      data.setExpand("1");
      data.setIs_mobile("0");
      data.setRdate(rdate);
      menu_item_orl = daoMenu.sqlMenuItemInsert(data);
      }

      CONFIG.setProperty(config_name + ".menu_item_orl",menu_item_orl);
    }

  }

}