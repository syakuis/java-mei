/*
 * FileUpload.java 2011.01.01
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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.*;
import org.apache.commons.io.FileUtils;

import com.syaku.core.*;
import com.syaku.core.util.*;
import com.syaku.core.parser.*;

public class FileUpload extends MeiBasic {
  private Logger log = Logger.getLogger( this.getClass() );
  public InputStream inputStream;

  /* multipart/form-data paramter */
  public String module_orl;
  public String getModule_orl() { return module_orl; }
  public void setModule_orl(String module_orl) { this.module_orl = module_orl; }
  public String mid;
  public String getMid() { return mid; }
  public void setMid(String mid) { this.mid = mid; }

  private File file_upload;
  private String file_uploadContentType;
  private String filename;
  private String file_upload_multi;
  private String member_orl;
  private String target_orl;
  private String sid;
  private String seq;

  public File getFile_upload() { return file_upload; }
  public void setFile_upload(File file_upload) { this.file_upload = file_upload; }

  public String getFile_uploadContentType() { return file_uploadContentType; }
  public void setFile_uploadContentType(String file_uploadContentType) { this.file_uploadContentType = file_uploadContentType; }

  public String getFilename() { return filename; }
  public void setFilename(String filename) { this.filename = filename; }
  public String getFile_upload_multi() { return file_upload_multi; }
  public void setFile_upload_multi(String file_upload_multi) { this.file_upload_multi = file_upload_multi; }

  public String getMember_orl() { return member_orl; }
  public void setMember_orl(String member_orl) { this.member_orl = member_orl; }
  public String getTarget_orl() { return target_orl; }
  public void setTarget_orl(String target_orl) { this.target_orl = target_orl; }
  public String getSid() { return sid; }
  public void setSid(String sid) { this.sid = sid; }
  public String getSeq() { return seq; }
  public void setSeq(String seq) { this.seq = seq; }
  /* multipart/form-data paramter */

  public String execute() throws Exception {

    try {
      sqlMap.startTransaction();

      if (file_upload == null) { throw new Exception("파일이없습니다."); }

      long module_file_once_size = 0;
      long module_file_size = 0;
      int module_file_limit = 0;

      Map mapSch = new HashMap();

      if (MapUtils.isNotEmpty(MM)) {
        String options_file_once_size = StringUtils.defaultIfEmpty( (String) MM.get("options_file_once_size") , "0" );

        module_file_once_size = NumberUtils.createLong(StringUtils.defaultIfEmpty( (String) MM.get("options_file_once_size") , options_file_once_size ) );
        module_file_size = NumberUtils.createLong(StringUtils.defaultIfEmpty( (String) MM.get("options_file_size") , options_file_once_size ) );

        module_file_limit = NumberUtils.createInteger(StringUtils.defaultIfEmpty( (String) MM.get("options_file_limit") , "0" ) );
        
      }
      
      module_file_once_size = module_file_once_size * 1024; // KB -> Byte 변경
      module_file_size = module_file_size * 1024; // KB -> Byte 변경

      // 파일 용량
      long size = file_upload.length();
      
      // 한개 파일용량 체크
      if ( (module_file_once_size < size && module_file_once_size > 0) && module_file_once_size != 0) { // 0은 무제한으로 처리한다.
        throw new Exception("업로드 제한 용량을 넘었습니다.");
      }
      
      if ( StringUtils.equals( file_upload_multi ,"1" ) ) {

        // 파일 모두 가져오기
        mapSch = new HashMap();
        mapSch.put("module_orl",module_orl);
        mapSch.put("seq",seq);
        
        if ( StringUtils.isNotEmpty(target_orl) ) {
          mapSch.put("target_orl",target_orl);
        } else {
          mapSch.put("target_orl","0");
          mapSch.put("sid",sid);
        }

        // 파일 용량
        Map mapFileSizeSumCount = (Map) daoFile.sqlFileSizeSumCount(mapSch);
        long file_size_sum = 0;
        int file_limit = 0;

        if (MapUtils.isNotEmpty(mapFileSizeSumCount)) {

          file_size_sum = NumberUtils.createLong( ObjectUtils.toString( (Number) mapFileSizeSumCount.get("file_size") ) );
          file_limit = NumberUtils.createInteger( ObjectUtils.toString( (Number) mapFileSizeSumCount.get("file_count") ) );

          if (module_file_size < file_size_sum && module_file_size != 0) { // 0은 무제한으로 처리한다.
            throw new Exception("총 업로드 제한 용량을 넘었습니다.");
          }

          if (module_file_limit <= file_limit && module_file_limit != 0) { // 0은 무제한으로 처리한다.
            throw new Exception("업로드 할 수 있는 파일 수를 넘었습니다.");
          }

        }

      }

      // 싱글이고 타켓이 있을 경우 파일 삭제
      if ( StringUtils.equals( file_upload_multi , "0" ) && !StringUtils.equals( target_orl , "0" ) && StringUtils.isNotEmpty(target_orl) ) {
        mapSch = new HashMap();
        mapSch.put("module_orl",module_orl);
        mapSch.put("target_orl",target_orl);
        mapSch.put("seq",seq);
        daoFile.getFileDeleteTarget(mapSch);
      }
      
      String yyyy = DateUtils.date("yyyy");
      String mm = DateUtils.date("MM");
      String dd = DateUtils.date("dd");
      String folder_date = yyyy + mm + dd;

      // 폴더생성
      String file_path = "/" + module_orl + "/" + yyyy +"/" + mm + "/" + dd;
      String path = daoFile.MEI_PATH_ATTACH_ABSOLUTE + file_path;
      File dir = new File(path);
      if(!dir.exists()){ dir.mkdirs(); }

      int indexof = filename.lastIndexOf("\\");
      
      // getName()은 경로를 다 가져오기 때문에 lastIndexOf로 잘라냄
      if (indexof == -1) { indexof = filename.lastIndexOf("/"); }
      String new_file_name = filename.substring(indexof + 1);

      indexof = 0;
      indexof = new_file_name.lastIndexOf(".");
      String extension = new_file_name.substring(indexof + 1);
      extension = extension.toLowerCase();//소문자 처리

      // 이름변경
      String file_name = DateUtils.date("ssMMHHyyyyddmm");
      file_name = (String) DigestUtils.md5Hex(file_name);
      
      new_file_name = file_name + "." + extension;

      long checksum = FileUtils.checksumCRC32(file_upload);
      File file = new File(path + "/" + new_file_name);

      String type = daoFile.getTypeCommon(extension);

      // 중복파일체크
      if(file.exists()){
        int cont = 50;
        for(int o=0; o < cont; o++){
          file = new File(path, o + new_file_name);
          if(!file.exists()) {
            new_file_name = o + new_file_name;
            break;
          }

          if (o == 49) {
            throw new Exception("잠시 후에 다시 업로드하세요.");
          }

        }
      }

      FileBean data = new FileBean();
      data.setTarget_orl(StringUtils.defaultIfEmpty(target_orl,"0"));
      data.setSid(sid);
      data.setSeq(StringUtils.defaultIfEmpty(seq,"0"));
      data.setModule_orl(StringUtils.defaultIfEmpty(module_orl,"0"));
      data.setMember_orl(StringUtils.defaultIfEmpty(member_orl,"0"));
      data.setFilename(filename);
      data.setRe_filename(new_file_name);
      data.setFolder(file_path);
      data.setFolder_date(folder_date);
      data.setSize( ObjectUtils.toString( size ) );
      data.setChecksum( ObjectUtils.toString( checksum ) );
      data.setExtension(extension);
      data.setType(type);
      data.setRdate(DateUtils.date("yyyyMMddHHmmss"));
      data.setIp(request.getRemoteAddr());

      String file_orl = (String) daoFile.sqlFileInsert(data);
      if (StringUtils.isNotEmpty(target_orl) && !StringUtils.equals(target_orl,"0")) {
        daoFile.getFileCounterInsert(module_orl,target_orl,seq);
      }

      FileUtils.copyFile(file_upload,file);

      sqlMap.commitTransaction();

      MESSAGE.put("error","false");
      MESSAGE.put("file_orl",file_orl);
      MESSAGE.put("filename",filename);
      MESSAGE.put("re_file",new_file_name);
      MESSAGE.put("folder",file_path);
      MESSAGE.put("file_size", ObjectUtils.toString( size ) );
      MESSAGE.put("extension",extension);
      MESSAGE.put("type",type);

    } catch (Exception e) {
      log.error(e.toString()); // 로그 출력
      MESSAGE.put("message",e.getMessage());
      MESSAGE.put("error","true");

    } finally {
      sqlMap.endTransaction();
    }

    inputStream = Object2Xml.toInputStream(MESSAGE,"UTF-8");
    return "xml";

  }

  public Object getModel() {
    log.debug("[## MEI MeiBasic getModel] Called");
    return new Object();
  }


}