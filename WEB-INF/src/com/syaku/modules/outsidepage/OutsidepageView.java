/*
 * OutsidepageView.java 2011.07.24
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.outsidepage;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class OutsidepageView extends ActionOutsidepage {
  private Logger log = Logger.getLogger( this.getClass() );

  private boolean getOutsidepageGrant(boolean grant) throws Exception {
    if (!grant) {
      MESSAGE.put("message","권한이 없습니다.");
      MESSAGE.put("display","");
      MESSAGE.put("error","true");
      MESSAGE.put("action","");
      MESSAGE.put("source","");
      valuestack.push(MESSAGE);
    }
    return grant;
  }

  public String dispOutsidepageView() throws Exception {
    if (!getOutsidepageGrant(MEI_GRANT_VIEW)) { return "message"; }
    String target = param.value("target");

    if (StringUtils.isNotEmpty(target)) {
      int t = NumberUtils.stringToInt(target);
      String options_sub_path = StringUtils.defaultIfEmpty( (String) MM.get("options_sub_path"),"" );
      String[] path_div = StringUtils.split(options_sub_path,',');
      String options_sub_is_use = StringUtils.defaultIfEmpty( (String) MM.get("options_sub_is_use"),"" );
      String[] use_div = StringUtils.split(options_sub_is_use,',');
      String is_use = use_div[t];

      if ( StringUtils.equals(is_use,"Y") ) {
        MOD_FTL_MODULE = MOD_FTL_MODULE_SKIN + "/" + path_div[t];
      } else {
        MESSAGE.put("message","잘못된 접속경로 입니다.");
        MESSAGE.put("display","");
        MESSAGE.put("error","true");
        MESSAGE.put("action","");
        MESSAGE.put("source","");
        valuestack.push(MESSAGE);
        return "message";
      }

    } else {
      String options_outside_page = StringUtils.defaultIfEmpty( (String) MM.get("options_outside_page"),"" );
      MOD_FTL_MODULE = options_outside_page;
    }

    return SUCCESS;
  }

}