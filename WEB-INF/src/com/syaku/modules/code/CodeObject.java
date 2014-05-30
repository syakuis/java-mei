package com.syaku.modules.code;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.configuration.*;
import org.apache.commons.lang.*;
import org.apache.commons.io.*;

import com.syaku.core.*;
import com.syaku.core.util.*;
import com.syaku.core.common.*;
import com.syaku.core.xml.*;

public class CodeObject extends CodeAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public final Configuration meiConfig = ConfigUtils.getProperties("mei.properties"); // mei 정보
  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/code/info.properties");
  public final String CODE_CACHE_PATH = meiConfig.getString("mei.path.absolute_relative") + meiConfig.getString("mei.path.cache") + "/code";

  public void getCodeItemInsert(ParameterUtils param) throws Exception {
    String code_orl = param.value("code_orl");
    String code_item_orl = param.value("code_item_orl");
    String parent_orl = param.valueIfEmpty("parent_orl","0");
    String num = param.valueIfEmpty("num","0");
    String name = param.value("name");
    String color = param.value("color");

    CodeItemBean obj = new CodeItemBean();
    obj.setCode_item_orl(code_item_orl);
    obj.setParent_orl(parent_orl);
    obj.setNum(num);
    obj.setCode_orl(code_orl);
    obj.setName(name);
    obj.setColor(color);
    obj.setRdate(DateUtils.date("yyyyMMddHHmmss"));

    Map mapSch = new HashMap();

    if (StringUtils.isEmpty(code_item_orl)) {
      if (StringUtils.equals(parent_orl,"0") || StringUtils.isEmpty(parent_orl)) {
        mapSch.put("code_orl",code_orl);
        mapSch.put("parent_orl","0");
        num = (String) sqlCodeItemNumMax(mapSch);
        num = ObjectUtils.toString(NumberUtils.stringToInt(num) + 1);
      } else {
        num = ObjectUtils.toString(NumberUtils.stringToInt(num) + 2);
        mapSch.put("code_orl",code_orl);
        mapSch.put("num",num);
        sqlCodeItemNumSortUpdate(mapSch); // 정렬
      }

      obj.setNum(num);

      sqlCodeItemInsert(obj);
    } else {
      sqlCodeItemUpdate(obj);
    }

  }

  public void getCodeItemMove(ParameterUtils param) throws Exception {
    String code_orl = param.value("code_orl");
    String code_item_orl = param.value("code_item_orl");
    String parent_orl = param.value("parent_orl");
    String num = param.value("num");

    Map mapSch = new HashMap();

    if (StringUtils.equals(parent_orl,"0") && (StringUtils.equals(num,"0") || StringUtils.isEmpty(num))) { // 부모의 최상위 인 경우
      num = "0";
    } else if (!StringUtils.equals(parent_orl,"0") && (StringUtils.equals(num,"0") || StringUtils.isEmpty(num))) { // 자식의 최상위 인 경우
      mapSch.put("code_orl",code_orl);
      mapSch.put("code_item_orl",parent_orl);
      CodeItemBean codeitembean = (CodeItemBean) sqlCodeItemObject(mapSch);
      num = codeitembean.getNum();
    } else {
      mapSch.put("code_item_orl",num);
      mapSch.put("code_orl",code_orl);
      num = (String) sqlCodeItemNumMove(mapSch);
    }
    
    mapSch = new HashMap();
    mapSch.put("code_orl",code_orl);
    mapSch.put("num",num);
    sqlCodeItemNumSortUpdate(mapSch); // 정렬

    mapSch = new HashMap();
    num = ObjectUtils.toString(NumberUtils.stringToInt(num) + 1);
    mapSch.put("num",num);
    mapSch.put("code_orl",code_orl);
    mapSch.put("code_item_orl",code_item_orl);
    mapSch.put("parent_orl",parent_orl);
    sqlCodeItemMoveUpdate(mapSch);
  }


  public List getCodeItemList(Map map) throws Exception {
    List listRet = new ArrayList();

    List list = (List) sqlCodeItemList(map);
    int count = list.size();

    for (int i = 0; i < count; i++ ) {
      CodeItemBean obj= (CodeItemBean) list.get(i);
      String code_item_orl = obj.getCode_item_orl();
      String parent_orl = obj.getParent_orl();
      String num = obj.getNum();
      String code_orl = obj.getCode_orl();
      String name = obj.getName();
      String color = obj.getColor();
      String rdate = obj.getRdate();

      Map mapRet = new HashMap();
      mapRet.put("code_item_orl",code_item_orl);
      mapRet.put("parent_orl",parent_orl);
      mapRet.put("num",num);
      mapRet.put("code_orl",code_orl);
      mapRet.put("name",name);
      mapRet.put("color",color);
      mapRet.put("rdate",rdate);

      listRet.add(i,mapRet);
    }

    return listRet;

  }


  public void getCodeCacheFile(String code_orl) throws Exception {

    Map mapSch = new HashMap();
    mapSch.put("code_orl",code_orl);

    CodeBean objCode = (CodeBean) sqlCodeObject(mapSch);
    String code_id = objCode.getCode_id();

    String name = CODE_CACHE_PATH + "/code." + code_id + ".xml";
    String name2 = CODE_CACHE_PATH + "/code_list." + code_id + ".xml";

    List list = (List) getCodeItemList(mapSch);

    if (list.size() > 0) {
      CreateTreeXml xml = new CreateTreeXml(list,"code_item_orl","code_item_orls");
      // 폴더 생성
      File dir = new File(CODE_CACHE_PATH);
      if(!dir.exists()){ dir.mkdirs(); }
      xml.file(name,name2);
    } else {
      FileUtils.forceDelete(new File(name));
      FileUtils.forceDelete(new File(name2));
    }

  }

  public String getXmlCache(String code_id,String mod) throws Exception {
    // list
    String category_xml = CODE_CACHE_PATH + "/code_list." + code_id + ".xml";

    if (StringUtils.equals(mod,"depth")) {
      category_xml = CODE_CACHE_PATH + "/code." + code_id + ".xml";
    }
    
    File xml = new File(category_xml);
    if ( xml.exists() ) {
      return FileUtils.readFileToString(new File(category_xml),"utf-8");
    }

    return null;
  }

}
