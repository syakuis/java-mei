/*
 * DocumentAccess.java 2011.05.23
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

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class DocumentAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public Long sqlDocumentCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.document.select-document-count", map); }
  public List sqlDocumentPageList(Map map) throws Exception { return sqlMap.queryForList("modules.document.select-document-join-page-list", map); }
  public List sqlDocumentReplyPageList(Map map) throws Exception { return sqlMap.queryForList("modules.document.select-document-reply-join-page-list", map); }
  public List sqlDocumentFileList(Map map) throws Exception { return sqlMap.queryForList("modules.document.select-document-file-list", map); }
  public Object sqlDocumentObject(Map map) throws Exception { return sqlMap.queryForObject("modules.document.select-document-one", map); }
  public Object sqlDocumentPervnextObject(Map map) throws Exception { return sqlMap.queryForObject("modules.document.select-document-pervnext", map); }

  public String sqlDocumentInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.document.insert-document",object); }
  public void sqlDocumentUpdate(Object object) throws Exception { sqlMap.update("modules.document.update-document",object); }
  public void sqlDocumentDelete(Map map) throws Exception { sqlMap.delete("modules.document.delete-document", map); }
  public void sqlDocumentModuleDelete(Map map) throws Exception { sqlMap.delete("modules.document.delete-document-module", map); }

  public void sqlDocumentReplySeqUpdate(Map map) throws Exception { sqlMap.update("modules.document.update-document-reply-seq",map); }
  public void sqlDocumentUpdateReadedCount(Map map) throws Exception { sqlMap.update("modules.document.update-document-readed-count",map); }

  public List sqlDocumentExtraList(Map map) throws Exception { return sqlMap.queryForList("modules.document.select-document-extrakeys-join-list", map); }
  public List sqlDocumentExtraView(Map map) throws Exception { return sqlMap.queryForList("modules.document.select-document-extrakeys-join-view", map); }

  public List sqlDocumentExtraKeysList(Map map) throws Exception { return sqlMap.queryForList("modules.document.select-document-extrakeys-list", map); }
  public Object sqlDocumentExtraKeysObject(Map map) throws Exception { return sqlMap.queryForObject("modules.document.select-document-extrakeys-one", map); }
  public List sqlDocumentExtraVarsList(Map map) throws Exception { return sqlMap.queryForList("modules.document.select-document-extravars-list", map); }

  public void sqlDocumentExtraKeysInsert(Object object) throws Exception { sqlMap.insert("modules.document.insert-document-extrakeys",object); }
  public void sqlDocumentExtraKeysUpdate(Object object) throws Exception { sqlMap.update("modules.document.update-document-extrakeys",object); }
  public void sqlDocumentExtraKeysIdxUpdate(Map map) throws Exception { sqlMap.update("modules.document.update-document-extrakeys-idx",map); }
  public void sqlDocumentExtraKeysDelete(Map map) throws Exception { sqlMap.delete("modules.document.delete-document-extrakeys", map); }
  public void sqlDocumentExtraKeysModuleDelete(Map map) throws Exception { sqlMap.delete("modules.document.delete-document-extrakeys-module", map); }

  public void sqlDocumentExtraVarsInsert(Object object) throws Exception { sqlMap.insert("modules.document.insert-document-extravars",object); }
  public void sqlDocumentExtraVarsIterateInsert(Map map) throws Exception { sqlMap.insert("modules.document.insert-document-extravars-iterate",map); }

  public void sqlDocumentExtraVarsDocumentDelete(Map map) throws Exception { sqlMap.delete("modules.document.delete-document-extravars-document", map); }
  public void sqlDocumentExtraVarsModuleDelete(Map map) throws Exception { sqlMap.delete("modules.document.delete-document-extravars-module", map); }

  public void sqlDocumentCreate(Map map) throws Exception { sqlMap.insert("modules.document.create-document",map); }
  public void sqlDocumentExtraValCreate(Map map) throws Exception { sqlMap.insert("modules.document.create-document-extra-vars",map); }
  public void sqlDocumentExtraKeyCreate(Map map) throws Exception { sqlMap.insert("modules.document.create-document-extra-keys",map); }


}