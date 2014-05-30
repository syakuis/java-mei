/*
 * FnUtils.java 2011.01.01
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
import javax.servlet.http.*;

import java.util.regex.*;
import java.text.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.lang3.math.NumberUtils;

public class FnUtils {
  private static Logger log = Logger.getLogger(FnUtils.class);

    public static Map setCookies(Cookie[] cookies) {
    Map mapRet = new HashMap();

    if(cookies != null){
      for (int i = 0; i < cookies.length; i++) {
        Cookie obj = cookies[i];
        mapRet.put(obj.getName(),obj.getValue());
      }
    }

    return mapRet;
  }

  /**
  * @method : getWonFormat(parameter, parameter2)
  * @brief 숫자값을 입력하여 원단위로 표시합니다
  * @parameters {
        parameter : (int | Long | String) 숫자값
        parameter2 : (String) 구문자
    }
  * @return : (String) 
  */
  public static String getWonFormat(int value) {
     return getWonFormat(ObjectUtils.toString(value));
  }
  public static String getWonFormat(Long value) {
     return getWonFormat(ObjectUtils.toString(value));
  }
  public static String getWonFormat(String value) {
    return getWonFormat(value,",");
  }
  public static String getWonFormat(String value,String pad) {
    String ret="";

    for(int i = value.length(); i > 0; i = i - 3) {
      if(i > 3) { ret = pad + value.substring(i-3,i) + ret; }
      else { ret = value.substring(0,i) + ret; }
    }

    return ret;
  }

  /**
  * @method : getFileSizeUnit(parameter, parameter2, parameter3)
  * @brief 파일의 용량 값을 특정 단위로 표시함
  * @parameters {
        parameter : (int | long | String) 용량
        parameter2 : (int) 0 : auto , 1 : B , 2 : KB , 3 : MB , 4 : GB , 5 : TB 
        parameter3 : (boolean) 원단위 표시 여부
    }
  * @return : (String) 
  */
  public static String getFileSizeUnit(String size) { return getFileSizeUnit( size , 0 , false); }
  public static String getFileSizeUnit(long size) { return getFileSizeUnit( ObjectUtils.toString(size) , 0 , false); }
  public static String getFileSizeUnit(String size,int unit,boolean won) {
    double number = NumberUtils.toDouble(size);
    String unit_string = "";
    String ret_size = "";

    float size_kb = 1024;
    float size_mb = size_kb * 1024;
    float size_gb = size_mb * 1024;
    float size_tb = size_gb * 1024;

    switch (unit) {
    
      case 0 :

        if (size_tb <= number) {
          number = number / 1024 / 1024 / 1024 / 1024;
          unit_string = " TB";
        } else if (size_gb <= number && size_tb > number) {
          number = number / 1024 / 1024 / 1024;
          unit_string = " GB";
        } else if (size_mb <= number && size_gb > number) {
          number = number / 1024 / 1024;
          unit_string = " MB";
        } else if (size_kb <= number && size_mb > number) {
          number = number / 1024;
          unit_string = " KB";
        } else {
          unit_string = " B";
        }

      break;

      case 1 :
        unit_string = " B";
      break;

      case 2 :
        number = number / 1024;
        unit_string = " KB";
      break;
      case 3 :
        number = number / 1024 / 1024;
        unit_string = " MB";
      break;
      case 4 :
        number = number / 1024 / 1024 / 1024;
        unit_string = " GB";
      break;
      case 5 :
        number = number / 1024 / 1024 / 1024 / 1024;
        unit_string = " TB";
      break;
    
    }

    number = Double.parseDouble( String.format("%.2f", number) );
    String number_str = ObjectUtils.toString(number);
    number_str = StringUtils.replace(number_str,".0","");

    ret_size = number_str + unit_string;

//    if (won) { ret_size = getWonFormat(number_str) + unit_string; }

    return ret_size;
  }

  /**
  * @method : getNudeHtml(parameter)
  * @brief HTML 문자열을 받아 HTML 태그를 제거합니다.
  * @parameters {
        parameter : (String) HTML 문자열
    }
  * @return : (String) 
  */
  public static String getNudeHtml(String str) {
    String html = str.replaceAll("\\r\\n","");
    html = str.replaceAll("(<{1}\\/{0,1})[^<>]*(\\/{0,1}>{1})","");
    return html;
  }

  /**
  * @method : getStringPatten(parameter,parameter2)
  * @brief 문자열에서 원하는 문자열 패턴을 찾아 결과를 알려줍니다.
  * @parameters {
        parameter : (String) 문자열
        parameter2 : (String) 찾을 문자열
    }
  * @return : (boolean) 
  */
  public static boolean getStringPatten(String str,String patten) {
    Pattern comp = Pattern.compile(patten);
    return comp.matcher(str).find();
  }

  /**
  * @method : getCutSpaces(parameter)
  * @brief 문자열을 원하는 만큼 짤라서 반환합니다.
  * @parameters {
        parameter : (int) 길이
    }
  * @return : (String) 
  */
  public static String getCutSpaces(int count) { 
  StringBuffer sb = new StringBuffer(); 
  for (int i = 0; i < count; i++) { 
  sb.append(' '); 
  } 
  return sb.toString(); 
  } 

  /**
  * @method : getCutString(parameter,parameter2)
  * @brief 문자열을 원하는 만큼 짤라서 반환합니다.
  * @parameters {
        parameter : (String) 문자열
        parameter2 : (int) 길이
        parameter2 : (String) 구문자
    }
  * @return : (String) 
  */
  public static String getCutString(String str, int length , String fix) { 
  byte[] bytes = str.getBytes(); 
  int len = bytes.length; 
  int counter = 0; 
  if (length >= len) { 
  return str + getCutSpaces(length - len); 
  } 
  for (int i = length - 1; i >= 0; i--) { 
  if (((int)bytes[i] & 0x80) != 0) 
  counter++; 
  } 

  String ret = new String(bytes, 0, length + (counter % 2));
  return ret + fix; 
  } 
  public static String getCutInString(String str, int length , String fix) { 
  byte[] bytes = str.getBytes(); 
  int len = bytes.length; 
  int counter = 0; 
  if (length >= len) { 
  return str + getCutSpaces(length - len); 
  } 
  for (int i = length - 1; i >= 0; i--) { 
  if (((int)bytes[i] & 0x80) != 0) 
  counter++; 
  }

  String ret = new String(bytes, 0, length - (counter % 2));
  return ret + fix; 
  }


