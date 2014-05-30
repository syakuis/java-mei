/*
 * ActionCode.java 2012-03-23
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.code;

import java.util.*;

import org.apache.log4j.Logger;

import com.syaku.core.*;

public class ActionCode extends MeiBasic {
  private Logger log = Logger.getLogger( this.getClass() );
  public CodeObject daoCode = new CodeObject();

  public String CODE_CACHE_PATH;

  public void meiAfter() {
    CODE_CACHE_PATH = MEI_PATH_ABSOLUTE_RELATIVE + meiConfig.getString("mei.path.cache") + "/code";
  }

}