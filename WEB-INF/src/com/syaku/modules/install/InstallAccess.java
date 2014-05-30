/*
 * InstallAccess.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.install;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class InstallAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();
}