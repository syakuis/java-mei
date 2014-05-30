/*
 * IssueObject.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.issue;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;

public class IssueObject extends IssueAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/issue/info.properties");
}