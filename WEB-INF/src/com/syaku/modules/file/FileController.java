/*
 * FileController.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.file;

import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.io.FileUtils;

import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class FileController extends ActionFile {
  private Logger log = Logger.getLogger( this.getClass() );

  public String contentType = "application/octet-stream";
  public String contentDisposition;
  public long contentLength;
  public String getFileDownload() throws Exception {
    String file_orl = param.value("file_orl");

    Map mapSch = new HashMap();
    mapSch.put("file_orl", file_orl);
    FileBean obj = (FileBean) daoFile.sqlFileObject(mapSch);

    String filename = obj.getFilename();
    String re_filename = obj.getRe_filename();
    String folder = obj.getFolder();

    String download = daoFile.MEI_PATH_ATTACH_ABSOLUTE + folder + "/" + re_filename;

    File file = new File(download);
    contentLength = file.length();
    contentDisposition = "attachment; filename=" + URLEncoder.encode(filename,"utf-8");

    inputStream = new FileInputStream(download);
    return "file";

  }


  public String procFileDelete() throws Exception {
    String file_orl = param.value("file_orl");

    try {
      sqlMap.startTransaction();

      HashMap mapSch = new HashMap();
      mapSch.put("file_orl",file_orl);
      daoFile.getFileDeleteArray(mapSch);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.toString().replaceAll("\n",""));
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

}
