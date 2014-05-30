/*
 * TemplateUtils.java 2011.08.04
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

import freemarker.template.*;

public class TemplateUtils {
  private static Logger log = Logger.getLogger(TemplateUtils.class);


  /**
  * @method : getString()
  * @brief 템플릿을 문자열로 호출합니다.
  * @parameters {
        parameter : (String) 폴더 경로
        parameter2 : (Stirng) 템플릿 파일명
        parameter3 : (Map) 자료
        parameter2 : (Stirng) 언어
    }
    @return (String)
  */

  public static String getString(String dir,String file,Object data) throws Exception {
    return getString(dir,file,data,"utf-8");
  }
  public static String getString(String dir,String file,Object data,String charset) throws Exception {
    charset = StringUtils.defaultIfEmpty(charset,"utf-8");
    String content = "";

    try{
      Configuration cfg = new Configuration();

      cfg.setDirectoryForTemplateLoading(new File(dir));
      cfg.setObjectWrapper(new DefaultObjectWrapper());
      cfg.setDefaultEncoding(charset);
      cfg.setEncoding(Locale.KOREAN, charset);

      Template temp = cfg.getTemplate(file);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      Writer output = new OutputStreamWriter(os, charset);

      try {
        temp.process(data, output);
      } catch (TemplateException e) {
        e.printStackTrace();
      }

      output.flush();
      output.close();
      os.close();
//      map.clear();

      content = os.toString(charset);
      log.debug(content); // 로그 출력
    }catch (Exception e) {
      log.error(e.toString()); // 로그 출력
    }

    return content;

  }

}