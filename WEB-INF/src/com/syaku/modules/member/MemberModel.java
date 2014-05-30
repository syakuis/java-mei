/*
 * MemberModel.java 2011.01.01
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

import com.syaku.core.*;
import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class MemberModel extends ActionMember {
  private Logger log = Logger.getLogger( this.getClass() );

  public String getMemberUserIdCheck() throws Exception {
    String member_orl = param.value("member_orl");
    String user_id = param.value("user_id");

    if (daoMember.isMemberUserIdOverlapCK(member_orl,user_id)) { 
      MESSAGE.put("message","사용할 수 있습니다.");
      MESSAGE.put("error","false");
    } else {
      MESSAGE.put("message","사용할 수 없습니다.");
      MESSAGE.put("error","true");
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

  public String getMemberNickNameCheck() throws Exception {
    String member_orl = param.value("member_orl");
    String nickname = param.value("nickname");

    if (daoMember.isMemberNickNameOverlapCK(member_orl,nickname)) { 
      MESSAGE.put("message","사용할 수 있습니다.");
      MESSAGE.put("error","false");
    } else {
      MESSAGE.put("message","사용할 수 없습니다.");
      MESSAGE.put("error","true");
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

  public String getMemberEmailCheck() throws Exception {
    String member_orl = param.value("member_orl");
    String email = param.value("email");

    if (daoMember.isMemberEmailOverlapCK(member_orl,email)) { 
      MESSAGE.put("message","사용할 수 있습니다.");
      MESSAGE.put("error","false");
    } else {
      MESSAGE.put("message","사용할 수 없습니다.");
      MESSAGE.put("error","true");
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

  public String getMemberUserIdSearch() throws Exception {
    String user_name = param.value("user_name");
    String email = param.value("email");

    if (StringUtils.isNotEmpty(user_name) && StringUtils.isNotEmpty(email) ) {
      Map mapSch = new HashMap();
      mapSch.put("user_name",user_name);
      mapSch.put("email",email);

     MemberBean memberbean = (MemberBean) daoMember.sqlMemberObject(mapSch);
      if (memberbean != null) { 
        String user_id = memberbean.getUser_id();
        MESSAGE.put("message",user_id);
        MESSAGE.put("error","false");
      } else {
        MESSAGE.put("message","찾을 수 없습니다.");
        MESSAGE.put("error","true");
      }

    } else {
      MESSAGE.put("message","찾을 수 없습니다.");
      MESSAGE.put("error","true");
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }


}