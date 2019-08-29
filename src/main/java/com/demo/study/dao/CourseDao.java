package com.demo.study.dao;

import com.demo.study.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CourseDao {
    //获取课程分类列表
    List<CourseCategorybean> queryCourseCategoryList();
    //根据category_id 获取课程列表
    List<CourseListBean> queryCourseList(@Param("categoryId") Integer categoryId,
                                         @Param("pageNum")Integer pageNum,
                                         @Param("pageSize")Integer pageSize);
    //根据id获取课程详情
    CourseListBean queryCourseDetail(Integer id);
    //获取课程目录
    List<CourseMenuBean> queryCourseMenuList(Integer menuId);
    //获取课程评论列表
    List<CourseCommentBean> queryCourseComment(@Param("menuId")Integer meunId,
                                               @Param("pageNum")Integer pageNum,
                                               @Param("pageSize")Integer pageSize);
    //添加课程评论
    int insertCourseComment(@Param("userId")Integer userId,
                            @Param("content")String content,
                            @Param("createTime")Date createTime,
                            @Param("menuId")Integer menuId);
    //添加课程购买记录
    int insertMoneyRecord(MoneyRecordBean bean);
}