// --------------------------------------------- 휴지통 --------------------------------------------------------------

  public static String getFileSize(int size) { return getFileSize(ObjectUtils.toString(size)); }
  public static String getFileSize(Long size) { return getFileSize(ObjectUtils.toString(size)); }
  public static String getFileSize(String size) {
    String gubn[] = {"Byte", "KB", "MB" } ;
    String returnSize = new String ();
    int gubnKey = 0;
    double changeSize = 0;
    long fileSize = 0;
    try{
      fileSize =  Long.parseLong(size);
      for( int x=0 ; (fileSize / (double)1024 ) >0 ; x++, fileSize/= (double) 1024 ){
        gubnKey = x;
        changeSize = fileSize;
      }
      returnSize = changeSize + gubn[gubnKey];
    }catch ( Exception ex){ returnSize = "0.0 Byte"; }

    return returnSize;
  }
/*
  public static String getCutString(String str, int limit, String fix) {
    char[] char_array = str.toCharArray();
    int count = char_array.length;
    if (limit >= count) { return str; }
    return str.substring(0,limit) + fix;
  }
*/

  public static String cutString(String inputStr, int limit, String fixStr) {
    limit = limit * 2;
    if (inputStr == null)
        return "";
    if (limit <= 0)
        return inputStr;
    byte[] strbyte = null;
    strbyte = inputStr.getBytes();

    if (strbyte.length <= limit) {
        return inputStr;
    }
    char[] charArray = inputStr.toCharArray();
    int checkLimit = limit;
    for ( int i = 0 ; i < charArray.length ; i++ ) {
        if (charArray[i] < 256) {
            checkLimit -= 1;
        }
        else {
            checkLimit -= 2;
        }
        if (checkLimit <= 0) {
            break;
        }
    }
    //대상 문자열 마지막 자리가 2바이트의 중간일 경우 제거함
    byte[] newByte = new byte[limit + checkLimit];
    for ( int i = 0 ; i < newByte.length ; i++ ) {
        newByte[i] = strbyte[i];
    }
    if (fixStr == null) {
        return new String(newByte);
    }
    else {
        return new String(newByte) + fixStr;
    }
  }

}