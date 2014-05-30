/*
 * OutsidepagePrivilege.java 2011.07.24
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
import org.apache.commons.lang.*;

public class OutsidepagePrivilege {
  private Logger log = Logger.getLogger( this.getClass() );

  public void getOutsidepagePrivilegeException(boolean grant) throws Exception {
    if (!grant) { throw new Exception("권한이 없습니다."); }
  }

}