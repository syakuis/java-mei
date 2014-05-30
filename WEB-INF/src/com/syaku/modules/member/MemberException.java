/*
 * MemberException.java 2011.08.05
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.member;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class MemberException {
  private Logger log = Logger.getLogger( this.getClass() );
  private MemberObject daoMember = new MemberObject();

  public void getGrant(boolean grant) throws Exception {
    if (!grant) { throw new Exception("권한이 없습니다."); }
  }

  public void getUserIdValidator(String str,String regx) throws Exception {
    if (!daoMember.getRegxValidator(str,regx)) { throw new Exception("아이디를 올바르게 입력하세요."); }
  }

  public void getUserNameValidator(String str,String regx) throws Exception {
    if (!daoMember.getRegxValidator(str,regx)) { throw new Exception("이름을 올바르게 입력하세요."); }
  }

  public void getNickNameValidator(String str,String regx) throws Exception {
    if (!daoMember.getRegxValidator(str,regx)) { throw new Exception("닉네임을 올바르게 입력하세요."); }
  }

  public void getEmailValidator(String str,String regx) throws Exception {
    if (!daoMember.getRegxValidator(str,regx)) { throw new Exception("메일주소를 올바르게 입력하세요."); }
  }

  public void getUserIdOverlap(String member_orl,String user_id,String regx) throws Exception {
    getUserIdValidator(user_id,regx);
    if (!daoMember.isMemberUserIdOverlapCK(member_orl,user_id)) { throw new Exception("사용할 수 없는 아이디입니다. 다른 아이디를 입력하세요."); }
  }

  public void getNickNameOverlap(String member_orl,String nickname,String regx) throws Exception {
    getNickNameValidator(nickname,regx);
    if (!daoMember.isMemberNickNameOverlapCK(member_orl,nickname)) { throw new Exception("사용할 수 없는 닉네임입니다. 다른 닉네임를 입력하세요."); }
  }

  public void getEmailOverlap(String member_orl,String email,String regx) throws Exception {
    getEmailValidator(email,regx);
    if (!daoMember.isMemberEmailOverlapCK(member_orl,email)) { throw new Exception("사용할 수 없는 메일입니다. 다른 메일을 입력하세요."); }
  }

}