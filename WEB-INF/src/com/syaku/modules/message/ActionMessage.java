/*
 * ActionMessage.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
 package com.syaku.modules.message;

import org.apache.log4j.Logger;
import org.apache.commons.collections.*;

import com.syaku.core.*;

public class ActionMessage extends MeiBasic {
  private Logger log = Logger.getLogger( this.getClass() );

  // 오버라이드
  public void meiAfter() {
    log.info("[#MEI ActionMessage.meiAfter]");

    // 자신 모듈 정보를 사용함.
    if ( MapUtils.isNotEmpty(S) ) {
      String skin = (String) S.get("skin");
      modSkin(skin);
    }
  }

}