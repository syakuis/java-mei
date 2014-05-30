/*
 * AddonObject.java 2011.06.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.addon;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;

public class AddonObject extends AddonAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration meiConfig = ConfigUtils.getProperties("mei.properties"); // mei 정보
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/addon/info.properties");

  public final String MEI_PATH_RELATIVE = meiConfig.getString("mei.path.relative");
  public final String MEI_PATH_ABSOLUTE_RELATIVE = meiConfig.getString("mei.path.absolute_relative");
  public final String MEI_PATH_ADDONS = meiConfig.getString("mei.path.addons");
  public final String MEI_PATH_RESOURCE = meiConfig.getString("mei.path.resource");

  public void propReload() throws Exception {
    PropertiesConfiguration addonConfig = new PropertiesConfiguration(MEI_PATH_RESOURCE + "/mei.config.properties");

    List listProp = addonConfig.getList("mei.addons.load");
    int countProp = listProp.size();

    for (int p = 0; p < countProp; p++ ) {
      String name = (String) listProp.get(p);
      addonConfig.clearProperty("mei.addons.load");
    }
    addonConfig.save();

    Map mapSch = new HashMap();
    mapSch.put("is_use","Y");
    mapSch.put("listorder","first_load");

    List list = sqlAddonList(mapSch);
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      AddonBean addonbean= (AddonBean) list.get(i);
      String addon = addonbean.getAddon();
      String addon_path = addonbean.getAddon_path();
      addon_path = addon_path + "/addon.html";
      addonConfig.addProperty("mei.addons.load",addon_path);
    }

    addonConfig.save();
  }

  public Map prop2map(String source) {
    /*
    Configuration addonConfig = ConfigUtils.getProperties(source);

    Map mapData = new HashMap();
    mapData.put("version" , addonConfig.getString("mei.addon.version") );
    mapData.put("name",addonConfig.getString("mei.addon.name"));
    mapData.put("type",addonConfig.getString("mei.addon.type"));
    mapData.put("path",addonConfig.getString("mei.addon.path.relative"));
    mapData.put("title",addonConfig.getString("mei.addon.title"));
    mapData.put("site",addonConfig.getString("mei.addon.developer.site"));
    */

    XMLConfiguration addonConfig = ConfigUtils.getXml(source);
    Map mapData = new HashMap();
    mapData.put("version" , addonConfig.getString("version") );
    mapData.put("name",addonConfig.getString("name"));
    mapData.put("type",addonConfig.getString("type"));
    mapData.put("path", "/addons/" + addonConfig.getString("name"));
    mapData.put("title",addonConfig.getString("title"));
    mapData.put("description",addonConfig.getString("description"));
    mapData.put("site",addonConfig.getString("developer.site"));
    return mapData;
  }

  public List getAddonLoader() {
    List listAddonLoad = new ArrayList();

    // 애드온 로드
    /*
    mei.addon.version = 버전
    mei.addon.name = 애드온명
    mei.addon.type = 애드온 종류
    mei.addon.path.relative = 애드온 경로
    */
    String addons_dir = MEI_PATH_ABSOLUTE_RELATIVE + MEI_PATH_ADDONS;

    File dir = new File(addons_dir);
    File dir_arr[] = dir.listFiles();
    int count = dir_arr.length;
    for (int i = 0; i < count; i++) {
      File f = dir_arr[i];

      if (dir_arr[i].isDirectory()) {
        String name = f.getName();

        if ( !StringUtils.equals(name,".svn") && StringUtils.isNotEmpty(name) ) { // SVN 폴더 제외
          String f_path = f.getAbsolutePath();
          String addon_info = f_path + "/addon.xml";

          File check = new File(addon_info);
          if (check.exists()) {
            Map mapData = prop2map(addons_dir + "/" + name + "/addon.xml");
            listAddonLoad.add(mapData);
          }

        }

      }

    }

    return listAddonLoad;
  }

}