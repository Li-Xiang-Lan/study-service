package com.demo.study.service;

import com.demo.study.entity.CourseCategorybean;
import com.demo.study.entity.CourseListBean;
import com.demo.study.entity.CourseMenuBean;

import java.util.List;

public interface CourseService {
    List<CourseCategorybean> getCourseCategoryList();
    List<CourseListBean> getCourseList(Integer categoryId, Integer pageNum, Integer pageSize);
    CourseListBean getCourseDetail(Integer id);
    List<CourseMenuBean> getCourseMenuList(Integer menuId);
}
