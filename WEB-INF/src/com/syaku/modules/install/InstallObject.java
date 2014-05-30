/*
 * InstallObject.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.install;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;
import org.apache.commons.collections.*;

import com.syaku.core.util.*;
import com.syaku.modules.module.*;

public class InstallObject extends InstallAccess {
  private Logger log = Logger.getLogger( this.getClass() );

  public final Configuration meiConfig = ConfigUtils.getProperties("mei.properties");
  public final Configuration ccolletConfig = ConfigUtils.getProperties("mei.collection.properties");
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/install/info.properties");
  public final Configuration installConfig = ConfigUtils.getProperties("com/syaku/resource/mei.config.properties");

  public final String MEI_PATH_ABSOLUTE = meiConfig.getString("mei.path.absolute");
  public final String MEI_PATH_ABSOLUTE_RELATIVE = meiConfig.getString("mei.path.absolute_relative");
  public final String MEI_PATH_RESOURCE_CONFIG = meiConfig.getString("mei.path.rec_config");

  public List getModuleList() {
    List listRet = new ArrayList();

    List list = ccolletConfig.getList("mei.module.name");
    int count = list.size();
    for (int i = 0; i < count; i++ ) {
      String module = (String) list.get(i);
      String info = "com/syaku/modules/" + module + "/info.properties";
      Map mapRet = (HashMap) ConfigUtils.getModuleInfo(info);

      if (MapUtils.isNotEmpty(mapRet)) {
        boolean install = false;
        String rdate = null;
        if (installConfig != null) {
          install = installConfig.getBoolean("mei.install." + module,false);
          rdate = installConfig.getString("mei.install." + module + ".rdate","");
        }

        mapRet.put( "prop_install" , install );
        mapRet.put( "prop_rdate" , rdate );

        listRet.add(mapRet);
      }

    }

    return listRet;
  }
  public PropertiesConfiguration setModuleProperties() throws Exception {
    PropertiesConfiguration RESOURCE = new PropertiesConfiguration(MEI_PATH_RESOURCE_CONFIG);
    return RESOURCE;
  }

/*
  public PropertiesConfiguration setModuleProperties(String module) throws Exception {
    String MEI_PATH_CLASSES = MEI_PATH_ABSOLUTE_RELATIVE + "/WEB-INF/classes/";
    String properties = MEI_PATH_RESOURCE + "/mei.module." + module + ".properties";
    IoUtils.createFile(MEI_PATH_CLASSES + "/" + properties);
    PropertiesConfiguration RESOURCE = new PropertiesConfiguration(properties);
    RESOURCE.setListDelimiter(',');
    return RESOURCE;
  }

  public PropertiesConfiguration setModuleProperties() throws Exception {
    String MEI_PATH_CLASSES = MEI_PATH_ABSOLUTE_RELATIVE + "/WEB-INF/classes/";
    String properties = MEI_PATH_RESOURCE + "/mei.config.properties";
    IoUtils.createFile(MEI_PATH_CLASSES + "/" + properties);
    PropertiesConfiguration RESOURCE = new PropertiesConfiguration(properties);
    RESOURCE.setListDelimiter(',');
    return RESOURCE;
  }
*/
/*
  public void createInstallProp() {
    try {
      
      INST_CONFIG = new PropertiesConfiguration("/com/syaku/resource/mei.install.properties");
      INST_CONFIG.clear();
      INST_CONFIG.setAutoSave(true);

    } catch (Exception e) {
      log.error(e.toString());
    }

  }

  public List getModuleList() {
    List listRet = new ArrayList();

    List list = ccolletConfig.getList("mei.module.name");
    int count = list.size();
    for (int i = 0; i < count; i++ ) {
      String module = (String) list.get(i);
      String name = "/com/syaku/modules/" + module + "/info.properties";
      Configuration info = new PropertiesConfiguration(name);
      Map mapRet = new HashMap();
      mapRet.put("module",,module);
      mapRet.put("config",info);

      listRet.add(mapRet);
    }

    return listRet;
  }

  public void getModuleInstallOnce(String module) {
    String name = "/com/syaku/modules/" + module + "/info.properties";
    Configuration config = new PropertiesConfiguration(name);
    return getModuleInstallOnce(module,config);
  }

  public void getModuleInstallOnce(String module,Configuration config) {
    ModuleObject daoModule = new ModuleObject();
    boolean install = config.getBoolean("mei.module.install");

    List list = config.getList("mei.mybatis.create");
    int count = list.size();
    for (int i = 0; i < count; i++ ) {
      String sqlmap = (String) list.get(i);
      sqlMap.insert(sqlmap,null);
      INST_CONFIG.addProperty("mei.module.install." + module , "true");
    }

    String title = info.getString("mei.module.title");
    boolean single = info.getBoolean("mei.module.single");
    
    Map mapSch = new HashMap();
    mapSch.put("module",module);
    
    // 모듈 정보 테이블에 인서트
    if ( single && daoModule.sqlModuleCount(mapSch) == 0 ) {
      ModuleBean modulebean = new ModuleBean();
      modulebean.setModule(module);
      modulebean.setMid(module);
      modulebean.setBrowser_title(title);
      daoModule.moduleInsert(modulebean);
    } else {
      log.info("[#MEI Install Module]" + module + " 이미 등록된 모듈이 있습니다.");
    }

  }

  public void getModuleInstallList() {
    List list = getModuleList();
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      Map map = (String) list.get(i);

      String module = (String) map.get("module");
      Configuration config = (Configuration) map.get("config");
      getModuleInstallOnce(module,config);
    }
  }

*/
}

