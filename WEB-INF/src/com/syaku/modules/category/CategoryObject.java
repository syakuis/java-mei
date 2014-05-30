/*
 * CategoryObject.java 2011.06.09
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.category;

import java.util.*;
import java.io.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;
import org.apache.commons.io.*;
import org.apache.commons.digester.*;

import com.syaku.core.util.*;
import com.syaku.core.xml.*;

public class CategoryObject extends CategoryAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public Configuration meiConfig = ConfigUtils.getProperties("mei.properties"); // mei 정보
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/category/info.properties");
  public String CATEGORY_CACHE_PATH = meiConfig.getString("mei.path.absolute") + meiConfig.getString("mei.path.relative") + meiConfig.getString("mei.path.cache") + "/category";

  // 분류는 Map 리스트 형식으로 변환
  public List getCategoryList(Map map) throws Exception {
    List listCategory = new ArrayList();

    List list = sqlCategoryList(map);
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      CategoryBean obj= (CategoryBean) list.get(i);

      String category_orl = obj.getCategory_orl();
      String parent_orl = obj.getParent_orl();
      String category_seq = obj.getCategory_seq();
      String module_orl = obj.getModule_orl();
      String title = obj.getTitle();
      String rdate = obj.getRdate();

      String category_count = obj.getCount();

      HashMap mapRet = new HashMap();
      mapRet.put("category_orl",category_orl);
      mapRet.put("parent_orl",parent_orl);
      mapRet.put("category_seq",category_seq);
      mapRet.put("module_orl",module_orl);
      mapRet.put("title",title);
      mapRet.put("rdate",rdate);
      mapRet.put("count",category_count);

      listCategory.add(i,mapRet);
    }

    return listCategory;

  }

  // 분류 등록 및 수정
  public void getCategoryInsert(ParameterUtils param) throws Exception { // 2011.06.29
    String module_orl = param.value("module_orl");
    String category_orl = param.value("category_orl");
    String parent_orl = param.value("parent_orl","0");
    String category_seq = param.value("category_seq","0");
    String title = param.value("title");

    Map mapSch = new HashMap();
    mapSch.put("category_orl",category_orl);
    mapSch.put("parent_orl",parent_orl);
    mapSch.put("category_seq",category_seq);
    mapSch.put("module_orl",module_orl);
    mapSch.put("title",title);

    getCategoryInsert(mapSch);
  }

  public void getCategoryInsert(Map map) throws Exception {
    String category_orl = StringUtils.defaultString( (String) map.get("category_orl") , "");
    String parent_orl = StringUtils.defaultString( (String) map.get("parent_orl") , "0" );
    String category_seq = StringUtils.defaultString( (String) map.get("category_seq") , "0" );
    String module_orl = (String) map.get("module_orl");
    String title = (String) map.get("title");

    Map mapSch = new HashMap();

    CategoryBean data = new CategoryBean();
    data.setCategory_orl(category_orl);
    data.setParent_orl(parent_orl);
    data.setCategory_seq(category_seq);
    data.setModule_orl(module_orl);
    data.setTitle(title);
    data.setRdate(DateUtils.date("yyyyMMddHHmmss"));
    if (StringUtils.isEmpty(category_orl)) {
      if (parent_orl.equals("0") || StringUtils.isEmpty(parent_orl)) {
        mapSch.put("module_orl",module_orl);
        mapSch.put("parent_orl","0");
        category_seq = (String) sqlCategoryMaxSeq(mapSch);
      } else {
        mapSch.put("module_orl",module_orl);
        mapSch.put("category_seq",category_seq);
        sqlCategoryUpdateSort(mapSch); // 정렬
      }

      category_seq = ObjectUtils.toString(NumberUtils.stringToInt(category_seq) + 1);
      data.setCategory_seq(category_seq);

      sqlCategoryInsert(data);
    } else {
      sqlCategoryUpdate(data);
    }

  }

  // 분류 이동
  public void getCategoryMove(ParameterUtils param) throws Exception {
    String category_orl = param.value("category_orl");
    String parent_orl = param.value("parent_orl");
    String category_seq = param.value("category_seq");
    String module_orl = param.value("module_orl");

    Map mapSch = new HashMap();

    if (StringUtils.equals(parent_orl,"0") && (StringUtils.equals(category_seq,"0") || StringUtils.isEmpty(category_seq))) { // 부모의 최상위 인 경우
      category_seq = "0";
    } else if (!StringUtils.equals(parent_orl,"0") && (StringUtils.equals(category_seq,"0") || StringUtils.isEmpty(category_seq))) { // 자식의 최상위 인 경우
      mapSch.put("module_orl",module_orl);
      mapSch.put("category_orl",parent_orl);
      CategoryBean categorybean = (CategoryBean) sqlCategoryObject(mapSch);
      category_seq = categorybean.getCategory_seq();
    } else {
      mapSch.put("category_orl",category_seq);
      mapSch.put("module_orl",module_orl);
      category_seq = (String) sqlCategoryMoveSeq(mapSch);
    }
    
    mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    mapSch.put("category_seq",category_seq);
    sqlCategoryUpdateSort(mapSch); // 정렬

    mapSch = new HashMap();
    category_seq = ObjectUtils.toString(NumberUtils.stringToInt(category_seq) + 1);
    mapSch.put("category_seq",category_seq);
    mapSch.put("module_orl",module_orl);
    mapSch.put("category_orl",category_orl);
    mapSch.put("parent_orl",parent_orl);
    sqlCategoryUpdateMove(mapSch);
  }

  // xml 캐쉬파일을 읽어서 맵으로 반환
  public Map getCategoryMapCache(String target_orl,String category_orl) throws Exception {
    String name = CATEGORY_CACHE_PATH + "/category_list_" + target_orl + ".xml";
    XMLConfiguration config = ConfigUtils.getXml(name);

    HashMap mapRet = new HashMap();
    
    if (config != null) {
      List list = config.configurationsAt("item");
      int i = 0;

      for(Iterator it = list.iterator(); it.hasNext();) {
        HierarchicalConfiguration obj = (HierarchicalConfiguration) it.next();

        if (category_orl.equals(obj.getString("[@category_orl]"))) {
          mapRet.put("category_orl",obj.getString("[@category_orl]"));
          mapRet.put("parent_orl",obj.getString("[@parent_orl]"));
          mapRet.put("category_seq",obj.getString("[@category_seq]"));
          mapRet.put("module_orl",obj.getString("[@module_orl]"));
          mapRet.put("title",obj.getString("[@title]"));
          mapRet.put("rdate",obj.getString("[@rdate]"));
          mapRet.put("count",obj.getString("[@count]"));
          mapRet.put("depth",obj.getString("[@depth]"));
          mapRet.put("category_orls",obj.getString("[@category_orls]"));
          break;
        }

      }

    }

    return mapRet;

  }

  // xml 캐쉬파일을 읽어서 리스트 맵으로 반환
  public List getCategoryListMap(String target_orl,String parent_orl) throws Exception {
    List listCategory = new ArrayList();
    String name = CATEGORY_CACHE_PATH + "/category_list_" + target_orl + ".xml";
    XMLConfiguration config = ConfigUtils.getXml(name);
    if (config != null) {
      List list = config.configurationsAt("item");
      int i = 0;

      for(Iterator it = list.iterator(); it.hasNext();) {
        HierarchicalConfiguration obj = (HierarchicalConfiguration) it.next();
        String parent_orl2 = obj.getString("[@parent_orl]");
        
        if (StringUtils.isNotEmpty(parent_orl) && !StringUtils.equals(parent_orl2,parent_orl)) {
          continue;
        }

        HashMap mapRet = new HashMap();

        mapRet.put("category_orl",obj.getString("[@category_orl]"));
        mapRet.put("parent_orl",obj.getString("[@parent_orl]"));
        mapRet.put("category_seq",obj.getString("[@category_seq]"));
        mapRet.put("module_orl",obj.getString("[@module_orl]"));
        mapRet.put("title",obj.getString("[@title]"));
        mapRet.put("rdate",obj.getString("[@rdate]"));
        mapRet.put("count",obj.getString("[@count]"));

        mapRet.put("depth",obj.getString("[@depth]"));
        mapRet.put("category_orls",obj.getString("[@category_orls]"));

        listCategory.add(i,mapRet);
        i++;
      }
    }

    return listCategory;
  }

  // xml 읽기
  public String getCategoryXml(String module_orl,String mod) throws Exception {
    String category_xml = CATEGORY_CACHE_PATH + "/category_list_" + module_orl + ".xml";

    if (StringUtils.equals(mod,"depth")) {
      category_xml = CATEGORY_CACHE_PATH + "/category_" + module_orl + ".xml";
    }
    File file = new File(category_xml);

    if ( !file.exists() ) {
      log.error("[#MEI CategoryObject.getCategoryXml]" + category_xml + " 파일이 존재하지 않습니다.");
      return null;
    }

    String xml = FileUtils.readFileToString(file,"utf-8");
    return xml;
  }
  
  // 디비 데이터 읽어서 xml 반환
  public Document getCategoryObject2Xml(String module_orl) throws Exception { // 2013-05-30
    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    List list = getCategoryList(mapSch);

    CreateTreeXml xml = new CreateTreeXml(list,"category_orl","category_orls");
    Element element = xml.xmlBulider();
    Document document = new Document(element);
    return document;
  }

  // 디비의 데이터를 읽어 xml 파일을 생성한다.
  public void createCategoryObject2Xml(String module_orl) throws Exception {
    String name = CATEGORY_CACHE_PATH + "/category_" + module_orl + ".xml";
    String name2 = CATEGORY_CACHE_PATH + "/category_list_" + module_orl + ".xml";

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    List list = (List) getCategoryList(mapSch);

    if (list.size() > 0) {
      CreateTreeXml xml = new CreateTreeXml(list,"category_orl","category_orls");
      // 폴더 생성
      File dir = new File(CATEGORY_CACHE_PATH);
      if(!dir.exists()){ dir.mkdirs(); }
      xml.file(name,name2);
    } else {
      FileUtils.forceDelete(new File(name));
      FileUtils.forceDelete(new File(name2));
    }

  }

  public String getXmlCache(String module_orl,String mod) throws Exception {
    // list
    String category_xml = CATEGORY_CACHE_PATH + "/category_list_" + module_orl + ".xml";

    if (StringUtils.equals(mod,"depth")) {
      category_xml = CATEGORY_CACHE_PATH + "/category_" + module_orl + ".xml";
    }

    return FileUtils.readFileToString(new File(category_xml),"utf-8");
  }

  // 분류 삭제
  public void getCategoryDelete(Map map) throws Exception {
    sqlCategoryDelete(map);
    sqlCategoryCounterDelete(map);
  }

  // 모듈 분류 삭제 : 분류와 캐쉬파일을 삭제한다.
  public void getCategoryModuleDelete(String module_orl) throws Exception {

    try {
    Map<String,String> mapSch = new HashMap<String,String>();
    mapSch.put("module_orl",module_orl);
    sqlCategoryModuleDelete(mapSch);
    sqlCategoryCounterModuleDelete(mapSch);

    String name = CATEGORY_CACHE_PATH + "/category_" + module_orl + ".xml";
    String name2 = CATEGORY_CACHE_PATH + "/category_list_" + module_orl + ".xml";
    File file = new File(name);
    if (file.exists()) { file.delete(); }
    file = new File(name2);
    if (file.exists()) { file.delete(); }

    } catch (Exception e) {
      log.debug(e.toString());
    }

  }

  public Long getCategoryCounterCountObject(String module_orl,String category_orl) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    mapSch.put("category_orl",category_orl);

    return sqlCategoryCounterCountObject(mapSch);
  }

  public void getCategoryCounterInsert(String module_orl,String category_orl,Long count) throws Exception {
    String reg_date = DateUtils.date("yyyyMMddHHmmss");

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    mapSch.put("category_orl",category_orl);
    long total = sqlCategoryCounterCount(mapSch);

    CategoryCounterBean md = new CategoryCounterBean();
    md.setModule_orl(module_orl);
    md.setCategory_orl(category_orl);
    md.setCount("" + count);
    md.setReg_date(reg_date);
    if (total == 0) {
      sqlCategoryCounterInsert(md);
    } else {
      md.setLast_update(reg_date);
      sqlCategoryCounterUpdate(md);
    }

  }

}