/*
 * CommentModel.java 2011.06.13
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.comment;

import java.util.*;
import org.apache.log4j.Logger;

public class CommentModel extends ActionComment {
  private Logger log = Logger.getLogger( this.getClass() );

  public String getCommentList() throws Exception {
    return SUCCESS;
  }

}
