/*
 * ModuleProperties.java 2011.10.27
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.module;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;
import org.apache.commons.collections.*; 

import com.syaku.core.util.*;

public class ModuleProperties {
  private Logger log = Logger.getLogger( this.getClass() );
  private final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/module/info.properties");

  private final Configuration meiConfig = ConfigUtils.getProperties("mei.properties");
  private final Configuration ccolletConfig = ConfigUtils.getProperties("mei.collection.properties");

  private final String MEI_PATH_ABSOLUTE = meiConfig.getString("mei.path.absolute");
  private final String MEI_PATH_ABSOLUTE_RELATIVE = meiConfig.getString("mei.path.absolute_relative");
  private final String MEI_PATH_RESOURCE = meiConfig.getString("mei.path.resource");

  private ModuleObject daoModule = new ModuleObject();

  private PropertiesConfiguration setProperties(String mid) throws Exception {
    PropertiesConfiguration info = null;

    String MEI_PATH_CLASSES = MEI_PATH_ABSOLUTE_RELATIVE + "/WEB-INF/classes/";
    String properties = MEI_PATH_RESOURCE + "/mei.module." + mid + ".properties";

    File file = new File(MEI_PATH_CLASSES + "/" + properties);

    if( !file.exists() ) { 
      file.createNewFile();
    }

    file = new File(MEI_PATH_CLASSES + "/" + properties);
    if( file.exists() ) { 
      info = new PropertiesConfiguration("com/syaku/resource/mei.module." + mid + ".properties");
    }

    return info;
  }

  public void delete(String mid) throws Exception {
    String MEI_PATH_CLASSES = MEI_PATH_ABSOLUTE_RELATIVE + "/WEB-INF/classes/";
    String properties = MEI_PATH_RESOURCE + "/mei.module." + mid + ".properties";
    File file = new File(MEI_PATH_CLASSES + "/" + properties);

    if( file.exists() ) { file.delete(); }
  }

  public void config2prop(String module_orl) throws Exception {

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    Map obj = daoModule.objectOne(mapSch);

    if ( MapUtils.isNotEmpty(obj) ) {
      String module = (String) obj.get("module");
      String mid = (String) obj.get("mid");

      PropertiesConfiguration info = setProperties(mid);
      info.clear();

      Iterator iterator = obj.keySet().iterator();
      while(iterator.hasNext()) {
        String name = (String) iterator.next();
        String value = (String) obj.get(name);

        if ( StringUtils.isNotEmpty( name ) ) {
          name = "module." + name;
          info.addProperty( name , value );
        }

      }

      info.save();
    }

  }

  public void listConfig2prop(String module) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("module",module);

    List list = daoModule.list(mapSch);
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      Map obj= (Map) list.get(i);

      if ( MapUtils.isNotEmpty(obj) ) {
        String mid = (String) obj.get("mid");
        PropertiesConfiguration info = setProperties(mid);
        info.clear();

        Iterator iterator = obj.keySet().iterator();
        while(iterator.hasNext()) {
          String name = (String) iterator.next();
          String value = (String) obj.get(name);

          if ( StringUtils.isNotEmpty( name ) ) {
            name = "module." + name;
            info.addProperty( name , value );
          }
        }

        info.save();
      }

    }
  }



}