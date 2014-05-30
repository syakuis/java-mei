/*
 * CategoryAccess.java 2011.01.01
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
package com.syaku.modules.category;

import java.util.*;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.syaku.core.common.*;

public class CategoryAccess {
  private Logger log = Logger.getLogger( this.getClass() );
  public static final SqlMapClient sqlMap = MyBatis.getInstance();
  //getCategoryList
  public List sqlCategoryList(Map map) throws Exception { return sqlMap.queryForList("modules.category.select-category-list", map); }
  public List sqlCategoryXml(Map map) throws Exception { return sqlMap.queryForList("modules.category.select-category-xml", map); }
  // getCategoryObject
  public Object sqlCategoryObject(Map map) throws Exception { return sqlMap.queryForObject("modules.category.select-category-one", map); }
  // getCategoryMap
  public Map sqlCategoryMap(Map map) throws Exception { return (Map) sqlMap.queryForObject("modules.category.select-category-map", map); }
  public String sqlCategoryMoveSeq(Map map) throws Exception { return (String) sqlMap.queryForObject("modules.category.select-category-move-seq", map); }
  public String sqlCategoryMaxSeq(Map map) throws Exception { return (String) sqlMap.queryForObject("modules.category.select-category-max-seq", map); }
  public String sqlCategoryMinSeq(Map map) throws Exception { return (String) sqlMap.queryForObject("modules.category.select-category-min-seq", map); }
  public String sqlCategoryInsert(Object object) throws Exception { return (String) sqlMap.insert("modules.category.insert-category",object); }
  public void sqlCategoryUpdate(Object object) throws Exception { sqlMap.update("modules.category.update-category",object); }
  public void sqlCategoryUpdateMove(Map map) throws Exception { sqlMap.delete("modules.category.update-category-move",map); }
  public void sqlCategoryUpdateSort(Map map) throws Exception { sqlMap.delete("modules.category.update-category-sort",map); }
  public void sqlCategoryDelete(Map map) throws Exception { sqlMap.delete("modules.category.delete-category",map); }
  public void sqlCategoryModuleDelete(Map map) throws Exception { sqlMap.delete("modules.category.delete-category-module",map); }

  public Long sqlCategoryCounterCount(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.category.select-category-counter-count",map); }
  public Long sqlCategoryCounterCountObject(Map map) throws Exception { return (Long) sqlMap.queryForObject("modules.category.select-category-counter-count-one",map); }
  public void sqlCategoryCounterInsert(Object object) throws Exception { sqlMap.insert("modules.category.insert-category-counter",object); }
  public void sqlCategoryCounterUpdate(Object object) throws Exception { sqlMap.update("modules.category.update-category-counter",object); }
  public void sqlCategoryCounterDelete(Map map) throws Exception { sqlMap.delete("modules.category.delete-category-counter",map); }
  public void sqlCategoryCounterModuleDelete(Map map) throws Exception { sqlMap.delete("modules.category.delete-category-counter-module",map); }

  public void sqlCategoryCreate(Map map) throws Exception { sqlMap.insert("modules.category.create-category",map); }
  public void sqlCategoryCounterCreate(Map map) throws Exception { sqlMap.insert("modules.category.create-category-counter",map); }

}