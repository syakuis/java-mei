/*
 * MemberAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.member;

import java.util.*;
import java.util.regex.*;
import java.text.*;

import org.apache.log4j.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;
import org.apache.commons.configuration.*;

import com.syaku.core.util.*;

public class MemberObject extends MemberAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration meiConfig = ConfigUtils.getProperties("mei.properties");
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/member/info.properties");

  // 회원 정보 암호화
  public String getMemberEncrypt(String val) {
    String key = "$Y@KU&ME!";
    val = DigestUtils.md5Hex(val);
    val = DigestUtils.md5Hex(key + val);
    return val;
  }
  // 회원등록
  /*
  public String getMemberInsert(ParameterUtils param) throws Exception {
    return getMemberInsert(param,new HashMap());
  }    
  public String getMemberInsert(ParameterUtils param , Map Opt) throws Exception {
    String module_orl = param.value("module_orl");
    if (MapUtils.isNotEmpty(Opt)) {
      module_orl = (String) Opt.get("module_orl");
    }
    String member_orl = param.value("member_orl");
    String user_id = param.value("user_id");
    String user_name = param.value("user_name");
    String nickname = param.value("nickname");
    String password = param.value("password");

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
    String is_admin = param.valueIfEmpty("is_admin","0"); // 최고 관리자 권한 없음
    String rdate = DateUtils.date("yyyyMMddHHmmss");

    MemberBean memberbean = new MemberBean();
    memberbean.setModule_orl(module_orl);
    memberbean.setMember_orl(member_orl);
    memberbean.setUser_id(user_id);
    memberbean.setNickname(nickname);
    memberbean.setUser_name(user_name);
    memberbean.setPassword(password);
    memberbean.setEmail(email);
    memberbean.setEmail_id(email_id);
    memberbean.setEmail_host(email_host);
    memberbean.setHomepage(homepage);
    memberbean.setRdate(rdate);

    if (StringUtils.isEmpty(member_orl)) {
      memberbean.setIs_admin(is_admin);
      memberbean.setState("0");
    }

    return getMemberInsert(memberbean,Opt);
  }
*/
  public String getMemberInsert(MemberBean memberbean) throws Exception {
    return getMemberInsert(memberbean,new HashMap());
  }

  // 회원등록
  public String getMemberInsert(MemberBean memberbean,Map Opt) throws Exception {
    String member_orl = memberbean.getMember_orl();

    String password = memberbean.getPassword();
    memberbean.setPassword(getMemberEncrypt(password));

    if (StringUtils.isEmpty(member_orl)) {
      member_orl = sqlMemberInsert(memberbean);

      // 소속 기본 그룹
      String group_orl = getGroupMemberDefault();
      Map mapSch = new HashMap();
      mapSch.put("member_orl",member_orl);
      mapSch.put("group_orl",group_orl);
      getGroupMemberInsert(mapSch);

    } else {
      sqlMemberUpdate(memberbean);
    }

    return member_orl;
  }
  
  // 회원삭제
  public void getMemberDelete(ParameterUtils param) throws Exception { 
    String member_orl = param.value("member_orl");

    HashMap mapSch = new HashMap();
    mapSch.put("member_orl",member_orl);
    sqlMemberDelete(mapSch);
    sqlGroupMemberDelete(mapSch);
    sqlMemberAuthDelete(mapSch);
  }

  // 기본 그룹
  public String getGroupMemberDefault() throws Exception { 
    String group_orl = "";
    GroupBean groupbean = (GroupBean) sqlGroupDefaultObject();
    if (groupbean != null) { group_orl = groupbean.getGroup_orl(); }
    return group_orl;
  }

  // 회원 그룹등록
  public void getGroupMemberInsert(Map map) throws Exception {
    String member_orl = (String) map.get("member_orl");

    if (StringUtils.isEmpty(member_orl)) { return; }

    Map mapSch = new HashMap();
    mapSch.put("member_orl",member_orl);
    sqlGroupMemberDelete(mapSch);

    String regdate = DateUtils.date("yyyyMMddHHmmss");

    String group_orl_array[] = (String[]) map.get("group_orl_array");
    if (group_orl_array == null) { 

      String group_orl = (String) map.get("group_orl");
      if (StringUtils.isEmpty(group_orl)) { return; }

      GroupMemberBean groupmemberbean = new GroupMemberBean();
      groupmemberbean.setMember_orl(member_orl);
      groupmemberbean.setGroup_orl(group_orl);
      groupmemberbean.setRegdate(regdate);
      sqlGroupMemberInsert(groupmemberbean);
    } else {
      int group_orl_array_cnt = group_orl_array.length;
      if (group_orl_array_cnt == 0) { return; }

      Map mapRet = new HashMap();
      mapRet.put("member_orl",member_orl);
      mapRet.put("group_orl_array",group_orl_array);
      mapRet.put("regdate",regdate);
      sqlGroupMemberArrayInsert(mapRet);
    }

  }

  public boolean getIsLogin(Map session) throws Exception {
      String MEI_USER_ID = (String) session.get("_MEI_USER_ID");
      String MEI_USER_NAME = (String) session.get("_MEI_USER_NAME");
      String MEI_NICKNAME = (String) session.get("_MEI_NICKNAME");
      String _MEI_MEMBER_ORL = (String) session.get("_MEI_MEMBER_ORL");

      if (StringUtils.isEmpty(MEI_USER_ID) || StringUtils.isEmpty(MEI_USER_NAME) || StringUtils.isEmpty(MEI_NICKNAME) || StringUtils.isEmpty(_MEI_MEMBER_ORL)) {
        return false;
      }

      return true;
  }

  public boolean getIsAdmin(String member_orl) throws Exception {
    if (StringUtils.isEmpty(member_orl)) { return false;}

    Map mapSch = new HashMap();
    mapSch.put("member_orl",member_orl);
    MemberBean memberbean = (MemberBean) sqlMemberObject(mapSch);
    if (memberbean == null) { return false; }
    if (StringUtils.equals(memberbean.getIs_admin(),"1")) { return true;}

    return false;
  }

  public void getLoginRemove(Map session) throws Exception {
    session.remove("_MEI_USER_ID");
    session.remove("_MEI_USER_NAME");
    session.remove("_MEI_NICKNAME");
    session.remove("_MEI_MEMBER_ORL");
    session.remove("_MEI_MODULE_ORL");
    session.remove("_MEI_EMAIL");
    session.remove("_MEI_HOMEPAGE");
    session.remove("_MEI_IS_ADMIN");
  }

  public boolean getRegxValidator(String str,String regx) throws Exception {
    Pattern comp = Pattern.compile(regx);
    return comp.matcher(str).find();
  }

  // 중복체크
  public boolean isMemberUserIdOverlapCK(String member_orl,String user_id) throws Exception{
    Map mapSch = new HashMap();
    mapSch.put("member_orl",member_orl);
    mapSch.put("user_id",user_id);
    mapSch.put("overlap_type","user_id");
    if (sqlMemberOverlap(mapSch) == 0) { return true; }
    return false;
  }

  public boolean isMemberNickNameOverlapCK(String member_orl,String nickname) throws Exception{
    Map mapSch = new HashMap();
    mapSch.put("member_orl",member_orl);
    mapSch.put("nickname",nickname);
    mapSch.put("overlap_type","nickname");
    if (sqlMemberOverlap(mapSch) == 0) { return true; }
    return false;
  }

  public boolean isMemberEmailOverlapCK(String member_orl,String email) throws Exception{
    Map mapSch = new HashMap();
    mapSch.put("member_orl",member_orl);
    mapSch.put("email",email);
    mapSch.put("overlap_type","email");
    if (sqlMemberOverlap(mapSch) == 0) { return true; }
    return false;
  }

  // 회원 인증 여부
  public boolean isMemberAuth(String member_orl) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("member_orl", member_orl);
    int auth_last = sqlMemberAuthLast(mapSch);
    if (0 < auth_last) { return false; } else { return true; }
  }

  // 메일인증
  public void getMemberAuthSuccess(Map map) throws Exception {
    String auth_key = (String) map.get("auth_key");
    Map mapSch = new HashMap();
    mapSch.put("auth_key",auth_key);

    MemberAuthBean memberauthbean = (MemberAuthBean) sqlMemberAuthObject(mapSch);
    String member_orl = memberauthbean.getMember_orl();
    String success_date = memberauthbean.getSuccess_date();

    if (StringUtils.isEmpty(success_date)) {

      sqlMemberAuthUpdateSuccessDate(map);

      // 소속 기본 그룹
      String group_orl = getGroupMemberDefault();

      mapSch = new HashMap();
      mapSch.put("member_orl",member_orl);
      mapSch.put("group_orl",group_orl);
      getGroupMemberInsert(mapSch);

    }

  }

  // 회원 탈퇴
  public void getMemberOut(String member_orl) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("member_orl",member_orl);
    mapSch.put("state","-2");
    sqlMemberUpdateState(mapSch);
  }

}