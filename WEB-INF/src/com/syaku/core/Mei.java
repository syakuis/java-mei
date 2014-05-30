/*
 * Mei.java 2011.03.15
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html


 모듈유틸생성중
 */
package com.syaku.core;

import java.util.*;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.commons.configuration.*;
import org.apache.commons.lang.*;
import org.apache.commons.collections.*;

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.Parameterizable;

import org.apache.struts2.ServletActionContext;

import com.ibatis.sqlmap.client.SqlMapClient;

import net.sf.json.JSONObject;

import com.syaku.core.common.*;
import com.syaku.core.util.*;

import com.syaku.modules.module.*;
import com.syaku.modules.layout.*;
import com.syaku.modules.menu.*;
import com.syaku.modules.code.*;
import com.syaku.modules.file.*;
import com.syaku.modules.category.*;

public abstract class Mei extends ActionSupport implements Parameterizable, Preparable , ModelDriven {
  private Logger log = Logger.getLogger( this.getClass() );

  public ModuleObject daoModule = new ModuleObject();
  public FileObject daoFile = new FileObject();
  public CodeObject daoCode = new CodeObject();
  public CategoryObject daoCategory = new CategoryObject();
  public LayoutObject daoLayout = new LayoutObject();
  public MenuObject daoMenu = new MenuObject();

  public FnUtils fnCls = new FnUtils();

  /* Struts2 Parameterizable */
  public Map params;
  public void setParams(Map params) { this.params = params; }
  public void addParam(String name, String value) { }
  public Map getParams() { return this.params; }
  /* Struts2 Parameterizable */

  public Map parameters;

  public HttpServletRequest request = ServletActionContext.getRequest();
  public HttpServletResponse response = ServletActionContext.getResponse();
  public HttpSession httpsession = request.getSession();
  public Map session = (Map) ServletActionContext.getContext().getSession();
  public ValueStack valuestack; // [참고] 밸류스택을 추가할 경우 상위 자료를 모두 리셋됨.
  public Map stack = new HashMap();

  public static final SqlMapClient sqlMap = MyBatis.getInstance();

  public ParameterUtils param;
  public PageNavigator paging;
  public Configuration meiConfig,meiCollection,meiResConfing;
  public String MEI_PATH_ABSOLUTE;
  public String MEI_PATH_RELATIVE;
  public String MEI_PATH_ABSOLUTE_RELATIVE;
  public String MEI_LAYOUT_PATH_RELATIVE; // 레이아웃 절대경로
  public String MEI_PATH_ATTACH,MEI_PATH_CACHE,MEI_HOST_URL;
  public String MEI_PATH_ADDONS,MEI_PATH_RESOURCE;

  /* Struts2 Action */
  public String ACT_NAME;
  public String ACT_PERMISSION;
  public String ACT_TYPE;
  public String ACT_TEMPLATE;
  /* Struts2 Action */

  /* module */
  public String MOD_NAME; // 모듈 명

  public Configuration modConfig; // 모듈 config

  public String MOD_FTL_LAYOUT; // 기본 레이아웃 템플릿 경로 (최상위 템플릿)
  public String MOD_FTL_MODULE_LAYOUT; // 모듈 레이아웃 경로 
  public String MOD_FTL_MODULE_SKIN; // 모듈 스킨 경로
  public String MOD_FTL_MODULE; // 모듈 스킨 템플릿 경로
  public boolean MOD_FTL_MODULE_LAYOUT_HIDE = false; // 템플릿 레이아웃을 호출하지만 레이아웃 틀은 제거한다. 즉, 자바스크립트 나 스타일시트 그리고 변수들 자료만 가져와서 사용할 수 있게 한다.

  public void setMod_ftl_module(String ftl) {
    this.MOD_FTL_MODULE = ftl;
    M.put("MOD_FTL_MODULE",MOD_FTL_MODULE);
    log.debug("[#MEI Mei.setMod_ftl_module : MOD_FTL_MODULE ->" + MOD_FTL_MODULE);
  }

  public String MOD_PATH_RELATIVE;
  public String MOD_PROPERTIES;
  public boolean MOD_SINGLE;
  public String MOD_MODULE_ORL; // 싱글 인 경우 MOD_NAME 으로 구해지는 module_orl

  private String PARAM_MID; // 파라메터로 받은 mid
  private String PARAM_MODULE_ORL; // 파라메터로 받은 module_orl
  public String MID;
  public String MID_MODULE_ORL;
  public String MODULE_ORL;
  public String S_MODULE_ORL; // 싱글 모듈의 번호
  public String INTERLOCK; // 모듈 연동 여부

