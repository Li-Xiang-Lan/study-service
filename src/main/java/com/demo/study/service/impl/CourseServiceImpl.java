package com.demo.study.service.impl;

import com.demo.study.dao.CourseDao;
import com.demo.study.entity.CourseCategorybean;
import com.demo.study.entity.CourseListBean;
import com.demo.study.entity.CourseMenuBean;
import com.demo.study.service.CourseService;
import com.demo.study.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;

    @Override
    public List<CourseCategorybean> getCourseCategoryList() {
        return courseDao.queryCourseCategoryList();
    }

    @Override
    public List<CourseListBean> getCourseList(Integer categoryId, Integer pageNum, Integer pageSize) {
        return courseDao.queryCourseList(categoryId, Util.calculateRowIndex(pageNum, pageSize),pageSize);
    }

    @Override
    public CourseListBean getCourseDetail(Integer id) {
        return courseDao.queryCourseDetail(id);
    }

    @Override
    public List<CourseMenuBean> getCourseMenuList(Integer menuId) {
        return courseDao.queryCourseMenuList(menuId);
    }
}
