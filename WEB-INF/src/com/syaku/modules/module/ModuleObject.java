/*
 * ModuleObject.java 2011.10.27
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.module;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;
import org.apache.commons.collections.*; 

import com.syaku.core.util.*;

import com.syaku.modules.member.*;
import com.syaku.modules.layout.*;

public class ModuleObject extends ModuleAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/module/info.properties");

  // getModuleSkinList
  public List skinList(String module) {
    List listSkins = new ArrayList();

    try {
      if (StringUtils.isEmpty(module)) { return listSkins; }

      File dir = new File(module);
      if (!dir.exists()) { return listSkins; }

      File dir_arr[] = dir.listFiles();
      int count = dir_arr.length;
      int o = 0;

      for (int i = 0; i < count; i++) {
        File info = dir_arr[i];
        if (dir_arr[i].isDirectory()) {
          String name = info.getName();
          if (name.equals(".svn") || StringUtils.isEmpty(name)) { // SVN 폴더 제외
            continue;
          }

          listSkins.add(o,name);
          o++;
        }
      }

    } catch (Exception e) {
      log.error("[#MEI ModuleObject.skinList]" + e.toString() );
    }

    return listSkins;
  }

  // getModuleMid
  public String mid(Map map) {
    String mid = null;
    try {

      ModuleBean modulebean = (ModuleBean) sqlModuleObject(map);
      if (modulebean == null) { return null; }
      mid = modulebean.getMid();
    } catch (Exception e) {
      log.error("[#MEI ModuleObject.mid]" + e.toString() );
    }

    return mid;
  }

  public String getModule_orl(String mid) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("mid",mid);

    ModuleBean modulebean = (ModuleBean) sqlModuleObject(mapSch);
    if (modulebean == null) { return null; }
    return modulebean.getModule_orl();
  }

  // 모듈 존재 유무
  public boolean moduleAble(String module) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("module",module);

    long count = sqlModuleCount(mapSch);
    return count > 0;
  }
  public boolean moduleMidAble(String mid) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("mid",mid);

    long count = sqlModuleCount(mapSch);
    return count > 0;
  }

  // 모듈 옵션 존재 유무
  public boolean moduleOptionsMidAble(String mid) throws Exception {
    String module_orl = getModule_orl(mid);
    if (module_orl == null) { return false; }

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    long count = sqlModuleOptionsCount(mapSch);
    return count > 0;
  }

  // getModuleMap
  public Map objectProp(String resource) {
    return ConfigUtils.prop2map(resource,"module");
  }
  public Map objectOne(Map mapSch) {
    Map mapRet = new HashMap();

    try {

      ModuleBean modulebean = (ModuleBean) sqlModuleObject(mapSch);
      if (modulebean == null) { return mapRet;}

      String module_orl = modulebean.getModule_orl();
      String mid = modulebean.getMid();
      String module = modulebean.getModule();
      String browser_title = modulebean.getBrowser_title();
      String skin = modulebean.getSkin();
      String layout_orl = modulebean.getLayout_orl();
      String menu_orl = modulebean.getMenu_orl();
      String content = modulebean.getContent();
      String rdate = modulebean.getRdate();
      String header_content = modulebean.getHeader_content();
      String footer_content = modulebean.getFooter_content();

      mapRet.put("module_orl",module_orl);
      mapRet.put("module",module);
      mapRet.put("mid",mid);
      mapRet.put("browser_title",browser_title);
      mapRet.put("skin",skin);
      mapRet.put("layout_orl",layout_orl);
      mapRet.put("menu_orl",menu_orl);
      mapRet.put("content",content);
      mapRet.put("rdate",rdate);
      mapRet.put("header_content",header_content);
      mapRet.put("footer_content",footer_content);

      mapSch.put("module_orl",module_orl);
      List list = sqlModuleOptionsList(mapSch);
      int count = list.size();

      for (int i = 0; i < count; i++ ) {
        ModuleOptionsBean obj= (ModuleOptionsBean) list.get(i);
        String name = obj.getName();
        String value = obj.getValue();

        mapRet.put(name,value);
      }

    } catch (Exception e) {
      log.error("[#MEI ModuleObject.objectOne]" + e.toString() );
    }

    return mapRet;
  }

  // getModuleOptionsList
  public List list(Map map) throws Exception {
    List listModule = new ArrayList();

    List list = sqlModuleList(map);
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      ModuleBean modulebean= (ModuleBean) list.get(i);

      String module_orl = modulebean.getModule_orl();
      String module = modulebean.getModule();
      String mid = modulebean.getMid();
      String browser_title = modulebean.getBrowser_title();
      String skin = modulebean.getSkin();
      String layout_orl = modulebean.getLayout_orl();
      String menu_orl = modulebean.getMenu_orl();
      String content = modulebean.getContent();
      String rdate = modulebean.getRdate();
      String header_content = modulebean.getHeader_content();
      String footer_content = modulebean.getFooter_content();

      HashMap mapRet = new HashMap();
      mapRet.put("module_orl",module_orl);
      mapRet.put("module",module);
      mapRet.put("mid",mid);
      mapRet.put("browser_title",browser_title);
      mapRet.put("skin",skin);
      mapRet.put("layout_orl",layout_orl);
      mapRet.put("menu_orl",menu_orl);
      mapRet.put("content",content);
      mapRet.put("rdate",rdate);
      mapRet.put("header_content",header_content);
      mapRet.put("footer_content",footer_content);

      map.put("module_orl",module_orl);
      List listOptions = sqlModuleOptionsList(map);
      int countOptions = listOptions.size();

      for (int o = 0; o < countOptions; o++ ) {
        ModuleOptionsBean moduleoptionsbean= (ModuleOptionsBean) listOptions.get(o);
        String name = moduleoptionsbean.getName();
        String value = moduleoptionsbean.getValue();

        mapRet.put(name,value);
      }

      listModule.add(i,mapRet);
    }

    return listModule;

  }

  public Map<String,String> listOptions(String module_orl) throws Exception {
    Map map = new HashMap();
    map.put("module_orl",module_orl);
    return listOptions(map);
  }
  public Map<String,String> listOptions(String module_orl,String name) throws Exception {
    Map map = new HashMap();
    map.put("module_orl",module_orl);
    map.put("name",name);
    return listOptions(map);
  }
  public Map<String,String> listOptions(Map<String,String> map) throws Exception {
    Map<String,String> mapRet = new HashMap<String,String>();
    List list = sqlModuleOptionsList(map);
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      ModuleOptionsBean moduleoptionsbean= (ModuleOptionsBean) list.get(i);
      String module_orl = moduleoptionsbean.getModule_orl();
      String name = moduleoptionsbean.getName();
      String value = moduleoptionsbean.getValue();

      mapRet.put(name,value);
    }

    return mapRet;
  }

  // getModuleInsert
  public String moduleInsert(String module,ParameterUtils param) throws Exception {
    String module_orl = param.value("module_orl");
    String mid = param.value("module_id");
    String browser_title = param.value("browser_title");
    String skin = param.value("skin");
    String layout_orl = param.valueIfEmpty("layout_orl","0");
    String content = param.valueIfEmpty("content","");
    String header_content = param.valueIfEmpty("header_content","");
    String footer_content = param.valueIfEmpty("footer_content","");

    ModuleBean modulebean = new ModuleBean();
    modulebean.setModule_orl(module_orl);
    modulebean.setModule(module);
    modulebean.setMid(mid);
    modulebean.setBrowser_title(browser_title);
    modulebean.setSkin(skin);
    modulebean.setLayout_orl(layout_orl);
    modulebean.setMenu_orl("0"); // 레이어 정보 호출
    modulebean.setContent(content);
    modulebean.setHeader_content(header_content);
    modulebean.setFooter_content(footer_content);

    return moduleInsert(modulebean);
  }

  public String moduleInsert(ModuleBean bean) throws Exception {
    String module_orl = bean.getModule_orl();
    String layout_orl = bean.getLayout_orl();

    // 선택된 레이아웃 메뉴정보 호출
    if ( !StringUtils.equals(layout_orl,"0") && StringUtils.isNotEmpty(layout_orl) ) {

      Map mapSch = new HashMap();
      mapSch.put("layout_orl",layout_orl);
      LayoutObject daoLayout = new LayoutObject();
      LayoutBean layout = (LayoutBean) daoLayout.sqlLayoutObject(mapSch);
      if (layout != null) { 
        bean.setMenu_orl( layout.getMenu_orl() );
      }

    }

    if (StringUtils.isEmpty(module_orl)) {
      bean.setRdate( (String) DateUtils.date("yyyyMMddHHmmss") );
      module_orl = sqlModuleInsert(bean);
    } else {
      sqlModuleUpdate(bean);
    }

    return module_orl;
  }

  // getModuleOptionsInsert
  public void moduleOptionsInsert(String module_orl,Map map) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    sqlModuleOptionsDelete(mapSch);

    if ( MapUtils.isNotEmpty(map) ) {


    Iterator iterator = map.keySet().iterator();
    while(iterator.hasNext()) {
      String name = (String) iterator.next();
      String value = (String) map.get(name);

      if (StringUtils.isNotEmpty(name)) {
        ModuleOptionsBean bean = new ModuleOptionsBean();
        bean.setModule_orl(module_orl);
        bean.setName(name);
        bean.setValue(value);
        sqlModuleOptionsInsert(bean);
        /*
        Map mapSch = new HashMap();
        mapSch.put("module_orl",module_orl);
        mapSch.put("name",name);

        long total = sqlModuleOptionsCount(mapSch);
        if (total == 0) {
          sqlModuleOptionsInsert(bean);
        } else {
          sqlModuleOptionsUpdate(bean);
        }*/

      }
    }

    }

  }

  // getModulesInsert
  public void modulesInsert(String module_name,String module_orl,ParameterUtils param,Map map) throws Exception {
    module_orl = moduleInsert(module_name,param);
    moduleOptionsInsert(module_orl,map);
  }
  
  // getModuleDelete
  public void moduleDelete(String module_orl) throws Exception {
    HashMap mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    sqlModuleGrantsDelete(mapSch);
    sqlModuleOptionsDelete(mapSch);
    sqlModuleDelete(mapSch);
  }
  
  // getModuleGrantsCheckMap
  public Map moduleGrantsCheck(String member_orl,String module_orl) throws Exception {
    MemberObject daoMember = new MemberObject();

    boolean isLogin = (StringUtils.isNotEmpty(member_orl) && !StringUtils.equals(member_orl,"0"));

    Map mapRet = new HashMap();
    mapRet.put("login",isLogin);
    mapRet.put("admin",false);
    mapRet.put("mine",false);
    mapRet.put("access",true);
    mapRet.put("list",true);
    mapRet.put("view",true);
    mapRet.put("write",true);
    mapRet.put("comment",true);
    mapRet.put("manager",false);

    boolean isAdmin = daoMember.getIsAdmin(member_orl);

    if (isAdmin) {
      mapRet.put("admin",true);
      mapRet.put("manager",true);
      mapRet.put("mine",true);
      return mapRet;
    }

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    List listGrants = sqlModuleGrantsList(mapSch);
    int grants_count = listGrants.size();
    
    // 권한이 없을 경우 모든 권한 허용 단 manager 제외
    if (grants_count == 0) {
      mapRet.put("admin",false);
      mapRet.put("manager",false);
      return mapRet;
    }

    Object[] access = { };
    Object[] list = { };
    Object[] view = { };
    Object[] write = { };
    Object[] comment = { };
    Object[] manager = { };
    
    for (int i = 0; i < grants_count; i++) {
      ModuleGrantsBean object = (ModuleGrantsBean) listGrants.get(i);

      String group_orl = object.getGroup_orl();
      String name = object.getName();

      int group_orl_int = NumberUtils.stringToInt(group_orl);
      boolean is = false;

      switch (group_orl_int) {
        case 0 : // 모든 사용자
          mapRet.put(name,true);
          is = true;
        break;
        case -1 : // 로그인 사용자
        case -2 : // 가입한 사용자
          if (!isLogin) { mapRet.put(name,false); }
          is = true;
        break;

        case -99 :
          if (!isAdmin) { mapRet.put(name,false); }
          is = true;
        break;

        default :

          if (StringUtils.equals(name,"access")) {
            access = ArrayUtils.add(access,group_orl);
          } else if (StringUtils.equals(name,"list")) {
            list = ArrayUtils.add(list,group_orl);
          } else if (StringUtils.equals(name,"view")) {
            view = ArrayUtils.add(view,group_orl);
          } else if (StringUtils.equals(name,"write")) {
            write = ArrayUtils.add(write,group_orl);
          } else if (StringUtils.equals(name,"comment")) {
            comment = ArrayUtils.add(comment,group_orl);
          } else if (StringUtils.equals(name,"manager")) {
            manager = ArrayUtils.add(manager,group_orl);
          }

        break;

      }

      if (is) { continue; }

    }

    Object[] group = { };

    if (StringUtils.isNotEmpty(member_orl)) {

      mapSch.clear();
      mapSch.put("member_orl",member_orl);
      List listGroup = daoMember.sqlGroupMemberList(mapSch);
      int group_count = listGroup.size();
      for (int g = 0; g < group_count; g++) {
        GroupMemberBean object = (GroupMemberBean) listGroup.get(g);
        String group_orl = object.getGroup_orl();
        group = ArrayUtils.add(group,group_orl);
      }
    }

    if (ArrayUtils.isNotEmpty(access)) { mapRet.put("access",moduleGrantsGroupEquals(access,group)); }
    if (ArrayUtils.isNotEmpty(list)) { mapRet.put("list",moduleGrantsGroupEquals(list,group)); }
    if (ArrayUtils.isNotEmpty(view)) { mapRet.put("view",moduleGrantsGroupEquals(view,group)); }
    if (ArrayUtils.isNotEmpty(write)) { mapRet.put("write",moduleGrantsGroupEquals(write,group)); }
    if (ArrayUtils.isNotEmpty(comment)) { mapRet.put("comment",moduleGrantsGroupEquals(comment,group)); }
    if (ArrayUtils.isNotEmpty(manager)) { 
      if (moduleGrantsGroupEquals(manager,group)) { // 관리자 권한인 경우
        mapRet.put("mine",true);
        mapRet.put("access",true);
        mapRet.put("list",true);
        mapRet.put("view",true);
        mapRet.put("write",true);
        mapRet.put("comment",true);
        mapRet.put("manager",true);
      }
    }

    return mapRet;

  }

  // getModuleGrantsGroupEquals
  public boolean moduleGrantsGroupEquals(Object[] grants,Object[] group) {
    int grants_count = ArrayUtils.getLength(grants);
    int group_count = ArrayUtils.getLength(group);

    for (int i = 0; i < grants_count; i++) {
      int grants_orl = NumberUtils.stringToInt((String) grants[i]);
      for (int g = 0; g < group_count; g++) {
        int group_orl = NumberUtils.stringToInt((String) group[g]);
        if (grants_orl == group_orl) { return true; }
      }

    }

    return false;
  }


  // getModuleGrantsCheck
  public boolean moduleIsGrantsCheck(String member_orl,String module_orl,String name) throws Exception {
    MemberObject daoMember = new MemberObject();
    boolean isAdmin = daoMember.getIsAdmin(member_orl);
    if (isAdmin) { return true; }

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    mapSch.put("name",name);
    List listGrants = sqlModuleGrantsList(mapSch);
    int grants_count = listGrants.size();
    
    if (grants_count == 0) {

      if (StringUtils.equals(name,"manager")) {
        return false;
      } else {
        return true;
      }
    }

    boolean is = false;
    boolean isLogin = StringUtils.isNotEmpty(member_orl);

    for (int i = 0; i < grants_count; i++) {
      ModuleGrantsBean object = (ModuleGrantsBean) listGrants.get(i);
      
      String group_orl_str = object.getGroup_orl();
      if (StringUtils.isEmpty(group_orl_str)) { continue; }

      int group_orl = NumberUtils.stringToInt(group_orl_str);
      switch (group_orl) {
        case 0 : // 모든 사용자
          is = true;
        break;

        case -1 : // 로그인 사용자
        case -2 : // 가입한 사용자
          is = isLogin;
        break;

        case -99 :
          is = isAdmin;
        break;

        default :
          mapSch.clear();
          mapSch.put("group_orl",ObjectUtils.toString(group_orl));
          mapSch.put("member_orl",member_orl);
          long group = daoMember.sqlGroupMemberCount(mapSch);
          if (group > 0) { is = true; }

        break;

      }

    }

    return is;

  }

  // getModuleGrantsMap
  public Map<String,Object> moduleGrants(Map map) throws Exception {
    List listGrants = (List) sqlModuleGrantsList(map);
    int count = listGrants.size();

    Object[] access = { };
    Object[] list = { };
    Object[] view = { };
    Object[] write = { };
    Object[] comment = { };
    Object[] manager = { };

    for (int i = 0; i < count; i++) {
      ModuleGrantsBean obj = (ModuleGrantsBean) listGrants.get(i);
      String module_orl = obj.getModule_orl();
      String name = obj.getName();
      String group_orl = obj.getGroup_orl();

      if (StringUtils.equals(name,"access")) {
        access = ArrayUtils.add(access,group_orl);
      } else if (StringUtils.equals(name,"list")) {
        list = ArrayUtils.add(list,group_orl);
      } else if (StringUtils.equals(name,"view")) {
        view = ArrayUtils.add(view,group_orl);
      } else if (StringUtils.equals(name,"write")) {
        write = ArrayUtils.add(write,group_orl);
      } else if (StringUtils.equals(name,"comment")) {
        comment = ArrayUtils.add(comment,group_orl);
      } else if (StringUtils.equals(name,"manager")) {
        manager = ArrayUtils.add(manager,group_orl);
      }

    }

    Map<String,Object> mapData = new HashMap<String,Object>();
    mapData.put("access",access);
    mapData.put("list",list);
    mapData.put("view",view);
    mapData.put("write",write);
    mapData.put("comment",comment);
    mapData.put("manager",manager);

    mapData.put("access_string",StringUtils.replaceEach(ArrayUtils.toString(access), new String[]{"{","}"}, new String[]{"",""}));
    mapData.put("list_string",StringUtils.replaceEach(ArrayUtils.toString(list), new String[]{"{","}"}, new String[]{"",""}));
    mapData.put("view_string",StringUtils.replaceEach(ArrayUtils.toString(view), new String[]{"{","}"}, new String[]{"",""}));
    mapData.put("write_string",StringUtils.replaceEach(ArrayUtils.toString(write), new String[]{"{","}"}, new String[]{"",""}));
    mapData.put("comment_string",StringUtils.replaceEach(ArrayUtils.toString(comment), new String[]{"{","}"}, new String[]{"",""}));
    mapData.put("manager_string",StringUtils.replaceEach(ArrayUtils.toString(manager), new String[]{"{","}"}, new String[]{"",""}));
    return mapData;
  }

  // getModuleGrantsInsert
  public void moduleGrantsInsert(ParameterUtils param) throws Exception {
    String module_orl = param.value("module_orl");
    moduleGrantsInsert(param,module_orl);
  }
  public void moduleGrantsInsert(ParameterUtils param,String module_orl) throws Exception {
    Map mapRet = new HashMap();
    List listGrant = new ArrayList();
    mapRet.put("module_orl",module_orl);
    sqlModuleGrantsDelete(mapRet);

    int total = 0;
    
    // 접속 권한
    String access_default = param.value("access_default");
    if (StringUtils.isEmpty(access_default)) {
      String grant_access_array[] = param.values("grant_access");

      if (grant_access_array != null) {
        int grant_access_count = grant_access_array.length;

        for (int a = 0; a < grant_access_count; a++) {
          String grant_access = grant_access_array[a];
          Map mapData = new HashMap();
          mapData.put("name","access");
          mapData.put("group_orl",grant_access);
    
          listGrant.add(total,mapData);
          total++;
        }

      }
    } else {
      Map mapData = new HashMap();
      mapData.put("name","access");
      mapData.put("group_orl",access_default);

      listGrant.add(total,mapData);
      total++;
    }
    // 접속 권한

    // 목록 권한
    String list_default = param.value("list_default");
    if (StringUtils.isEmpty(list_default)) {
      String grant_list_array[] = param.values("grant_list");

      if (grant_list_array != null) {
      int grant_list_count = grant_list_array.length;
      for (int l = 0; l < grant_list_count; l++) {
        String grant_list = grant_list_array[l];
        Map mapData = new HashMap();
        mapData.put("name","list");
        mapData.put("group_orl",grant_list);
  
        listGrant.add(total,mapData);
        total++;
      }
      }
    } else {
      Map mapData = new HashMap();
      mapData.put("name","list");
      mapData.put("group_orl",list_default);

      listGrant.add(total,mapData);
      total++;
    }
    // 목록 권한


    // 열람 권한
    String view_default = param.value("view_default");
    if (StringUtils.isEmpty(view_default)) {
      String grant_view_array[] = param.values("grant_view");

      if (grant_view_array != null) {
      int grant_view_count = grant_view_array.length;
      for (int v = 0; v < grant_view_count; v++) {
        String grant_view = grant_view_array[v];
        Map mapData = new HashMap();
        mapData.put("name","view");
        mapData.put("group_orl",grant_view);
  
        listGrant.add(total,mapData);
        total++;
      }
      }

    } else {
      Map mapData = new HashMap();
      mapData.put("name","view");
      mapData.put("group_orl",view_default);

      listGrant.add(total,mapData);
      total++;
    }
    // 열람 권한

    // 글작성 권한
    String write_default = param.value("write_default");
    if (StringUtils.isEmpty(write_default)) {
      String grant_write_array[] = param.values("grant_write");

      if (grant_write_array != null) {
      int grant_write_count = grant_write_array.length;
      for (int w = 0; w < grant_write_count; w++) {
        String grant_write = grant_write_array[w];
        Map mapData = new HashMap();
        mapData.put("name","write");
        mapData.put("group_orl",grant_write);
  
        listGrant.add(total,mapData);
        total++;
      }
      }

    } else {
      Map mapData = new HashMap();
      mapData.put("name","write");
      mapData.put("group_orl",write_default);

      listGrant.add(total,mapData);
      total++;
    }
    // 글작성 권한

    // 댓글작성 권한
    String comment_default = param.value("comment_default");
    if (StringUtils.isEmpty(comment_default)) {
      String grant_comment_array[] = param.values("grant_comment");

      if (grant_comment_array != null) {
      int grant_comment_count = grant_comment_array.length;
      for (int c = 0; c < grant_comment_count; c++) {
        String grant_comment = grant_comment_array[c];
        Map mapData = new HashMap();
        mapData.put("name","comment");
        mapData.put("group_orl",grant_comment);
  
        listGrant.add(total,mapData);
        total++;
      }
      }

    } else {
      Map mapData = new HashMap();
      mapData.put("name","comment");
      mapData.put("group_orl",comment_default);

      listGrant.add(total,mapData);
      total++;
    }
    // 댓글작성 권한

    // 관리 권한
    String manager_default = param.value("manager_default");
    if (StringUtils.isEmpty(manager_default)) {
      String grant_manager_array[] = param.values("grant_manager");

      if (grant_manager_array != null) {
      int grant_manager_count = grant_manager_array.length;
      for (int m = 0; m < grant_manager_count; m++) {
        String grant_manager = grant_manager_array[m];
        Map mapData = new HashMap();
        mapData.put("name","manager");
        mapData.put("group_orl",grant_manager);
  
        listGrant.add(total,mapData);
        total++;
      }
      }

    } else {
      Map mapData = new HashMap();
      mapData.put("name","manager");
      mapData.put("group_orl",manager_default);

      listGrant.add(total,mapData);
      total++;
    }
    // 관리 권한
    
    if (total > 0) {
      mapRet.put("listGrant",listGrant);
      sqlModuleGrantsArrayInsert(mapRet);
    }
  }

}