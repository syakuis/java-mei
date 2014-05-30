/*
 * MeiBasic.java 2008.12.17
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

import com.syaku.modules.module.*;

public class MeiBasic extends Mei {
  private Logger log = Logger.getLogger( this.getClass() );

  public void meiBefore() { }
  public void meiAfter() { }

  public void prepare() {
    log.debug("[## MEI MeiBasic prepare] Called");
    meiBefore();
    meiInit();
    meiPrepare();
    modProperties();
    modInfo();
    grant();
    modSkin();
    layoutInfo();
    menuList();
    meiAfter();
    log.info( StringUtils.join(new String[] {
      meiConfig.getString("mei.log.message.head") , 
      LOG_TEXT , 
      meiConfig.getString("mei.log.message.foot")
    }) );
  }

}