/*
 * MyBatis.java 2008.12.17
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.common;

import java.io.Reader;
import java.nio.charset.*;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class MyBatis {
  private static Logger log = Logger.getLogger( MyBatis.class );

  private static final SqlMapClient sqlMap;
  private static String resource = "mybatis.xml";

  public MyBatis() { }

  public MyBatis(String resource) { this.resource = resource; }

  static {
    log.debug("[## MEI MyBatis] Called : Xml = " + resource);

    try {
      Reader reader = Resources.getResourceAsReader (resource);
      Charset set = Charset.forName("UTF-8");
      Resources.setCharset(set);

      sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
      log.debug("[## MEI MyBatis getInstance] Called");
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException ("Error initializing SqlConfig class. Cause: " + e);
    }
  }

  public static SqlMapClient getInstance() {
    return sqlMap;
  }

}