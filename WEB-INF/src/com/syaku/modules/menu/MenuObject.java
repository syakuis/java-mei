/*
 * MenuObject.java 2011.08.01
 *
 * Copyright (c) MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.menu;

import java.util.*;
import java.io.*;

import java.util.regex.*;
import java.text.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;
import org.apache.commons.io.*;

import net.sf.json.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import com.syaku.core.util.*;
import com.syaku.core.xml.*;

public class MenuObject extends MenuAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public Configuration meiConfig = ConfigUtils.getProperties("mei.properties"); // mei 정보
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/menu/info.properties");

  public CacheUtils cache = new CacheUtils();

  public String MENU_CACHE_PATH = meiConfig.getString("mei.path.absolute_relative") + meiConfig.getString("mei.path.cache") + "/menu";

  public Document getMenuCacheXml(Map map) throws Exception { // 2011.07.01
    List list = getMenuItemList(map);
    CreateTreeXml xml = new CreateTreeXml(list,"menu_item_orl","menu_item_orls");
    Element root = xml.xmlBulider();
    return new Document(root);
  }

  public List getMenuItemCache(String menu_orl,String parent_orl) throws Exception {
    List listMenuItemCache = new ArrayList();

    // 캐쉬 파일명
    String name = MENU_CACHE_PATH + "/menu_list_" + menu_orl + ".xml";

    XMLConfiguration configLayout = ConfigUtils.getXml(name);
    if (configLayout != null) {
      List list = configLayout.configurationsAt("item");
      int i = 0;

      for(Iterator it = list.iterator(); it.hasNext();) {
        HierarchicalConfiguration obj = (HierarchicalConfiguration) it.next();

        String parent_orl2 = obj.getString("[@parent_orl]");
        
        if (StringUtils.isNotEmpty(parent_orl) && !StringUtils.equals(parent_orl2,parent_orl)) {
          continue;
        }

        HashMap mapRet = new HashMap();
        mapRet.put("menu_orl",obj.getString("[@menu_orl]"));
        mapRet.put("menu_item_orl",obj.getString("[@menu_item_orl]"));
        mapRet.put("parent_orl",obj.getString("[@parent_orl]"));
        mapRet.put("url",obj.getString("[@url]"));
        mapRet.put("href",obj.getString("[@href]"));
        mapRet.put("name",obj.getString("[@name]"));
        mapRet.put("open_window",obj.getString("[@open_window]"));
        mapRet.put("expand",obj.getString("[@expand]"));
        mapRet.put("is_mobile",obj.getString("[@is_mobile]"));
        mapRet.put("depth",obj.getString("[@depth]"));
        mapRet.put("menu_item_orls",obj.getString("[@menu_item_orls]"));

        String[] group_orls_ar = obj.getStringArray("[@group_orls]");
        String group_orls = ArrayUtils.toString(group_orls_ar);
        group_orls = StringUtils.isNotEmpty(group_orls) ? group_orls.replaceAll("[{}]","") : "";
        mapRet.put("group_orls",group_orls);
        mapRet.put("btn_normal",obj.getString("[@btn_normal]"));
        mapRet.put("btn_hover",obj.getString("[@btn_hover]"));
        mapRet.put("btn_active",obj.getString("[@btn_active]"));
        mapRet.put("select_query",obj.getString("[@select_query]"));

        listMenuItemCache.add(i,mapRet);
        i++;
      }
    }

    return listMenuItemCache;
  }

  // 인스톨에서 사용
  public String getMenu_orl(String name) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("name" , name);

    MenuBean obj = (MenuBean) sqlMenuSearchObject(mapSch);
    if (obj == null) { return null; }

    return obj.getMenu_orl();
  }

  // 인스톨에서 사용
  public String getMenu_item_orl(String name,String url) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("name" , name);
    mapSch.put("url" , url);

    MenuItemBean obj = (MenuItemBean) sqlMenuItemSearchObject(mapSch);
    if (obj == null) { return null; }

    return obj.getMenu_item_orl();
  }


  public List getMenuItemList(Map map) throws Exception {
    List listRet = new ArrayList();

    List list = (List) sqlMenuItemList(map);
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      MenuItemBean obj= (MenuItemBean) list.get(i);
      
      String menu_item_orl = obj.getMenu_item_orl();
      String num = obj.getNum();

      String parent_orl = obj.getParent_orl();
      String url = obj.getUrl();
      String name = obj.getName();
      String open_window = obj.getOpen_window();
      String expand = obj.getExpand();
      String is_mobile = obj.getIs_mobile();
      String menu_orl = obj.getMenu_orl();
      String rdate = obj.getRdate();

      String group_orls = obj.getGroup_orls();
      String btn_normal = obj.getBtn_normal();
      String btn_hover = obj.getBtn_hover();
      String btn_active = obj.getBtn_active();
      String select_query = obj.getSelect_query();

      String href = url;
      if (StringUtils.isNotEmpty(href)) {
        // 실제사용되는 url 생성
        href = url.replaceAll("[0-9a-zA-z_]+","");
        if (StringUtils.isEmpty(href)) {
          href = meiConfig.getString("mei.path.relative") + "/?mid=" + url;
        } else {
          href = url;
        }
      }

      HashMap mapRet = new HashMap();
      mapRet.put("menu_item_orl",menu_item_orl);
      mapRet.put("parent_orl",parent_orl);
      mapRet.put("num",num);
      mapRet.put("url",url);
      mapRet.put("name",name);
      mapRet.put("open_window",open_window);
      mapRet.put("expand",expand);
      mapRet.put("is_mobile",is_mobile);
      mapRet.put("menu_orl",menu_orl);
      mapRet.put("rdate",rdate);
      mapRet.put("href",href);

      mapRet.put("group_orls",group_orls);
      mapRet.put("btn_normal",btn_normal);
      mapRet.put("btn_hover",btn_hover);
      mapRet.put("btn_active",btn_active);
      mapRet.put("select_query",select_query);

      listRet.add(i,mapRet);
    }

    return listRet;

  }

  // 메뉴 Num 구하기 2013-05-30
  public String getMenuItemNumSort(String menu_orl,String parent_orl) throws Exception {
    Map mapSch = new HashMap();
    String num = null;
    if (StringUtils.equals( parent_orl , "0" ) || StringUtils.isEmpty(parent_orl)) {
      mapSch.put("menu_orl",menu_orl);
      mapSch.put("parent_orl","0");
      num = (String) sqlMenuItemMaxNum(mapSch);
      num = ObjectUtils.toString(NumberUtils.stringToInt(num) + 1);
    } else {
      mapSch.put("menu_orl",menu_orl);
      mapSch.put("parent_orl",parent_orl);
      num = (String) sqlMenuItemMaxNum(mapSch);
      num = ObjectUtils.toString(NumberUtils.stringToInt(num) + 1);
      mapSch.put("menu_orl",menu_orl);
      mapSch.put("num",num);
      sqlMenuItemUpdateNumSort(mapSch); // 정렬
    }

    return num;
  }

  // 메뉴아이템 등록 및 수정
  public void getMenuItemInsert(ParameterUtils param) throws Exception { // 2011.06.29
    String menu_orl = param.value("menu_orl");
    String menu_item_orl = param.value("menu_item_orl");
    String parent_orl = param.valueIfEmpty("parent_orl","0");
    String num = param.valueIfEmpty("num","0");
    String name = param.value("name");
    String url = param.value("url");
    String open_window = param.valueIfEmpty("open_window","0");
    String expand = param.valueIfEmpty("expand","0");
    String is_mobile = param.valueIfEmpty("is_mobile","0");

    String[] group_orls_ar = param.values("group_orls");
    String group_orls = ArrayUtils.toString(group_orls_ar);
    group_orls = StringUtils.isNotEmpty(group_orls) ? group_orls.replaceAll("[{}]","") : null;

    String btn_normal = param.value("btn_normal");
    String btn_hover = param.value("btn_hover");
    String btn_active = param.value("btn_active");
    String select_query = param.value("select_query");

    Map mapSch = new HashMap();

    MenuItemBean data = new MenuItemBean();

    data.setMenu_item_orl(menu_item_orl);
    data.setParent_orl(parent_orl);
    data.setNum(num);
    data.setMenu_orl(menu_orl);
    data.setUrl(url);
    data.setName(name);
    data.setOpen_window(open_window);
    data.setExpand(expand);
    data.setIs_mobile(is_mobile);
    
    data.setRdate(DateUtils.date("yyyyMMddHHmmss"));

    data.setGroup_orls(group_orls);
    data.setBtn_normal(btn_normal);
    data.setBtn_hover(btn_hover);
    data.setBtn_active(btn_active);
    data.setSelect_query(select_query);

    if (StringUtils.isEmpty(menu_item_orl)) {
      if (parent_orl.equals("0") || StringUtils.isEmpty(parent_orl)) {
        mapSch.put("menu_orl",menu_orl);
        mapSch.put("parent_orl","0");
        num = (String) sqlMenuItemMaxNum(mapSch);
        num = ObjectUtils.toString(NumberUtils.stringToInt(num) + 1);
      } else {
        num = ObjectUtils.toString(NumberUtils.stringToInt(num) + 2);
        mapSch.put("menu_orl",menu_orl);
        mapSch.put("num",num);
        sqlMenuItemUpdateNumSort(mapSch); // 정렬
      }

      data.setNum(num);

      sqlMenuItemInsert(data);
    } else {
      sqlMenuItemUpdate(data);
    }

  }

  // 메뉴아이템 이동
  public void getMenuItemMove(ParameterUtils param) throws Exception {
    String menu_orl = param.value("menu_orl");
    String menu_item_orl = param.value("menu_item_orl");
    String parent_orl = param.value("parent_orl");
    String num = param.value("num");

    Map mapSch = new HashMap();

    if (parent_orl.equals("0") && (num.equals("0") || StringUtils.isEmpty(num))) { // 부모의 최상위 인 경우
      num = "0";
    } else if (!parent_orl.equals("0") && (num.equals("0") || StringUtils.isEmpty(num))) { // 자식의 최상위 인 경우
      mapSch.put("menu_orl",menu_orl);
      mapSch.put("menu_item_orl",parent_orl);
      MenuItemBean menuitembean = (MenuItemBean) sqlMenuItemObject(mapSch);
      num = menuitembean.getNum();
    } else {
      mapSch.put("menu_item_orl",num);
      mapSch.put("menu_orl",menu_orl);
      num = (String) sqlMenuItemMoveNum(mapSch);
    }
    
    mapSch = new HashMap();
    mapSch.put("menu_orl",menu_orl);
    mapSch.put("num",num);
    sqlMenuItemUpdateNumSort(mapSch); // 정렬

    mapSch = new HashMap();
    num = ObjectUtils.toString(NumberUtils.stringToInt(num) + 1);
    mapSch.put("num",num);
    mapSch.put("menu_orl",menu_orl);
    mapSch.put("menu_item_orl",menu_item_orl);
    mapSch.put("parent_orl",parent_orl);
    sqlMenuItemUpdateMove(mapSch);
  }

  // 캐쉬파일생성
  public void getMenuCacheFile() throws Exception { // 모든 메뉴 캐쉬파일 생성
    
    List list = (List) sqlMenuList(null);
    int count = list.size();

    for ( int i = 0; i < count; i++ ) {
      MenuBean menubean = (MenuBean) list.get(i);
      String menu_orl = menubean.getMenu_orl();
      Map map = new HashMap();
      map.put("menu_orl",menu_orl);

      getMenuCacheFile(map);
    }

  }

  public void getMenuCacheFile(Map map) throws Exception { // 2011.07.01
    String menu_orl = (String) map.get("menu_orl");
    String path = MENU_CACHE_PATH;
    String name = path + "/menu_" + menu_orl + ".xml";
    String name2 = path + "/menu_list_" + menu_orl + ".xml";

    List list = (List) getMenuItemList(map);

    if (list.size() > 0) {
      CreateTreeXml xml = new CreateTreeXml(list,"menu_item_orl","menu_item_orls");
      // 폴더 생성
      File dir = new File(path);
      if(!dir.exists()){ dir.mkdirs(); }
      xml.file(name,name2);
    } else {
      FileUtils.forceDelete(new File(name));
      FileUtils.forceDelete(new File(name2));
    }

  }


  public List getMenuCacheList(String menu_orl) throws Exception {
    Map map = new HashMap();
    map.put("expand","");
    map.put("is_mobile","");
    map.put("open_window","");
    map.put("href","");
    map.put("rdate","");
    map.put("menu_item_orl","");
    map.put("menu_item_orls","");
    map.put("parent_orl","");
    map.put("depth","");
    map.put("menu_orl","");
    map.put("url","");
    map.put("name","");

    map.put("group_orls","");
    map.put("btn_normal","");
    map.put("btn_hover","");
    map.put("btn_active","");
    map.put("select_query","");

    String name = MENU_CACHE_PATH + "/menu_list_" + menu_orl + ".xml";
    return cache.getTreeList(name,map);
  }

  public String getMenuXmlCache(String menu_orl,String mod) throws Exception {
    String category_xml = MENU_CACHE_PATH + "/menu_" + menu_orl + ".xml";

    if (StringUtils.equals(mod,"depth")) {
      category_xml = MENU_CACHE_PATH + "/menu_list_" + menu_orl + ".xml";
    }

    return FileUtils.readFileToString(new File(category_xml),"utf-8");
  }

  // 선택된 메뉴 경로
  public List getMenuItemDepth(String menu_orl,String menu_item_orls) throws Exception {
    List listRet = new ArrayList();
    menu_item_orls = "," + menu_item_orls;

    List list = getMenuCacheList(menu_orl);
    int count = list.size();
    for (int i = 0; i < count; i++) {
      Map<String,String> map = (Map<String,String>) list.get(i);
      String menu_item_orl = "," + map.get("menu_item_orl") + ",";
      if (menu_item_orls.indexOf(menu_item_orl) > -1) {
        listRet.add(map);
      }
    }

    return listRet;
  }

  // 선택된 메뉴 검색
  public List getMenuItemListSelected(Map<String,String> menuMap,String get_depth) throws Exception {
    List listRet = new ArrayList();

    String sel_menu_orl = menuMap.get("menu_orl");
    String sel_depth = StringUtils.defaultIfEmpty( menuMap.get("depth") , "" );
    String sel_menu_item_orl = StringUtils.defaultIfEmpty( menuMap.get("menu_item_orl") , "" );
    String sel_parent_orl = StringUtils.defaultIfEmpty( menuMap.get("parent_orl") , "0" );
    String sel_menu_item_orls = StringUtils.defaultIfEmpty( menuMap.get("menu_item_orls") , "");

    int sel_parent_orl_int = NumberUtils.stringToInt(sel_parent_orl,0);
/*
    log.debug("[MENU] sel_menu_item_orl : " + sel_menu_item_orl);
    log.debug("[MENU] sel_menu_orl : " + sel_menu_orl);
    log.debug("[MENU] sel_parent_orl : " + sel_parent_orl);
    log.debug("[MENU] sel_menu_item_orls : " + sel_menu_item_orls);
    log.debug("[MENU] get_depth : " + get_depth);
*/
    List list = getMenuCacheList(sel_menu_orl);
    int count = list.size();
    for (int i = 0; i < count; i++) {
      Map<String,String> map = (Map<String,String>) list.get(i);
      String name = map.get("name");
      String href = map.get("href");
      String menu_item_orl = map.get("menu_item_orl");
      String parent_orl = map.get("parent_orl");
      String depth = map.get("depth");

      String is_mobile = map.get("is_mobile");
      map.put("is_mobile",is_mobile);
      map.put("menu_selected","");

      boolean is = false;

      if (StringUtils.isNotEmpty(get_depth)) {
        if (get_depth.equals(depth)) { is = true; }
      } else {

        // 최상위가선택된 경우 자식들 호출, 자식이 선택된 경우 해당 그룹 노드들 호출, 자식이 선택된 경우 상위 부모호출,최상위가 선택된 경우 최상위 노드 호출
        if ( (sel_menu_item_orl.equals(parent_orl) && sel_parent_orl_int == 0) || (sel_parent_orl.equals(parent_orl) && sel_parent_orl_int > 0) || (sel_parent_orl.equals(menu_item_orl) && sel_parent_orl_int > 0) || (sel_menu_item_orl.equals(menu_item_orl) && sel_parent_orl_int == 0) ) { 
          is = true; 
        }
      }


      if (is) {
        String sel_menu_item_orls_chk = "," + sel_menu_item_orls;
        boolean iss = StringUtils.isNotEmpty(sel_menu_item_orls) ? (sel_menu_item_orls_chk.indexOf("," + menu_item_orl + ",") > -1) : false;
        if (iss) map.put("menu_selected","selected");
        listRet.add(map);
      }
/*
if ( ( get_depth.equals(depth) &&  sel_parent_orl.equals(parent_orl) ) || (sel_parent_orl_int > 0 && sel_parent_orl.equals(menu_item_orl) ) ) {

      // 부모 : sel_parent_orl.equals(menu_item_orl) , 자신이 속한 그룹들 : sel_parent_orl.equals(parent_orl)
//      if ( (depth.equals(get_depth) && sel_parent_orl.equals(parent_orl) ) || get_depth.equals("0") || sel_parent_orl.equals(menu_item_orl)) {

        boolean iss = StringUtils.isNotEmpty(sel_menu_item_orls) ? (sel_menu_item_orls.indexOf(menu_item_orl + ",") > -1) : false;
        if (iss) map.put("menu_selected","selected");
        listRet.add(map);
      }

*/
    }

    return listRet;
  }

  // 선택된 메뉴 검색
  public Map getMenuItemSelected(String menu_orl, HttpServletRequest request) throws Exception {
    Map mapRet = new HashMap();

    String serlet_path = request.getServletPath();
    serlet_path = StringUtils.isNotEmpty(serlet_path) ? serlet_path.replaceAll("^\\/?","") : "";

    String param = "";
    Enumeration paramter = request.getParameterNames();
    if (paramter != null) {
       while(paramter.hasMoreElements()){
        String para_name = (String)paramter.nextElement();
        String para_value = request.getParameter(para_name);
        para_value = StringUtils.defaultString(para_value,"");
        param += "," + para_name + "=" + para_value + ",";
      }
    }
/*
    log.debug("[MENU] serlet_path : " + serlet_path);
    log.debug("[MENU] param : " + param);
*/
    List list = getMenuCacheList(menu_orl);
    int count = list.size();

    for (int i = 0; i < count; i++) {
      Map<String,String> map = (Map<String,String>) list.get(i);
      String select_query = map.get("select_query");
      if (getMenuItemSelected(serlet_path,param,select_query)) mapRet = map;
    }

    mapRet.put("menu_orl",menu_orl);
    return mapRet;
  }

  // 선택된 메뉴 검색
  public boolean getMenuItemSelected(String serlet_path,String param,String select_query) throws Exception {
    boolean is = false;
    boolean is_and = true;
    if (StringUtils.isNotEmpty(select_query) && StringUtils.isNotEmpty(param)) {
//      log.debug("[MENU SEARCH] select_query : " + select_query);

      JSONObject objJson = JSONObject.fromObject(select_query);
      List keyword = (List) objJson.get("keyword");
      String condition = StringUtils.defaultIfEmpty( (String) objJson.get("condition") , "and" );
      String compare = StringUtils.defaultIfEmpty( (String) objJson.get("compare") , "equals" );
      /*
      log.debug("[MENU SEARCH] condition : " + condition);
      log.debug("[MENU SEARCH] compare : " + compare);
*/
      Iterator keywords = keyword.iterator();
      boolean or = condition.equals("or");
      while(keywords.hasNext()) {
        String str = (String) keywords.next();

        String chk_str = str.replaceAll("^[/?]+","");
        String p = "([?]+)(.*)";
        Pattern patten = Pattern.compile(p,Pattern.MULTILINE);
        if (patten.matcher(chk_str).find()) {
          String r = chk_str.replaceAll(p,"$1");
          String o = chk_str.replaceAll(r,"");
          r = r.replaceAll("[/?]+$",""); // 경로
          o = o.replaceAll("^[/?]+",""); // 파라메터
          
//          log.debug("[MENU SEARCH] Pattern : " + r + "#######" + o);

          if (compare.equals("equals")) {
            if ( (param.indexOf(o + ",") > -1 && serlet_path.equals(r) ) && or) { is = true; break; } // or 조건
            if ( (param.indexOf(o + ",") == -1 || !serlet_path.equals(r) ) && !or) { is_and = false; break; } // and 조건
          } else {
            if ( (param.indexOf("," + o) > -1 && serlet_path.equals(r) ) && or) { is = true; break; } // or 조건
            if ( (param.indexOf("," + o) == -1 || !serlet_path.equals(r) ) && !or) { is_and = false; break; } // and 조건
          }

        } else {
          if (compare.equals("equals")) {
          if (param.indexOf(str + ",") > -1 && or) { is = true; break; }
          if (param.indexOf(str + ",") == -1 && !or) { is_and = false; break; }
          } else {
          if (param.indexOf("," + str) > -1 && or) { is = true; break; }
          if (param.indexOf("," + str) == -1 && !or) { is_and = false; break; }
          }

        }


      }

      if (!or) is = is_and;
//      log.debug("[MENU SEARCH] is : " + is);
    }
    return is;

  }

}