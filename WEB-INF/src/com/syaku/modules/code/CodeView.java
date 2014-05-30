package com.syaku.modules.code;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.*;
import com.syaku.core.util.*;

public class CodeView extends ActionCode {
  private Logger log = Logger.getLogger( this.getClass() );

public String dispCodeList() throws Exception  {

 // 목록 수
  int page_row = NumberUtils.stringToInt( (String) MM.get("options_list_count") , meiConfig.getInt("mei.paging.row") );
  // 페이지 수
  int page_link = NumberUtils.stringToInt( (String) MM.get("options_page_count") , meiConfig.getInt("mei.paging.link") );

  int page = meiConfig.getInt("mei.paging.now");
  paging = new PageNavigator(request,page,page_row,page_link);

  Map mapSch = new HashMap();
  long total = daoCode.sqlCodeCount(mapSch);
  paging.setTotal_count(total);
  paging.setPageIndex();

  mapSch.put("page_start", paging.getStart_idx());
  mapSch.put("page_row", paging.getPage_row());

  String code_orl = param.value("code_orl","");

  mapSch.put("code_orl",code_orl);
  O.put("listCode", (List) daoCode.sqlCodeList(mapSch));

  return SUCCESS;
  }

  public String dispCodeInsert() throws Exception  {

    String code_orl = param.value("code_orl","");
    String member_orl = StringUtils.defaultIfEmpty((String) session.get("_MEI_MEMBER_ORL"),"0");

    CodeBean objCode = new CodeBean();
    Map mapSch = new HashMap();

    String mid = "";
    if (StringUtils.isNotEmpty(code_orl) ) {
      mapSch.put("code_orl",code_orl);
      objCode = (CodeBean) daoCode.sqlCodeObject(mapSch);
      String module_orl = objCode.getModule_orl();

      mapSch = new HashMap();
      mapSch.put("module_orl",module_orl);
      mid = daoModule.mid(mapSch);
    }

    O.put("objCode", objCode);
    O.put("mid", mid);
    return SUCCESS;
  }

  public String dispCodeItemInsert() throws Exception {
    String code_orl = param.value("code_orl");
    String code_item_orl = param.value("code_item_orl","");
    String parent_orl = param.value("parent_orl","0");
    String num = param.value("num","0");

    O.put("code_orl", code_orl);
    O.put("code_item_orl", code_item_orl);
    O.put("parent_orl", parent_orl);
    O.put("num", num);

    CodeItemBean object = new CodeItemBean();
    O.put("objCodeItem", object);

    if (!StringUtils.isEmpty(code_item_orl)) {
      Map mapSch = new HashMap();
      mapSch.put("code_item_orl",code_item_orl);

      O.put("objCodeItem", (Object) daoCode.sqlCodeItemObject(mapSch));
    }

    MOD_FTL_LAYOUT = MOD_FTL_MODULE; // 모듈 스킨 템플릿만 노출

    return SUCCESS;
  }


}

