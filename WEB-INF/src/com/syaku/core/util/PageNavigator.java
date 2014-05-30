/*
 * PageNavigator.java 2010.08.16
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

public class PageNavigator {
  private static Logger log = Logger.getLogger(PageNavigator.class);

  private HttpServletRequest request;

  private long total_count = 0;
  private int page,page_row,page_link;
  private int total_page,start_page,now_page,end_page,virtual_idx;
  private int start_idx = 0;

  /**
  * @method : PageNavigator(parameter,parameter2,parameter3,parameter4)
  * @brief 페이지네비게이터를 구하기 위한 선언
  * @parameters {
        parameter : (HttpServletRequest) request 서버변수
        parameter2 : (int) 기본 페이지 번호
        parameter2 : (int) 기본 목록 수
        parameter2 : (int) 기본 페이지 링크 수
    }
  */
  public PageNavigator(HttpServletRequest request,int default_page,int default_page_row,int default_page_link) {
    this.request = request;

    page = NumberUtils.stringToInt(request.getParameter("page"),default_page);
    page_row = NumberUtils.stringToInt(request.getParameter("page_row"),default_page_row);
    if (page_link == 0) { page_link = default_page_link; }
  }

  /**
  * @method : setPageIndex
  * @brief 페이지네비게이션을 연산 작업
  */

  public void setPageIndex() {
    total_page = NumberUtils.stringToInt(ObjectUtils.toString(((total_count - 1) / page_row) + 1));
    start_page = ((page - 1) / page_link) * page_link + 1;
    now_page = (start_page / page_link) + 1;
    end_page = start_page + (page_link - 1);
    start_idx = (page - 1) * page_row;
    virtual_idx = NumberUtils.stringToInt(ObjectUtils.toString(total_count - (page_row * (page - 1))));

    log.info(StringUtils.join(new String[] {
      "{PageNavigator.setPageIndex}\r\n" , 
      "==========================================================================\r\n" , 
      "> total_count : " , ObjectUtils.toString(total_count) , "\r\n" , 
      "> page_row : " , ObjectUtils.toString(page_row) , "\r\n" , 
      "> page_link : " , ObjectUtils.toString(page_link) , "\r\n" , 
      "> page : " , ObjectUtils.toString(page) , "\r\n" , 
      "> total_page : " , ObjectUtils.toString(total_page) , "\r\n" , 
      "> start_page : " , ObjectUtils.toString(start_page) , "\r\n" , 
      "> now_page : " , ObjectUtils.toString(now_page) , "\r\n" , 
      "> end_page : " , ObjectUtils.toString(end_page) , "\r\n" , 
      "> start_idx : " , ObjectUtils.toString(start_idx) , "\r\n" , 
      "> virtual_idx : " , ObjectUtils.toString(virtual_idx) , "\r\n" , 
      "=========================================================================="
    }));
  }

  public int getPage() { return this.page; }
  public void setPage(int page) { this.page = page; }
  public int getPage_row() { return this.page_row; }
  public void setPage_row(int page_row) { this.page_row = page_row; }
  public int getPage_link() { return this.page_link; }
  public void setPage_link(int page_link) { this.page_link = page_link; }
  public long getTotal_count() { return this.total_count; }
  public void setTotal_count(long total_count) { this.total_count = total_count; }
  public int getTotal_page() { return this.total_page; }
  public int getStart_page() { return this.start_page; }
  public int getNow_page() { return this.now_page; }
  public int getEnd_page() { return this.end_page; }
  public int getStart_idx() { return this.start_idx; }
  public long getVirtual_idx() { return this.virtual_idx; }
}