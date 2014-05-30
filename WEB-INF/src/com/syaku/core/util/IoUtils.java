/*
 * IoUtils.java 2011.02.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import com.syaku.core.util.*;

public class IoUtils {
  private static Logger log = Logger.getLogger(IoUtils.class);

  public static void createFile(String name) {
    createFile(name,false);
  }
  public static void createFile(String name, boolean is_new) {
    try {
      File file = new File(name);

      if(file.exists()) { 
        if (is_new) { file.delete(); file.createNewFile(); }
        else { System.out.println(StringUtils.join(new String[] {"[Create File@exist] " , name })); }
      } else {
        file.createNewFile();
        System.out.println(StringUtils.join(new String[] {"[Create File@success] " , name }));
      }

    } catch (Exception e) {
      System.out.println(StringUtils.join(new String[] {"[Create File@error] Exception : " , e.toString() }));
    }

  }


  public static void writeXml(String name,Document document) throws Exception {
    writeXml(name,document,false);
  }
  public static void writeXml(String name,Document document, boolean is_new) throws Exception {
    File file = null;
    FileOutputStream fos = null;
    OutputStreamWriter osw = null;
    BufferedWriter bw = null;

    try {

      file = new File(name);
      if (file.exists() && is_new) { file.delete(); }
      file.createNewFile();

      fos = new FileOutputStream(file);
      osw=new OutputStreamWriter(fos,"UTF-8");
      bw=new BufferedWriter(osw);

      if (document != null) {
        XMLOutputter outputter = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        format.setEncoding("utf-8");
        outputter.setFormat(format);
        outputter.output(document, bw);
      }

      file = new File(name);
      if (!file.exists()) { throw new Exception("Create Xml File : " + name + " > 실패"); }

    } catch (Exception e) {
      log.error(e.toString());
    } finally {
      fos.close();
      osw.close();
      bw.close();
    }
  }


  public static void writeFile(String name,StringBuffer document) throws Exception {
    writeFile(name,document,false);
  }
  public static void writeFile(String name,StringBuffer document, boolean is_new) throws Exception {
    File file = null;
    FileOutputStream fos = null;
    OutputStreamWriter osw = null;
    BufferedWriter bw = null;

    try {

      file = new File(name);
      if (file.exists() && is_new) { file.delete(); }
      file.createNewFile();

      fos = new FileOutputStream(file);
      osw=new OutputStreamWriter(fos,"utf-8");
      bw=new BufferedWriter(osw);

      if (document != null) {
        bw.write(document.toString());
        bw.flush();
      }

      file = new File(name);
      if (!file.exists()) { throw new Exception("Create File : " + name + " > 실패"); }
      System.out.println("Create File : " + name + " > 생성");

    } catch (Exception e) {
      System.out.println(e.toString());
    } finally {
      fos.close();
      osw.close();
      bw.close();
    }
  }

  public static void createDir(String name) {
    try {
      File dir = new File(name);
      if(!dir.exists()) { 
        dir.mkdirs();

        System.out.println(StringUtils.join(new String[] {"[Create Directory@success] " , name }));
      } else {
        System.out.println(StringUtils.join(new String[] {"[Create Directory@exist] " , name }));
      }

    } catch (Exception e) {
      System.out.println(StringUtils.join(new String[] {"[Create Directory@error] Exception " , e.toString() }));
    }
  }

  public static List listFiles(String path, boolean is_dir) {
    List list = new ArrayList();

    log.debug("@IoUtils.listFiles(" + path + "," + is_dir + ")");

    try {
      if (StringUtils.isEmpty(path)) { return list; }

      File obj = new File(path);
      if (!obj.exists()) { return list; }

      File arr[] = obj.listFiles();
      int count = arr.length;

      for (int i = 0; i < count; i++) {
        File info = arr[i];
        String name = info.getName();

        if (name.equals(".svn") || StringUtils.isEmpty(name)) { // SVN 폴더 제외
          continue;
        }

        log.debug(name);

        if ( arr[i].isDirectory() == false && is_dir == false) {
          list.add(name);
        } else {
          list.add(name);
        }

      }

    } catch (Exception e) {
      log.error("@IoUtils.listFiles : " + e.toString() );
    }

    return list;
  }

}