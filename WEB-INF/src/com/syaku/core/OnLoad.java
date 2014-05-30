/*
 * OnLoad.java 2011.03.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.core;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.interceptor.*;
import org.apache.struts2.ServletActionContext;
import org.apache.commons.configuration.*;
import org.apache.struts2.util.*;

import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class OnLoad {
  private Logger log = Logger.getLogger( this.getClass() );

  public OnLoad(Configuration meiCollection) {
    log.info("[## MEI OnLoad] Called");
    List list = meiCollection.getList("mei.onload.classes");
    for (int cnt = 0; cnt < list.size(); cnt++ ) {
      String cls_class = (String) list.get(cnt);
      if (StringUtils.isNotEmpty(cls_class)) {

         try {
          Class cls =Class.forName(cls_class);
          Object obj = (Object) cls.newInstance();

          log.info("[#MEI OnLoad] Loader Classes : " + cls_class);

         } catch(ClassNotFoundException e) {
          log.error("[#MEI OnLoad] Loader Classes : 클래스를 찾지 못함.");
         } catch(InstantiationException e) {
          log.error("[#MEI OnLoad] Loader Classes : Instance 생성하지 못함.");
         } catch(IllegalAccessException e) {
          log.error(e.toString());
         }

      }

    }

  }

}