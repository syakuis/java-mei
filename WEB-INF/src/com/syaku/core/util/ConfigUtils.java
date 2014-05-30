/*
 * ConfigUtils.java 2010.01.01
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
import org.apache.commons.configuration.*;
import org.apache.commons.lang.*;

public class ConfigUtils {
  private static Logger log = Logger.getLogger(ConfigUtils.class);

  public static Configuration getProperties(String resource) {
    Configuration config = null;
    try {
      PropertiesConfiguration.setDefaultListDelimiter((char)0);
      config = new PropertiesConfiguration(resource);
    } catch (ConfigurationException e) {
      log.error("[#MEI ConfigUtils.getProperties] " + e.toString());
    }

    return config;

  }

  public static XMLConfiguration getXml(String resource) {
    XMLConfiguration config = null;
    try {
      XMLConfiguration.setDefaultListDelimiter((char)0);
      config = new XMLConfiguration(resource);
    } catch (ConfigurationException e) {
      log.error("[#MEI ConfigUtils.getXml] " + e.toString());
    }

    return config;

  }

  // properties to map
  public static Map<String,String> prop2map(String resource,String key) {
    Configuration data = getProperties(resource);
    return prop2map(data,key);
  }
  public static Map<String,String> prop2map(Configuration resource,String key) {
    
    if (resource == null) { return null; }
    Map<String,String> retMap = new HashMap<String,String>();

    Iterator list = resource.getKeys(key);
    while(list.hasNext()){
      String name = (String) list.next();
      String value = resource.getString(name);
      name = StringUtils.replace(name,key + ".","");
      retMap.put( name , value );
    }

    return retMap;
  }

  public static Map<String,String> getModuleInfo(String resource) {
    Map<String,String> mapSch = new HashMap<String,String>();
    mapSch.put("prop_name","mei.module.name");
    mapSch.put("prop_title","mei.module.title");
    mapSch.put("prop_single","mei.module.single");
    mapSch.put("prop_path","mei.module.path.relative");
    mapSch.put("prop_path_skins","mei.module.path.skins");
    mapSch.put("prop_classes_package","mei.module.package.relative");
    mapSch.put("prop_default_action","mei.struts.default.action");
    mapSch.put("prop_install_classes","mei.module.install.classes");
    mapSch.put("prop_install","mei.module.install");
    mapSch.put("prop_rdate","mei.module.rdate");

    return getModuleInfo(resource,mapSch);
  }

  public static Map<String,String> getModuleInfo(String resource,Map<String,String> map) {
    Configuration data = getProperties(resource);

    Iterator list = data.getKeys("mei");
    while(list.hasNext()){
      String name = (String) list.next();
    }

    if (data == null) { return null; }
    Map<String,String> retMap = new HashMap<String,String>();

    Iterator iterator = map.keySet().iterator();
    while(iterator.hasNext()){
      String name = (String) iterator.next();
      String properties = (String) map.get(name);
      retMap.put( name , data.getString(properties) );
    }

    return retMap;
  }


}