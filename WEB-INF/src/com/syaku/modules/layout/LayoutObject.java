/*
 * LayoutObject.java 2012-03-23
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.layout;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;

public class LayoutObject extends LayoutAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/layout/info.properties");

  // 레이아웃 설정 정보 호출
  public Map prop2map(String source) {
    log.debug(source);
    XMLConfiguration config = ConfigUtils.getXml(source);
    Map mapData = new HashMap();
    String name = config.getString("name");
    String path = "/layouts/" + name;
    String login_ftl = config.getString("login_ftl");
    mapData.put("name",name);
    mapData.put("path", path);
    mapData.put("login_layout_hide",config.getString("login_layout_hide","false"));
    mapData.put( "login_ftl" , login_ftl );
    mapData.put("description",config.getString("description"));
    return mapData;
  }

  // layout 경로
  public List getLayoutDirList(String layout) throws Exception {
    List list = new ArrayList();
    if (StringUtils.isEmpty(layout)) { return list; }

    File dir = new File(layout);
    if (!dir.exists()) { return list; }

    File dir_arr[] = dir.listFiles();
    int count = dir_arr.length;
    int o = 0;

    for (int i = 0; i < count; i++) {
      File info = dir_arr[i];
      if (dir_arr[i].isDirectory()) {
        String name = info.getName();
        if (name.equals(".svn") || StringUtils.isEmpty(name) || name.equals("default")) { // SVN 폴더 제외
          continue;
        }

        list.add(o,name);
        o++;
      }
    }

    return list;

  }

}