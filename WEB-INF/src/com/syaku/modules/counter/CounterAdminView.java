/*
 * CounterAdminView.java 2011.12.21
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.counter;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class CounterAdminView extends ActionCounter {
  private Logger log = Logger.getLogger( this.getClass() );

  private boolean getCounterGrant() throws Exception {
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

  public String dispCounterAdminItemList() throws Exception {
    if (!getCounterGrant()) { return "message"; }

    int page = meiConfig.getInt("mei.paging.now");
    int page_row = meiConfig.getInt("mei.paging.row");
    int page_link = meiConfig.getInt("mei.paging.link");
    paging = new PageNavigator(request,page,page_row,page_link);

    Map mapSch = new HashMap();
    mapSch.put("module", MOD_NAME); // 모듈 네임

    long total = daoCounter.sqlCounterCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();


    mapSch.put("listorder", "rdate DESC");
    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());

    O.put("listCounter", (List) daoCounter.sqlCounterList(mapSch));

    return SUCCESS;
  }

  public String dispCounterAdminItemView() throws Exception {
    if (!getCounterGrant()) { return "message"; }

    MEI_ADDONS.add("/addons/jqplot/addon.html"); // 애드온 추가

    CalendarUtils calendar = new CalendarUtils();
    Map mapSch = new HashMap();
    mapSch.put("edate",calendar.date("yyyyMMdd"));
    calendar.day(-7);
    mapSch.put("sdate",calendar.date("yyyyMMdd"));
    O.put("listCounter", (List) daoCounter.getCounterStatsDayList(mapSch));

    return SUCCESS;
  }

}
