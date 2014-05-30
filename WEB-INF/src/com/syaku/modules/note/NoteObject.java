/*
 * NoteObject.java 2011.06.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.note;

import java.util.*;
import java.io.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
 import org.apache.commons.configuration.*;

import com.syaku.core.util.*;

public class NoteObject extends NoteAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration meiConfig = ConfigUtils.getProperties("mei.properties"); // mei 정보
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/note/info.properties");

}