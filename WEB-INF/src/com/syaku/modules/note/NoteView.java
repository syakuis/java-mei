/*
 * NoteView.java 2011.05.23
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
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

 import com.syaku.modules.module.*;

public class NoteView extends ActionNote {
  private Logger log = Logger.getLogger( this.getClass() );


  public String dispNoteSendInsert() throws Exception {
    return SUCCESS;
  }


  public String dispNoteView() throws Exception {
    String note_orl = param.value("note_orl");

    Map  map = new HashMap();
    map.put("note_orl",note_orl );
    NoteBean objNote = (NoteBean)daoNote.sqlNoteObject(map);

    O.put("objNote",objNote);
     return SUCCESS;
  }

}
