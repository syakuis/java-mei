/*
 * MemberAdminView.java 2011.06.08
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.member;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class MemberAdminView extends ActionMember {
  private Logger log = Logger.getLogger( this.getClass() );

  private boolean getGrantManager() throws Exception {
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

  public String dispMemberAdminList() throws Exception {
    if (!getGrantManager()) { return "message"; }

    String sch_type = param.value("sch_type");
    String sch_value = param.value("sch_value");

    int page = meiConfig.getInt("mei.paging.now");
    int page_row = meiConfig.getInt("mei.paging.row");
    int page_link = meiConfig.getInt("mei.paging.link");
    paging = new PageNavigator(request,page,page_row,page_link);

    Map mapSch = new HashMap();
    mapSch.put("sch_type", sch_type);
    mapSch.put("sch_value", sch_value);

    long total = daoMember.sqlMemberCount(mapSch);
    paging.setTotal_count(total);
    paging.setPageIndex();

    mapSch.put("page_start", paging.getStart_idx());
    mapSch.put("page_row", paging.getPage_row());

    O.put("listMember", (List) daoMember.sqlMemberList(mapSch));
    return SUCCESS;
  }


  public String dispMemberAdminView() throws Exception {
    if (!getGrantManager()) { return "message"; }

    String member_orl = param.value("member_orl");

    O.put("listGroup",(List) daoMember.sqlGroupList(null));

    Map mapSch = new HashMap();
    mapSch.put("member_orl", member_orl);
    MemberBean memberbean = (MemberBean) daoMember.sqlMemberObject(mapSch);

    if ( memberbean == null ) {
      MESSAGE.put("message","올바른 접속이 아닙니다.");
      MESSAGE.put("display","");
      MESSAGE.put("error","true");
      MESSAGE.put("action","");
      MESSAGE.put("source","");
      valuestack.push(MESSAGE);
      return "message";
    }

    O.put("objMember" , memberbean );
    O.put("is_auth" , daoMember.isMemberAuth(member_orl) );
    O.put("listGroupMember" , (List) daoMember.sqlGroupMemberList(mapSch) );

    return SUCCESS;
  }

  public String dispMemberAdminInsert() throws Exception {
    if (!getGrantManager()) { return "message"; }

    String member_orl = param.value("member_orl");

    MemberBean memberbean = new MemberBean();

    if (StringUtils.isNotEmpty(member_orl)) {
      Map mapSch = new HashMap();
      mapSch.put("member_orl", member_orl);

      memberbean = (MemberBean) daoMember.sqlMemberObject(mapSch);
      O.put("objMember", memberbean );
      O.put("listGroupMember" , (List) daoMember.sqlGroupMemberList(mapSch) );
    }

    O.put("objMember" , memberbean );
    O.put("listGroup" , (List) daoMember.sqlGroupList(null) ); // 그룹목록

    return SUCCESS;
  }


  public String dispGroupAdminList() throws Exception {
    if (!getGrantManager()) { return "message"; }

    O.put("listGroup", (List) daoMember.sqlGroupList(null));
    return SUCCESS;
  }

public String dispGroupAdminUpdate() throws Exception {
    if (!getGrantManager()) { return "message"; }

    String group_orl = param.value("group_orl","");

    Map mapSch = new HashMap();
    mapSch.put("group_orl",group_orl);
    O.put("objGroup", (Object) daoMember.sqlGroupObject(mapSch));

    return SUCCESS;
  }

}
