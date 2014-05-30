/*
 * IssueAdminController.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.issue;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class IssueAdminController extends ActionIssue {
  private Logger log = Logger.getLogger( this.getClass() );

 public String procIssueAdminDelete() throws Exception {
    String module_orl = param.value("module_orl");

    try {
      sqlMap.startTransaction();
 
       // 모듈삭제
      daoModule.moduleDelete(module_orl);
       HashMap mapSch = new HashMap();
      mapSch.put("module_orl",module_orl);
      // 게시판 삭제
      daoIssue.sqlIssueModuleDelete(mapSch);
      // 분류삭제
      daoCategory.getCategoryModuleDelete(module_orl);
      // 첨부파일 삭제
      daoFile.getFileModuleDelete(module_orl);
      // 코드 삭제

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
