/*
 * SnsAuthController.java 2011.11.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.snsauth;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class SnsAuthController extends ActionSnsAuth {
  private Logger log = Logger.getLogger( this.getClass() );

  public String procSnsAuthMainUpdate() throws Exception {

    try {
      sqlMap.startTransaction();

      String sns_name = param.value("sns_name");
      String token = daoSnsAuthStored.getAuthCookie();

      daoSnsAuth.getSnsAuthMainResetUpdate(token);

      if (StringUtils.isNotEmpty(token)) {
        Map map = new HashMap();
        map.put("token",token);
        map.put("name",sns_name);
        map.put("main","Y");
        daoSnsAuth.sqlSnsAuthMainUpdate(map);
      }

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("display","alert");
      MESSAGE.put("message",e.getMessage().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

  public String procSnsAuthSignOut() throws Exception {
    String sns_name = param.value("sns_name","");

    if (StringUtils.isEmpty(sns_name)) { new Exception("잘못되었습니다."); }

    String token = daoSnsAuthStored.getAuthCookie();

    if (StringUtils.isNotEmpty(token)) {

      try {
        sqlMap.startTransaction();

        Map mapSch = new HashMap();
        mapSch.put("token",token);
        mapSch.put("name",sns_name);
        daoSnsAuth.sqlSnsAuthDelete(mapSch);

        daoSnsAuthStored.getSessRemove(sns_name);

        mapSch.clear();
        mapSch.put("token",token);
        long cnt = daoSnsAuth.sqlSnsAuthCount(mapSch);
        if (cnt == 0) {
          daoSnsAuthStored.getDestroy();
        } else {
          mapSch.clear();
          mapSch.put("token",token);
          mapSch.put("main","Y");
          daoSnsAuth.sqlSnsAuthMainOneUpdate(mapSch);
        }

        sqlMap.commitTransaction();

        MESSAGE.put("error","false");

      } catch (Exception e) {

        log.error(e.toString()); // 로그 출력
        MESSAGE.put("display","alert");
        MESSAGE.put("message",e.getMessage().replaceAll("\n",""));
        MESSAGE.put("error","true");

      } finally {
        sqlMap.endTransaction();
      }

    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }


  public String procSnsAuthPostSend() throws Exception {
    String sns_name = param.value("sns_name");
    String post_send = param.value("post_send");

    if (StringUtils.isEmpty(sns_name)) { new Exception("잘못되었습니다."); }

    String token = daoSnsAuthStored.getAuthCookie();

    if (StringUtils.isNotEmpty(token)) {

      try {
        sqlMap.startTransaction();

        Map mapSch = new HashMap();
        mapSch.put("token",token);
        mapSch.put("name",sns_name);
        mapSch.put("post_send",post_send);
        daoSnsAuth.sqlSnsAuthPostSendUpdate(mapSch);

        sqlMap.commitTransaction();

        MESSAGE.put("error","false");

      } catch (Exception e) {

        log.error(e.toString()); // 로그 출력
        MESSAGE.put("display","alert");
        MESSAGE.put("message",e.getMessage().replaceAll("\n",""));
        MESSAGE.put("error","true");

      } finally {
        sqlMap.endTransaction();
      }

    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

}
