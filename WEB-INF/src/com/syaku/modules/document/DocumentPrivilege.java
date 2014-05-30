/*
 * DocumentPrivilege.java 2011.05.23
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

public class DocumentPrivilege extends DocumentObject {
  private Logger log = Logger.getLogger( this.getClass() );

  // 해당 자료에 대한 소유자 체크
  public boolean getDocumentIsMine(String document_orl,String member_orl) throws Exception {
    if (StringUtils.isEmpty(document_orl) || StringUtils.isEmpty(member_orl)) {
      return false;
    }

    Map map = new HashMap();
    map.put("document_orl",document_orl);
    map.put("member_orl",member_orl);
    long cnt = (long) sqlDocumentCount(map);
    if (cnt > 0) { return true; } else { return false; }
  }

  public boolean getDocumentPassword(String document_orl,String password) throws Exception {
    if (StringUtils.isEmpty(document_orl) || StringUtils.isEmpty(password)) {
      return false;
    }
    Map map = new HashMap();
    map.put("document_orl",document_orl);
    map.put("password",password);
    long cnt = (long) sqlDocumentCount(map);
    if (cnt > 0) { return true; } else { return false; }
  }

  public void getDocumentPrivilegeException(String document_orl,String member_orl) throws Exception {
    getDocumentPrivilegeException(getDocumentIsMine(document_orl,member_orl));
  }
  public void getDocumentPrivilegeException(boolean grant) throws Exception {
    if (!grant) { throw new Exception("권한이 없습니다."); }
  }

}