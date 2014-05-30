/*
 * CountString.java 2010.10.19
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import org.apache.log4j.Logger;
import org.apache.commons.lang.ArrayUtils;

public class CountString {
  private Logger log = Logger.getLogger( this.getClass() );

  private static char number[] = {'0','1','2','3','4','5','7','8','9'};
  private static char alphabet[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
  private static char ALPHABET[] = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
  private static char all[] = ArrayUtils.addAll(ArrayUtils.addAll(number,alphabet),ALPHABET);
  private static int total = all.length;

  /**
  * @method : string2number(parameter)
  * @brief : 인자의 문자와 일치하는 값은 순번을 반환한다.
  * @parameter : 문자열
  * @return : int[]
  */
  public static int[] string2number(String str) {
    int chr = str.length();
    char ch[] =str.toCharArray(); 

    int[] data = { };

    for (int c = (chr-1); c >= 0; c--) {
      char s = ch[c]; // 인수 문자

      for (int i = 0; i < total; i++) {
        char key = all[i];

        // 인수의 문자와 all 배열의 문자가 일치할 경우 배열에 삽입.
        if (key == s) { data = ArrayUtils.add(data,i); }
      }

    }

    return data;
  }

  /**
  * @method : number2string(parameter1,parameter2,parameter3)
  * @brief : 숫자형으로 얻은 순번배열을 parameter2 의 값만큼 더하고 순번의 문자값을 반환한다.
  * @parameters {
        parameter : int[] 숫자형 순번배열
        parameter2 : int 카운트할 숫자값.
        parameter3 : boolean - true 최고 값을 넘을 경우 예외처리함 , false 반올림처리함.
      }
  * @return : String 숫자형 순번배열의 순번 위치의 문자열을 조합하여 반환함.
  */
  public static String number2string(int[] array,int num,boolean asc) {
    int count = array.length;
    boolean is_key = false;
    char data[] = new char[count];


    for (int i = 0; i < count; i++) {

      int key = array[i];

      if (i == 0) { // 첫번째 배열에 인수의 값을 더함.
        key = key + num;
      }

      if (is_key) { // 올림수 여부
        key = key + 1;
      }
      

      if (key >= total) {
        key = key - total;
        is_key = true;
      } else {
        is_key = false;
      }

      data[(count-1) - i] = all[key]; // 역순으로 배열 생성.
    }


    if (is_key) { // 마지막 루프에서 올림수가 있을 경우 한자리 추가
      if (asc == false) {
         return null;
      } else {
        ArrayUtils.add(data,0,'1');
      }
    }

    // , 제거하고 배열 조인.
    String ret = ArrayUtils.toString(data);
    return ret.replaceAll("[{},]",""); 
  }
  
  // 랜덤 숫자
  public static int[] rand(int length) {
    int data[] = new int[length];

    for (int i = 0; i < length; i++ ) {
      int num = (int) (Math.random() * total); 
      data[i] = num;
    }

    return data;
  }

  /**
  * @method : get(parameter1,parameter2,parameter3)
  * @brief : 배열을 사용하여 오름 카운트를 구현함.
  * @parameters {
        parameter : 문자열
        parameter2 : int 카운트할 숫자값.
        parameter3 : boolean - true 최고 값을 넘을 경우 예외처리함 , false 반올림처리함.
      }
  * @return : String 숫자형 순번배열의 순번 위치의 문자열을 조합하여 반환함.
  */
  public static String get(String str,int num,boolean asc) {
    int data[] = string2number(str);
    return number2string(data,num,asc);
  }

  public static String getRand(int length) {
    int data[] = rand(length);
    return number2string(data,0,true);
  }

}