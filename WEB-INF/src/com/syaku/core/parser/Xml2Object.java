/*
 * Xml2Object.java 2010.01.05
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.parser;

import org.apache.log4j.Logger;

import java.util.*;
import java.io.*;

import org.jdom.*;
import org.jdom.xpath.*;
import org.jdom.input.*;
import org.jdom.output.*;

public class Xml2Object {
  private Logger log = Logger.getLogger( this.getClass() );
  private SAXBuilder builder;
  private Document document;

  public void builder(String xml) {
    try {
      builder = new SAXBuilder();
      document = builder.build( new StringReader(xml) );
    } catch (Exception e) {
      log.error("[#MEI Xml2Object.builder] " + e.toString());
    }
  }

  
  public String selectNodeOnce(String xpath) {
    XPath x;
    Element node;
    String node_text = null;

    try {
      x = XPath.newInstance(xpath);
      node = (Element) x.selectSingleNode(document);
      node_text = node.getText();
    } catch (Exception e) {
      log.error("[#MEI Xml2Object.selectNodeOnce] " + e.toString());
    }

    return node_text;
  }

  public List selectNodeList(String xpath) {

    List listRet = new ArrayList();

    try {

      XPath x = XPath.newInstance(xpath);
      List parents = x.selectNodes(document);
      int parents_cnt = parents.size();
      for (int p = 0; p < parents_cnt; p++ ) {
        Element parent = (Element) parents.get(p);

        List childs = parent.getChildren();
        int childs_cnt = childs.size();
        if (childs_cnt > 0) {
          
          Map map = new HashMap();

          for (int c = 0; c < childs_cnt; c++ ) {
            Element child = (Element) childs.get(c);
            map.put(child.getName(),child.getText());
          }

          listRet.add(map);

        }

      }

    } catch (Exception e) {
      log.error("[#MEI Xml2Object.selectNodeList] " + e.toString());
    }

    return listRet;
  }

}