  /* module */

  /* 모듈 설정 정보 */
  /*
  mid 파라메터로 받은 값의 모듈정보는 M 에 저장되며 동시에 MM 에도 저장된다.
  module_orl 파라메터로 받은 값의 모듈정보는 MM 에 엎어쓰게 됨.
  싱글로 설정된 모듈정보는 MOD 에 저장됨.
  */
  public Map M = new HashMap(); // mid 디비 정보
  public Map MM = new HashMap(); // module_orl 디비 정보
  public Map S = new HashMap(); // 싱글 모듈 디비 정보
  public Map MESSAGE = new HashMap(); // 메세지
  /* 모듈 설정 정보 */

  /* 자료 */
  public Map P = new HashMap(); // 파라메터
  public Map O = new HashMap(); // 변수
  /* 자료 */

  /* layout */
  public Map layoutConfig;
  public Map LAYOUT = new HashMap(); // 레이아웃 디비 정보
  public String LAYOUT_PATH_RELATIVE; // 선택된 레이아웃 경로
  public String LAYOUT_PROPERTIES; // 선택된 레이아웃 정보 경로
  /* layout */

  /* menu */
  public Map MENU = new HashMap(); // 메뉴 디비 정보
  /* menu */

  /* 권한 */
  public boolean MEI_GRANT_LOGIN = false;
  public boolean MEI_GRANT_ADMIN = false;
  public boolean MEI_GRANT_MINE = false;
  public boolean MEI_GRANT_ACCESS = false;
  public boolean MEI_GRANT_LIST = false;
  public boolean MEI_GRANT_VIEW = false;
  public boolean MEI_GRANT_WRITE = false;
  public boolean MEI_GRANT_COMMENT = false;
  public boolean MEI_GRANT_MANAGER = false;
  /* 권한 */

  public InputStream inputStream; // io 스트림

  public String MEI_SID;

  public List MEI_ADDONS = new ArrayList();

  // mei initialization
  public void meiInit() {
    try { MEI_SID = (String) Crypto.encrypt( httpsession.getId() ); } catch (Exception e) { log.error( e.toString() ); }
    param = new ParameterUtils(request); // 파라메터 제어
    P = (Map) param.valuesMap();

    MESSAGE.put("message","");
    MESSAGE.put("display","");
    MESSAGE.put("error","false");
    MESSAGE.put("action","");
    MESSAGE.put("source","");
  }

  // mei 최초 실행
  public void meiPrepare() {
    meiConfig = ConfigUtils.getProperties("mei.properties"); // mei 정보
    
    // 세션 유지시간
    httpsession.setMaxInactiveInterval( meiConfig.getInt("mei.session.life_time") );

    MEI_PATH_ABSOLUTE = meiConfig.getString("mei.path.absolute"); // 절대경로
    MEI_PATH_RELATIVE = meiConfig.getString("mei.path.relative"); // 상대경로
    MEI_PATH_ABSOLUTE_RELATIVE = meiConfig.getString("mei.path.absolute_relative");
    MEI_LAYOUT_PATH_RELATIVE = MEI_PATH_ABSOLUTE_RELATIVE + meiConfig.getString("mei.path.layouts");

    MEI_PATH_ATTACH = meiConfig.getString("mei.path.attach"); // 파일첨부
    MEI_PATH_CACHE = meiConfig.getString("mei.path.cache"); // 캐쉬
    MEI_PATH_ADDONS = meiConfig.getString("mei.path.addons"); // 애드온
    MEI_PATH_RESOURCE = meiConfig.getString("mei.path.resource");

    MEI_HOST_URL = meiConfig.getString("mei.host.URL");

    meiCollection = ConfigUtils.getProperties("mei.collection.properties"); // mei 정리
    meiResConfing = ConfigUtils.getProperties( meiConfig.getString("mei.path.resource") + "/mei.config.properties" );

    OnLoad ol = new OnLoad(meiCollection);

    ActionContext context = ActionContext.getContext();
    ActionInvocation invocation = context.getActionInvocation();
    ActionProxy proxy = invocation.getProxy();
    ActionConfig config = proxy.getConfig();
    valuestack = context.getValueStack();
    parameters = context.getParameters();
    
    param.setParameters(parameters); // 스트럿츠 파라메터 MEI util 셋팅

    String package_name = config.getPackageName();
    MOD_NAME = package_name;
    String action_name = context.getName();
    ACT_NAME = action_name;
    MOD_PROPERTIES = "com/syaku/modules/" + MOD_NAME + "/info.properties";

    MOD_FTL_LAYOUT = meiConfig.getString("mei.template.layout.default");
    if (MapUtils.isNotEmpty(params)) {
      ACT_PERMISSION = (String) params.get("act_permission");
      ACT_TYPE = (String) params.get("act_type");
      ACT_TEMPLATE = (String) params.get("act_template");
      INTERLOCK = StringUtils.defaultIfEmpty( (String) params.get("INTERLOCK") , "false" );
    }

    meiLogShow( "" , StringUtils.join(new String[] {
      "> module name : " , MOD_NAME , "\r\n" ,
      "> module properties : " , MOD_PROPERTIES , "\r\n" ,
      "> action name : " , ACT_NAME , "\r\n" ,
      "> action permission : " , ACT_PERMISSION , "\r\n" ,
      "> action type : " , ACT_TYPE , "\r\n" ,
      "> action template : " , ACT_TEMPLATE , "\r\n" ,
      "> action params : " , params.toString() , "\r\n" ,
      "> action parameters : " , parameters.toString() , "\r\n" 
    }) );

  }

