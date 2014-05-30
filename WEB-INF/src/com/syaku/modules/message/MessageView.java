  /*
 * MessageView.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.message;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class MessageView extends ActionMessage {
  private Logger log = Logger.getLogger( this.getClass() );

  public String dispMessageView() throws Exception {

    String message = (String) valuestack.findValue("message");
    String display = (String) valuestack.findValue("display");
    String error = (String) valuestack.findValue("error");
    String action = (String) valuestack.findValue("action");
    String source = (String) valuestack.findValue("source");

    MESSAGE.put("message",message);
    MESSAGE.put("display",display);
    MESSAGE.put("error",error);
    MESSAGE.put("action",action);
    MESSAGE.put("source",source);

    // alter 일 경우 xml 템플릿으로 변경
    if (StringUtils.equals(display,"alert")) {
      MOD_FTL_MODULE = MOD_FTL_MODULE_SKIN + "/message.xml.html";
      MOD_FTL_LAYOUT = MOD_FTL_MODULE;
    } else {
    }

    return SUCCESS;
  }

}
