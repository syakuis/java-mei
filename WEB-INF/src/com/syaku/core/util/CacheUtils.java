/*
 * CacheUtils.java 2011.08.08
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;
import org.apache.commons.io.*;

public class CacheUtils {
  private Logger log = Logger.getLogger( this.getClass() );
  
  // ,(콤마)는 배열이나 리스트로 인식되어. 문자로 받게되면 맨 처음 배열이나 리스트만 가져와 문제가 발생함.
  // 배열로 받아 문자열로 변환하는 작업으로 대처함.
  public List getTreeList(String path,Map map) throws Exception {
    List listRet = new ArrayList();
    XMLConfiguration config = ConfigUtils.getXml(path);
    if (config != null) {
      List list = config.configurationsAt("item");
      int i = 0;

      for(Iterator it = list.iterator(); it.hasNext();) {
        HierarchicalConfiguration obj = (HierarchicalConfiguration) it.next();

        Map<String,String> mapRet = new HashMap<String,String>();
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext()){
          String name = (String) iterator.next();
          String value = obj.getString("[@" + name + "]");

          mapRet.put(name,value);
        }

        listRet.add(i,mapRet);
        i++;
      }
    }

    return listRet;
  }

  public String getString(String path,String mod) throws Exception {
    return FileUtils.readFileToString(new File(path),"utf-8");
  }

}