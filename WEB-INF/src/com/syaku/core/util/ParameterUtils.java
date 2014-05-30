/*
 * ParameterUtils.java 2009.09.22
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import java.util.*;
import java.util.regex.*;
import java.text.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

public class ParameterUtils {
  private Logger log = Logger.getLogger(ParameterUtils.class);
  
  private HttpServletRequest request;
  private HttpServletResponse response;

  private String parameter = null;
  private Map<String,String> params = new HashMap<String,String>();
  private Pattern para_patten = Pattern.compile("^(.*)=(.*)$",Pattern.MULTILINE);

  private Map parameters = null;
  public void setParameters(Map parameters) {
    
    if (MapUtils.isNotEmpty(parameters)) {
      this.parameters = parameters;
    }

  }

  /**
  * @method : ParameterUtils(parameter)
  * @brief 파라메터를 제어하기 위한 선언
  * @parameters {
        parameter : (HttpServletRequest) request 서버변수
    }
  */
  public ParameterUtils(HttpServletRequest request) {
    this.request = request;
  }

  /**
  * @method : ParameterUtils(parameter,parameter2)
  * @brief 파라메터를 제어하기 위한 선언
  * @parameters {
        parameter : (HttpServletRequest) request 서버변수
        parameter2 : (HttpServletResponse) response 서버변수
    }
  */
  public ParameterUtils(HttpServletRequest request,HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }

  // 파라메터 empty 체크

  /**
  * @method : check(parameter,parameter2)
  * @brief 배열값에 빈값 체크
  * @parameters {
        parameter : (String[] | Stirng) 배열 혹은 문자열
        parameter2 : (boolean) 생략 여부
    }
    @return (boolean)
  */
  public boolean check(String[] value,boolean is) {
    int count = value.length;
    boolean ret = false;

    if (is) { return true; }

    try {
      for (int i = 0; i < count; i++ ) {
        ret = check(value[i],is);
      }

      ret = true;
    } catch (Exception e) {
      log.error(e.toString());
    }

    return ret;
  }

  // 파라메터 empty 체크
  public boolean check(String value,boolean is) {
    
    boolean ret = false;

    if (is) { return true; }

    try {

      if (StringUtils.isEmpty(value)) {
        throw new Exception("중요 파라메터 값이 없습니다.");
      }

      ret = true;
    } catch (Exception e) {
      log.error(e.toString());
    }

    return ret;
  }

  public void destroy() {
    parameter = null;
  }

  /**
  * @method : valuesMap()
  * @brief 파라메터 값을 Map 에 담아서 반화합니다.
    @return (Map)
  */
  public Map valuesMap() {
    Map map = new HashMap();

    try {
      if (request != null) {
        Enumeration params = request.getParameterNames();

        if (params != null) {
           while(params.hasMoreElements()){
            String para_name = (String)params.nextElement();
            
            String[] para_value = values(para_name);
            String para_value1 = value(para_name);

            if (para_value.length > 1) {
            map.put(para_name,para_value);
            } else {
            map.put(para_name,para_value1);
            }

          }
        }
      }
    } catch (Exception e) {
      log.error(e.toString());
    }

    return map;
  }


  private Map load() {
    // 파라메터를 담을 HashMap
    Map<String,String> hm = new HashMap<String,String>();

    if (request != null) {
      Enumeration params = request.getParameterNames();

      if (params != null) {
         while(params.hasMoreElements()){
          String para_name = (String)params.nextElement();
          String para_value = request.getParameter(para_name);
          para_value = StringUtils.defaultString(para_value,"");

          hm.put(para_name,para_value);
        }
      }
    }


    if (!StringUtils.isEmpty(parameter)) {
      
      String para = parameter.replaceAll("^[?&]","");

      String para_tokens[] = para.split("&");
      int para_cnt = para_tokens.length;

      for (int i = 0; i < para_cnt; i++ ) {        
        Matcher para_matcher = para_patten.matcher(para_tokens[i]);
        String para_name = para_matcher.replaceAll("$1");
        String para_value = para_matcher.replaceAll("$2");
        hm.put(para_name,para_value);
      }

      destroy();
    }

    return hm;
  }
  
  /**
  * @method : value()
  * @brief 파라메터 값을 읽어서 반환합니다.
  * @parameters {
        parameter : (Stirng) 읽어 올 파라메터명
        parameter2 : (Stirng) 빈값인 경우 대처 할 기본값
    }
    @return (String)
  */
  public String value(String name) {
    return value(name,null);
  }
  // 변수가 선언되지 않은 값.
  public String value(String name,String val) {
    String ret = request.getParameter(name);
    if (val != null) {
      ret = StringUtils.defaultString(ret,val);
    }

    return ret;
  }

  /**
  * @method : values()
  * @brief 배열 파라메터 값을 읽어서 반환합니다.
  * @parameters {
        parameter : (Stirng) 읽어 올 파라메터명
        parameter2 : (Stirng) 빈값인 경우 대처 할 기본값
    }
    @return (String[])
  */
  public String[] values(String name) throws Exception {
    return values(name,null);
  }
  public String[] values(String name,String val) throws Exception {
    String ret[] = request.getParameterValues(name);

    if (ret == null) {
      return null;
    }
    if (val != null) {

      for(int i=0; i < ret.length; i++) {
        ret[i] = StringUtils.defaultString(ret[i],val);
      }
    }

    return ret;
  }

  /**
  * @method : valueIfEmpty()
  * @brief 파라메터 값을 읽어서 반환합니다.
  * @parameters {
        parameter : (Stirng) 읽어 올 파라메터명
        parameter2 : (Stirng) 빈공백 값인 경우 대처 할 기본값
    }
    @return (String)
  */
  public String valueIfEmpty(String name) {
    return valueIfEmpty(name,null);
  }
  public String valueIfEmpty(String name,String val) {
    String ret = request.getParameter(name);
    if (val != null) {
      ret = StringUtils.defaultIfEmpty(ret,val);
    }

    return ret;
  }

  /**
  * @method : valuesIfEmpty()
  * @brief 배열 파라메터 값을 읽어서 반환합니다.
  * @parameters {
        parameter : (Stirng) 읽어 올 파라메터명
        parameter2 : (Stirng) 빈공백 값인 경우 대처 할 기본값
    }
    @return (String[])
  */
  public String[] valuesIfEmpty(String name) throws Exception {
    return valuesIfEmpty(name,null);
  }
  public String[] valuesIfEmpty(String name,String val) throws Exception {
    String ret[] = request.getParameterValues(name);

    if (ret == null) {
      return null;
    }
    if (val != null) {

      for(int i=0; i < ret.length; i++) {
        ret[i] = StringUtils.defaultIfEmpty(ret[i],val);
      }
    }

    return ret;
  }


  private String div(String str) {
    
    String patten = "^([?&]).*$";
    Pattern compile = Pattern.compile(patten);
    if (compile.matcher(str).find()) {
      return str.replaceAll(patten,"$1");
    } else {
      return "";
    }

  }

  /**
  * @method : pick()
  * @brief 파라메터 값을 읽어 원하는 값만 추출합니다.
  * @parameters {
        parameter : (Stirng) 읽어 올 파라메터 쿼리
        parameter2 : (Stirng) 임의의 파라메터 퀴리
    }
    @return (String)
  */
  public String pick(String update,String str) {
    parameter = str;
    return pick(update);
  }

  public String pick(String update) {
    String params_ext = "";

    params = load();

    String first_div = div(update);

    if (!StringUtils.isEmpty(update)) {

      String new_para = update.replaceAll("^\\?","");
      String new_para_tokens[] = new_para.split("&");
      int new_para_cnt = new_para_tokens.length;

      for (int x = 0; x < new_para_cnt; x++ ) {
        Matcher new_para_matcher = para_patten.matcher(new_para_tokens[x]);

        String new_para_name = new_para_matcher.replaceAll("$1");
        String new_para_value = new_para_matcher.replaceAll("$2");

        String addvalue = params.get(new_para_name);

        new_para_value = StringUtils.defaultIfEmpty(new_para_value,addvalue);

        // 값이 있는 파라메터만 유효
        if (!StringUtils.isEmpty(new_para_value)) {
          params_ext += new_para_name + "=" + new_para_value + "&";
        }

      }

    }

    params_ext = params_ext.replaceAll("&$","");

    if (StringUtils.isEmpty(params_ext)) {
      return "";
    } else {
      return first_div + params_ext;
    }

  }

  /**
  * @method : get()
  * @brief 파라메터 값을 읽어 원하는 값과 혼합하여 추출합니다.
  * @parameters {
        parameter : (Stirng) 혼합할 파라메터 쿼리
        parameter2 : (Stirng) 임의의 파라메터 퀴리
    }
    @return (String)
  */
  public String get(String update,String str) {
    parameter = str;
    return get(update);
  }

  public String get(String update) {
    params = load();

    String first_div = div(update);

    if (!StringUtils.isEmpty(update)) {
      String new_para = update.replaceAll("^\\?","");
      String new_para_tokens[] = new_para.split("&");
      int new_para_cnt = new_para_tokens.length;

      for (int x = 0; x < new_para_cnt; x++ ) {
        Matcher new_para_matcher = para_patten.matcher(new_para_tokens[x]);
        String new_para_name = new_para_matcher.replaceAll("$1");
        String new_para_value = new_para_matcher.replaceAll("$2");

        params.put(new_para_name,new_para_value);
      }
    }

    // 완성된 HashMap key 로드
    String params_ext = "";
    Iterator iter = params.keySet().iterator();
    while(iter.hasNext()){
      String addkey = (String) iter.next();
      String addvalue = params.get(addkey);

      // 값이 있는 파라메터만 유효
      if (!StringUtils.isEmpty(addvalue)) {
        params_ext += addkey + "=" + addvalue + "&";
      }
    }

    params_ext = params_ext.replaceAll("&$","");

    if (StringUtils.isEmpty(params_ext)) {
      return "";
    } else {
      return first_div + params_ext;
    }

  }

  /**
  * @method : target()
  * @brief 파라메터 값을 읽어 쿼리에 해당하는 대상만 추출합니다.
  * @parameters {
        parameter : (boolean) 빈공백 여부
        parameter2 : (Stirng) 임의의 파라메터 퀴리
    }
    @return (Map)
  */
  public Map target(boolean empty,String target) {
    // 파라메터를 담을 HashMap
    Map map = new HashMap();
    try {

      if (request == null) { return null; }

      Enumeration params = request.getParameterNames();
      if (params == null) { return null; }

      while(params.hasMoreElements()) {
        String para_name = (String)params.nextElement();

        if (StringUtils.isNotEmpty(target)) {
          if (StringUtils.lastIndexOf(para_name,target, 0) == -1) { continue; }
        }

        // 배열여부
        String paras[] = this.values(para_name,"");
        String para_value = "";
        int paras_cnt = paras.length;
        if (paras_cnt > 1) {
          para_value = ArrayUtils.toString(paras,"");
          para_value = para_value.replaceAll("\\{|\\}","");
        } else {
          para_value = this.value(para_name,"");
        }

        if (empty && StringUtils.isEmpty(para_value)) { continue; }
        map.put(para_name,para_value);
      }

    } catch (Exception e) {
      log.error("[#MEI ParameterUtils.target] " + e.toString());
    }

    return map;
  }

  public String arrayValue(String name) {
    return arrayValue(name,null);
  }
  public String arrayValue(String name,String val) {
    String ret = null;
    if (parameters != null) {
      String[] array = (String[]) this.parameters.get(name);
      if (ArrayUtils.isNotEmpty(array)) { ret = array[0]; }
    }

    return StringUtils.defaultIfEmpty(ret,val);
  }

}