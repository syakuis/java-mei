/*
 * ActionAdmin.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.admin;

import java.util.*;

import org.apache.log4j.Logger;

import com.syaku.core.*;

public class ActionAdmin extends MeiBasic {
  private Logger log = Logger.getLogger( this.getClass() );
  public AdminObject daoAdmin = new AdminObject();
}