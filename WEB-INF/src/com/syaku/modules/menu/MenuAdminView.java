/*
 * MenuAdminView.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.menu;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class MenuAdminView extends ActionMenu {
  private Logger log = Logger.getLogger( this.getClass() );

  public String dispMenuAdminList() throws Exception {
    int page = meiConfig.getInt("mei.paging.now");
    int page_row = meiConfig.getInt("mei.paging.row");
    int page_link = meiConfig.getInt("mei.paging.link");
    paging = new PageNavigator(request,page,page_row,page_link);

    Map mapSch = new HashMap();
    long total = daoMenu.sqlMenuCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());

    O.put("listMenu", (List) daoMenu.sqlMenuList(mapSch));

    return SUCCESS;
  }

  public String dispMenuAdminView() throws Exception {
    String menu_orl = param.value("menu_orl");
    Map mapSch = new HashMap();
    mapSch.put("menu_orl", menu_orl);
    O.put("objMenu", (Object) daoMenu.sqlMenuObject(mapSch));
    return SUCCESS;
  }

  public String dispMenuAdminInsert() throws Exception {
    String menu_orl = param.value("menu_orl");

    MenuBean menubean = new MenuBean();

    if (StringUtils.isNotEmpty(menu_orl)) {
      HashMap mapSch = new HashMap();
      mapSch.put("menu_orl",menu_orl);
      menubean = (MenuBean) daoMenu.sqlMenuObject(mapSch);
    }

    O.put("objMenu", menubean);

    return SUCCESS;
  }

  public String dispMenuItemAdminView() throws Exception {
    String menu_orl = param.value("menu_orl");

    MenuBean menubean = new MenuBean();

    if (StringUtils.isNotEmpty(menu_orl)) {
      HashMap mapSch = new HashMap();
      mapSch.put("menu_orl",menu_orl);

      menubean = (MenuBean) daoMenu.sqlMenuObject(mapSch);
    }

    O.put("objMenu", menubean);

    return SUCCESS;
  }

  public String dispMenuItemAdminInsert() throws Exception {

    String menu_orl = param.value("menu_orl");
    String menu_item_orl = param.value("menu_item_orl");
    String parent_orl = param.valueIfEmpty("parent_orl","0");
    String num = param.valueIfEmpty("num","0");

    MenuItemBean menuitembean = new MenuItemBean();

//    O.put("listGroup",(List) mMemberObject.getGroupList(null)); // 그룹목록

    if (StringUtils.isNotEmpty(menu_item_orl)) {
      Map mapSch = new HashMap();
      mapSch.put("menu_item_orl",menu_item_orl);

      menuitembean = (MenuItemBean) daoMenu.sqlMenuItemObject(mapSch);
    }

    O.put("objMenuItem", menuitembean);

    MOD_FTL_LAYOUT = MOD_FTL_MODULE; // 모듈 스킨 템플릿만 노출

    return SUCCESS;
  }

}
