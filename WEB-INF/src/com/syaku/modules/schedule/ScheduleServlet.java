/*
 * ScheduleServlet.java 2011.03.15
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html

<servlet>
<servlet-name>ScheduleServlet</servlet-name>
<servlet-class>com.syaku.modules.schedule.ScheduleServlet</servlet-class>
<load-on-startup>1</load-on-startup>
</servlet>

 */
package com.syaku.modules.schedule;

import java.util.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class ScheduleServlet extends HttpServlet {
  private Logger log = Logger.getLogger( this.getClass() );
  private static final ScheduledExecutorService executor = ScheduleSingleton.getInstance();

  public void init(ServletConfig config) throws ServletException {
    log.info(StringUtils.join(new String[] {
      "{ScheduleServlet.init}\r\n" , 
      "==========================================================================\r\n" , 
      "> Schedule Module Servlet Starting ... \r\n" , 
      "=========================================================================="
    }));

    super.init(config);
    ScheduleManager manager = new ScheduleManager();
    manager.process();
  }

  public void destroy() {

    log.info(StringUtils.join(new String[] {
      "{ScheduleServlet.init}\r\n" , 
      "==========================================================================\r\n" , 
      "> Schedule Module Servlet Shutdown ... \r\n" , 
      "=========================================================================="
    }));

    try {
      executor.shutdownNow();
      executor.awaitTermination(5000, TimeUnit.MICROSECONDS);

      if(!executor.isTerminated()) {
        log.info("Schedule Force shut down the thread.");
      }
    } catch (InterruptedException ie) {
      log.debug(ie.getMessage());
    }
  }

}
