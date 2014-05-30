/*
 * MeiBuilder.java 2012.02.03
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

import org.apache.commons.configuration.*;
import org.apache.commons.lang.*;
import org.apache.commons.io.*;

import com.syaku.core.util.*;
import com.syaku.core.common.*;

public class MeiBuilder {
  private static Configuration MEI;
  private static PropertiesConfiguration COLLECTS,MODULES;
  private static String path_root,path_src,path_classes,mode;

  public static void main(String[] args) throws Exception {
    path_root = args[0];
    path_src = args[1];
    path_classes = args[2];

    building();
  }

  public static void building() {
    log_start("MEI Bulider");

    try {
      MEI = new PropertiesConfiguration("mei.properties");

      // 폴더생성
      getCreateFolderList(MEI.getList("mei.builder.create.folder"));

      // 모듈정보 로드
      String path_modules = path_classes + "/com/syaku/modules";
      List listMInfo = getFileList(path_modules,"info.properties");

      // 스트럿츠 생성
      MODULES = new PropertiesConfiguration();
      MODULES.addProperty("mei.struts.resource", MEI.getString("mei.struts.resource") );

      // mei configure collection
      String path_resource = path_classes + "/com/syaku/resource";
      File folder_resource = new File(path_resource);
      if(!folder_resource.exists()) {
          throw new Exception("폴더가 존재하지 않습니다.");
      }

      String collects_file = path_classes + "/mei.collection.properties";
      IoUtils.createFile(collects_file);

      String config_file = path_resource + "/mei.config.properties";
      IoUtils.createFile(config_file);

      COLLECTS = new PropertiesConfiguration("mei.collection.properties");
      COLLECTS.clear();
      COLLECTS.setAutoSave(true);
      // mei configure collection

      // 모듈 빌트인
      getModuleBuiltIn(listMInfo);
      
      String classes = MEI.getString("mei.path.absolute") + "/WEB-INF/classes";
      strutsBuilding(classes + "/" + MEI.getString("mei.builder.struts"), MODULES.getList("mei.struts.resource") );
      mybatisBuilding(classes + "/" + MEI.getString("mei.builder.mybatis"),(List) MODULES.getList("mei.mybatis.resource"));

    } catch (Exception e) {
      log_println(e.toString());
    }

    log_end();
  }

  private static void getModuleBuiltIn(List list) {

    try {
      int count = list.size();
      for (int i = 0; i < count; i++) {
        Map map = (Map) list.get(i);

        String name = "/com/syaku/modules/" + map.get("name") + "/info.properties";
        Configuration info = new PropertiesConfiguration(name);

        /* configure collection */
        addPropertyList("mei.globals.import.js",info,COLLECTS);
        addPropertyList("mei.globals.import.css",info,COLLECTS);
        addPropertyList("mei.onload.classes",info,COLLECTS);
        addPropertyList("mei.module.name",info,COLLECTS);
        addPropertyList("mei.module.install.classes",info,COLLECTS);
        /* configure collection */
        
        /* 스트럿츠 */
        List listStruts = info.getList("mei.struts.resource");
        int count_s = listStruts.size();
        for (int l = 0; l < count_s; l++ ) {
          String value = (String) listStruts.get(l);
          MODULES.addProperty("mei.struts.resource", value);
        }
        /* 스트럿츠 */

        /* myBatis */
        List listMy = info.getList("mei.mybatis.resource");
        int count_my = listMy.size();
        for (int m = 0; m < count_my; m++ ) {
          String value = (String) listMy.get(m);
          MODULES.addProperty("mei.mybatis.resource", value);
        }
        /* myBatis */

      }

    } catch (Exception e) {
      log_println(StringUtils.join(new String[] {"[Module Built-in@error] Exception : " , e.toString() }));
    }

  }

  private static void getCreateFolderList(List list) {
    try {
      for (int i = 0; i < list.size(); i++ ) {
        String name = (String) list.get(i);
        getCreateFolder(name);
      }
    } catch (Exception e) {  }
  }


  public static void getCreateFolder(String name) {
    try {
      File dir = new File(name);
      if(!dir.exists()) { 
        dir.mkdirs();
        log_println(StringUtils.join(new String[] {"[Create Directory@success] " , name }));
      } else {
        log_println(StringUtils.join(new String[] {"[Create Directory@exist] " , name }));
      }
    } catch (Exception e) {
      log_println(StringUtils.join(new String[] {"[Create Directory@error] Exception " , e.toString() }));
    }
  }


  private static List getFileList(String search_dir,String search_file) {
    List list = new ArrayList();

    try {

      File dir = new File(search_dir);
      File dir_arr[] = dir.listFiles();
      int count = 0;

      for (int i = 0; i < dir_arr.length; i++) {
        File info = dir_arr[i];

        if (dir_arr[i].isDirectory()) {
          String name = info.getName();

          if (!name.equals(".svn") && !StringUtils.isEmpty(name)) { // SVN 폴더 제외
            String info_path = info.getAbsolutePath();
            String info_search_file = info_path + "/" + search_file;

            File check = new File(info_search_file);
            if (check.exists()) {

              Map map = new HashMap();
              map.put("path",info_path);
              map.put("file",info_search_file);
              map.put("name",name);
              list.add(count,map);

              log_println(StringUtils.join(new String[] {"[Info Propertis File@success] " , info_search_file }));

              count++;
            }

          }
        }

      }

    } catch (Exception e) {
      log_println(StringUtils.join(new String[] {"[Info Propertis File@error] Exception : " , e.toString() }));
    }

    return list;
  }

  // struts builder
  public static void strutsBuilding(String name,List list) {

    try {
      XMLConfiguration config = new XMLConfiguration();
      config.setPublicID("-//Apache Software Foundation//DTD Struts Configuration 2.0//EN");
      config.setSystemID("http://struts.apache.org/dtds/struts-2.0.dtd");
      config.setRootElementName("struts");

      int count = list.size();
      for (int i = 0; i < count; i++) {
        String value = (String) list.get(i);
        config.addProperty("include(-1)[@file]", value);
        log_println(StringUtils.join(new String[] {"[strutsBuilding Action@add] " , value }));
      }

      config.setFileName(name);
      config.save();
    } catch (Exception e) {
      log_println( StringUtils.join(new String[] {"[strutsBuilding Action@error] Exception : " , e.toString() }));
    }

  }

  public static void mybatisBuilding(String name,List list) {

    try {
      XMLConfiguration config = new XMLConfiguration();
      config.setFileName(name);
      config.load();
      int count = list.size();
      for (int i = 0; i < count; i++) {
        String value = (String) list.get(i);
        config.addProperty("sqlMap(-1)[@resource]", value);
        log_println(StringUtils.join(new String[] {"[mybatisBuilding Mapper@add] " , value }));
      }
      config.save();
    } catch (Exception e) {
      log_println( StringUtils.join(new String[] {"[myBatisBuilding Mapper@error] Exception : " , e.toString() }));
    }

  }

  private static void addProperty(String name,Configuration config,PropertiesConfiguration properties) {
     properties.addProperty(name , config.getString(name));
  }

  private static void addPropertyList(String name,Configuration config,PropertiesConfiguration properties) {
    List list = config.getList(name);
    for (int i = 0; i <list.size(); i++) {
      String item = (String) list.get(i);
      properties.addProperty(name , item);
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