  // 모듈 프로퍼티
  public void modProperties() {
    modConfig = ConfigUtils.getProperties(MOD_PROPERTIES);
    MOD_PATH_RELATIVE = MEI_PATH_RELATIVE + modConfig.getString("mei.module.path.relative");
    MOD_SINGLE = modConfig.getBoolean("mei.module.single"); // 모듈이 멀팅형인지 싱글형인지 구분
  }

  private Map getModInfo(Map mapSch , String mid) {
    if ( StringUtils.isEmpty(mid) ) { return null; }
    String resource = MEI_PATH_RESOURCE + "/mei.module." + mid + ".properties";
    Map mapRet = daoModule.objectProp(resource);

    if (MapUtils.isEmpty(mapRet)) {
      mapRet = daoModule.objectOne(mapSch);
    }

    return mapRet;
  }

  // 모듈 정보
  public void modInfo() {
    PARAM_MID = param.value("mid");
    PARAM_MODULE_ORL = param.value("module_orl");
    modInfo(PARAM_MID,PARAM_MODULE_ORL);
  }
  public void modInfo(String mid , String m_orl) { // 메서드를 호출할 경우 mid, m_orl 값을 받는 다.
    /**
    다양한 변수 처리 순서

    mid=
    mid=&module=
    mid=&module_orl=
    mid=&module_orl=&module=

    우선 순위 1) mid 존재여부 2) module_orl 존재여부 3) single 여부
    1 = M , 2or3 = MM , SINGLE MODULE = S
    */

    Map mapSch = new HashMap();
    MID = mid;
    MODULE_ORL = m_orl;
//    INTERLOCK = param.value("interlock","false");

    INTERLOCK = param.arrayValue("interlock" , INTERLOCK);
    if ( StringUtils.equals(INTERLOCK,"true") ) {
      MID = param.arrayValue("mid");
      MODULE_ORL = param.arrayValue("module_orl");
    }

    String module_name = "";

    if ( StringUtils.isNotEmpty(MID) ) {
      mapSch = new HashMap();
      mapSch.put("mid",PARAM_MID);
      M = getModInfo(mapSch , PARAM_MID);
      //M = (Map) daoModule.objectOne(mapSch);

      MID_MODULE_ORL = (String) M.get("module_orl");
      module_name = (String) M.get("module");
      MM = M;

      // 싱글 모듈인 경우
      if ( MOD_SINGLE ) { // 싱글 모듈은 프로퍼티와 모듈명이 같아야한다.

        mapSch = new HashMap();
        mapSch.put("module",MOD_NAME);
        S = getModInfo(mapSch , MOD_NAME);
        //S = (Map) daoModule.objectOne(mapSch);
        S_MODULE_ORL = (String) S.get("module_orl");
      }

      // module_orl 존재 할 경우
      if ( StringUtils.isNotEmpty(MODULE_ORL) ) {
        
        // mid 와 module_orl 같지 않을 경우
        if (!StringUtils.equals(MODULE_ORL,MID_MODULE_ORL) ) {
          mapSch = new HashMap();
          mapSch.put("module_orl",MODULE_ORL);
          String module_id = daoModule.mid(mapSch); // mid
          MM = getModInfo(mapSch , module_id);
          //MM = (Map) daoModule.objectOne(mapSch);
          MODULE_ORL = (String) MM.get("module_orl");
        } else { MODULE_ORL = MID_MODULE_ORL; }

      } else {

        // 싱글 모듈 인 경우
        if (MOD_SINGLE && !StringUtils.equals(module_name,MOD_NAME) && StringUtils.equals(INTERLOCK , "false") ) {
          MM = S;
          MODULE_ORL = S_MODULE_ORL;
        } else {
        
        // 멀티 모듈 : 모듈 추가 인 경우
        if (!MOD_SINGLE && !StringUtils.equals(module_name,MOD_NAME) && StringUtils.equals(INTERLOCK , "false") ) {
          MM = new HashMap();
          MODULE_ORL = null;
        } else { // MID 모듈인 경우
          MODULE_ORL = MID_MODULE_ORL;
        }
        
        }

      }

    }

/*
    if (MOD_SINGLE) {
      mapSch = new HashMap();
      mapSch.put("module",MOD_NAME);
      MM = (Map) daoModule.objectOne(mapSch);
      MOD_MODULE_ORL = (String) MM.get("module_orl");
    } else {

      if ( StringUtils.isNotEmpty(PARAM_MODULE_ORL) ) {
        mapSch = new HashMap();
        mapSch.put("module_orl",PARAM_MODULE_ORL);
        MM = (Map) daoModule.objectOne(mapSch);
      }

    }
*/
    // 벨류스택에 담아서 공유함.
//    Map stack = new HashMap();
    stack.put("PARAM_MID",PARAM_MID);
    stack.put("MID_MODULE_ORL",MID_MODULE_ORL);
    meiLogShow("MEI Mei.modInfo" , StringUtils.join(new String[] {
      "> P : " , P.toString() , "\r\n" , 
      "> PARAM_MID : " , PARAM_MID , "\r\n" , 
      "> PARAM_MODULE_ORL : " , PARAM_MODULE_ORL , "\r\n" , 
      "> MID : " , MID , "\r\n" , 
      "> MID_MODULE_ORL : " , MID_MODULE_ORL , "\r\n" , 
      "> MODULE_ORL : " , MODULE_ORL , "\r\n" , 
      "> MOD_NAME : " , MOD_NAME , "\r\n" , 
      "> M : " + M , "\r\n" , 
      "> MM : " + MM , "\r\n" , 
      "> S : " + S , "\r\n" , 
      "> INTERLOCK : " + INTERLOCK , "\r\n" , 
      "> MOD_SINGLE : " + MOD_SINGLE , "\r\n" 
    }));

  }

