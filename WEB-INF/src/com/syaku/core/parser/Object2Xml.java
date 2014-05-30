/*
 * Object2Xml.java 2010.01.05
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.parser;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

public class Object2Xml {
  private static Logger log = Logger.getLogger(Object2Xml.class);

  private static SAXBuilder builder = new SAXBuilder();
  private static XMLOutputter outputter = new XMLOutputter();
  private static String root_node = "data";
  private static String child_node = "item";

  // 해쉬맵을 이용하여 문서 생성
  public static Document make(Map hm) {
    
    Element data = new Element(root_node);

    Element element = new Element(child_node);
    Set set = hm.keySet();
    Object []items = set.toArray();
    for(int i = 0; i < items.length; i++) {
      String name = (String) items[i]; 
      String value = (String) hm.get(name);

      addElement(element,name,value);
    }
    hm.clear();

    data.addContent(element);

    Document document = new Document(data);

    return document;
  }

  // 배열
  public static Document make(String[] nodeName,String[][] nodeValue) {
    Element data = new Element(root_node);

    for (int i = 0; i < nodeValue.length; i++ ) {
      Element element = new Element(child_node);

      for (int ii = 0; ii < nodeName.length; ii++) {
        String name = nodeName[ii];
        String value = nodeValue[i][ii];

        addElement(element,name,value);
      }

      data.addContent(element);
    }

    Document document = new Document(data);
    return document;
  }

  public static Document make(List list) {
    return make(list,true);
  }
  public static Document make(List list,boolean is_map) {
    if (is_map) {
      return ListMapForMake(list);
    } else {
      return ListForMake(list);
    }
  }

  public static Document ListForMake(List list) {
    Element data = new Element(root_node);

    for (int i = 0; i < list.size(); i++ ) {
      String value = (String) list.get(i);

      if ( StringUtils.isEmpty(value) ) {
        continue;
      }

      Element element = new Element(child_node);
      element.setText(value);
      data.addContent(element);
    }

    Document document = new Document(data);
    return document;
  }

  public static Document ListMapForMake(List list) {
    Element data = new Element(root_node);

    for (int i = 0; i < list.size(); i++ ) {
      Element element = new Element(child_node);

      Map<String,String> mapObj = (Map) list.get(i);

      Set set = mapObj.keySet();
      Object []items = set.toArray();

      for(int ii = 0; ii < items.length; ii++) {
        String name = (String) items[ii]; 
        String value = (String) mapObj.get(name);

        addElement(element,name,value);
      }

      data.addContent(element);
    }

    Document document = new Document(data);
    return document;
  }

  // iBATIS
  public static Document iBATISForMake(Object result) {
    Element data = new Element(root_node);
    Element element = new Element(child_node);

    String xml = (String)result;

    try {
      Document doc = builder.build(new StringReader(xml));

      Element root = doc.getRootElement();
      List child = root.getChildren();
      for (Iterator iter = child.iterator();iter.hasNext();) {
        Element node = (Element) iter.next();

        String name = (String) node.getName();
        String value = (String) node.getText();
        addElement(element,name,value);
      }
      data.addContent(element);

    } catch (Exception e) {
      log.error("[#MEI Object2Xml.iBATISForMake] " + e.toString());
    }

    Document document = new Document(data);

    return document;
  }


  public static Document iBATISForMake(List result) {
    Element data = new Element(root_node);

    Document document;

    try {

      for (int i = 0; i < result.size(); i++ ) {
        Element element = new Element(child_node);
        String xml = (String)result.get(i);
        document = builder.build(new StringReader(xml));

        Element root = document.getRootElement();
        List child = root.getChildren();
        for (Iterator iter = child.iterator();iter.hasNext();) {
          Element node = (Element) iter.next();

          String name = (String) node.getName();
          String value = (String) node.getText();

          addElement(element,name,value);
        }

        data.addContent(element);
      }

    } catch (Exception e) {
      log.error("[#MEI Object2Xml.iBATISForMake] " + e.toString());
    }

    document = new Document(data);

    return document;
  }

  // 엘리먼트 생성
  public static Element addElement(Element parent, String name, String value) {
    Element element = new Element(name);
    element.setText(value);
    parent.addContent(element);
    return parent;
  }

  // 애트리뷰트 생성
  public static void addAttribute(Element element, String name, String value){
    Attribute attribute = new Attribute(name,value);
    element.setAttribute(attribute);
  }

  public static String toString(Map map) {
    Document document = (Document) make(map);
    return toString(document);
  }
  public static String toString(Document document) {
    String xml = "";
    if (document != null) {
      xml = outputter.outputString(document);
    }
    
    return xml;
  }

  public static String toString(List list) {
    Document document = (Document) make(list);

    String xml = "";
    if (document != null) {
      xml = outputter.outputString(document);
    }
    
    return xml;
  }

  public static String toSqlString(List list) {
    Document document = (Document) iBATISForMake(list);

    String xml = "";
    if (document != null) {
      xml = outputter.outputString(document);
    }
    
    return xml;
  }

  public static InputStream toInputStream(Map map,String charset) {
    String ret = null;
    try {
      ret = toString(map);
    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
    }
    return toInputStream(ret,charset);
  }
  public static InputStream toInputStream(Document document,String charset) {
    String ret = null;
    try {
      ret = toString(document);
    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
    }
    return toInputStream(ret,charset);
  }
  public static InputStream toInputStream(String string,String charset) {
    InputStream inputStream = null;

    try {
      inputStream = new ByteArrayInputStream(string.getBytes(charset));
    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
    }

    return inputStream;
  }


}