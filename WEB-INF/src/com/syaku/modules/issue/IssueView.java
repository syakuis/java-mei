/*
 * IssueView.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.issue;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.*;
import com.syaku.core.util.*;

public class IssueView extends ActionIssue {
  private Logger log = Logger.getLogger( this.getClass() );

  private boolean getIssueGrant(boolean grant) throws Exception {
    if (!grant) {
      MESSAGE.put("message","권한이 없습니다.");
      MESSAGE.put("display","");
      MESSAGE.put("error","true");
      MESSAGE.put("action","");
      MESSAGE.put("source","");
      valuestack.push(MESSAGE);
    }
    return grant;
  }

  public String dispIssueInsert() throws Exception {

    MEI_ADDONS.add("/addons/ckeditor/addon.html"); // 애드온 추가
    MEI_ADDONS.add("/addons/SWFUpload/addon.html"); // 애드온 추가

    String issue_state_xml = daoCode.getXmlCache("dsad","depth");
    O.put("issue_state_xml",issue_state_xml);

    Map mapSch = new HashMap();
    mapSch.put("module_orl", MODULE_ORL);
    O.put("listCategory",(List) daoCategory.sqlCategoryList(mapSch)); // 분류

    String member_orl = StringUtils.defaultIfEmpty((String) session.get("_MEI_MEMBER_ORL"),"0");
    String nickname = (String) session.get("_MEI_NICKNAME");
    String user_id = (String) session.get("_MEI_USER_ID");

    IssueBean issuebean = new IssueBean();
    issuebean.setMember_orl(member_orl);
    issuebean.setNickname(nickname);
    issuebean.setUser_id(user_id);

    O.put("objIssue", issuebean);

    return SUCCESS;
  }


  public String dispIssueList() throws Exception {
    int page_row = NumberUtils.stringToInt( (String) MM.get("options_list_count") , meiConfig.getInt("mei.paging.row") );
    int page_link = NumberUtils.stringToInt( (String) MM.get("options_page_count") , meiConfig.getInt("mei.paging.link") );

    int page = meiConfig.getInt("mei.paging.now");
    paging = new PageNavigator(request,page,page_row,page_link);

    Map mapSch = new HashMap();
    mapSch.put("module_orl", MODULE_ORL );
    mapSch.put("issue_root", "Y" );

     // 분류
    O.put("listCategory",(List) daoCategory.sqlCategoryList(mapSch));  

    long total = daoIssue.sqlIssueCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());
    O.put("listIssue", (List) daoIssue.sqlIssueList(mapSch));

    return SUCCESS;
  }


  public String dispIssueView() throws Exception {
    String group_orl = param.value("group_orl");
     int page_row = NumberUtils.stringToInt( (String) MM.get("options_list_count") , meiConfig.getInt("mei.paging.row") );
    int page_link = NumberUtils.stringToInt( (String) MM.get("options_page_count") , meiConfig.getInt("mei.paging.link") );

    int page = meiConfig.getInt("mei.paging.now");
    paging = new PageNavigator(request,page,page_row,page_link);

    Map mapSch = new HashMap();
    mapSch.put("group_orl", group_orl);
    mapSch.put("module_orl", MODULE_ORL);

     O.put("listCategory",(List) daoCategory.sqlCategoryList(mapSch));  

    long total = daoIssue.sqlIssueCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());
    O.put("listGroup", (List) daoIssue.sqlIssueList(mapSch));

    return SUCCESS;
  }


}

 