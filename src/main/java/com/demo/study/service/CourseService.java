package com.demo.study.service;

import com.demo.study.entity.*;

import java.util.Date;
import java.util.List;

public interface CourseService {
    List<CourseCategorybean> getCourseCategoryList();
    List<CourseListBean> getCourseList(Integer categoryId, Integer pageNum, Integer pageSize);
    CourseListBean getCourseDetail(Integer id);
    List<CourseMenuBean> getCourseMenuList(Integer menuId);
    List<CourseCommentBean> getCourseCommentList(Integer menuId,Integer pageNum,Integer pageSize);
    int addCourseComment(Integer userId, String content, Date createTime,Integer menuId);
    BaseBackBean buyCourse(Integer userId, Integer menuId);
}
