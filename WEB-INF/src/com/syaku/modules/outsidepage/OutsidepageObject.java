/*
 * OutsidepageObject.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.outsidepage;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.commons.configuration.*;

import com.ibatis.sqlmap.client.SqlMapClient;

import com.syaku.core.common.*;
import com.syaku.core.util.*;

public class OutsidepageObject {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/outsidepage/info.properties");

}