/*
 * MenuModel.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.menu;

import java.util.*;

import org.apache.log4j.Logger;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import com.syaku.core.*;
import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class MenuModel extends ActionMenu {
  private Logger log = Logger.getLogger( this.getClass() );

  public String getMenuItemXml() throws Exception {
    String menu_orl = param.value("menu_orl");

    Map map = new HashMap();
    map.put("menu_orl",menu_orl);
    Document document = (Document) daoMenu.getMenuCacheXml(map);

    inputStream = Object2Xml.toInputStream(document,"UTF-8");
    return "xml";
  }

  public String getMenuItemCache() throws Exception {
    String menu_orl = param.value("menu_orl");

    Map map = new HashMap();
    map.put("menu_orl",menu_orl);
    daoMenu.getMenuCacheFile(map); // 캐쉬파일 생성

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

}