/*
 * ActionComment.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

 /*
 MODULE_ORL 파라메터 값을 필수적으로 받아야 함.
 */

package com.syaku.modules.comment;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

import com.syaku.core.*;

public class ActionComment extends MeiBasic {
  private Logger log = Logger.getLogger( this.getClass() );
  public CommentObject daoComment = new CommentObject();
  public String MOD_IS_SNS = "N";

  // 오버라이드
  public void meiAfter() {
    log.info("[#MEI ActionComment.meiAfter]");
    String skin = "";
    if ( MapUtils.isNotEmpty(MM) ) {
      skin = (String) MM.get("options_comment_skin");
      modSkin(skin);
      MOD_IS_SNS = (String) MM.get("options_is_sns");
    }
  }

}