  // 모듈 스킨경로
  public void modSkin() {
    String skin = (String) M.get("skin");

    // 관리자가 아닌 경우 MM 스킨 사용
    if ( !StringUtils.equals( MID , "admin" ) ) {
      skin = (String) MM.get("skin");
    }
    
    // 서브 모듈을 호출할 경우 스킨 경로가 메인 정보를 사용하기 때문에 발생하는 문제 해결
    if ( StringUtils.equals(INTERLOCK,"true") ) {
      skin = (String) S.get("skin");
    }

    modSkin(skin);
  }
  public void modSkin(String skin) {
    if (StringUtils.isNotEmpty(skin)) {
      MOD_FTL_MODULE_SKIN = "/skins/" + skin;
    } else {
      MOD_FTL_MODULE_SKIN = "/ftl";
    }

    MOD_FTL_MODULE_SKIN = MOD_PATH_RELATIVE + MOD_FTL_MODULE_SKIN;
    MOD_FTL_MODULE = MOD_FTL_MODULE_SKIN + "/" + ACT_TEMPLATE;
    meiLogShow("MEI Mei.modSkin" , "MOD_FTL_MODULE : " + MOD_FTL_MODULE);

    // 파일 존재 체크 없을 경우 기본 스킨으로 변경
    /*
    File ftl_file = new File(MEI_PATH_ABSOLUTE_RELATIVE + MOD_FTL_MODULE);
    if ( ftl_file.exists() == false) {
    MOD_FTL_MODULE_SKIN = MOD_PATH_RELATIVE + "/ftl";
    MOD_FTL_MODULE = MOD_FTL_MODULE_SKIN + "/" + ACT_TEMPLATE;
    meiLogShow("MEI Mei.modSkin" , "MOD_FTL_MODULE 파일이 존재하지 않음 FTL 변경 : " + MOD_FTL_MODULE);
    }*/

    M.put("MOD_FTL_MODULE_SKIN",MOD_FTL_MODULE_SKIN);
    M.put("MOD_FTL_MODULE",MOD_FTL_MODULE);

    meiLogShow("MEI Mei.modSkin" , "MOD_PATH_RELATIVE : " + MOD_PATH_RELATIVE);
    meiLogShow("MEI Mei.modSkin" , "MOD_FTL_MODULE_SKIN : " + MOD_FTL_MODULE_SKIN);
  }
  
