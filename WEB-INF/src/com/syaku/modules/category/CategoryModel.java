/*
 * CategoryModel.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.category;

import java.util.*;

import org.apache.log4j.Logger;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class CategoryModel extends ActionCategory {
  private Logger log = Logger.getLogger( this.getClass() );

  public String getCategoryXml() throws Exception {
    String module_orl = param.value("module_orl");

    Document document = (Document) daoCategory.getCategoryObject2Xml(module_orl);

    inputStream = Object2Xml.toInputStream(document,"UTF-8");
    return "xml";
  }

  public String getCategoryCache() throws Exception {
    String module_orl = param.value("module_orl");

    daoCategory.createCategoryObject2Xml(module_orl);

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }


}