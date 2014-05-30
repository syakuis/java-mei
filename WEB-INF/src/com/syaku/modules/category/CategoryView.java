/*
 * CategoryView.java 2011.05.23
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

public class CategoryView extends ActionCategory {
  private Logger log = Logger.getLogger( this.getClass() );


  public String dispCategoryView() throws Exception {
    return SUCCESS;
  }

  public String dispCategoryInsert() throws Exception {
    String category_orl = param.value("category_orl");
    String parent_orl = param.valueIfEmpty("parent_orl","0");
    String category_seq = param.valueIfEmpty("category_seq","0");

    CategoryBean object = new CategoryBean();

    if ( StringUtils.isNotEmpty(category_orl) ) {
      Map mapSch = new HashMap();
      mapSch.put("category_orl",category_orl);
      object = (CategoryBean) daoCategory.sqlCategoryObject(mapSch);
    }

    O.put("objCategory", object);

    MOD_FTL_LAYOUT = MOD_FTL_MODULE; // 모듈 스킨 템플릿만 노출

    return SUCCESS;
  }

}
