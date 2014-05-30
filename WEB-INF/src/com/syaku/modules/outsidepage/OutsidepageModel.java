/*
 * OutsidepageModel.java 2011.01.01
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

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import com.syaku.core.*;
import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class OutsidepageModel extends ActionOutsidepage {
  private Logger log = Logger.getLogger( this.getClass() );

  public String getSkinFileList() throws Exception {
    String skin = param.value("skin");
    String path = MEI_PATH_ABSOLUTE + MOD_PATH_RELATIVE + "/skins/" + skin;
    List list = IoUtils.listFiles(path,false);
    Document doc = (Document) Object2Xml.make(list,false);

    inputStream = Object2Xml.toInputStream(doc,"UTF-8");
    return "xml";
  }

}