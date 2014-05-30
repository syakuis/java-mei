/*
 * AdminInstall.java 2011.01.01
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
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;
import com.syaku.modules.module.*;
import com.syaku.modules.menu.*;
import com.syaku.modules.layout.*;
import com.syaku.modules.install.*;

public class AdminInstall extends AdminObject {
  private Logger log = Logger.getLogger( this.getClass() );
  private ModuleObject daoModule = new ModuleObject();
  private MenuObject daoMenu = new MenuObject();
  private LayoutObject daoLayout = new LayoutObject();
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
      moduleTableCreate();

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
      moduleDataInsert();
      menuDataInsert();
      layoutDataInsert();
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

  public void moduleTableCreate() throws Exception {
    sqlAdminCreate(null);

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

  public void moduleDataInsert() throws Exception {
    String get_module_orl = daoModule.getModule_orl(module);
    this.module_orl = CONFIG.getString(config_name + ".module_orl" , get_module_orl );

    if ( StringUtils.isEmpty(module_orl) ) {
      ModuleBean modulebean = new ModuleBean();
      modulebean.setModule(module);
      modulebean.setMid(module);
      modulebean.setBrowser_title(title);
      modulebean.setLayout_orl("1");
      modulebean.setMenu_orl("1");
      modulebean.setRdate(rdate);
      module_orl = daoModule.moduleInsert(modulebean);
    }

    CONFIG.setProperty(config_name + ".module_orl",module_orl);
  }

  public void menuDataInsert() throws Exception {
    String menu_orl = CONFIG.getString(config_name + ".menu_orl");

    Map mapSch = new HashMap();

    if ( StringUtils.isEmpty(menu_orl) ) {
      menu_orl = daoMenu.getMenu_orl("관리자");
      
      if (StringUtils.isEmpty(menu_orl) ) {
        MenuBean menubean = new MenuBean();
        menubean.setMenu_orl(menu_orl);
        menubean.setTitle("관리자");
        menubean.setRdate(rdate);
        menu_orl = daoMenu.sqlMenuInsert(menubean);
      }

      CONFIG.setProperty(config_name + ".menu_orl",menu_orl);
    }
    
    mapSch = new HashMap();
    mapSch.put("menu_orl",menu_orl);

    if ( daoMenu.sqlMenuItemCount(mapSch) == 0 && StringUtils.isNotEmpty(menu_orl) ) {
      CONFIG.clearProperty(config_name + ".menu_item_orl");
      String parent_orl = "0";
      String menu_item_orl = null;
      String menu_item_orl_array[] = new String[3];

      MenuItemBean data = new MenuItemBean();
      data.setMenu_item_orl(null);
      data.setParent_orl(parent_orl);
      data.setNum("0");
      data.setMenu_orl(menu_orl);
      data.setUrl("admin");
      data.setName("제어판");
      data.setOpen_window("0");
      data.setExpand("1");
      data.setIs_mobile("0");
      data.setRdate(rdate);
      menu_item_orl = daoMenu.sqlMenuItemInsert(data);
      menu_item_orl_array[0] = menu_item_orl;

      parent_orl = menu_item_orl;

      data = new MenuItemBean();
      data.setMenu_item_orl(null);
      data.setParent_orl(parent_orl);
      data.setNum( daoMenu.getMenuItemNumSort(menu_orl,parent_orl) );
      data.setMenu_orl(menu_orl);
      data.setUrl("");
      data.setName("사이트관리");
      data.setOpen_window("0");
      data.setExpand("1");
      data.setIs_mobile("0");
      data.setRdate(rdate);
      menu_item_orl = daoMenu.sqlMenuItemInsert(data);
      menu_item_orl_array[1] = menu_item_orl;

      data = new MenuItemBean();
      data.setMenu_item_orl(null);
      data.setParent_orl(parent_orl);
      data.setNum( daoMenu.getMenuItemNumSort(menu_orl,parent_orl) );
      data.setMenu_orl(menu_orl);
      data.setUrl("");
      data.setName("모듈관리");
      data.setOpen_window("0");
      data.setExpand("1");
      data.setIs_mobile("0");
      data.setRdate(rdate);
      menu_item_orl = daoMenu.sqlMenuItemInsert(data);
      menu_item_orl_array[2] = menu_item_orl;

      CONFIG.setProperty(config_name + ".menu_item_orl",menu_item_orl_array);
    }

  }

  public void layoutDataInsert() throws Exception {
    String layout_orl = CONFIG.getString(config_name + ".layout_orl");
    String menu_orl = "1";
    String layout = "admin";

    Map mapSch = new HashMap();
    mapSch.put("layout_orl",layout_orl);

    if (daoLayout.sqlLayoutCount(mapSch) == 0) {
      LayoutBean bean = new LayoutBean();
      bean.setLayout_orl(layout_orl);
      bean.setMenu_orl(menu_orl);
      bean.setLayout(layout);
      bean.setTitle(title);
      bean.setHead_script(null);
      bean.setExtra_vars(null);
      bean.setMobile(null);
      bean.setRdate(DateUtils.date("yyyyMMddHHmmss"));
      layout_orl = daoLayout.sqlLayoutInsert(bean);
      CONFIG.setProperty(config_name + ".layout_orl",layout_orl);
    }

  }

}