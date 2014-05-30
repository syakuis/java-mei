/*
 * CategoryController.java 2011.05.23
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
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class CategoryController extends ActionCategory {
  private Logger log = Logger.getLogger( this.getClass() );

  // 분류 등록 및 수정
  public String procCategoryInsert() throws Exception {
    String module_orl = param.value("module_orl");

    try {
      sqlMap.startTransaction();

      daoCategory.getCategoryInsert(param);
      daoCategory.createCategoryObject2Xml(module_orl);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

  public String procCategoryMove() throws Exception {
    String module_orl = param.value("module_orl");

    try {
      sqlMap.startTransaction();

      daoCategory.getCategoryMove(param);
      daoCategory.createCategoryObject2Xml(module_orl);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

  public String procCategoryDelete() throws Exception {
    String category_orl = param.value("category_orl");
    String module_orl = param.value("module_orl");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("category_orl",category_orl);
      mapSch.put("module_orl",module_orl);

      daoCategory.sqlCategoryDelete(mapSch);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

}
