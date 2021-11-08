package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;

import java.util.List;
/*
* 查询所有
* */
public interface CategoryDao {
  public List<Category> findAll();
}
