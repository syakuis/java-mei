/*
 * MemberInsert.java 2011.01.01
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
import org.apache.commons.collections.*;
import org.apache.commons.configuration.*;

import com.syaku.core.common.*;
import com.syaku.core.util.*;

public class MemberInsert {
  private Logger log = Logger.getLogger( this.getClass() );
  private Configuration meiConfig, modConfig;
  private Map MM = new HashMap();

  private String MEI_PATH_ABSOLUTE_RELATIVE,MOD_FTL_MODULE_SKIN;
  private final String EMAIL_JOIN_FORM_FTL = "member.join.mail.form.html";

  private String[] group_orl_array;
  private boolean insert = false; // 회원 가입인 경우 true
  private boolean admin = false; // 관리자 여부

  private MemberObject daoMember = new MemberObject();
  private MemberBean beanMember = new MemberBean();

  public MemberInsert(Map MM,String skin) {
    this.meiConfig = ConfigUtils.getProperties("mei.properties");
    this.modConfig = ConfigUtils.getProperties("com/syaku/modules/member/info.properties");
    this.MM = MM;
    this.MOD_FTL_MODULE_SKIN = skin;
    init();
  }

  public MemberInsert(Configuration modConfig, Map MM,String skin) {
    this.meiConfig = ConfigUtils.getProperties("mei.properties");
    this.modConfig = modConfig;
    this.MM = MM;
    this.MOD_FTL_MODULE_SKIN = skin;
    init();
  }

  public MemberInsert(Configuration meiConfig, Configuration modConfig, Map MM,String skin) {
    this.meiConfig = meiConfig;
    this.modConfig = modConfig;
    this.MM = MM;
    this.MOD_FTL_MODULE_SKIN = skin;
    init();
  }

  private void init() {
    MEI_PATH_ABSOLUTE_RELATIVE = meiConfig.getString("mei.path.absolute_relative");
  }

  public void setParam(ParameterUtils param) throws Exception {
    String module_orl = param.value("module_orl");
    if (MapUtils.isNotEmpty(MM)) { module_orl = StringUtils.defaultIfEmpty( module_orl , (String) MM.get("module_orl") ); }

    // 항목 사용여부
    String options_field_nickname = (String) MM.get("options_field_nickname");

    String member_orl = param.value("member_orl");
    String user_name = param.value("user_name");
    String nickname = param.value("nickname");
    if ( StringUtils.equals( options_field_nickname , "N" ) ) {
      nickname = user_name;
    }

    String email = param.value("email");
    if ( StringUtils.equals(email,"@") ) { email = null; }
    String email_id = param.value("email_id");
    String email_host = param.value("email_host");
    if ( StringUtils.isNotEmpty(email) && ( StringUtils.isEmpty(email_id) || StringUtils.isEmpty(email_host) ) ) {
      String email_div[] = StringUtils.split(email,'@');
      email_id = StringUtils.defaultString( email_div[0] , null );
      email_host = StringUtils.defaultString( email_div[1] , null );
    }

    String homepage = param.value("homepage");
    String is_admin = "0";

    if (StringUtils.isEmpty(member_orl)) {
      this.insert = true;

      String user_id = param.value("user_id");

      String password = param.value("password");
      // 비밀번호 암호화
      beanMember.setPassword( daoMember.getMemberEncrypt(password) );

      String rdate = DateUtils.date("yyyyMMddHHmmss");


      beanMember.setUser_id(user_id);
      beanMember.setIs_admin(is_admin);
      beanMember.setState("0");
      beanMember.setRdate(rdate);
    }

    beanMember.setModule_orl(module_orl);
    beanMember.setMember_orl(member_orl);
    beanMember.setNickname(nickname);
    beanMember.setUser_name(user_name);
    beanMember.setEmail(email);
    beanMember.setEmail_id(email_id);
    beanMember.setEmail_host(email_host);
    beanMember.setHomepage(homepage);

  }

  public void setParamAdmin(ParameterUtils param) throws Exception {
    setParam(param);

    String is_admin = param.valueIfEmpty("is_admin","0");
    this.group_orl_array = param.values("group_orl");

    beanMember.setIs_admin(is_admin);
  }

  // 회원등록
  public String insert() throws Exception {
    String member_orl = beanMember.getMember_orl();

    // 회원 가입 인 경우
    if (insert) {
      member_orl = daoMember.sqlMemberInsert(beanMember);
    } else {
      daoMember.sqlMemberUpdate(beanMember);
    }

    return member_orl;
  }

  public void check() throws Exception {
    MemberException exception = new MemberException();

    // 항목 사용여부
    String options_field_nickname = (String) MM.get("options_field_nickname");

    // 설정된 정규식 패턴
    String user_id_regx = (String) MM.get("options_user_id_regx");
    String user_name_regx = (String) MM.get("options_user_name_regx");
    String nickname_regx = (String) MM.get("options_nickname_regx");
    String email_regx = modConfig.getString("mei.options.options_email_regx");

    // 회원 인증 방식
    String options_auth_field = (String) MM.get("options_auth_field");
    boolean options_auth_email = StringUtils.indexOf(options_auth_field,"email") > -1;
    boolean options_auth_sns = StringUtils.indexOf(options_auth_field,"sns") > -1;

    // 중복 체크
    String options_overlap_nickname = StringUtils.defaultIfEmpty( (String) MM.get("options_overlap_nickname") , "N");
    String options_overlap_email = StringUtils.defaultIfEmpty( (String) MM.get("options_overlap_email") , "N");

    String member_orl = beanMember.getMember_orl();
    String user_id = beanMember.getUser_id();
    String user_name = beanMember.getUser_name();
    String nickname = beanMember.getNickname();
    String email = beanMember.getEmail();

    // 회원 가입인 경우 중복아이디 체크
    if (insert) {
      exception.getUserIdOverlap(member_orl,user_id,user_id_regx); // 중복아이디 체크
      exception.getUserNameValidator(user_name,user_name_regx); // 이름 체크
    }

    // 중복 닉네임체크
    if ( StringUtils.equals(options_field_nickname,"Y") ) {

    if ( StringUtils.equals(options_overlap_nickname,"Y") ) {
      exception.getNickNameOverlap(member_orl,nickname,nickname_regx);
    } else {
      exception.getNickNameValidator(nickname,nickname_regx); // 닉네임체크
    }

    }

    
    // 중복 이메일체크
    if ( StringUtils.equals(options_overlap_email,"Y") || options_auth_email ) {
      exception.getEmailOverlap(member_orl,email,email_regx);
    } else {
      exception.getEmailValidator(email,email_regx); // 닉네임체크
    }

  }

  public void group(String member_orl) throws Exception {
    Map mapSch = new HashMap();
    
    // 그룹 선택이 없을 경우 기본 그룹으로 저장
    if ( ArrayUtils.isEmpty(group_orl_array) ) {

      // 회원 가입인 경우에만 기본 그룹을 저장한다.
      if ( insert ) {
        mapSch.put("member_orl",member_orl);
        mapSch.put("group_orl_array",group_orl_array);
        daoMember.getGroupMemberInsert(mapSch);
      }

    } else {

      if (this.admin) {

      mapSch.put("member_orl",member_orl);
      mapSch.put("group_orl_array",group_orl_array);
      daoMember.getGroupMemberInsert(mapSch);

      }

    }

  }

  // 메일 회원 인증
  public void auth_email() throws Exception {
    if (insert && this.admin == false) {

    String member_orl = beanMember.getMember_orl();
    String user_id = beanMember.getUser_id();
    String user_name = beanMember.getUser_name();
    String email = beanMember.getEmail();

    // 회원인증방식
    String options_auth_field = (String) MM.get("options_auth_field");
    boolean options_auth_email = StringUtils.indexOf(options_auth_field,"email") > -1;
    boolean options_auth_sns = StringUtils.indexOf(options_auth_field,"sns") > -1;
    String options_auth_subject = (String) MM.get("options_auth_subject");
    String options_auth_username = (String) MM.get("options_auth_username");
    String options_auth_userid = (String) MM.get("options_auth_userid");

    // 회원가입 인증방식 설정
    String rdate = DateUtils.date("yyyyMMddHHmmss");

    // 메일인증
    if (options_auth_email) {
      String temp_key = (String) Crypto.encrypt("$y@ku" + Crypto.encrypt(user_id + "$%#$%@#$^&%&^" + rdate + email + "%@#$^&%&^SYAKU") );
      MemberAuthBean memberauthbean = new MemberAuthBean();
      memberauthbean.setMember_orl(member_orl);
      memberauthbean.setAuth_key(temp_key);
      memberauthbean.setRegdate(rdate);
      daoMember.sqlMemberAuthInsert(memberauthbean);

      Map mapData = new HashMap();
      mapData.put("user_id" , user_id);
      mapData.put("user_name" , user_name);
      mapData.put("url" , meiConfig.getString("mei.host.URL") + "/dispMemberAuthView.me?mid=" + MM.get("mid") + "&auth=" + URLEncoder.encode(temp_key, "utf-8"));
      mapData.put("MM" , MM );
      mapData.put("meiConfig" , meiConfig);

      MailSend email_auth = new MailSend();
      email_auth.setFrom(options_auth_userid);
      email_auth.setFrom_name(options_auth_username);
      email_auth.setTo(email);
      email_auth.setTo_name(user_name);
      email_auth.setSubject(options_auth_subject);
      email_auth.setTemplate( MEI_PATH_ABSOLUTE_RELATIVE + MOD_FTL_MODULE_SKIN , EMAIL_JOIN_FORM_FTL , mapData );
      email_auth.send();
    }

    }

  }

  // 관리자 호출인 경우 admin = true
  public String prosses(ParameterUtils param,boolean admin) throws Exception {
    this.admin = admin;
    String member_orl = beanMember.getMember_orl();
    setParam(param);
    if (admin) {
      setParamAdmin(param);
    }

    check(); // 입력값 체크
    member_orl = insert(); // 저장
    group(member_orl); // 그룹 저장

    // 회원 가입 인 경우
    auth_email();

    return member_orl;
  }

}