/*
 * ScheduleTester.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.schedule;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class ScheduleTester {
  private Logger log = Logger.getLogger( this.getClass() );

  public ScheduleTester() throws Exception {

    log.debug( StringUtils.join(new String[] {
      "{#MEI ScheduleTester}\r\n" , 
      "==========================================================================\r\n" , 
      "작업스케줄 : Call ScheduleTester ... " , "\r\n" ,
      "=========================================================================="
    }) );

  }

}
