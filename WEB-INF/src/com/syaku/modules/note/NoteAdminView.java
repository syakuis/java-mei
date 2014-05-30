/*
 * NoteAdminView.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.note;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class NoteAdminView extends ActionNote {
  private Logger log = Logger.getLogger( this.getClass() );

  public String dispNoteAdminItemList() throws Exception {
    // 목록 수
    int page_row = NumberUtils.stringToInt( (String) MM.get("options_list_count") , meiConfig.getInt("mei.paging.row") );
    // 페이지 수
    int page_link = NumberUtils.stringToInt( (String) MM.get("options_page_count") , meiConfig.getInt("mei.paging.link") );

    int page = meiConfig.getInt("mei.paging.now");
    paging = new PageNavigator(request,page,page_row,page_link);

    Map mapSch = new HashMap();
    long total = daoNote.sqlNoteCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());
    O.put("listNote", (List) daoNote.sqlNoteList(mapSch));
     return SUCCESS;
  }

}
