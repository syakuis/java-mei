/*
 * ModuleAdminView.java 2012.02.01
 *
 * Copyright (c) 2011, MEI By Seok Kyun. Choi. (최석균)
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
import com.syaku.modules.comment.*;

public class ModuleAdminView extends ActionModule {
  private Logger log = Logger.getLogger( this.getClass() );

  private CommentObject daoComment = new CommentObject();

  public String dispModuleAdminPopupList() throws Exception {
    int page = meiConfig.getInt("mei.paging.now");
    int page_row = meiConfig.getInt("mei.paging.row");
    int page_link = meiConfig.getInt("mei.paging.link");
    paging = new PageNavigator(request,page,page_row,page_link);

    String sch_mid = param.value("sch_mid");
    String sch_module = param.value("sch_module");

    Map mapSch = new HashMap();
    mapSch.put("mid",sch_mid);
    mapSch.put("module",sch_module);

    List list = daoModule.sqlModuleList(mapSch);
    O.put("listModule", list);

    paging.setTotal_count(list.size());
    
    MOD_FTL_MODULE_LAYOUT_HIDE = true; // 팝업형태 레이아웃 삭제

    return SUCCESS;
  }

  public String dispModuleAdminList() throws Exception {

    int page = meiConfig.getInt("mei.paging.now");
    int page_row = meiConfig.getInt("mei.paging.row");
    int page_link = meiConfig.getInt("mei.paging.link");
    paging = new PageNavigator(request,page,page_row,page_link);

    Map mapSch = new HashMap();

    if ( !StringUtils.equals( "module" , MOD_NAME) ) {
      mapSch.put("module", MOD_NAME); // 모듈 네임
    }

    long total = daoModule.sqlModuleCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());
    O.put("listModule" , daoModule.sqlModuleList(mapSch) );

    return SUCCESS;
  }

  public String dispModuleAdminInsert() throws Exception {
    String module_orl = param.value("module_orl");
    
    O.put("listLayout",(List) daoLayout.sqlLayoutList(null));
    O.put("listSkins",(List) daoModule.skinList( MEI_PATH_ABSOLUTE_RELATIVE + modConfig.getString("mei.module.path.skins") ));
    O.put("listCommentSkins",(List) daoComment.skinList());

    if (MOD_SINGLE) {
      O.put("objModule", MM );
    } else {

      Map map = new HashMap();
      if (StringUtils.isNotEmpty(module_orl)) {
        Map mapSch = new HashMap();
        mapSch.put("module_orl",module_orl);
        map = (Map) daoModule.objectOne(mapSch);
        
        String skin = (String) map.get("skin");
        // 스킨에서 추가 옵션이 있을 경우
        if ( StringUtils.isNotEmpty(skin) ) {
          String options_include = MOD_PATH_RELATIVE + "/skins/" + skin + "/admin.options.insert.html";
          File file = new File(MEI_PATH_ABSOLUTE_RELATIVE + options_include);
          if ( file.exists() ) {
            O.put("options_include", MEI_PATH_RELATIVE + options_include);
          }
        }

      }

      O.put("objModule", map );
    }


    return SUCCESS;
  }

}
