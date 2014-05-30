/*
 * LayoutAdminView.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.layout;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import net.sf.json.*;

import com.syaku.core.util.*;

public class LayoutAdminView extends ActionLayout {
  private Logger log = Logger.getLogger( this.getClass() );

  public String dispLayoutAdminList() throws Exception {
    int page = meiConfig.getInt("mei.paging.now");
    int page_row = meiConfig.getInt("mei.paging.row");
    int page_link = meiConfig.getInt("mei.paging.link");
    paging = new PageNavigator(request,page,page_row,page_link);

    Map mapSch = new HashMap();
    long total = daoLayout.sqlLayoutCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());
    O.put("listLayout", daoLayout.sqlLayoutList(mapSch) );

    return SUCCESS;
  }


  public String dispLayoutAdminInsert() throws Exception {
    String layout_orl = param.value("layout_orl");

    Map mapSch = new HashMap();

    O.put("listMenu" , (List) daoMenu.sqlMenuList(mapSch) );
    O.put("listLayout" , (List) daoLayout.getLayoutDirList(MEI_LAYOUT_PATH_RELATIVE));

    LayoutBean object = new LayoutBean();
    O.put("objLayoutOptions",new Object());

    if (StringUtils.isNotEmpty(layout_orl)) {
      mapSch = new HashMap();
      mapSch.put("layout_orl", layout_orl); 
      object = (LayoutBean) daoLayout.sqlLayoutObject(mapSch);

      String layout = object.getLayout();
      String extra_vars = object.getExtra_vars();
      if ( StringUtils.isNotEmpty(extra_vars) ) {
        JSONObject objJson = JSONObject.fromObject(extra_vars);
        O.put("objLayoutOptions",objJson);
      }

      String options_include = meiConfig.getString("mei.path.layouts") + "/" + layout + "/admin.layout.insert.html";
      File file = new File(MEI_PATH_ABSOLUTE_RELATIVE + options_include);
      if ( file.exists() ) {
        O.put("options_include", MEI_PATH_RELATIVE + options_include);
      }

    }

    O.put("objLayout", object);

    return SUCCESS;
  }

}
