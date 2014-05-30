/*
 * CodeModel.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.code;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import com.syaku.core.xml.*;
import com.syaku.core.parser.*; 
import com.syaku.core.*;
import com.syaku.core.util.*;
import com.syaku.core.common.*;
 
public class CodeModel extends ActionCode {
  private Logger log = Logger.getLogger( this.getClass() );

  public String getCodeXml() throws Exception {
    String code_orl = param.valueIfEmpty("code_orl",""); // code_id

    Map map = new HashMap();
    map.put("code_orl",code_orl);

    List list = daoCode.getCodeItemList(map);
    CreateTreeXml xml = new CreateTreeXml(list,"code_item_orl","code_item_orls");
    Element element = xml.xmlBulider();
    Document document = new Document(element);

    inputStream = Object2Xml.toInputStream(document,"UTF-8");
    return "xml";
  }


  public String getCodeCache() throws Exception {

    String code_orl = param.valueIfEmpty("code_orl",""); // code_id
    daoCode.getCodeCacheFile(code_orl); // 캐쉬파일 생성

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

}