/*
 * DocumentAdminController.java 2011.05.23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.document;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

import com.syaku.modules.comment.*;

public class DocumentAdminController extends ActionDocument {
  private Logger log = Logger.getLogger( this.getClass() );

  private DocumentPrivilege privilege = new DocumentPrivilege();
  private CommentObject daoComment = new CommentObject();

  public String procDocumentAdminDelete() throws Exception {
    String module_orl = MODULE_ORL;

    try {
      sqlMap.startTransaction();
      privilege.getDocumentPrivilegeException(MEI_GRANT_MANAGER);

      // 분류 삭제
      daoCategory.getCategoryModuleDelete(module_orl);
      // 모듈삭제
      daoModule.moduleDelete(module_orl);
      // 코맨트 삭제
      daoComment.getCommentModuleDelete(module_orl);
      // 첨부파일 삭제
      daoFile.getFileModuleDelete(module_orl);

      HashMap mapSch = new HashMap();
      mapSch.put("module_orl",module_orl);
      // 게시판 삭제
      daoDocument.getDocumentModuleDelete(mapSch);

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


  public String procDocumentAdminExtraKeysInsert() throws Exception {
    String module_orl = MODULE_ORL;
    String var_idx = param.value("var_idx");
    String var_name = param.value("var_name");
    String var_type = param.value("var_type");
    String var_is_required = param.valueIfEmpty("var_is_required","N");
    String var_search = param.valueIfEmpty("var_search","N");
    String var_default = param.value("var_default");
    String var_desc = param.value("var_desc");
    String eid = param.value("eid");

    try {
      sqlMap.startTransaction();
      privilege.getDocumentPrivilegeException(MEI_GRANT_MANAGER);

      DocumentExtraKeysBean documentextrakeysbean = new DocumentExtraKeysBean();
      documentextrakeysbean.setModule_orl(module_orl);
      documentextrakeysbean.setVar_idx(var_idx);
      documentextrakeysbean.setVar_name(var_name);
      documentextrakeysbean.setVar_type(var_type);
      documentextrakeysbean.setVar_is_required(var_is_required);
      documentextrakeysbean.setVar_search(var_search);
      documentextrakeysbean.setVar_default(var_default);
      documentextrakeysbean.setVar_desc(var_desc);
      documentextrakeysbean.setEid(eid);
      
      if (StringUtils.isEmpty(var_idx)) {
        daoDocument.sqlDocumentExtraKeysInsert(documentextrakeysbean);
      } else {
        daoDocument.sqlDocumentExtraKeysUpdate(documentextrakeysbean);
      }

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("display","alert");
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }


  public String procDocumentAdminGrantInsert() throws Exception {
    try {
      sqlMap.startTransaction();
      privilege.getDocumentPrivilegeException(MEI_GRANT_MANAGER);

      daoModule.moduleGrantsInsert(param,MODULE_ORL);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("display","alert");
      MESSAGE.put("message",e.getMessage().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }


}