  // 템플릿 파일 변경 할 경우
  public void modSkinChange(String ftl) {
    MOD_FTL_MODULE = MOD_FTL_MODULE_SKIN + "/" + ftl;
    M.put("MOD_FTL_MODULE",MOD_FTL_MODULE);
    meiLogShow("MEI Mei.modSkinChange" , "MOD_FTL_MODULE : " + MOD_FTL_MODULE);
  }

  public void layoutInfo() {

    try {
      String layout_orl = (String) M.get("layout_orl");

      if ( StringUtils.isEmpty(layout_orl) || StringUtils.equals(layout_orl,"0") ) {
        throw new Exception("layout_orl 존재하지 않습니다.");
      }

      if (StringUtils.equals(ACT_TYPE,"view")) {
        Map mapSch = new HashMap();
        mapSch.put("layout_orl",layout_orl);

        LayoutBean layoutbean = (LayoutBean) daoLayout.sqlLayoutObject(mapSch);
        String layout = layoutbean.getLayout();
        String extra_vars = layoutbean.getExtra_vars();

        LAYOUT.put("object",layoutbean);

        if (StringUtils.isNotEmpty(extra_vars)) {
          JSONObject objJson = JSONObject.fromObject(extra_vars);
          LAYOUT.put("option",objJson);
        }

//        LAYOUT_PROPERTIES = "com/syaku/layouts/" + layout + "/info.properties";
//        layoutConfig = ConfigUtils.getProperties(LAYOUT_PROPERTIES);

        LAYOUT_PROPERTIES = MEI_LAYOUT_PATH_RELATIVE + "/" + layout + "/layout.xml";
        File layout_xml_file = new File(LAYOUT_PROPERTIES);



        if ( layout_xml_file.exists() ) {

          layoutConfig = daoLayout.prop2map(LAYOUT_PROPERTIES);
          LAYOUT_PATH_RELATIVE = MEI_PATH_RELATIVE + layoutConfig.get("path");

          MOD_FTL_MODULE_LAYOUT = LAYOUT_PATH_RELATIVE +"/layout.html";

          M.put("MOD_FTL_MODULE_LAYOUT",MOD_FTL_MODULE_LAYOUT);

          meiLogShow("MEI Mei.layoutInfo" , "LAYOUT_PATH_RELATIVE : " + LAYOUT_PATH_RELATIVE);
          meiLogShow("MEI Mei.layoutInfo" , "MOD_FTL_MODULE_LAYOUT : " + MOD_FTL_MODULE_LAYOUT);
          meiLogShow("MEI Mei.layoutInfo" , "layout_orl : " + layout_orl);

        } else {
          throw new Exception( LAYOUT_PROPERTIES + " 존재하지 않습니다.");
        }

      }

    } catch (Exception e) {
      meiLogShow("MEI Mei.layoutInfo" , "ERROR : " + e.toString() );
      log.error("[#MEI Mei.layoutInfo]" + e.toString() );
    }

  }

  public void menuList() {

    try {

      String menu_orl = (String) M.get("menu_orl");

      // 메뉴
      if ( StringUtils.isEmpty(menu_orl) || StringUtils.equals(menu_orl,"0") ) {
        throw new Exception("menu_orl 존재하지 않습니다.");
      }

      MENU.put("list",(List) daoMenu.getMenuCacheList(menu_orl));
      Map<String,String> mapMenu = (Map<String,String>) daoMenu.getMenuItemSelected(menu_orl,request);
      MENU.put("select",mapMenu);
      MENU.put("depth",(List) daoMenu.getMenuItemDepth(menu_orl,mapMenu.get("menu_item_orls")));

      meiLogShow("MEI Mei.menuList" , "MENU : " + mapMenu);
      meiLogShow("MEI Mei.menuList" , "menu_orl : " + menu_orl);

    } catch (Exception e) {
      meiLogShow("MEI Mei.menuList" , "MENU ERROR : " + e.toString() );
      log.error("[#MEI Mei.menuList]" + e.toString() );
    }

  }

