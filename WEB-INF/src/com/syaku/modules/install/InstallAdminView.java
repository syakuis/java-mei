/*
 * InstallAdminView.java 2011.01.01
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

import com.syaku.core.util.*;

public class InstallAdminView extends ActionInstall {
  private Logger log = Logger.getLogger( this.getClass() );

  public String dispInstallAdminView() throws Exception {
    O.put("objModule", MM );
    O.put("listModule", (List) daoInstall.getModuleList() );
    return SUCCESS;
  }

}
