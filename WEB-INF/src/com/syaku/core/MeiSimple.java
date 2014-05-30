/*
 * MeiSimple.java 2008.12.17
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core;

import java.util.*;

import org.apache.log4j.Logger;

public class MeiSimple extends Mei {
  private Logger log = Logger.getLogger( this.getClass() );

  public void meiBefore() { }
  public void meiAfter() { }

  public void prepare() {
    log.debug("[## MEI MeiSimple prepare] Called");
  }
  public void after() {
    log.debug("[## MEI MeiSimple after] Called");
    meiBefore();
    meiInit();
    meiPrepare();
    modProperties();
    modInfo();
    grant();
    modSkin();
    layoutInfo();
    menuList();
    meiAfter();
  }
  public void after(String mid , String m_orl) {
    log.debug("[## MEI MeiSimple after] Called");
    meiBefore();
    meiInit();
    meiPrepare();
    modProperties();
    modInfo(mid,m_orl);
    grant();
    modSkin();
    layoutInfo();
    menuList();
    meiAfter();
  }

  public Object getModel() {
    log.debug("[## MEI MeiSimple getModel] Called");
    return new Object();
  }

}