  public void grant() {

    try {
      String member_orl = (String) session.get("_MEI_MEMBER_ORL");
      String module_orl = StringUtils.defaultIfEmpty( MODULE_ORL , MID_MODULE_ORL );

      if ( StringUtils.isEmpty(module_orl) ) {
        throw new Exception("module_orl 존재하지 않습니다.");
      }

      Map mapGrant = (Map) daoModule.moduleGrantsCheck(member_orl,module_orl);

      MEI_GRANT_LOGIN = (Boolean) mapGrant.get("login");
      MEI_GRANT_ADMIN = (Boolean) mapGrant.get("admin");
      MEI_GRANT_MINE = (Boolean) mapGrant.get("mine");
      MEI_GRANT_ACCESS = (Boolean) mapGrant.get("access");
      MEI_GRANT_LIST = (Boolean) mapGrant.get("list");
      MEI_GRANT_VIEW = (Boolean) mapGrant.get("view");
      MEI_GRANT_WRITE = (Boolean) mapGrant.get("write");
      MEI_GRANT_COMMENT = (Boolean) mapGrant.get("comment");
      MEI_GRANT_MANAGER = (Boolean) mapGrant.get("manager");

      stack.put("MEI_GRANT_LOGIN" , MEI_GRANT_LOGIN);
      stack.put("MEI_GRANT_ADMIN" , MEI_GRANT_ADMIN);
      stack.put("MEI_GRANT_MINE" , MEI_GRANT_MINE);
      stack.put("MEI_GRANT_ACCESS" , MEI_GRANT_ACCESS);
      stack.put("MEI_GRANT_LIST" , MEI_GRANT_LIST);
      stack.put("MEI_GRANT_VIEW" , MEI_GRANT_VIEW);
      stack.put("MEI_GRANT_WRITE" , MEI_GRANT_WRITE);
      stack.put("MEI_GRANT_COMMENT" , MEI_GRANT_COMMENT);
      stack.put("MEI_GRANT_MANAGER" , MEI_GRANT_MANAGER);

      meiLogShow("MEI Mei.grant" , StringUtils.join(new String[] {
        "> PARAM_MID : " , PARAM_MID , "\r\n" , 
        "> PARAM_MODULE_ORL : " , PARAM_MODULE_ORL , "\r\n" , 
        "> MID_MODULE_ORL : " , MID_MODULE_ORL , "\r\n" , 
        "> MODULE_ORL : " , MODULE_ORL , "\r\n" , 
        "> module_orl : " , module_orl , "\r\n" , 
        "> member_orl : " , member_orl , "\r\n" , 
        "> MEI_GRANT_LOGIN : " , ObjectUtils.toString(MEI_GRANT_LOGIN) , "\r\n" , 
        "> MEI_GRANT_ADMIN : " , ObjectUtils.toString(MEI_GRANT_ADMIN) , "\r\n" , 
        "> MEI_GRANT_MINE : " , ObjectUtils.toString(MEI_GRANT_MINE) , "\r\n" , 
        "> MEI_GRANT_ACCESS : " , ObjectUtils.toString(MEI_GRANT_ACCESS) , "\r\n" , 
        "> MEI_GRANT_LIST : " , ObjectUtils.toString(MEI_GRANT_LIST) , "\r\n" , 
        "> MEI_GRANT_VIEW : " , ObjectUtils.toString(MEI_GRANT_VIEW) , "\r\n" , 
        "> MEI_GRANT_WRITE : " , ObjectUtils.toString(MEI_GRANT_WRITE) , "\r\n" , 
        "> MEI_GRANT_COMMENT : " , ObjectUtils.toString(MEI_GRANT_COMMENT) , "\r\n" , 
        "> MEI_GRANT_MANAGER : " , ObjectUtils.toString(MEI_GRANT_MANAGER) , "\r\n"
      }));

    } catch (Exception e) {
      log.error("[#MEI Mei.grant]" + e.toString() );
    }

  }

  public String LOG_TEXT;
  private void meiLogShow(String title , String text) {
    
    String title_text = "";
    if ( StringUtils.isEmpty(title) ) {
      title_text = "\r\n" + title + "\r\n";
    }

    LOG_TEXT = StringUtils.join(new String[] {LOG_TEXT , 
    title_text , 
    text , "\r\n\r\n"
    });
    
  }

  public abstract void prepare();
//  public abstract Object getModel();
  public Object getModel() {
    log.debug("[## MEI Mei getModel] Called");
    return stack;
  }


  // mei 데이터 정리 전에 실행
  public abstract void meiBefore();
  // mei 데이터 정리 후에 실행
  public abstract void meiAfter();

}
