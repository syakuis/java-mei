/*
 * MemberController.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.member;

import java.util.*;
import java.net.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.common.*;
import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class MemberController extends ActionMember {
  private Logger log = Logger.getLogger( this.getClass() );
  private MemberException exception = new MemberException();

  public String procMemberLogin() throws Exception {
    String member_orl = param.value("member_orl");
    String user_id = param.value("user_id");
    String password = param.value("password");
    password = daoMember.getMemberEncrypt(password);

    String referer = param.valueIfEmpty("referer","");
    String ret_url = param.value("ret_url");

    try {
      sqlMap.startTransaction();

      Map mapSch = new HashMap();
      mapSch.put("user_id", user_id);
      MemberBean memberbean = (MemberBean) daoMember.sqlMemberObject(mapSch);
      if (memberbean == null) { memberbean = new MemberBean(); }
      
      String message = "";
      String error = "";

      String mei_user_id = memberbean.getUser_id();
      if (StringUtils.isNotEmpty(mei_user_id)) {
        String mei_member_orl = memberbean.getMember_orl();
        String mei_password = memberbean.getPassword();
        String mei_temp_password = memberbean.getTemp_password(); // 임시 비밀번호

        if (((StringUtils.isNotEmpty(mei_temp_password) && mei_temp_password.equals(password)) || mei_password.equals(password)) && NumberUtils.stringToInt(mei_member_orl) > 0) {
          int state = NumberUtils.stringToInt(memberbean.getState());
          if (0 > state) { throw new Exception("사용 정지이거나, 탈퇴하신 회원이십니다."); }
          
          // 회원 인증절차
          if ( !daoMember.isMemberAuth(mei_member_orl) ) { throw new Exception("회원인증 절차를 거쳐야 로그인 할 수 있습니다."); }

          session.put("_MEI_USER_ID",memberbean.getUser_id());
          session.put("_MEI_USER_NAME",memberbean.getUser_name());
          session.put("_MEI_NICKNAME",memberbean.getNickname());
          session.put("_MEI_MEMBER_ORL",memberbean.getMember_orl());
          session.put("_MEI_MODULE_ORL",memberbean.getModule_orl());
          session.put("_MEI_EMAIL",memberbean.getEmail());
          session.put("_MEI_HOMEPAGE",memberbean.getHomepage());
          session.put("_MEI_IS_ADMIN",memberbean.getIs_admin());

          log.info(StringUtils.join(new String[] {
            "{ProcMemberLoginCheck.intercept}\r\n" , 
            "==========================================================================\r\n" , 
            "Syaku JAVA Library. Powered by Seok Kyun. Choi. http://syaku.tistory.com\r\n" , 
            "> _MEI_USER_ID : " , (String) session.get("_MEI_USER_ID") , "\r\n" , 
            "=========================================================================="
          }));
          
          // 접속시간 업데이트
          mapSch = new HashMap();
          mapSch.put("user_id", user_id);
          mapSch.put("last_login", DateUtils.date("yyyyMMddHHmmss"));

          daoMember.sqlMemberUpdateLastLogin(mapSch);
          
          // 임시 비밀번호로 로그인한 경우 비밀번호 업데이트 후 임시 비밀번호 삭제
          if (StringUtils.isNotEmpty(mei_temp_password) && mei_temp_password.equals(password)) {
            mapSch = new HashMap();
            mapSch.put("member_orl", mei_member_orl);
            mapSch.put("password", mei_temp_password);
            daoMember.sqlMemberUpdateTempPasswordReset(mapSch);
          }

          if (StringUtils.isNotEmpty(ret_url)) {
            MESSAGE.put("error","false");
            MESSAGE.put("action","url");
            MESSAGE.put("source", ret_url);
          } else {
            
            boolean is_login_skip = false;
            List login_skip = modConfig.getList("mei.module.member.login_skip");
            int count = login_skip.size();
            for (int i = 0; i < count; i++ ) {
              String referer_chk = (String) login_skip.get(i);
              if (referer.indexOf(referer_chk) > -1) {
                is_login_skip = true;
                break;
              }
            }

            if (is_login_skip) {
              MESSAGE.put("error","false");
              MESSAGE.put("action","url");
              MESSAGE.put("source", "/" );
            } else {
              MESSAGE.put("error","false");
              MESSAGE.put("action","javascript");
              MESSAGE.put("source","location.reload();");
            }

          }

        } else {
          MESSAGE.put("message","비밀번호가 틀렸습니다.");
          MESSAGE.put("display","alert");
          MESSAGE.put("error","true");
        }
      
      } else {
        MESSAGE.put("message","입력하신 정보가 올바르지 않습니다.");
        MESSAGE.put("display","alert");
        MESSAGE.put("error","true");
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

  public String procMemberLogout() throws Exception {
    daoMember.getLoginRemove(session);
    MESSAGE.put("error","false");

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

  
  public String procMemberInsert() throws Exception {

    try {
      sqlMap.startTransaction();

      MemberInsert memberInsert = new MemberInsert( meiConfig , modConfig , MM , MOD_FTL_MODULE_SKIN );
      memberInsert.prosses(param,false);

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

  // 회원 인증메일 발송 (MemberInsert 중복 메서드)
  public String procMemberAuthMailSend() throws Exception {
    String options_auth_subject = (String) MM.get("options_auth_subject");
    String options_auth_username = (String) MM.get("options_auth_username");
    String options_auth_userid = (String) MM.get("options_auth_userid");

    String user_id = param.value("user_id");
    String email = param.value("email");

    try {

      if ( StringUtils.isEmpty(email) || StringUtils.isEmpty(user_id)) {
        throw new Exception("입력 값을 모두 입력하세요.");
      }

      Map mapSch = new HashMap();
      mapSch.put("user_id",user_id);
      mapSch.put("email",email);

      MemberBean obj = (MemberBean) daoMember.sqlMemberObject(mapSch);

      if (obj != null) { 
        String user_name = obj.getUser_name();
        String member_orl = obj.getMember_orl();

        mapSch = new HashMap();
        mapSch.put("member_orl",member_orl);

        MemberAuthBean objMAuth = (MemberAuthBean) daoMember.sqlMemberAuthObject(mapSch);
        String auth_key = objMAuth.getAuth_key();
        String success_date = objMAuth.getSuccess_date();
        if (StringUtils.isNotEmpty(success_date)) {

          MESSAGE.put("message","이미 회원인증을 완료하셨습니다.");
          MESSAGE.put("error","true");

        } else if (StringUtils.isEmpty(auth_key)) {

          MESSAGE.put("message","인증키를 찾을 수 없습니다.");
          MESSAGE.put("error","true");

        } else {

          MESSAGE.put("message","인증키를 " + email + " 메일로 발송하였습니다.");
          MESSAGE.put("error","false");
          
          // 템플릿 생성
          String subject = options_auth_subject;
          Map mapData = new HashMap();
          mapData.put("subject" , subject);
          mapData.put("user_id" , user_id);
          mapData.put("user_name" , user_name);
          mapData.put("url" , meiConfig.getString("mei.host.URL") + "/dispMemberAuthView.me?mid=" + MID + "&auth=" + URLEncoder.encode(auth_key, "utf-8"));
          mapData.put("MM" , MM );
          mapData.put("meiConfig" , meiConfig);

          // 메일발송
          MailSend email_auth = new MailSend();
          email_auth.setFrom(options_auth_userid);
          email_auth.setFrom_name(options_auth_username);
          email_auth.setTo(email);
          email_auth.setTo_name(user_name);
          email_auth.setSubject(subject);
          email_auth.setTemplate( MEI_PATH_ABSOLUTE_RELATIVE + MOD_FTL_MODULE_SKIN , "member.join.mail.form.html" , mapData );
          email_auth.send();

        }

      } else {
        MESSAGE.put("message","정보를 찾을 수 없습니다.");
        MESSAGE.put("error","true");
      }

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("display","alert");
      MESSAGE.put("message","메일발송실패하였습니다. : " + e.getMessage().replaceAll("\n",""));
      MESSAGE.put("error","true");
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

  // 회원 비밀번호 조회
  public String procMemberPasswordSearch() throws Exception {
    // 메일SMTP 서버 여부
    boolean mail_smtp_enable = meiConfig.getBoolean("mei.mail.smtp.enable");

    String user_id = param.value("user_id");
    String user_name = param.value("user_name");
    String email = param.value("email");

    try {
      sqlMap.startTransaction();

      if (StringUtils.isEmpty(user_name) || StringUtils.isEmpty(email) || StringUtils.isEmpty(user_id) ) {
        throw new Exception("입력 값을 모두 입력하세요.");
      }

      Map mapSch = new HashMap();
      mapSch.put("user_id",user_id);
      mapSch.put("user_name",user_name);
      mapSch.put("email",email);

      MemberBean obj = (MemberBean) daoMember.sqlMemberObject(mapSch);
      if (obj != null) { 
        String member_orl = obj.getMember_orl();
        String password = obj.getPassword();

        // 새로운 암호 생성 후 발송
        String temp_password = CountString.getRand(10); // 10자리의 랜덤 숫자 호출
        
        if (mail_smtp_enable) {
          String options_auth_subject = (String) MM.get("options_auth_subject");
          String options_auth_username = (String) MM.get("options_auth_username");
          String options_auth_userid = (String) MM.get("options_auth_userid");

          // 템플릿 생성
          String subject = "임시 비밀번호를 알려드립니다.";
          Map mapData = new HashMap();
          mapData.put("objMember" , obj);
          mapData.put("temp_password" , temp_password);
          mapData.put("subject" , subject);
          mapData.put("MM" , MM );
          mapData.put("meiConfig" , meiConfig);

          // 메일발송
          MailSend email_auth = new MailSend();
          email_auth.setFrom(options_auth_userid);
          email_auth.setFrom_name(options_auth_username);
          email_auth.setTo(email);
          email_auth.setTo_name(user_name);
          email_auth.setSubject(subject);
          email_auth.setTemplate( MEI_PATH_ABSOLUTE_RELATIVE + MOD_FTL_MODULE_SKIN , "member.password.mail.form.html" , mapData );
          email_auth.send();

          MESSAGE.put("message","임시 비밀번호를 메일로 발송하였습니다.");
          MESSAGE.put("error","false");

        } else {
          MESSAGE.put("message","임시 비밀번호는 " + temp_password + " 입니다.");
          MESSAGE.put("error","false");
        }

        mapSch = new HashMap();
        mapSch.put("member_orl",member_orl);
        temp_password= daoMember.getMemberEncrypt(temp_password);
        mapSch.put("temp_password",temp_password);
        daoMember.sqlMemberUpdateTempPassword(mapSch);

      } else {
        MESSAGE.put("message","찾을 수 없습니다.");
        MESSAGE.put("error","true");
      }

      sqlMap.commitTransaction();

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("display","alert");
      MESSAGE.put("message","회원 비밀번호 조회 과정에 오류가 발생했습니다. : " + e.getMessage().replaceAll("\n",""));
      MESSAGE.put("error","true");
    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }


  // 회원 아이디 조회
  public String procMemberUserIdSearch() throws Exception {
    String user_name = param.value("user_name");
    String email = param.value("email");

    if (StringUtils.isNotEmpty(user_name) && StringUtils.isNotEmpty(email) ) {
      Map mapSch = new HashMap();
      mapSch.put("user_name",user_name);
      mapSch.put("email",email);

     MemberBean obj = (MemberBean) daoMember.sqlMemberObject(mapSch);
      if (obj != null) { 
        String user_id = obj.getUser_id();
        MESSAGE.put("message",user_id);
        MESSAGE.put("error","false");
      } else {
        MESSAGE.put("message","찾을 수 없습니다.");
        MESSAGE.put("error","true");
      }

    } else {
      MESSAGE.put("message","찾을 수 없습니다.");
      MESSAGE.put("error","true");
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";
  }

  // 비밀번호 변경
  public String procMemberPasswordUpdate() throws Exception {
    String member_orl = param.value("member_orl");
    String password = param.value("password");
    password= daoMember.getMemberEncrypt(password);
   
    String password1 = param.value("password1");
    password1= daoMember.getMemberEncrypt(password1);

    try {
      sqlMap.startTransaction();

      HashMap mapSch = new HashMap();
      mapSch.put("member_orl",member_orl);

      MemberBean obj = (MemberBean) daoMember.sqlMemberObject(mapSch);
      if (obj == null) { throw new Exception("회원정보가 일치하지 않습니다."); }
      String password2 = obj.getPassword();
      if (!StringUtils.equals(password,password2)) { throw new Exception("비밀번호가 일치하지 않습니다."); }
      mapSch.put("password",password1);
      daoMember.sqlMemberUpdatePassword(mapSch);

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

}