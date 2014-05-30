package com.syaku.modules.note;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;
import com.syaku.modules.module.*;

public class NoteController extends ActionNote {
  private Logger log = Logger.getLogger( this.getClass() );
  private NoteObject daoNote = new NoteObject();

  public String procNoteSendInsert() throws Exception {

    String note_orl = param.value("note_orl");
    String member_orl = (String) session.get("_MEI_MEMBER_ORL");
    String user_id = (String) session.get("_MEI_USER_ID");
    String user_name = (String) session.get("_MEI_USER_NAME");
    String nickname = (String) session.get("_MEI_NICKNAME");
    String rec_member_orl = param.value("rec_member_orl");
    String rec_user_id = param.value("rec_user_id");
    String rec_user_name = param.value("rec_user_name");
    String rec_nickname = param.value("rec_nickname");
    String title = param.value("title");
    String content = param.value("content");
    String send_date = DateUtils.date("yyyyMMddHHmmss");
    String readed_date = DateUtils.date("yyyyMMddHHmmss");
    String ipaddress = ipaddress = request.getRemoteAddr();

    try {
      sqlMap.startTransaction();
      NoteBean bean= new NoteBean();

      bean.setNote_orl(note_orl);
      bean.setMember_orl(member_orl);
      bean.setUser_id(user_id);
      bean.setUser_name(user_name);
      bean.setNickname(nickname);
      bean.setRec_member_orl(rec_member_orl);
      bean.setRec_user_id(rec_user_id);
      bean.setRec_user_name(rec_user_name);
      bean.setRec_nickname(rec_nickname);
      bean.setTitle(title);
      bean.setContent(content);
      bean.setSend_date(send_date);
      bean.setReaded_date(readed_date);
      bean.setIpaddress(ipaddress);

      daoNote.sqlNoteInsert(bean);

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



  public String procNoteDelete() throws Exception {
    try {
        sqlMap.startTransaction();

        String[] note_orlArr = request.getParameterValues("note_orl");
        if( note_orlArr != null && note_orlArr.length>0){
          for(int i=0 ; i<note_orlArr.length ; i++){ 

          Map map = new HashMap();
          map.put("note_orl",note_orlArr[i]);
          daoNote.sqlNoteDelete(map);

          }
        }
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