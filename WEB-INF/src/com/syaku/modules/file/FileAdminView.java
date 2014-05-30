/*
 * FileAdminView.java 2011.12.07
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.file;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class FileAdminView extends ActionFile {
  private Logger log = Logger.getLogger( this.getClass() );

  private boolean getFileGrant() throws Exception {
    if (!MEI_GRANT_MANAGER) {
      MESSAGE.put("message","권한이 없습니다.");
      MESSAGE.put("display","");
      MESSAGE.put("error","true");
      MESSAGE.put("action","");
      MESSAGE.put("source","");
      valuestack.push(MESSAGE);
    }

    return MEI_GRANT_MANAGER;
  }

  public String dispFileAdminView() throws Exception {
    if (!getFileGrant()) { return "message"; }
    // 총 용량 true_item_size , true_item_count , false_item_size , false_item_count , total_item_size , total_item_count
    O.put("objFileSize",daoFile.getFileItemSizeInfo(null));
    return SUCCESS;
  }

  public String dispFileAdminClearView() throws Exception {
    O.put("objFile",daoFile.sqlFileAdminModuleList(null));
    return SUCCESS;
  }

  public String dispFileAdminItemList() throws Exception {
    if (!getFileGrant()) { return "message"; }

    String sch_type = param.value("sch_type");
    String sch_value = param.value("sch_value");
    Map mapSch = new HashMap();
    mapSch.put("sch_type", sch_type );
    mapSch.put("sch_value", sch_value );

    long total = daoFile.sqlFileAdminCount(mapSch);

    int page = meiConfig.getInt("mei.paging.now");
    int page_row = meiConfig.getInt("mei.paging.row");
    int page_link = meiConfig.getInt("mei.paging.link");
    paging = new PageNavigator(request,page,page_row,page_link);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());

    O.put("listFile", (List) daoFile.sqlFileAdminList(mapSch) );


    return SUCCESS;
  }


}
