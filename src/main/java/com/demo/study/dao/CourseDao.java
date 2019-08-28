package com.demo.study.dao;

import com.demo.study.entity.CourseCategorybean;
import com.demo.study.entity.CourseListBean;
import com.demo.study.entity.CourseMenuBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseDao {
    /**
     * 获取课程分类列表
     * @return
     */
    List<CourseCategorybean> queryCourseCategoryList();

    /**
     * 根据category_id 获取课程列表
     * @param categoryId
     * @return
     */
    List<CourseListBean> queryCourseList(@Param("categoryId") Integer categoryId,
                                         @Param("pageNum")Integer pageNum,
                                         @Param("pageSize")Integer pageSize);

    /**
     * 根据id获取课程详情
     * @param id
     * @return
     */
    CourseListBean queryCourseDetail(Integer id);

    /**
     * 获取课程目录
     * @param menuId
     * @return
     */
    List<CourseMenuBean> queryCourseMenuList(Integer menuId);

}
