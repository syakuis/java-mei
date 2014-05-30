/*
 * AddonAdminView.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.addon;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class AddonAdminView extends ActionAddon {
  private Logger log = Logger.getLogger( this.getClass() );

  public String dispAddonAdminList() throws Exception {
    O.put("listAddonType", modConfig.getList("mei.addon.type") );
    O.put("listAddonLoad" , daoAddon.getAddonLoader() );
    Map mapSch = new HashMap();
    O.put("listAddon", (List) daoAddon.sqlAddonList(mapSch));
     return SUCCESS;
  }

}
