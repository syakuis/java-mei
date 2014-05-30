/*
 * SnsAuthAdminView.java 2011.10.27
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.snsauth;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class SnsAuthAdminView extends ActionSnsAuth {
  private Logger log = Logger.getLogger( this.getClass() );

  private boolean getSnsAuthGrant() throws Exception {
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

  public String dispSnsAuthItemAdminList() throws Exception {
    // 권한체크
    if (!getSnsAuthGrant()) { return "message"; }

    int page = meiConfig.getInt("mei.paging.now");
    int page_row = meiConfig.getInt("mei.paging.row");
    int page_link = meiConfig.getInt("mei.paging.link");
    paging = new PageNavigator(request,page,page_row,page_link);

    Map mapSch = new HashMap();

    long total = daoSnsAuth.sqlSnsAuthCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());

    O.put("listAuth", (List) daoSnsAuth.sqlSnsAuthList(mapSch));

    return SUCCESS;
  }


}