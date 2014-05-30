/*
 * FileView.java 2011.12.07
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.file;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import com.syaku.core.util.*;

public class FileView extends ActionFile {
  private Logger log = Logger.getLogger( this.getClass() );

  /*
3rd module : options S , S_MODULE_ORL
<@s.action name="dispFileUploadInsert" executeResult="true">
<@s.param name="interlock">true</@s.param>
<@s.param name="seq">1</@s.param>
<@s.param name="target_orl">${O.objDocument.document_orl?if_exists}</@s.param>
<@s.param name="editor">objEditor</@s.param>
<@s.param name="file_once_size">${MM.options_file_size?if_exists}</@s.param>
<@s.param name="file_size">${MM.options_file_size?if_exists}</@s.param>
<@s.param name="file_limit">${MM.options_file_limit?if_exists}</@s.param>
<@s.param name="file_type">${MM.options_file_type?if_exists}</@s.param>
<@s.param name="file_multi">true</@s.param>
</@s.action>
  */
  public String dispFileUploadInsert() throws Exception {

    // 초기화
    String file_multi = "true";
    String file_once_size = StringUtils.defaultIfEmpty( (String) MM.get("options_file_once_size") , "0" );
    String file_size = StringUtils.defaultIfEmpty( (String) MM.get("options_file_size") , "0" );
    String file_limit = StringUtils.defaultIfEmpty( (String) MM.get("options_file_limit") , "0" );
    String file_type = StringUtils.defaultIfEmpty( (String) MM.get("options_file_type") , "" );

    String module_orl = MODULE_ORL;

    param.setParameters(parameters);
    String target_orl = param.arrayValue("target_orl");
    String seq = param.arrayValue("seq");
    String editor = param.arrayValue("editor");

    file_once_size = param.arrayValue("file_once_size" , file_once_size);
    file_size = param.arrayValue("file_size" , file_size);
    file_limit = param.arrayValue("file_limit" , file_limit);
    file_type = param.arrayValue("file_type" , file_type);
    file_multi = param.arrayValue("file_multi" , file_multi);

    MM.put("options_file_once_size",file_once_size);
    MM.put("options_file_size",file_size);
    MM.put("options_file_limit",file_limit);
    MM.put("options_file_type",file_type);
    MM.put("options_file_multi",file_multi);

    List list = new ArrayList();
    FileBean objFile = new FileBean();
    Map mapSch = new HashMap();

    if (StringUtils.isNotEmpty(target_orl) && !StringUtils.equals(target_orl,"0")) {

      if ( StringUtils.equals("true",file_multi) ) {
        mapSch.put("module_orl", module_orl);
        mapSch.put("target_orl", target_orl);
        mapSch.put("seq", seq);
        list = (List) daoFile.sqlFileItem(mapSch);
      } else {
        mapSch = new HashMap();
        mapSch.put("module_orl", module_orl);
        mapSch.put("target_orl", target_orl);
        mapSch.put("seq", seq);
        objFile = (FileBean) daoFile.sqlFileObject(mapSch);
        modSkinChange("file.single.upload.insert.html");
      }
    } else {

      if (StringUtils.isNotEmpty(module_orl) && !StringUtils.equals(module_orl,"0")) {
        mapSch = new HashMap();
        mapSch.put("module_orl", module_orl);
        mapSch.put("target_orl", "0");
        mapSch.put("sid", MEI_SID);
        mapSch.put("seq", seq);
        daoFile.sqlFileSidDelete(mapSch);
      }

    }

    O.put( "target_orl" , target_orl);
    O.put( "seq" , seq);
    O.put( "editor" , editor);
    O.put( "listFile" , list );
    O.put( "objFile" , objFile );

    MOD_FTL_LAYOUT = MOD_FTL_MODULE; // 모듈 스킨 템플릿만 노출

    return SUCCESS;
  }

}
