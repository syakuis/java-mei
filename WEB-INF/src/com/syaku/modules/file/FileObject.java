/*
 * FileObject.java 2011.01.01
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

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;
import org.apache.commons.configuration.*;
import org.apache.commons.io.*;

import com.syaku.core.util.*;
import com.syaku.modules.module.*;

public class FileObject extends FileAccess {
  private Logger log = Logger.getLogger( this.getClass() );

  public Configuration meiConfig = ConfigUtils.getProperties("mei.properties"); // mei 정보
  public final String MEI_PATH_ABSOLUTE = meiConfig.getString("mei.path.absolute");
  public final String MEI_PATH_RELATIVE = meiConfig.getString("mei.path.relative");
  public final String MEI_PATH_ABSOLUTE_RELATIVE = meiConfig.getString("mei.path.absolute_relative");

  public final String MEI_PATH_ATTACH_ABSOLUTE = MEI_PATH_ABSOLUTE_RELATIVE + meiConfig.getString("mei.path.attach");
  public final String MEI_PATH_ATTACH_RELATIVE = MEI_PATH_RELATIVE + meiConfig.getString("mei.path.attach");

  public final String MEI_PATH_THUMBNAIL_ABSOLUTE = MEI_PATH_ABSOLUTE_RELATIVE + meiConfig.getString("mei.path.thumbnail");
  public final String MEI_PATH_THUMBNAIL_RELATIVE = MEI_PATH_RELATIVE + meiConfig.getString("mei.path.thumbnail");

  public final Configuration modConfig = ConfigUtils.getProperties("com/syaku/modules/file/info.properties");
  public final String MEI_MOD_FTL_PATH_ABSOLUTE = MEI_PATH_ABSOLUTE_RELATIVE + modConfig.getString("mei.module.path.relative") + "/ftl";

  // getFileUpdateTarget
  public void getFileUpdateTarget(Map map) throws Exception { 
    sqlFileUpdateTarget(map);
    
    String module_orl = (String) map.get("module_orl");
    String target_orl = (String) map.get("target_orl");
    String seq = (String) map.get("seq");
    if (StringUtils.isNotEmpty(target_orl) && !StringUtils.equals(target_orl,"0")) {
      getFileCounterInsert(module_orl,target_orl,seq);
    }
  }

  
  public void getFileDeleteArray(Map map) throws Exception {
    String file_orl = (String) map.get("file_orl");

    if (StringUtils.isEmpty(file_orl)) { throw new Exception("삭제 대상이 없습니다. file_orl : EMPTY "); }

    Map mapSch = new HashMap();

    String[] file_orl_ar = file_orl.split("\\|");

    String module_orl = "";
    String target_orl = "";
    String seq = "";

    int cnt = file_orl_ar.length;
    for (int i =0; i < cnt; i++) { 
      mapSch.clear();
      mapSch.put("file_orl",file_orl_ar[i]);
      FileBean objFile = (FileBean) sqlFileObject(mapSch);

      if (objFile == null) { throw new Exception("삭제 대상 자료가 없습니다. File Object : NULL "); }

      module_orl = objFile.getModule_orl();
      target_orl = objFile.getTarget_orl();
      seq = objFile.getSeq();
      String folder = objFile.getFolder();
      String type = objFile.getType();
      String filename = objFile.getRe_filename();
      String extension = objFile.getExtension();
      
      // 파일 삭제
      String file_path = MEI_PATH_ATTACH_ABSOLUTE + folder + "/" + filename;
      File file = new File(file_path);
      if (file.exists()) { file.delete(); }
    }
    
    if (cnt > 0) {
      mapSch.clear();
      mapSch.put("file_orl_array",file_orl_ar);
      sqlFileDeleteArray(mapSch);

      getFileCounterInsert(module_orl,target_orl,seq);
    }

  }

  public void getFileDeleteTarget(Map map) throws Exception {
    String module_orl = (String) map.get("module_orl");
    String target_orl = (String) map.get("target_orl");
    String seq = (String) map.get("seq");

    if (!StringUtils.isEmpty(module_orl) && !StringUtils.isEmpty(target_orl) && !StringUtils.isEmpty(seq)) {
      Map mapSch = new HashMap();

      List list = sqlFileItem(map);
      int count = list.size();

      for (int i = 0; i < count; i++ ) {
        FileBean objFile= (FileBean) list.get(i);
        String folder = objFile.getFolder();
        String type = objFile.getType();
        String filename = objFile.getRe_filename();
        String extension = objFile.getExtension();

        String file_path = MEI_PATH_ATTACH_ABSOLUTE + folder + "/" + filename;
        File file = new File(file_path);
        if (file.exists()) { file.delete(); }

      }

      sqlFileDeleteTarget(map);
      sqlFileCounterDelete(map);
    }

  }

  public Long getFileCounterCountObject(String module_orl,String target_orl,String seq) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    mapSch.put("target_orl",target_orl);
    mapSch.put("seq",seq);

    return sqlFileCounterCountObject(mapSch);
  }

  public void getFileCounterInsert(String module_orl,String target_orl,String seq) throws Exception {
    String reg_date = DateUtils.date("yyyyMMddHHmmss");

    Map mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    mapSch.put("target_orl",target_orl);
    mapSch.put("seq",seq);
    long total = sqlFileCounterCount(mapSch);
    long count = sqlFileCount(mapSch);

    FileCounterBean md = new FileCounterBean();
    md.setModule_orl(module_orl);
    md.setTarget_orl(target_orl);
    md.setSeq(seq);
    md.setCount("" + count);
    md.setReg_date(reg_date);
    if (total == 0) {
      sqlFileCounterInsert(md);
    } else {
      md.setLast_update(reg_date);
      sqlFileCounterUpdate(md);
    }

  }

  public void getFileModuleDelete(String module_orl) throws Exception {
    HashMap mapSch = new HashMap();
    mapSch.put("module_orl",module_orl);
    sqlFileModuleDelete(mapSch);
    sqlFileCounterModuleDelete(mapSch);
    FileUtils.deleteDirectory(new File(MEI_PATH_ATTACH_ABSOLUTE + "/" + module_orl));
  }

  public String getTypeCommon(String type) {
    String image = modConfig.getString("mei.module.file.type.image");
    String zip = modConfig.getString("mei.module.file.type.zip");
    String movie = modConfig.getString("mei.module.file.type.movie");
    String music = modConfig.getString("mei.module.file.type.music");
    String document = modConfig.getString("mei.module.file.type.document");

    if (StringUtils.containsIgnoreCase(image,type)) {
      return "image";
    } else if (StringUtils.containsIgnoreCase(zip,type)) {
      return "zip";
    } else if (StringUtils.containsIgnoreCase(movie,type)) {
      return "movie";
    } else if (StringUtils.containsIgnoreCase(music,type)) {
      return "music";
    } else if (StringUtils.containsIgnoreCase(document,type)) {
      return "document";
    } else {
      return "unknown";
    }

  }

  public FileBean getFileOnce(String module_orl , String seq , String target_orl , String type) throws Exception {
    Map mapSch = new HashMap();
    mapSch.put("module_orl", module_orl);
    mapSch.put("target_orl", target_orl);
    mapSch.put("seq", seq);
    mapSch.put("type", type);

    FileBean obj= (FileBean) sqlFileItemObject(mapSch);

    String file_name = "";

    if (obj != null) {
      file_name = obj.getRe_filename();
      String folder = obj.getFolder() + "/";
      file_name = folder + file_name;
    }

    File file = new File(MEI_PATH_ATTACH_ABSOLUTE + file_name);
    if(file.exists()) { return obj; } else { return null; }
  }

  public String getFilePath(String module_orl,String seq,String target_orl,boolean def_size) throws Exception {
    String size = "";
    if (def_size) { size = modConfig.getString("mei.module.file.image.thumbnail.size"); }
    return getFilePath(module_orl,seq,target_orl,size);
  }

  public String getFilePath(String module_orl,String seq,String target_orl,String size) throws Exception {
    String file_name = "";
    String folder = "";
    String type = "";

    Map mapSch = new HashMap();
    mapSch.put("module_orl", module_orl);
    mapSch.put("target_orl", target_orl);
    mapSch.put("seq", seq);

    FileBean obj= (FileBean) sqlFileItemObject(mapSch);

    if (obj != null) {
      file_name = obj.getRe_filename();
      folder = obj.getFolder() + "/";
      type = obj.getType();
    }

    return getFilePath(folder + file_name,type,size);
  }

  public String getFilePath(String file_name,String type,boolean def_size) throws Exception {
    String size = "";
    if (def_size) { size = modConfig.getString("mei.module.file.image.thumbnail.size"); }
    return getFilePath(file_name,type,size);
  }
  public String getFilePath(String file_name,String type,String size) throws Exception {
    String file_path = "";

    if (StringUtils.isNotEmpty(size) && StringUtils.equals(type,"image")) {
      File file = new File(MEI_PATH_THUMBNAIL_ABSOLUTE + "/" + size + file_name);
      if(file.exists()) { file_path = MEI_PATH_THUMBNAIL_RELATIVE + "/" + size + file_name; }
    }

    if (StringUtils.isEmpty(size)) {
      File file2 = new File(MEI_PATH_ATTACH_ABSOLUTE + file_name);
      if(file2.exists()) { file_path = MEI_PATH_ATTACH_RELATIVE + file_name; }
    }

    return file_path;

  }

  public Map getFileDownloadOnce(String module_orl,String seq,String target_orl) throws Exception {

    Map mapRet = new HashMap();
    Map mapSch = new HashMap();
    mapSch.put("module_orl", module_orl);
    mapSch.put("target_orl", target_orl);
    mapSch.put("seq", seq);

    FileBean obj= (FileBean) sqlFileItemObject(mapSch);

    if (obj != null) {
      String file_orl = obj.getFile_orl();
      String sid = obj.getSid();
      String member_orl = obj.getMember_orl();
      String filename = obj.getFilename();
      String re_filename = obj.getRe_filename();
      String folder = obj.getFolder();
      String folder_date = obj.getFolder_date();
      String size = obj.getSize();
      String checksum = obj.getChecksum();
      String extension = obj.getExtension();
      String type = obj.getType();

      mapRet.put("file_orl",file_orl);
      mapRet.put("target_orl",target_orl);
      mapRet.put("sid",sid);
      mapRet.put("seq",seq);
      mapRet.put("module_orl",module_orl);
      mapRet.put("member_orl",member_orl);
      mapRet.put("filename",filename);
      mapRet.put("re_filename",re_filename);
      mapRet.put("folder",folder);
      mapRet.put("folder_date",folder_date);
      mapRet.put("size",size);
      mapRet.put("checksum",checksum);
      mapRet.put("extension",extension);
      mapRet.put("type",type);

      mapRet.put("size_unit",FnUtils.getFileSize(size));
      mapRet.put("url","getFileDownload.me");
      mapRet.put("path",MEI_PATH_ATTACH_RELATIVE + folder);

    }

    return mapRet;

  }

  public List getFileDownload(String module_orl,String seq,String target_orl) throws Exception {
    List listFile = new ArrayList();

    if (StringUtils.isNotEmpty(module_orl) && StringUtils.isNotEmpty(seq) && StringUtils.isNotEmpty(target_orl)) {
      Map mapSch = new HashMap();
      mapSch.put("module_orl", module_orl);
      mapSch.put("target_orl", target_orl);
      mapSch.put("seq", seq);
      List list = (List) sqlFileItem(mapSch);

      int count = list.size();
      long num = 0;

      for (int i = 0; i < count; i++ ) {
        FileBean obj= (FileBean) list.get(i);
        
        String file_orl = obj.getFile_orl();
        String sid = obj.getSid();
        String member_orl = obj.getMember_orl();
        String filename = obj.getFilename();
        String re_filename = obj.getRe_filename();
        String folder = obj.getFolder();
        String folder_date = obj.getFolder_date();
        String size = obj.getSize();
        String checksum = obj.getChecksum();
        String extension = obj.getExtension();
        String type = obj.getType();

        Map mapRet = new HashMap();
        mapRet.put("file_orl",file_orl);
        mapRet.put("target_orl",target_orl);
        mapRet.put("sid",sid);
        mapRet.put("seq",seq);
        mapRet.put("module_orl",module_orl);
        mapRet.put("member_orl",member_orl);
        mapRet.put("filename",filename);
        mapRet.put("re_filename",re_filename);
        mapRet.put("folder",folder);
        mapRet.put("folder_date",folder_date);
        mapRet.put("size",size);
        mapRet.put("checksum",checksum);
        mapRet.put("extension",extension);
        mapRet.put("type",type);

        mapRet.put("size_unit",FnUtils.getFileSize(size));
        mapRet.put("url","getFileDownload.me");
        mapRet.put("path",MEI_PATH_ATTACH_RELATIVE + folder);

        listFile.add(i,mapRet);
      }

    }

    return listFile;
  }

  // 저장된 파일 용량 : 관리자 페이지에서 사용
  public Map getFileItemSizeInfo(Map mapSch) {
    Map mapRet = new HashMap();

    try {

      List list = sqlFileList(mapSch);
      int count = list.size();
      long num = 0;

      long true_item_size = 0;
      long false_item_size = 0;

      long true_item_count = 0;
      long false_item_count = 0;

      for (int i = 0; i < count; i++ ) {
        FileBean obj= (FileBean) list.get(i);

        String target_orl = obj.getTarget_orl();
        String size = obj.getSize();

        if (StringUtils.isNotEmpty(target_orl) && !StringUtils.equals(target_orl,"0")) {
          true_item_size += NumberUtils.createLong(size);
          true_item_count++;
        } else {
          false_item_size += NumberUtils.createLong(size);
          false_item_count++;
        }

      }

      long total_item_size = true_item_size + false_item_size;
      long total_item_count = true_item_count + false_item_count;

      mapRet.put("true_item_size",true_item_size);
      mapRet.put("true_item_count",true_item_count);
      mapRet.put("false_item_size",false_item_size);
      mapRet.put("false_item_count",false_item_count);
      mapRet.put("total_item_size",total_item_size);
      mapRet.put("total_item_count",total_item_count);
    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
    }

    return mapRet;

  }

}

