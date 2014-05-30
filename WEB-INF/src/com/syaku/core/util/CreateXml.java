/*
 * CreateXml.java 2011.07.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

public class CreateXml {
  private Logger log = Logger.getLogger( this.getClass() );

  private Map<String,List<Map<String,String>>> mapRoot;
  private List<Map<String,String>> listRoot;

  private int depth = 0;
  private String pkey = ""; // depth 를 구하기 위한 부모키
  private String category_orls = ""; // 분류 코드모두

  public CreateXml(List<Map<String,String>> list) {
    this.listRoot = list;
  }

  private void mapBulider() {
    int count = listRoot.size();

    Map<String,List<Map<String,String>>> mapPut = new HashMap<String,List<Map<String,String>>>();

    for (int i = 0; i < count; i++) {
      
      Map<String,String> mapGet = (Map<String,String>)listRoot.get(i);

      String map_name = "";

      Map<String,String> mapItemPut = new HashMap<String,String>();
      Iterator iterator = mapGet.keySet().iterator();
      while(iterator.hasNext()){
        String key = (String) iterator.next();
        String value = mapGet.get(key);
        
        if (StringUtils.equals(key,"parent_orl")) { map_name = "m" + value; }
        mapItemPut.put(key,value);
      }

      List<Map<String,String>> listPut = new ArrayList();
      if (mapPut.containsKey(map_name)) {
        listPut = (List<Map<String,String>>) mapPut.get(map_name);
        int sz = listPut.size();
        listPut.add(sz,mapItemPut);
        mapPut.put(map_name,listPut);
      } else {
        listPut.add(0,mapItemPut);
        mapPut.put(map_name,listPut);
      }

    }

    mapRoot = mapPut;
  }

  public Element xmlBulider() {
    mapBulider();

    Element root = new Element("data");
    Element item = null;

    if (MapUtils.isEmpty( mapRoot )) {
      return root;
    }

    List<Map<String,String>> list = (List<Map<String,String>>) mapRoot.get("m0"); // 대분류
    int total = list.size();

    for (int i = 0; i < total; i++ ) {
      Map<String,String> mapGet = (Map<String,String>)list.get(i);

      String map_name = "";
      String category_orl = "";

      Map<String,String> mapItemPut = new HashMap<String,String>();
      Iterator iterator = mapGet.keySet().iterator();
      while(iterator.hasNext()){
        String key = (String) iterator.next();
        String value = mapGet.get(key);
        if (StringUtils.equals(key,"category_orl")) { category_orl = value; map_name = "m" + value; }
        if (StringUtils.equals(key,"menu_item_orl")) { category_orl = value; map_name = "m" + value; }
        mapItemPut.put(key,value);
      }

      pkey = "";
      depth = 0;
      category_orls = category_orl + ",";

      mapItemPut.put("depth","" + depth);
      mapItemPut.put("category_orls",category_orls);
      mapItemPut.put("menu_item_orls",category_orls);

      item = createElement("item",mapItemPut);
      if (mapRoot.containsKey(map_name)) {

        pkey = category_orl;
        item = xmlChildBulider(item,"item",(List) mapRoot.get(map_name));
      }

      root.addContent(item);
    }

    return root;
  }

  private Element xmlChildBulider(Element parent, String item,List<Map<String,String>> list) {
    int next = 0;
    int p_depth = depth;
    String p_category_orls = category_orls;

    int total = list.size();
    Element item_child = null;
    for (int i = 0; i < total; i++ ) {
      Map<String,String> mapGet = (Map<String,String>)list.get(i);

      String map_name = "";
      String category_orl = "";
      String parent_orl = "";

      Map<String,String> mapItemPut = new HashMap<String,String>();
      Iterator iterator = mapGet.keySet().iterator();
      while(iterator.hasNext()){
        String key = (String) iterator.next();
        String value = mapGet.get(key);

        if (StringUtils.equals(key,"category_orl")) { category_orl = value; map_name = "m" + value; }
        if (StringUtils.equals(key,"menu_item_orl")) { category_orl = value; map_name = "m" + value; }
        if (StringUtils.equals(key,"parent_orl")) { parent_orl = value; }
        mapItemPut.put(key,value);
      }

      if (pkey.equals(parent_orl) && next == 0) { // 자식 처음 노드
        depth++;
        p_depth = depth;
        
        category_orls += category_orl + ",";

      } else if (p_depth != depth && next > 0) { // 자식이지만 처음 노드가 아닌 경우
        depth = p_depth;
        category_orls = p_category_orls + category_orl + ",";

      } else { // 자식의 반복되는 노드
        category_orls = p_category_orls + category_orl + ",";
      }

      mapItemPut.put("depth","" + depth);
      mapItemPut.put("category_orls",category_orls);
      mapItemPut.put("menu_item_orls",category_orls);

      item_child = createElement("item",mapItemPut);
      if (mapRoot.containsKey(map_name)) {
        pkey = category_orl;
        item_child = xmlChildBulider(item_child,"item",(List) mapRoot.get(map_name));
      }

      parent.addContent(item_child);
      next++;
    }

    return parent;

  }

  public Element xmlListBulider() {
    mapBulider();

    Element root = new Element("data");
    Element item = null;

    List<Map<String,String>> list = (List<Map<String,String>>) mapRoot.get("m0"); // 대분류
    int total = list.size();

    for (int i = 0; i < total; i++ ) {
      Map<String,String> mapGet = (Map<String,String>)list.get(i);

      String map_name = "";
      String category_orl = "";

      Map<String,String> mapItemPut = new HashMap<String,String>();
      Iterator iterator = mapGet.keySet().iterator();
      while(iterator.hasNext()){
        String key = (String) iterator.next();
        String value = mapGet.get(key);
        if (StringUtils.equals(key,"category_orl")) { category_orl = value; map_name = "m" + value; }
        if (StringUtils.equals(key,"menu_item_orl")) { category_orl = value; map_name = "m" + value; }
        mapItemPut.put(key,value);
      }

      pkey = "";
      depth = 0;
      category_orls = category_orl + ",";

      mapItemPut.put("depth","" + depth);
      mapItemPut.put("category_orls",category_orls);
      mapItemPut.put("menu_item_orls",category_orls);

      root.addContent(createElement("item",mapItemPut));

      if (mapRoot.containsKey(map_name)) {

        pkey = category_orl;
        item = xmlListChildBulider(root,"item",(List) mapRoot.get(map_name));
      }

    }

    return root;
  }

  private Element xmlListChildBulider(Element parent, String item,List<Map<String,String>> list) {
    int next = 0;
    int p_depth = depth;
    String p_category_orls = category_orls;

    int total = list.size();
    Element item_child = null;
    for (int i = 0; i < total; i++ ) {
      Map<String,String> mapGet = (Map<String,String>)list.get(i);

      String map_name = "";
      String category_orl = "";
      String parent_orl = "";

      Map<String,String> mapItemPut = new HashMap<String,String>();
      Iterator iterator = mapGet.keySet().iterator();
      while(iterator.hasNext()){
        String key = (String) iterator.next();
        String value = mapGet.get(key);

        if (StringUtils.equals(key,"category_orl")) { category_orl = value; map_name = "m" + value; }
        if (StringUtils.equals(key,"menu_item_orl")) { category_orl = value; map_name = "m" + value; }
        if (StringUtils.equals(key,"parent_orl")) { parent_orl = value; }
        mapItemPut.put(key,value);
      }

      if (pkey.equals(parent_orl) && next == 0) { // 자식 처음 노드
        depth++;
        p_depth = depth;
        
        category_orls += category_orl + ",";

      } else if (p_depth != depth && next > 0) { // 자식이지만 처음 노드가 아닌 경우
        depth = p_depth;
        category_orls = p_category_orls + category_orl + ",";

      } else { // 자식의 반복되는 노드
        category_orls = p_category_orls + category_orl + ",";
      }

      mapItemPut.put("depth","" + depth);
      mapItemPut.put("category_orls",category_orls);
      mapItemPut.put("menu_item_orls",category_orls);

      parent.addContent(createElement("item",mapItemPut));

      if (mapRoot.containsKey(map_name)) {
        pkey = category_orl;
        xmlListChildBulider(parent,"item",(List) mapRoot.get(map_name));
      }

      next++;
    }

    return parent;

  }


  public void file(String name,String name2) throws Exception {
    xmlFile(name);
    xmlListFile(name2);
  }

  public void xmlFile(String name) throws Exception {
    Element root = xmlBulider();
    Document document = new Document(root);
    createXmlFile(name,document);
  }
  public void xmlFile(String name,Document document) throws Exception { createXmlFile(name,document); }

  public void xmlListFile(String name) throws Exception {
    Element root = xmlListBulider();
    Document document = new Document(root);
    createXmlFile(name,document);
  }
  public void xmlListFile(String name,Document document) throws Exception { createXmlFile(name,document); }

  private void createXmlFile(String name,Document document) throws Exception {
    IoUtils.writeXml(name,document);
  }

  private Element createElement(String item,Map<String,String> mapItem) {
    Element element = new Element(item);
    Iterator iterator = mapItem.keySet().iterator();
    while(iterator.hasNext()){
      String key = (String) iterator.next();
      String value = mapItem.get(key);
      addAttribute(element,key,value);
    }
    return element;
  }

  // 엘리먼트 생성
  private Element addElement(Element parent, String item, String value) {
    Element element = new Element(item);
    element.setText(value);
    parent.addContent(element);
    return parent;
  }

  // 애트리뷰트 생성
  private void addAttribute(Element element, String name, String value){
    Attribute attribute = new Attribute(name,value);
    element.setAttribute(attribute);
  }


}
