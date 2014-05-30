/*
 * CounterObject.java 2011.06.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.counter;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;
import com.syaku.modules.module.*;

public class CounterObject extends CounterAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/counter/info.properties");

  public List getCounterStatsDayList(Map map) throws Exception { 
    ModuleObject daoModule = new ModuleObject();
    String MODULE_NAME = modConfig.getString("mei.module.name"); // 고정네임

    Map mapSch = new HashMap();
    mapSch.put("module",MODULE_NAME);
    Map mapRet = (Map) daoModule.objectOne(mapSch);

    if (MapUtils.isNotEmpty(mapRet)) {

      String options_filter_keyword = (String) mapRet.get("options_filter_keyword");
      if (StringUtils.isNotEmpty(options_filter_keyword)) {
        String[] keyword_ar = options_filter_keyword.split(";");
        map.put("user_agent_array",keyword_ar);
      }

      String options_filter_ip = (String) mapRet.get("options_filter_ip");
      if (StringUtils.isNotEmpty(options_filter_ip)) {
        String[] ip_ar = options_filter_ip.split(";");
        map.put("ip_array",ip_ar);
      }
    }

    return (List) sqlCounterStatsDayList(map); 
  }

}