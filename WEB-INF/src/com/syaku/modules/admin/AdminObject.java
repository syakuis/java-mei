/*
 * CounterAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.admin;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;

public class AdminObject extends AdminAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration meiConfig = ConfigUtils.getProperties("mei.properties");
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/admin/info.properties");

}