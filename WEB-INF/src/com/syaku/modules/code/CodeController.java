package com.syaku.modules.code;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;

import org.apache.commons.lang.*;

import com.syaku.core.*;
import com.syaku.core.util.*;
import com.syaku.core.common.*;
import com.syaku.core.parser.*;

public class CodeController extends ActionCode {
  private Logger log = Logger.getLogger( this.getClass() );

    private void getCodeException(boolean is) throws Exception {
    if (!is) { throw new Exception("코드아이디는 기존의 코드아이디와 중복될 수없습니다."); }
  }

     public String procCodeInsert() throws Exception {
     try {
      sqlMap.startTransaction();
      String code_orl = param.value("code_orl");
      String code_id= param.value("code_id");
      String module_orl= StringUtils.defaultIfEmpty( param.value("module_orl") , "0" );
      String title = param.value("title");
      String comment = param.value("comment");
      String rdate = DateUtils.date("yyyyMMddHHmmss");

      CodeBean Bean=new CodeBean();
      Bean.setCode_orl(code_orl);
      Bean.setCode_id(code_id);
      Bean.setModule_orl(module_orl);
      Bean.setTitle(title);
      Bean.setComment(comment);
      Bean.setRdate(rdate);

      Map map = new HashMap();
      map.put("code_orl",code_orl);
      map.put("code_id",code_id);

      if (daoCode.sqlCodeCountOverlap(map) > 0) {
        getCodeException(false);
      }

      if( StringUtils.isEmpty(code_orl) ){
         daoCode.sqlCodeInsert(Bean);
      }else{
        daoCode.sqlCodeUpdate(Bean);
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
    
  public String procCodeDelete() throws Exception {
      String code_orl = param.value("code_orl");
     try {
      sqlMap.startTransaction();

       Map mapSch=new HashMap();
      mapSch.put("code_orl",code_orl);
      daoCode.sqlCodeDelete(mapSch);
      daoCode.sqlCodeItemDelete(mapSch);
 
      // 아이템 삭제
      String name = CODE_CACHE_PATH + "/code_list_" + code_orl + ".xml";
      String name_depth = CODE_CACHE_PATH + "/code_" + code_orl+ ".xml";
      File file = new File(name);
      if (file.exists()) { file.delete(); }
      file = new File(name_depth);
      if (file.exists()) { file.delete(); }

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

  public String procCodeItemInsert() throws Exception {
    String code_orl = param.value("code_orl");

    try {
      sqlMap.startTransaction();

      daoCode.getCodeItemInsert(param);
      daoCode.getCodeCacheFile(code_orl); // 캐쉬파일 생성

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

  public String procCodeItemMove() throws Exception {
    String code_orl = param.valueIfEmpty("code_orl","");
    Map mapSch = new HashMap();

    try {
      sqlMap.startTransaction();

      daoCode.getCodeItemMove(param);

      daoCode.getCodeCacheFile(code_orl); // 캐쉬파일 생성

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

  public String procCodeItemDelete() throws Exception {
    String code_orl = param.valueIfEmpty("code_orl");
    String code_item_orl = param.value("code_item_orl");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("code_orl",code_orl);
      mapSch.put("code_item_orl",code_item_orl);

      daoCode.sqlCodeItemDelete(mapSch);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }



}
