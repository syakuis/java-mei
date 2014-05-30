/*
 * CommentView.java 2011.06.13
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.comment;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.modules.snsauth.*;

public class CommentView extends ActionComment {
  private Logger log = Logger.getLogger( this.getClass() );

  private SnsAuthObject daoSnsAuth = new SnsAuthObject();
  private SnsAuthStored daoSnsAuthStored = new SnsAuthStored();

  /*
<@s.action name="dispCommentList" executeResult="true">
<@s.param name="interlock">true</@s.param>
<@s.param name="target_orl"></@s.param>
</@s.action>
  */
  public String dispCommentList() throws Exception {
    Map mapSns = new HashMap();

    if (StringUtils.equals(MOD_IS_SNS,"Y")) {
      mapSns = daoSnsAuth.getSignIn(null);
      O.put("sns_main_name",mapSns.get("sns_main_name"));
      O.put("twitter",mapSns.get("twitter"));
      O.put("yozm",mapSns.get("yozm"));
      O.put("me2day",mapSns.get("me2day"));
      O.put("facebook",mapSns.get("facebook"));

      // SNS 세션이 존재할 경우 로그인으로 인정
      if (daoSnsAuth.getAuthSessValid()) { MEI_GRANT_LOGIN = true; }
    }

    String member_orl = (String) session.get("_MEI_MEMBER_ORL");
    String target_orl = param.arrayValue("target_orl");

    Map mapSch = new HashMap();
    mapSch.put("module_orl", MODULE_ORL);
    mapSch.put("target_orl", target_orl);

    if (StringUtils.equals(MOD_IS_SNS,"Y")) {
      O.put("listComment", (List) daoComment.getCommentSnsList(mapSch,member_orl,MEI_GRANT_MANAGER,mapSns));
    } else {
      O.put("listComment", (List) daoComment.getCommentList(mapSch,member_orl,MEI_GRANT_MANAGER));
    }

    O.put("target_orl",target_orl);

    MOD_FTL_LAYOUT = MOD_FTL_MODULE;

    return SUCCESS;
  }


}