/*
 * ScheduleSingleton.java 2008.12.17
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.schedule;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.log4j.Logger;

public class ScheduleSingleton {
  private static Logger log = Logger.getLogger( ScheduleSingleton.class );

  private static final ScheduledExecutorService schedule;

  static {

    try {
      schedule = new ScheduledThreadPoolExecutor(1);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException ("Error initializing ScheduleSingleton class. Cause: " + e);
    }

  }

  public static ScheduledExecutorService getInstance() {
    return schedule;
  }

  public static void begin() {

    if ( schedule.isShutdown() ) {
      log.debug(schedule);
      ScheduleManager manager = new ScheduleManager();
      manager.process();
    }

  }

}