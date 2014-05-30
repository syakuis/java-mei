/*
 * AdminView.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.admin;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class AdminView extends ActionAdmin {
  private Logger log = Logger.getLogger( this.getClass() );

  public String dispAdminSettingInsert() throws Exception {

    AdminBean adminbean = new AdminBean();
    Map objModule = new HashMap();

    adminbean = (AdminBean) daoAdmin.sqlAdminObject(null);
    
    if (adminbean != null) {
      String module_orl = adminbean.getModule_orl();
      Map mapSch = new HashMap();
      mapSch.put("module_orl",module_orl);
      objModule = (Map) daoModule.objectOne(mapSch);
    }

    O.put("objAdmin" , adminbean);
    O.put("objModule" , objModule);

    return SUCCESS;
  }

}
