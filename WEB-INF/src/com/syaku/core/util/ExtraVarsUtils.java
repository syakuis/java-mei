/*
 * ExtraVarsUtils.java 2011.05.18
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

import net.sf.json.*;

// request 파라메터에서 확장변수만 추출하여 Map 으로 반환함.

public class ExtraVarsUtils {
  private static Logger log = Logger.getLogger(ExtraVarsUtils.class);

  /**
  * @method : getRequest(parameter, parameter2, parameter3)
  * @brief 특정문자가 포함된 파라메터 값만 읽어옴.
  * @parameters {
        parameter : (HttpServletRequest) request
        parameter2 : (boolean) 빈값 읽어오기 여부
        parameter3 : (String) 특정문자 입력
    }
  * @return : (Map) 파라메터 값
  */
  public static Map getRequest(HttpServletRequest request,boolean empty) throws Exception {
    String target_name = "extra_vars_";
    return getRequest(request,empty,target_name);
  }
  public static Map getRequest(HttpServletRequest request,boolean empty,String target_name) throws Exception {

    // 파라메터를 담을 HashMap
    Map map = new HashMap();

    if (request == null) { return null; }

    Enumeration params = request.getParameterNames();
    if (params == null) { return null; }
    
    ParameterUtils param = new ParameterUtils(request);

    while(params.hasMoreElements()) {
      String para_name = (String)params.nextElement();

      if (StringUtils.isNotEmpty(target_name)) {
        if (StringUtils.lastIndexOf(para_name,target_name, 0) == -1) { continue; }
      }

      // 배열여부
      String paras[] = param.values(para_name,"");
      String para_value = "";
      int paras_cnt = paras.length;
      if (paras_cnt > 1) {
        para_value = ArrayUtils.toString(paras,"");
        para_value = para_value.replaceAll("\\{|\\}","");
      } else {
        para_value = param.value(para_name,"");
      }

      if (empty && StringUtils.isEmpty(para_value)) { continue; }
      map.put(para_name,para_value);

    }

    return map;
  }

  /**
  * @method : json(parameter, parameter2)
  * @brief 1차 배열 형식의 json 문자열을 생성합니다.
  * @parameters {
        parameter : (String) 문자열 자료
        parameter2 : (String) 구문자
    }
  * @return : (String) json
  */
  public static String json(String data,String a) throws Exception {
    a = StringUtils.defaultIfEmpty(a,",");

    String div[] = data.split(a);
    int div_cnt = div.length;
    String para_value = "";
    
    for (int i =0; i < div_cnt; i++) {
      para_value += "\"" + div[i] + "\",";
    }
    para_value = "[" + para_value.replaceAll(",$","") + "]";

    return para_value;
  }

  /**
  * @method : getObjectJson(parameter, parameter2)
  * @brief 파라메터 값을 추출하여 json 문자열을 생성합니다.
  * @parameters {
        parameter : (HttpServletRequest) request 서버정보
        parameter2 : (String) 추출 파라메터명
    }
  * @return : (String) json
  */
  public static String getObjectJson(HttpServletRequest request,String target_name) throws Exception {
    ParameterUtils params = new ParameterUtils(request);
    Map<String,Object> mapJson = (Map<String,Object>) params.target(true,target_name);
    JSONObject objJson = JSONObject.fromObject(mapJson);
    String json = objJson.toString();
    return json;
  }


  public static Map extendMap(boolean overlay, Map target, Map map) throws Exception {
    if (MapUtils.isEmpty(target) && MapUtils.isEmpty(map)) { return null; }
    if (MapUtils.isEmpty(target)) { return map; }
    if (MapUtils.isEmpty(map)) { return target; }

    Map retMap = new HashMap();

    Iterator iterator = map.keySet().iterator();

    while(iterator.hasNext()){
      String name = (String) iterator.next();
      String value = (String) map.get(name);

      boolean is = target.containsKey(name);

      // 기존 자료가 존재하지 않을 경우 추가
      if (!is) {
        if ( StringUtils.isNotEmpty(value) ) { target.put(name,value); }
      } else {
      
        // 기존 값이 없을 경우 덮어쓰기 인 경우
        if (overlay) {
          String val = (String) target.get(name);
          if ( StringUtils.isEmpty(val) ) { target.put(name,value); }
        }

      }
/*
      if (overlay) {
        if ( StringUtils.isNotEmpty(value) ) { target.put(name,value); }
      } else {
        if (!is) { // 기존 값이 존재하지 않을 경우
          if ( StringUtils.isNotEmpty(value) ) { target.put(name,value); }
        } else { // 기존값이 존재하지만 값이 없거나 널인경우
          String val = (String) target.get(name);
          if ( StringUtils.isEmpty(val) ) { target.put(name,value); }
        }
      }
      */
    }

    return target;
  }

}
