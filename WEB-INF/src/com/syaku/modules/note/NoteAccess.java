/*
 * NoteAccess.java 2012-03-23
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.note;

import java.util.*;

import org.apache.log4j.Logger;
import com.ibatis.sqlmap.client.SqlMapClient;

import com.syaku.core.common.*;

public class NoteAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public Long sqlNoteCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.note.select-note-count", map); }
  public List sqlNoteList(Map map) throws Exception { return sqlMap.queryForList("modules.note.select-note-list", map); }
  public String sqlNoteInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.note.insert-note",object); }
  public Object sqlNoteObject(Map map) throws Exception { return sqlMap.queryForObject("modules.note.select-note-one", map); }
  public void sqlNoteDelete(Map map) throws Exception { sqlMap.delete("modules.note.delete-note", map); }
  public void sqlNoteCreate(Map map) throws Exception { sqlMap.insert("modules.note.create-note",map); }

  
}