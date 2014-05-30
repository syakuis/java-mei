/*
 * MeiInstall.java 2012.02.03
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.builder;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Method.*;

import org.apache.log4j.*;
import org.apache.commons.configuration.*;
import org.apache.commons.lang.*;
import org.apache.commons.io.*;
import com.ibatis.sqlmap.client.SqlMapClient;

import com.syaku.core.util.*;
import com.syaku.core.common.*;

public class MeiInstall {
  private static Logger log = Logger.getLogger(MeiInstall.class);
  private static Configuration MEI;
  private static PropertiesConfiguration CONFIG;
  private static final SqlMapClient sqlMap = MyBatis.getInstance();

  public static void main(String[] args) throws Exception {
    building();
  }

  public static void building() {
    log_start("MEI Install");

    try {
      MEI = new PropertiesConfiguration("mei.properties");
      String path_config = MEI.getString("mei.path.rec_config");

      CONFIG = new PropertiesConfiguration(path_config);
      CONFIG.clear();
      CONFIG.setAutoSave(true);

      getInstallBefore();
      getInstallAfter();

    } catch (Exception e) {
      log_println(e.toString());
    }

    log_end();
  }

  private static void getInstallBefore() throws Exception {

    try {

      List list = MEI.getList("mei.builder.module");
      int count = list.size();
      
      for (int i = 0; i < count; i++ ) {
        String module = (String) list.get(i);
        if ( StringUtils.isNotEmpty(module) ) {
          getInstallBeforeBuild(module);
        }
      }

    } catch (Exception e) {
      log_println(StringUtils.join(new String[] {"[MeiInstall.getInstallBefore] Exception : " , e.toString() }));
    }

  }

  private static void getInstallAfter() throws Exception {

    try {

      List list = MEI.getList("mei.builder.module");
      int count = list.size();
      
      for (int i = 0; i < count; i++ ) {
        String module = (String) list.get(i);
        if ( StringUtils.isNotEmpty(module) ) {
          getInstallAfterBuild(module);
        }
      }

    } catch (Exception e) {
      log_println(StringUtils.join(new String[] {"[MeiInstall.getInstallAfter] Exception : " , e.toString() }));
    }
  }

  private static void getInstallBeforeBuild(String module) throws Exception {
    String conf = "/com/syaku/modules/" + module + "/info.properties";
    Configuration info = new PropertiesConfiguration(conf);

    List list = info.getList("mei.module.install.classes");
    int count = list.size();
    for (int i = 0; i < count; i++ ) {
      String classes = (String) list.get(i);
      if ( StringUtils.isNotEmpty(classes) ) {
        Class cls =Class.forName(classes);
        Object obj = cls.newInstance();
        Method method = obj.getClass().getDeclaredMethod("getInstallBefore");
        method.invoke(obj);
        log_println(StringUtils.join(new String[] {"[MeiInstall getInstallBeforeBuild]" + module + "/" + classes + " Success" }));
      }
    }
  }

  private static void getInstallAfterBuild(String module) throws Exception {
    String conf = "/com/syaku/modules/" + module + "/info.properties";
    Configuration info = new PropertiesConfiguration(conf);

    List list = info.getList("mei.module.install.classes");
    int count = list.size();
    for (int i = 0; i < count; i++ ) {
      String classes = (String) list.get(i);
      if ( StringUtils.isNotEmpty(classes) ) {

        Class cls =Class.forName(classes);
        Object obj = cls.newInstance();
        Method method = obj.getClass().getDeclaredMethod("getInstallAfter");
        method.invoke(obj);

        log_println(StringUtils.join(new String[] {"[MeiInstall getInstallAfterBuild]" + module + "/" + classes + " Success" }));
      }
    }
  }

  public static void log_start(String title) {

    System.out.print(StringUtils.join(new String[] {
      "\r\n" , title , "\r\n" , 
      "=============================================================================\r\n" , 
      "-----------------------------------------------------------------------------\r\n" , 
      "Copyright (c) MEI (Modularize. Extension. Interaction.)\r\n", 
      "Powered by Seok Kyun. Choi. 최석균.\r\n" , 
      "http://syaku.tistory.com\r\n" , 
      "GNU Lesser General Public License.\r\n" , 
      "http://www.gnu.org/licenses/lgpl.html\r\n\r\n"
    } ));

  }

  public static void log_print(String content) {
    System.out.print(content);
  }

  public static void log_println(String content) {
    System.out.println(content);
  }

  public static void log_end() {
    System.out.print(StringUtils.join(new String[] {
      "\r\n\r\n" , 
      "-----------------------------------------------------------------------------\r\n" , 
      "=============================================================================\r\n"
    } ));
  }


}
