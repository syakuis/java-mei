/*
 * CounterBean.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.modules.counter;

public class CounterBean {

  private String ip;
  private String rdate;
  private String user_agent;

  public String getIp() { return ip; }
  public void setIp(String ip) { this.ip = ip; }
  public String getRdate() { return rdate; }
  public void setRdate(String rdate) { this.rdate = rdate; }
  public String getUser_agent() { return user_agent; }
  public void setUser_agent(String user_agent) { this.user_agent = user_agent; }

}
